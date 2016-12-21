package com.canyinghao.canshare.weixin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.canyinghao.canokhttp.CanOkHttp;
import com.canyinghao.canokhttp.annotation.CacheType;
import com.canyinghao.canokhttp.callback.CanSimpleCallBack;
import com.canyinghao.canshare.CanShare;
import com.canyinghao.canshare.R;
import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.constants.ShareConstants;
import com.canyinghao.canshare.listener.ShareListener;
import com.canyinghao.canshare.model.OauthInfo;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * 微信回调页面
 */
public class WeiXinHandlerActivity extends Activity implements IWXAPIEventHandler {

    public static IWXAPI mIWXAPI;

    private ShareListener mShareListener;



    /**
     * BaseResp的getType函数获得的返回值，1:第三方授权， 2:分享
     */
    private static final int TYPE_LOGIN = 1;

    private OauthInfo oauthInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (mIWXAPI != null) {
            mIWXAPI.handleIntent(getIntent(), this);
        }
        finish();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mIWXAPI != null) {
            mIWXAPI.handleIntent(getIntent(), this);
        }
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }


    @Override
    public void onResp(BaseResp resp) {


        mShareListener = CanShare.getInstance().getShareListener();


        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:

                if (resp.getType() == TYPE_LOGIN) {

                    oauthInfo = new OauthInfo();

                    final String code = ((SendAuth.Resp) resp).code;

                    boolean isCode = CanShare.getInstance().isWinXinCode();

                    if (isCode) {

                        if (mShareListener != null) {
                            mShareListener.onWeiXinLogin(code);
                        }

                    } else {
                        getWeiXinToken(code);
                    }


                } else {


                    if (mShareListener != null) {
                        mShareListener
                                .onComplete(ShareType.WEIXIN,
                                        null);
                    }
                }


                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:


                if (mShareListener != null) {
                    mShareListener
                            .onCancel();
                }


                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:

                if (mShareListener != null) {
                    mShareListener
                            .onError();
                }


                break;
        }
        finish();
    }

    public void getWeiXinToken(String code) {
        CanOkHttp.getInstance().add("appid", CanShare.getInstance().getWeiXinAppId())
                .add("secret", CanShare.getInstance().getWeiXinSecret())
                .add("code", code)
                .add("grant_type", "authorization_code")
                .url(WeiXinUtils.API_URL + "/sns/oauth2/access_token")
                .setCacheType(CacheType.NETWORK)
                .post()
                .setCallBack(new CanSimpleCallBack() {

                    @Override
                    public void onResponse(Object result) {
                        super.onResponse(result);


                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result.toString());

                            final String accessToken = jsonObject
                                    .getString("access_token");
                            final String openId = jsonObject.getString("openid");
                            final String refreshToken = jsonObject.getString("refresh_token");
                            final String unionid = jsonObject.getString("unionid");

                            if (oauthInfo != null) {
                                oauthInfo.accesstoken = accessToken;
                                oauthInfo.openid = openId;
                                oauthInfo.unionid = unionid;
                                oauthInfo.refreshtoken = refreshToken;
                            }

                            boolean isNeed = CanShare.getInstance().isNeedUserInfo();

                            if (isNeed) {
                                getWeiXinUserInfo(accessToken, openId);
                            } else {
                                if (mShareListener != null) {
                                    mShareListener
                                            .onComplete(ShareType.WEIXIN,
                                                    oauthInfo);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (mShareListener != null) {
                                mShareListener.onError();
                            }

                        }


                    }

                    @Override
                    public void onFailure(int type, int code, String e) {
                        super.onFailure(type, code, e);

                        if (mShareListener != null) {
                            mShareListener.onError();
                        }
                    }
                });
    }


    private void getWeiXinUserInfo(String accessToken, String openid) {


        CanOkHttp.getInstance().add("access_token", accessToken)
                .add("openid", openid)
                .setCacheType(CacheType.NETWORK)
                .url(WeiXinUtils.API_URL + "/sns/userinfo")
                .post()
                .setCallBack(new CanSimpleCallBack() {


                    @Override
                    public void onResponse(Object result) {
                        super.onResponse(result);


                        try {


                            if (oauthInfo != null) {

                                JSONObject jsonObject = new JSONObject(
                                        result.toString());

                                oauthInfo.nickname = jsonObject.getString(
                                        "nickname");

                                oauthInfo.headimgurl = jsonObject.getString(
                                        "headimgurl");

                                oauthInfo.unionid = jsonObject
                                        .getString(
                                                "unionid");


                                oauthInfo.sex = jsonObject.getString(
                                        "sex");
                            }


                            if (mShareListener != null) {
                                mShareListener
                                        .onComplete(ShareType.WEIXIN,
                                                oauthInfo);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int type, int code, String e) {
                        super.onFailure(type, code, e);

                        if (mShareListener != null) {
                            mShareListener.onError();
                        }


                    }
                });


    }

}
