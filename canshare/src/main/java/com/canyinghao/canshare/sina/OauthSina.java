package com.canyinghao.canshare.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.canyinghao.canshare.CanShare;
import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.listener.ShareListener;
import com.canyinghao.canshare.model.OauthInfo;


import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;

import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;

import com.sina.weibo.sdk.openapi.WBAPIFactory;

/**
 * 新浪登录
 */
public class OauthSina {




    private Activity activity;


    private ShareListener mShareListener;


    private final OauthInfo oauthInfo;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private final IWBAPI mWBAPI;

    public OauthSina(Context context, String appId) {
        activity = (Activity) context;

        oauthInfo = new OauthInfo();

        mWBAPI = WBAPIFactory.createWBAPI(context);


    }


    public OauthSina login(ShareListener shareListener, String url) {

        mShareListener = shareListener;

        mWBAPI.authorize(activity, new WbAuthListener() {
            @Override
            public void onComplete(Oauth2AccessToken accessToken) {
                if (accessToken != null && accessToken.isSessionValid()) {

                    oauthInfo.accesstoken = accessToken.getAccessToken();

                    oauthInfo.refreshtoken = accessToken.getRefreshToken();

                    oauthInfo.unionid = accessToken.getUid();


                    if (mShareListener != null) {
                        mShareListener.onComplete(ShareType.SINA, oauthInfo);
                    }
                    reset();


                } else {

                    if (mShareListener != null) {
                        mShareListener.onError();
                    }
                    reset();
                }
            }

            @Override
            public void onError(UiError error) {
                if (mShareListener != null) {
                    mShareListener.onError();
                }
                reset();
            }

            @Override
            public void onCancel() {
                if (mShareListener != null) {
                    mShareListener.onCancel();
                }
                reset();
            }
        });

        return this;

    }


    private void reset() {
        activity = null;

        mShareListener = null;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mWBAPI.isAuthorizeResult(requestCode, resultCode, data)) {
            mWBAPI.authorizeCallback(activity, requestCode, resultCode, data);
        }
    }

}
