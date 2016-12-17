package com.canyinghao.canshare.sina;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;


import com.canyinghao.canshare.CanShare;
import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.constants.ShareConstants;
import com.canyinghao.canshare.listener.ShareListener;
import com.canyinghao.canshare.model.OauthInfo;
import com.canyinghao.canshare.sina.model.User;
import com.canyinghao.canshare.sina.model.UsersAPI;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import java.util.HashMap;

/**
 * 新浪登录
 */
public class OauthSina {


    private String redirectUrl = "http://sns.whalecloud.com/sina2/callback";

    private static final String SCOPE =
            "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog";

    private Context mContext;

    private String mSinaAppKey;


    private ShareListener mShareListener;


    private OauthInfo oauthInfo;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;


    public OauthSina(Context context, String appId) {

        mContext = context;
        mSinaAppKey = appId;

        oauthInfo = new OauthInfo();

    }


    public SsoHandler getSsoHandler() {
        return mSsoHandler;
    }

    public OauthSina login(ShareListener shareListener, String url) {

        if (!TextUtils.isEmpty(url)) {
            redirectUrl = url;
        }
        mShareListener = shareListener;
        AccessTokenKeeper.clear(mContext);
        AuthInfo mAuthInfo = new AuthInfo(mContext, mSinaAppKey, redirectUrl, SCOPE);
        mSsoHandler = new SsoHandler((Activity) mContext, mAuthInfo);
        mSsoHandler.authorize(new AuthListener());

        return this;

    }

    /**
     * * 1. SSO 授权时，需要在 onActivityResult 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非SSO 授权时，当授权结束后，该回调就会被执行
     */
    private class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            final Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(mContext, accessToken);
                UsersAPI userAPI = new UsersAPI(mContext, mSinaAppKey, accessToken);

                oauthInfo.accesstoken = accessToken.getToken();

                oauthInfo.refreshtoken = accessToken.getRefreshToken();

                oauthInfo.unionid = accessToken.getUid();


                userAPI.show(Long.parseLong(accessToken.getUid()), mListener);
            } else {

                if (mShareListener != null) {
                    mShareListener.onError();
                }
            }

        }

        @Override
        public void onWeiboException(WeiboException e) {
            if (mShareListener != null) {
                mShareListener.onError();
            }
        }

        @Override
        public void onCancel() {
            if (mShareListener != null) {
                mShareListener.onCancel();
            }
        }
    }

    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {

            boolean isSuccess = false;
            if (!TextUtils.isEmpty(response)) {

                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {

                    oauthInfo.nickname = user.name;
                    oauthInfo.sex = user.gender;
                    oauthInfo.headimgurl = user.avatar_large;

                    isSuccess = true;
                    if (mShareListener != null) {
                        mShareListener.onComplete(ShareType.SINA, oauthInfo);
                    }
                }
            }

            if (!isSuccess) {
                if (mShareListener != null) {
                    mShareListener.onError();
                }

            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            if (mShareListener != null) {
                mShareListener.onError();
            }
        }
    };


}
