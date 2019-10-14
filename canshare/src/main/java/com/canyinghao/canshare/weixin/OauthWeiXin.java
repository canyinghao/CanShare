package com.canyinghao.canshare.weixin;


import android.content.Context;
import android.text.TextUtils;

import com.canyinghao.canshare.CanShare;
import com.canyinghao.canshare.R;
import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.listener.ShareListener;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信登录
 */
public class OauthWeiXin {

    private static final String SCOPE = "snsapi_userinfo";

    private static final String STATE = "lls_engzo_wechat_login";




    public OauthWeiXin(Context context, String appId) {

        if (!TextUtils.isEmpty(appId)) {
            WeiXinHandlerActivity.mIWXAPI = WXAPIFactory.createWXAPI(context.getApplicationContext(), appId, true);



            if (!WeiXinHandlerActivity.mIWXAPI.isWXAppInstalled()) {

                String hint = CanShare.getInstance().getNoInstallWeiXin();
                if (TextUtils.isEmpty(hint)) {
                    hint = context.getString(R.string.share_install_wechat_tips);
                }

                ShareListener shareListener = CanShare.getInstance().getShareListener();

                if (shareListener != null) {
                    shareListener.onNoInstall(ShareType.WEIXIN,hint);
                }

            } else {
                WeiXinHandlerActivity.mIWXAPI.registerApp(appId);
            }
        }
    }


    public void login(ShareListener listener) {
        if (WeiXinHandlerActivity.mIWXAPI != null) {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = SCOPE;
            req.state = STATE;
            WeiXinHandlerActivity.mIWXAPI.sendReq(req);

        }
    }
}
