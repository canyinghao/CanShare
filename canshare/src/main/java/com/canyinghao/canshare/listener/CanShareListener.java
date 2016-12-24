package com.canyinghao.canshare.listener;

import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.model.OauthInfo;

/**
 * 分享监听
 */
public class CanShareListener implements ShareListener {


    @Override
    public void onComplete(@ShareType int shareType, OauthInfo oauthInfo) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onWeiXinLogin(String code) {

    }

    @Override
    public void onWeiXinNoInstall(String hint) {

    }
}
