package com.canyinghao.canshare.sina;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.canyinghao.canshare.CanShare;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;

/**
 * Created by canyinghao on 2016/12/20.
 */


public class SinaHandlerActivity extends AppCompatActivity implements IWeiboHandler.Response {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CanShare.getInstance().onNewIntent(getIntent(), this);
        finish();


    }

    @Override
    public void onResponse(BaseResponse baseResponse) {


    }
}
