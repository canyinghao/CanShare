package com.canyinghao.canshare.weixin;


import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.canyinghao.canshare.CanShare;
import com.canyinghao.canshare.R;
import com.canyinghao.canshare.listener.ShareListener;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信登录
 */
public class OauthWeiXin {

    private static final String SCOPE = "snsapi_userinfo";

    private static final String STATE = "lls_engzo_wechat_login";

    private IWXAPI mIWXAPI;


    public OauthWeiXin(Context context, String appId) {

        if (!TextUtils.isEmpty(appId)) {
            mIWXAPI = WXAPIFactory.createWXAPI(context, appId, true);

            WeiXinHandlerActivity.mIWXAPI = mIWXAPI;

            if (!mIWXAPI.isWXAppInstalled()) {

                String hint = CanShare.getInstance().getNoInstallWeiXin();
                if (TextUtils.isEmpty(hint)) {
                    hint = context.getString(R.string.share_install_wechat_tips);
                }
                Toast.makeText(context.getApplicationContext(), hint, Toast.LENGTH_SHORT).show();

            } else {
                mIWXAPI.registerApp(appId);
            }
        }
    }


    public void login(ShareListener listener) {
        if (mIWXAPI != null) {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = SCOPE;
            req.state = STATE;
            mIWXAPI.sendReq(req);

        }
    }
}
