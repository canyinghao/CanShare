package com.canyinghao.canshare.qq;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.constants.ShareConstants;
import com.canyinghao.canshare.listener.ShareListener;
import com.canyinghao.canshare.model.ShareContent;
import com.socks.library.KLog;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.miniapp.MiniApp;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * QQ分享
 */

public class ShareQQ {


    private Tencent mTencent;


    private boolean isShareToQQ;


    private ShareListener shareListener;

    private Context mContext;

    private Activity activity;

    public ShareQQ(Context context, String appId, boolean isShareToQQ, ShareListener shareListener) {

        this.activity = (Activity) context;

        this.mContext = context.getApplicationContext();

        this.isShareToQQ = isShareToQQ;


        this.shareListener = shareListener;

        try {
            mTencent = Tencent.createInstance(appId, context.getApplicationContext(),context.getPackageName()+".fileprovider");
        } catch (Throwable e) {
        }


    }


    private void sharePageQzone(Activity activity, ShareContent shareContent) {

        Bundle params = new Bundle();

        if (shareContent.shareWay == ShareConstants.SHARE_WAY_MINI) {
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_MINI_PROGRAM);
            params.putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_APPID, shareContent.getMiniProgramUserName());
            params.putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_PATH, shareContent.getMiniProgramPath());
            params.putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_TYPE, shareContent.getMiniProgramType());
        } else if (shareContent.shareWay == ShareConstants.SHARE_WAY_PIC) {
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                    QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE);
        } else {
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                    QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        }

        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareContent.getTitle());
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareContent.getContent());
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareContent.getURL());
        ArrayList<String> imageUrls = new ArrayList<String>();
        String imageUrl = shareContent.getImageUrl();
        if (!TextUtils.isEmpty(imageUrl) && imageUrl.startsWith("file://")) {
            imageUrls.add(imageUrl.replace("file://", ""));
        } else {
            imageUrls.add(imageUrl);
        }
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        doShareToQzone(activity, params);

    }


    private void sharePageQQ(Activity activity, ShareContent shareContent) {

        Bundle params = new Bundle();


        //类型
        if (shareContent.shareWay == ShareConstants.SHARE_WAY_MINI) {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_MINI_PROGRAM);

            params.putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_APPID, shareContent.getMiniProgramUserName());
            params.putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_PATH, shareContent.getMiniProgramPath());
            params.putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_TYPE, shareContent.getMiniProgramType());


        } else if (shareContent.shareWay == ShareConstants.SHARE_WAY_PIC) {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);

        } else {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);

        }

        //标题
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareContent.getTitle());
        //内容的url
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.getURL());
        //摘要
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent.getContent());

        //图片url
        String imageUrl = shareContent.getImageUrl();
        if (!TextUtils.isEmpty(imageUrl) && imageUrl.startsWith("file://")) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl.replace("file://", ""));
        } else {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        }


        doShareToQQ(activity, params);

    }


    /**
     * 用异步方式启动分享
     *
     * @param params Bundle
     */
    private void doShareToQQ(final Activity activity, final Bundle params) {


        if (mTencent != null) {
            mTencent.shareToQQ(activity, params, iUiListener);
        } else {
            if (shareListener != null) {
                shareListener
                        .onError();
            }
        }
    }

    private void doShareToQzone(final Activity activity, final Bundle params) {


        if (mTencent != null) {
            mTencent.shareToQzone(activity, params, iUiListener);
        } else {
            if (shareListener != null) {
                shareListener
                        .onError();
            }
        }

    }


    public ShareQQ share(ShareContent shareContent) {


//        if (!ShareUtil.isQQClientAvailable(mContext)) {
//
//            if (shareListener != null) {
//
//                shareListener.onNoInstall(ShareType.QQ, mContext.getString(R.string.share_install_qq_tips));
//            }
//
//            return this;
//        }

        if (isShareToQQ) {

            sharePageQQ(activity, shareContent);
        } else {

            sharePageQzone(activity, shareContent);
        }


        return this;

    }


    public final IUiListener iUiListener = new IUiListener() {
        @Override
        public void onCancel() {


            if (shareListener != null) {
                shareListener.onCancel();
            }
            reset();
        }

        @Override
        public void onWarning(int i) {

        }

        @Override
        public void onComplete(Object response) {

            if (shareListener != null) {
                shareListener.onComplete(isShareToQQ ? ShareType.QQ : ShareType.QZONE, null);
            }
            reset();
        }

        @Override
        public void onError(UiError e) {


            if (shareListener != null) {
                shareListener.onError();
            }
            reset();

        }
    };


    public void openQQMini(ShareContent shareContent) {

        int ret = MiniApp.MINIAPP_UNKNOWN_TYPE;
        try {
            ret = mTencent.startMiniApp(activity, shareContent.getMiniProgramUserName(), shareContent.getMiniProgramPath(),
                    "");
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (ret != MiniApp.MINIAPP_SUCCESS) {
            // 互联demo针对纯输入出错的地方进行文字提示
            if (ret == MiniApp.MINIAPP_ID_EMPTY) {
                KLog.e("qqconnect_mini_app_id_empty");

            } else if (ret == MiniApp.MINIAPP_ID_NOT_DIGIT) {
                KLog.e("qqconnect_mini_app_id_not_digit");

            }

        }
    }

    private void reset() {
        mTencent = null;
        activity = null;
        mContext = null;
        shareListener = null;
    }


}
