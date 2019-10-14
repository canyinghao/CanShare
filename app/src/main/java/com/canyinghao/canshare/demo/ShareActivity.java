package com.canyinghao.canshare.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.listener.CanShareListener;
import com.canyinghao.canshare.model.OauthInfo;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by canyinghao on 2016/12/17.
 */

public class ShareActivity extends AppCompatActivity implements IWeiboHandler.Response {

    @BindView(R.id.btn_sina)
    AppCompatButton btnSina;
    @BindView(R.id.btn_qq)
    AppCompatButton btnQq;
    @BindView(R.id.btn_qzone)
    AppCompatButton btnQzone;
    @BindView(R.id.btn_weixin)
    AppCompatButton btnWeixin;
    @BindView(R.id.btn_weixin_circle)
    AppCompatButton btnWeixinCircle;
    @BindView(R.id.shareView)
    ShareView shareView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);


        shareView.setShareListener(new CanShareListener() {
            @Override
            public void onComplete(@ShareType int shareType, OauthInfo oauthInfo) {

                Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNoInstall(int shareType, String hint) {
                super.onNoInstall(shareType,hint);
                Toast.makeText(getApplicationContext(), hint, Toast.LENGTH_SHORT).show();
            }

        });
    }


    @OnClick({R.id.btn_sina, R.id.btn_qq, R.id.btn_qzone, R.id.btn_weixin, R.id.btn_weixin_circle})
    public void click(View v) {

        switch (v.getId()) {


            case R.id.btn_sina:


                shareView.sinaShare();

                break;


            case R.id.btn_qq:

                shareView.qqShare();

                break;


            case R.id.btn_qzone:

                shareView.qqZoneShare();

                break;


            case R.id.btn_weixin:

                shareView.weiChatShare();

                break;


            case R.id.btn_weixin_circle:

                shareView.weiChatTimeLineShare();

                break;


        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareView.onNewIntent(intent, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {

        shareView.onResponse(baseResponse);
    }
}
