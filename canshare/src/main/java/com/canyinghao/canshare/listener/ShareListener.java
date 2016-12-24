package com.canyinghao.canshare.listener;

import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.model.OauthInfo;

import java.util.HashMap;

/**
 * 分享监听
 */
public interface ShareListener {

    /**
     * 成功
     *
     * @param shareType int
     * @param oauthInfo OauthInfo
     */
    void onComplete(@ShareType int shareType, OauthInfo oauthInfo);

    /**
     * 失败
     */
    void onError();

    /**
     * 取消
     */
    void onCancel();

    /**
     * 取得微信code
     */
    void onWeiXinLogin(String code);

    /**
     * 微信未安装时回调
     */
    void onWeiXinNoInstall(String hint);


}
