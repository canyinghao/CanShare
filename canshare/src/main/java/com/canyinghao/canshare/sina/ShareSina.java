package com.canyinghao.canshare.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.canyinghao.canshare.CanShare;
import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.constants.ShareConstants;
import com.canyinghao.canshare.listener.ShareListener;
import com.canyinghao.canshare.model.ShareContent;
import com.canyinghao.canshare.utils.ShareUtil;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboResponse;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelbase.BaseResp;

/**
 * 新浪分享
 */

public class ShareSina {


    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    public String REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";

    private Activity activity;
    private Context mContext;

    private String appId;

    private ShareListener shareListener;


    /**
     * 微博分享的接口实例
     */
    private IWeiboShareAPI mSinaAPI;


    public ShareSina(Context context, String appId, ShareListener shareListener) {
        activity = (Activity) context;
        mContext = context.getApplicationContext();
        this.appId = appId;

        REDIRECT_URL = CanShare.getInstance().getWeiBoRedirectUrl();

        this.shareListener = shareListener;

        if (!TextUtils.isEmpty(appId)) {
            // 创建微博 SDK 接口实例
            mSinaAPI = WeiboShareSDK.createWeiboAPI(context, appId);
            mSinaAPI.registerApp();
        }
    }


    private void shareText(ShareContent shareContent) {

        //初始化微博的分享消息
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = getTextObj(shareContent.getContent());
        //初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = ShareUtil.buildTransaction("sinatext");
        request.multiMessage = weiboMultiMessage;
        allInOneShare(mContext, request);

    }

    private void sharePicture(ShareContent shareContent) {

        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.imageObject = getImageObj(shareContent);
        //初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = ShareUtil.buildTransaction("sinapic");
        request.multiMessage = weiboMultiMessage;
        allInOneShare(mContext, request);
    }

    private void shareWebPage(ShareContent shareContent) {

        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = getTextObj(shareContent.getContent());
        weiboMultiMessage.imageObject = getImageObj(shareContent);
        // 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = ShareUtil.buildTransaction("sinawebpage");
        request.multiMessage = weiboMultiMessage;
        allInOneShare(mContext, request);

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
            imageObject.setImageObject(shareContent.getShareImageBitmap());
        }
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
//    private WebpageObject getWebpageObj(ShareContent shareContent) {
//        WebpageObject mediaObject = new WebpageObject();
//        mediaObject.identify = Utility.generateGUID();
//        mediaObject.title = shareContent.getTitle();
//        mediaObject.description = shareContent.getContent();
//
//        // 设置 Bitmap 类型的图片到视频对象里
//        Bitmap bmp = ShareUtil.extractThumbNail(shareContent.getImageUrl(), 150, 150, true);
//        mediaObject.setThumbImage(bmp);
//        mediaObject.actionUrl = shareContent.getURL();
//        mediaObject.defaultText = shareContent.getContent();
//        return mediaObject;
//    }


    /**
     * 创建多媒体（音乐）消息对象。
     *
     * @return 多媒体（音乐）消息对象。
     */
//    private MusicObject getMusicObj(ShareContent shareContent) {
//        // 创建媒体消息
//        MusicObject musicObject = new MusicObject();
//        musicObject.identify = Utility.generateGUID();
//        musicObject.title = shareContent.getTitle();
//        musicObject.description = shareContent.getContent();
//
//        // 设置 Bitmap 类型的图片到视频对象里
//        musicObject.setThumbImage(shareContent.getShareImageBitmap());
//        musicObject.actionUrl = shareContent.getURL();
//        musicObject.dataUrl = REDIRECT_URL;
//        musicObject.dataHdUrl = REDIRECT_URL;
//        musicObject.duration = 10;
//        musicObject.defaultText = shareContent.getContent();
//        return musicObject;
//    }


    private void allInOneShare(final Context context, SendMultiMessageToWeiboRequest request) {

        AuthInfo authInfo = new AuthInfo(context, appId, REDIRECT_URL, SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context);
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }


        mSinaAPI.sendRequest(activity, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {


                if (shareListener != null) {
                    shareListener.onError();
                }
            }

            @Override
            public void onComplete(Bundle bundle) {




                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(context, newToken);

                if (shareListener != null) {
                    shareListener.onComplete(ShareType.SINA, null);
                }
            }

            @Override
            public void onCancel() {

                if (shareListener != null) {
                    shareListener.onCancel();
                }

            }
        });

    }


    public void onNewIntent(Intent intent, IWeiboHandler.Response response) {

        handleWeiboResponse(intent, response);

        String appPackage = intent.getStringExtra("_weibo_appPackage");


        if (TextUtils.isEmpty(appPackage)) {

            if (shareListener != null) {
                shareListener.onError();
            }

        } else {
            SendMessageToWeiboResponse data = new SendMessageToWeiboResponse(intent.getExtras());

            switch (data.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    if (shareListener != null) {
                        shareListener.onComplete(ShareType.SINA, null);
                    }
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:


                    if (shareListener != null) {

                        shareListener.onCancel();
                    }
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    if (shareListener != null) {
                        shareListener.onError();
                    }
                    break;
            }
        }

        reset();
    }

    public void handleWeiboResponse(Intent intent, IWeiboHandler.Response response) {
        if (mSinaAPI != null && response != null) {

            mSinaAPI.handleWeiboResponse(intent, response);

        }
    }

    public ShareSina share(ShareContent shareContent) {

        if (mSinaAPI != null) {

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


    public void onResponse(BaseResponse baseResponse) {

        switch (baseResponse.errCode) {

            case BaseResp.ErrCode.ERR_OK:
                if (shareListener != null) {
                    shareListener.onComplete(ShareType.SINA, null);
                }

                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (shareListener != null) {
                    shareListener.onCancel();
                }

                break;


            case BaseResp.ErrCode.ERR_SENT_FAILED:
                if (shareListener != null) {
                    shareListener.onError();
                }

                break;


        }

    }


    private void reset() {
        activity = null;
        mContext = null;
        shareListener = null;
    }
}
