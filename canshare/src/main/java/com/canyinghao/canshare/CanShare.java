package com.canyinghao.canshare;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.listener.ShareListener;
import com.canyinghao.canshare.model.ShareContent;
import com.canyinghao.canshare.qq.OauthQQ;
import com.canyinghao.canshare.qq.ShareQQ;
import com.canyinghao.canshare.sina.OauthSina;
import com.canyinghao.canshare.sina.ShareSina;
import com.canyinghao.canshare.weixin.OauthWeiXin;
import com.canyinghao.canshare.weixin.ShareWeiXin;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.SdkListener;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

/**
 * Created by canyinghao on 2016/12/14.
 */

public class CanShare {


    private static CanShare mInstance;

    private static final String SCOPE =
            "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog";

    private String weiXinAppId;
    private String weiXinSecret;

    private String qqAppId;

    private String weiBoAppId;
    private String weiBoRedirectUrl;

    private String noInstallWeiXin;



    private boolean isNeedUserInfo;


    private boolean isWinXinCode;

    private ShareListener shareListener;

    private int shareType = -1;


    private OauthSina oauthSina;

    private ShareSina shareSina;

    private IUiListener iUiListener;


    public String getWeiXinAppId() {
        return weiXinAppId;
    }

    public void setWeiXinAppId(String weiXinAppId) {
        this.weiXinAppId = weiXinAppId;
    }

    public String getWeiXinSecret() {
        return weiXinSecret;
    }

    public void setWeiXinSecret(String weiXinSecret) {
        this.weiXinSecret = weiXinSecret;
    }

    public String getQqAppId() {
        return qqAppId;
    }

    public void setQqAppId(String qqAppId) {
        this.qqAppId = qqAppId;
    }

    public String getWeiBoAppId() {
        return weiBoAppId;
    }

    public void setWeiBoAppId(String weiBoAppId) {
        this.weiBoAppId = weiBoAppId;
    }

    public String getWeiBoRedirectUrl() {
        return weiBoRedirectUrl;
    }

    public void setWeiBoRedirectUrl(String weiBoRedirectUrl) {
        this.weiBoRedirectUrl = weiBoRedirectUrl;
    }


    public String getNoInstallWeiXin() {
        return noInstallWeiXin;
    }

    public void setNoInstallWeiXin(String noInstallWeiXin) {
        this.noInstallWeiXin = noInstallWeiXin;
    }

    public boolean isNeedUserInfo() {
        return isNeedUserInfo;
    }

    public void setNeedUserInfo(boolean needUserInfo) {
        isNeedUserInfo = needUserInfo;
    }

    public boolean isWinXinCode() {
        return isWinXinCode;
    }

    public void setWinXinCode(boolean winXinCode) {
        isWinXinCode = winXinCode;
    }

    public ShareListener getShareListener() {
        return shareListener;
    }

    public void setShareListener(ShareListener shareListener) {

        reset();
        this.shareListener = shareListener;
    }

    public void setiUiListener(IUiListener iUiListener) {
        this.iUiListener = iUiListener;
    }

    public IUiListener getiUiListener() {
        return iUiListener;
    }

    public static void initConfig(Context context,String weiXinAppId, String weiXinSecret, String qqAppId, String weiBoAppId, String weiBoRedirectUrl) {


        mInstance = getInstance();

        mInstance.setWeiXinAppId(weiXinAppId);
        mInstance.setWeiXinSecret(weiXinSecret);
        mInstance.setQqAppId(qqAppId);
        mInstance.setWeiBoAppId(weiBoAppId);
        mInstance.setWeiBoRedirectUrl(weiBoRedirectUrl);

        String redirectUrl = "http://sns.whalecloud.com/sina2/callback";
        if(!TextUtils.isEmpty(weiBoRedirectUrl)){
            redirectUrl = weiBoRedirectUrl;
        }
        WBAPIFactory.createWBAPI(context).registerApp(context, new AuthInfo(context, weiBoAppId, redirectUrl, SCOPE), new SdkListener() {
            @Override
            public void onInitSuccess() {

            }

            @Override
            public void onInitFailure(Exception e) {

            }
        });

    }


    public static CanShare getInstance() {
        if (mInstance == null) {
            mInstance = new CanShare();
        }
        return mInstance;
    }


    public void share(Context context, @ShareType int shareType, ShareContent shareContent, ShareListener shareListener) {

        this.shareListener = shareListener;

        this.shareType = shareType;


        switch (shareType) {

            case ShareType.QQ:


                new ShareQQ(context, qqAppId, true, shareListener).share(shareContent);

                break;


            case ShareType.QZONE:
                new ShareQQ(context, qqAppId, false, shareListener).share(shareContent);

                break;


            case ShareType.WEIXIN:

                new ShareWeiXin(context, weiXinAppId).share(shareContent, ShareWeiXin.FRIEND);

                break;


            case ShareType.WEIXIN_CIRCLE:

                new ShareWeiXin(context, weiXinAppId).share(shareContent, ShareWeiXin.TIME_LINE);

                break;


            case ShareType.SINA:

                shareSina = new ShareSina(context, weiBoAppId, shareListener).share(shareContent);

                break;
            case ShareType.WEIXIN_MINI:

                new ShareWeiXin(context, weiXinAppId).openWxMini(shareContent);

                break;
            case ShareType.QQ_MINI:

                new ShareQQ(context, qqAppId, true, shareListener).openQQMini(shareContent);

                break;

        }

    }


    public void oauth(Context context, @ShareType int shareType, ShareListener shareListener) {

        this.shareListener = shareListener;

        this.shareType = shareType;

        switch (shareType) {

            case ShareType.QQ:

//                if (!ShareUtil.isQQClientAvailable(context)) {
//
//                    if (shareListener != null) {
//
//                        shareListener.onNoInstall(ShareType.QQ,context.getString(R.string.share_install_qq_tips));
//                    }
//
//                    return;
//                }

                OauthQQ oauthQQ = new OauthQQ(context, qqAppId);
                iUiListener = oauthQQ.getiUiListener();
                oauthQQ.login(shareListener);

                break;


            case ShareType.WEIXIN:

                new OauthWeiXin(context, weiXinAppId).login(shareListener);


                break;


            case ShareType.SINA:

                if (WBAPIFactory.createWBAPI(context).isWBAppInstalled()) {

                    if (shareListener != null) {

                        shareListener.onNoInstall(ShareType.SINA,context.getString(R.string.share_install_weibo_tips));
                    }

                    return;
                }

                oauthSina = new OauthSina(context, weiBoAppId).login(shareListener, weiBoRedirectUrl);


                break;
        }


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (oauthSina != null) {
            oauthSina.onActivityResult(requestCode, resultCode, data);
            oauthSina = null;
        }

        if(shareSina!=null){
            shareSina.onActivityResult(requestCode,resultCode,data);
            shareSina = null;
        }


        if (shareType == ShareType.QQ || shareType == ShareType.QZONE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, null);
        }


    }


//    public void onNewIntent(Intent intent, IWeiboHandler.Response response) {
//
//        if (shareSina != null) {
//            shareSina.onNewIntent(intent, response);
//        }
//        shareSina = null;
//    }


//    public void onResponse(BaseResponse baseResponse) {
//
//        if (shareSina != null) {
//            shareSina.onResponse(baseResponse);
//        }
//    }


    public void reset() {
        this.shareType = -1;
        this.oauthSina = null;
        this.shareSina = null;

    }


}
