package com.canyinghao.canshare.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.listener.ShareListener;
import com.canyinghao.canshare.model.OauthInfo;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianyang on 2016/12/17.
 */

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.btn_qq)
    AppCompatButton btnQq;
    @BindView(R.id.btn_sina)
    AppCompatButton btnSina;
    @BindView(R.id.btn_weixin)
    AppCompatButton btnWeixin;
    @BindView(R.id.shareView)
    ShareView shareView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        shareView.setShareListener(new ShareListener() {
            @Override
            public void onComplete(@ShareType int shareType, OauthInfo oauthInfo) {

                Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();

                if (oauthInfo != null) {

                    KLog.e(oauthInfo.toString());
                }
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @OnClick({R.id.btn_qq, R.id.btn_sina, R.id.btn_weixin})
    public void click(View v) {


        switch (v.getId()) {

            case R.id.btn_qq:
                shareView.qqLogin();

                break;

            case R.id.btn_sina:

                shareView.sinaLogin();
                break;


            case R.id.btn_weixin:

                shareView.weiChatLogin();

                break;


        }


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareView.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareView.onActivityResult(requestCode, resultCode, data);
    }
}
