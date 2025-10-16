package com.canyinghao.canshare.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.canyinghao.canshare.CanShare;
import com.canyinghao.canshare.R;
import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.constants.ShareConstants;
import com.canyinghao.canshare.listener.ShareListener;
import com.canyinghao.canshare.model.ShareContent;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.sina.weibo.sdk.share.WbShareCallback;


/**
 * 新浪分享
 */

public class ShareSina implements WbShareCallback {



    private Activity activity;
    private Context mContext;


    private ShareListener shareListener;


    private final IWBAPI mWBAPI;

    public ShareSina(Context context, String appId, ShareListener shareListener) {
        activity = (Activity) context;
        mContext = context.getApplicationContext();


        this.shareListener = shareListener;
        mWBAPI =WBAPIFactory.createWBAPI(context);


    }


    private void shareText(ShareContent shareContent) {

        //初始化微博的分享消息
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = getTextObj(shareContent.getContent());
        //初始化从第三方到微博的消息请求
//        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//        request.transaction = ShareUtil.buildTransaction("sinatext");
//        request.multiMessage = weiboMultiMessage;
        allInOneShare(mContext, weiboMultiMessage);

    }

    private void sharePicture(ShareContent shareContent) {

        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.imageObject = getImageObj(shareContent);
        //初始化从第三方到微博的消息请求
//        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//        request.transaction = ShareUtil.buildTransaction("sinapic");
//        request.multiMessage = weiboMultiMessage;
        allInOneShare(mContext, weiboMultiMessage);
    }

    private void shareWebPage(ShareContent shareContent) {

        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = getTextObj(shareContent.getContent());
        weiboMultiMessage.imageObject = getImageObj(shareContent);
        // 初始化从第三方到微博的消息请求
//        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
//        request.transaction = ShareUtil.buildTransaction("sinawebpage");
//        request.multiMessage = weiboMultiMessage;
        allInOneShare(mContext, weiboMultiMessage);

    }


//    private void shareMusic(ShareContent shareContent) {
//        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
//        weiboMultiMessage.mediaObject = getMusicObj(shareContent);
//        //初始化从第三方到微博的消息请求
//        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//        request.transaction = ShareUtil.buildTransaction("sinamusic");
//        request.multiMessage = weiboMultiMessage;
//        allInOneShare(mContext, request);
//    }


    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(ShareContent shareContent) {
        ImageObject imageObject = new ImageObject();
        String imageUrl = shareContent.getImageUrl();
        if(!TextUtils.isEmpty(imageUrl)&&imageUrl.startsWith("file://")){
            imageObject.imagePath = imageUrl.replace("file://","");
        }else{
            imageObject.setImageData(shareContent.getShareImageBitmap());
        }
        return imageObject;
    }




    private void allInOneShare(final Context context, WeiboMultiMessage weiboMessage) {

        mWBAPI.shareMessage(activity, weiboMessage, false);


    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (mWBAPI.isShareResult(requestCode, resultCode, data)) {
            mWBAPI.doResultIntent(data, this);
        }

    }




    public ShareSina share(ShareContent shareContent) {

        if (!mWBAPI.isWBAppInstalled()) {

            if (shareListener != null) {

                shareListener.onNoInstall(ShareType.SINA,mContext.getString(R.string.share_install_weibo_tips));
            }

            return this;
        }


        if (mWBAPI != null) {

            switch (shareContent.getShareWay()) {
                case ShareConstants.SHARE_WAY_TEXT:
                    shareText(shareContent);
                    break;
                case ShareConstants.SHARE_WAY_PIC:
                    sharePicture(shareContent);
                    break;
                case ShareConstants.SHARE_WAY_WEBPAGE:
                    shareWebPage(shareContent);
                    break;
            }
        }
        return this;


    }





    @Override
    public void onComplete() {
        if (shareListener != null) {
            shareListener.onComplete(ShareType.SINA, null);
        }
    }

    @Override
    public void onError(UiError error) {
        if (shareListener != null) {
            shareListener.onError();
        }
    }

    @Override
    public void onCancel() {
        if (shareListener != null) {
            shareListener.onCancel();
        }
    }

    private void reset() {
        activity = null;
        mContext = null;
        shareListener = null;
    }
}
