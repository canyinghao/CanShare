package com.canyinghao.canshare.qq;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;


import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.constants.ShareConstants;
import com.canyinghao.canshare.listener.ShareListener;
import com.canyinghao.canshare.model.OauthInfo;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * QQ登录
 */

public class OauthQQ {


    private Context mContext;


    private Tencent mTencent;


    private OauthInfo oauthInfo;


    private ShareListener shareListener;


    public OauthQQ(Context context, String appId) {
        mContext = context;

        try {
            mTencent = Tencent.createInstance(appId, context);
        } catch (Exception e) {
            e.printStackTrace();
        }


        oauthInfo = new OauthInfo();

    }


    private void initOpenidAndToken(JSONObject jsonObject) {

        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);

            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);

                oauthInfo.accesstoken = token;
                oauthInfo.openid = openId;


            }
        } catch (Exception e) {

            e.printStackTrace();
        }


    }


    public void login(ShareListener mShareListener) {
        if (!mTencent.isSessionValid()) {


            this.shareListener = mShareListener;

            mTencent.login((Activity) mContext, "all", new IUiListener() {
                @Override
                public void onComplete(Object object) {
                    JSONObject jsonObject = (JSONObject) object;

                    initOpenidAndToken(jsonObject);
                    UserInfo info = new UserInfo(mContext, mTencent.getQQToken());
                    info.getUserInfo(new IUiListener() {
                        @Override
                        public void onComplete(Object object) {


                            try {
                                JSONObject jsonObject = (JSONObject) object;


                                oauthInfo.nickname = jsonObject.getString("nickname");

                                oauthInfo.headimgurl = jsonObject.getString("figureurl_qq_2");

                                oauthInfo.sex = jsonObject.getString("gender");


                                if (shareListener != null) {


                                    shareListener
                                            .onComplete(ShareType.QQ, oauthInfo);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                if (shareListener != null) {
                                    shareListener
                                            .onError();
                                }
                            }


                        }


                        @Override
                        public void onError(UiError uiError) {


                            if (shareListener != null) {
                                shareListener
                                        .onError();
                            }
                        }

                        @Override
                        public void onCancel() {


                            if (shareListener != null) {
                                shareListener
                                        .onCancel();
                            }
                        }
                    });
                }

                @Override
                public void onError(UiError uiError) {


                    if (shareListener != null) {
                        shareListener
                                .onError();
                    }
                }

                @Override
                public void onCancel() {


                    if (shareListener != null) {
                        shareListener
                                .onCancel();
                    }
                }
            });

        } else {
            mTencent.logout(mContext);
        }
    }
}
