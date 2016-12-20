package com.canyinghao.canshare.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;

import com.canyinghao.canshare.CanShare;
import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.constants.ShareConstants;
import com.canyinghao.canshare.listener.ShareListener;
import com.canyinghao.canshare.model.ShareContent;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;

/**
 * Created by canyinghao on 2016/12/15.
 */

public class ShareView extends View {


    private ShareListener shareListener;

    private Activity context;

    public ShareView(Context context) {
        this(context, null);
    }

    public ShareView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }


    protected void onCreate() {
        context = (Activity) getContext();


    }

    public void setShareListener(ShareListener shareListener) {
        this.shareListener = shareListener;
    }

    ///////////////////////////////////////////////////////////////////////////
    // qq
    ///////////////////////////////////////////////////////////////////////////


    public void qqLogin() {

        CanShare.getInstance().oauth(context, ShareType.QQ, shareListener);

    }


    public void qqZoneShare() {

        CanShare.getInstance().share(context, ShareType.QZONE, getShareBean(), shareListener);

    }

    public void qqShare() {
        CanShare.getInstance().share(context, ShareType.QQ, getShareBean(), shareListener);

    }


    ///////////////////////////////////////////////////////////////////////////
    // 微信
    ///////////////////////////////////////////////////////////////////////////

    public void weiChatLogin() {
        CanShare.getInstance().oauth(context, ShareType.WEIXIN, shareListener);
    }

    public void weiChatShare() {
        CanShare.getInstance().share(context, ShareType.WEIXIN, getShareBean(), shareListener);
    }

    public void weiChatTimeLineShare() {
        CanShare.getInstance().share(context, ShareType.WEIXIN_CIRCLE, getShareBean(), shareListener);

    }


    ///////////////////////////////////////////////////////////////////////////
    // 新浪
    ///////////////////////////////////////////////////////////////////////////


    public void sinaLogin() {
        CanShare.getInstance().oauth(context, ShareType.SINA, shareListener);
    }

    public void sinaShare() {
        CanShare.getInstance().share(context, ShareType.SINA, getShareBean(), shareListener);
    }


    public void onNewIntent(Intent intent, IWeiboHandler.Response response) {
        CanShare.getInstance().onNewIntent(intent, response);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CanShare.getInstance().onActivityResult(requestCode, resultCode, data);

    }

    public void onResponse(BaseResponse baseResponse) {
        CanShare.getInstance().onResponse(baseResponse);
    }

    private ShareContent shareContent;


    public void setShareContent(ShareContent shareContent) {
        this.shareContent = shareContent;
    }

    public ShareContent getShareBean() {

        if (shareContent == null) {

            shareContent = new ShareContent();
            shareContent.setContent("分享天下事");
            shareContent.setTitle("知不知");
            shareContent.setURL("http://www.zzzz.com");
            shareContent.setImageUrl("http://ww3.sinaimg.cn/large/7a8aed7bjw1ezbriom623j20hs0qoadv.jpg");
            //分析PIC或者WEBPAGE请添加图片，直接添加bitmap
            shareContent.setShareImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            shareContent.setShareWay(ShareConstants.SHARE_WAY_WEBPAGE);
        }


        return shareContent;

    }

}
