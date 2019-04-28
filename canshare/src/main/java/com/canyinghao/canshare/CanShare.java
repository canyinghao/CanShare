package com.canyinghao.canshare;

import android.content.Context;
import android.content.Intent;

import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.listener.ShareListener;
import com.canyinghao.canshare.model.ShareContent;
import com.canyinghao.canshare.qq.OauthQQ;
import com.canyinghao.canshare.qq.ShareQQ;
import com.canyinghao.canshare.sina.OauthSina;
import com.canyinghao.canshare.sina.ShareSina;
import com.canyinghao.canshare.utils.ShareUtil;
import com.canyinghao.canshare.weixin.OauthWeiXin;
import com.canyinghao.canshare.weixin.ShareWeiXin;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.tauth.Tencent;

/**
 * Created by canyinghao on 2016/12/14.
 */

public class CanShare {


    private static CanShare mInstance;


    private String weiXinAppId;
    private String weiXinSecret;

    private String qqAppId;

    private String weiBoAppId;
    private String weiBoRedirectUrl;

    private String noInstallWeiXin;


    //   是否需要获取用户呢称等信息
    private boolean isNeedUserInfo;

    //    微信登录时直接返回code
    private boolean isWinXinCode;

    private ShareListener shareListener;

    private int shareType = -1;


    private OauthSina oauthSina;

    private ShareSina shareSina;


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

    public static void initConfig(String weiXinAppId, String weiXinSecret, String qqAppId, String weiBoAppId, String weiBoRedirectUrl) {


        mInstance = getInstance();

        mInstance.setWeiXinAppId(weiXinAppId);
        mInstance.setWeiXinSecret(weiXinSecret);
        mInstance.setQqAppId(qqAppId);
        mInstance.setWeiBoAppId(weiBoAppId);
        mInstance.setWeiBoRedirectUrl(weiBoRedirectUrl);


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

        }


    }


    public void oauth(Context context, @ShareType int shareType, ShareListener shareListener) {

        this.shareListener = shareListener;

        this.shareType = shareType;

        switch (shareType) {

            case ShareType.QQ:

                if (!ShareUtil.isQQClientAvailable(context)) {

                    if (shareListener != null) {

                        shareListener.onQQNoInstall(context.getString(R.string.share_install_qq_tips));
                    }

                    return;
                }

                OauthQQ oauthQQ = new OauthQQ(context, qqAppId);

                oauthQQ.login(shareListener);

                break;


            case ShareType.WEIXIN:

                new OauthWeiXin(context, weiXinAppId).login(shareListener);


                break;


            case ShareType.SINA:


                oauthSina = new OauthSina(context, weiBoAppId).login(shareListener, weiBoRedirectUrl);


                break;
        }


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (oauthSina != null) {
            SsoHandler mSsoHandler = oauthSina.getSsoHandler();
            // SSO 授权回调
            // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
            if (mSsoHandler != null) {
                mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            }
        }

        oauthSina = null;

        if (shareType == ShareType.QQ || shareType == ShareType.QZONE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, null);
        }


    }


    public void onNewIntent(Intent intent, IWeiboHandler.Response response) {

        if (shareSina != null) {
            shareSina.onNewIntent(intent, response);
        }
        shareSina = null;
    }


    public void onResponse(BaseResponse baseResponse) {

        if (shareSina != null) {
            shareSina.onResponse(baseResponse);
        }
    }


    public void reset() {
        this.shareType = -1;
        this.oauthSina = null;
        this.shareSina = null;

    }


}
