package com.canyinghao.canshare.weixin;

import com.canyinghao.canokhttp.CanOkHttp;
import com.canyinghao.canokhttp.annotation.CacheType;
import com.canyinghao.canokhttp.callback.CanSimpleCallBack;
import com.canyinghao.canshare.CanShare;
import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.listener.ShareListener;
import com.canyinghao.canshare.model.OauthInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jianyang on 2016/12/21.
 */

public class WeiXinUtils {


    public static final String API_URL = "https://api.weixin.qq.com";


    public void getWeiXinToken(String code, final ShareListener mShareListener) {
        CanOkHttp.getInstance().add("appid", CanShare.getInstance().getWeiXinAppId())
                .add("secret", CanShare.getInstance().getWeiXinSecret())
                .add("code", code)
                .add("grant_type", "authorization_code")
                .url(API_URL + "/sns/oauth2/access_token")
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

                            OauthInfo oauthInfo = new OauthInfo();
                            oauthInfo.accesstoken = accessToken;
                            oauthInfo.openid = openId;
                            oauthInfo.unionid = unionid;
                            oauthInfo.refreshtoken = refreshToken;


                            if (mShareListener != null) {
                                mShareListener
                                        .onComplete(ShareType.WEIXIN,
                                                oauthInfo);
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


}
