package com.canyinghao.canshare.qq;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.canyinghao.canshare.CanShare;
import com.canyinghao.canshare.R;
import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.listener.ShareListener;
import com.canyinghao.canshare.model.ShareContent;
import com.canyinghao.canshare.utils.ShareUtil;
import com.socks.library.Util;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
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

    private Context context;

    public ShareQQ(Context context, String appId, boolean isShareToQQ, ShareListener shareListener) {

        this.isShareToQQ = isShareToQQ;

        this.context = context;

        this.shareListener = shareListener;


        mTencent = Tencent.createInstance(appId, context);


    }


    public Tencent getTencent() {
        return mTencent;
    }

    private void sharePageQzone(Activity activity, ShareContent shareContent) {

        Bundle params = new Bundle();
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareContent.getTitle());
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareContent.getContent());
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareContent.getURL());
        ArrayList<String> imageUrls = new ArrayList<String>();
        imageUrls.add(shareContent.getImageUrl());
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        doShareToQzone(activity, params);

    }


    private void sharePageQQ(Activity activity, ShareContent shareContent) {

        Bundle params = new Bundle();
        //类型
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        //标题
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareContent.getTitle());
        //内容的url
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.getURL());
        //摘要
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent.getContent());
        //图片url
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareContent.getImageUrl());

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
        }
    }

    private void doShareToQzone(final Activity activity, final Bundle params) {


        if (mTencent != null) {
            mTencent.shareToQzone(activity, params, iUiListener);
        }

    }


    public ShareQQ share(ShareContent shareContent) {


        if (!ShareUtil.isQQClientAvailable(context)) {

            if (shareListener != null) {

                shareListener.onQQNoInstall(context.getString(R.string.share_install_qq_tips));
            }

            return this;
        }

        if (isShareToQQ) {

            sharePageQQ((Activity) context, shareContent);
        } else {

            sharePageQzone((Activity) context, shareContent);
        }


        return this;

    }


    public final IUiListener iUiListener = new IUiListener() {
        @Override
        public void onCancel() {


            if (shareListener != null) {
                shareListener.onCancel();
            }
        }

        @Override
        public void onComplete(Object response) {

            if (shareListener != null) {
                shareListener.onComplete(isShareToQQ ? ShareType.QQ : ShareType.QZONE, null);
            }
        }

        @Override
        public void onError(UiError e) {


            if (shareListener != null) {
                shareListener.onError();
            }

        }
    };


}
