package com.canyinghao.canshare.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.canyinghao.canshare.CanShare;
import com.socks.library.KLog;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        KLog.init(true, "Canyinghao");

        CanShare.initConfig("wx4d890cb90835c811","5ccde46244e25686f43daff8bb0de196","1104856246","705905453","");


    }

    @OnClick({R.id.btn_login, R.id.btn_share})
    public void click(View v) {

        switch (v.getId()) {

            case R.id.btn_login:

                startActivity(new Intent(this, LoginActivity.class));

                break;


            case R.id.btn_share:
                startActivity(new Intent(this, ShareActivity.class));

                break;

        }
    }
}
