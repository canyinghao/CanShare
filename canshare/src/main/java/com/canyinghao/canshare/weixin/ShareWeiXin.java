package com.canyinghao.canshare.weixin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.canyinghao.canshare.CanShare;
import com.canyinghao.canshare.R;
import com.canyinghao.canshare.annotation.ShareType;
import com.canyinghao.canshare.constants.ShareConstants;
import com.canyinghao.canshare.listener.ShareListener;
import com.canyinghao.canshare.model.ShareContent;
import com.canyinghao.canshare.utils.BitmapUtil;
import com.canyinghao.canshare.utils.ShareUtil;
import com.socks.library.KLog;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.core.content.FileProvider;

/**
 * 微信分享
 */
public class ShareWeiXin {


    /**
     * friends
     */
    public static final int FRIEND = SendMessageToWX.Req.WXSceneSession;

    /**
     * friends TimeLine
     */
    public static final int TIME_LINE = SendMessageToWX.Req.WXSceneTimeline;

    @IntDef(value = {FRIEND, TIME_LINE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WeiChatShareType {

    }


//    // 缩略图大小 = 116 微信里头xdpi就是以这个尺寸展示的, 并且这个尺寸平衡了大小与32k缩略图的限制
//    private static final int THUMB_SIZE = 116;
//
//
//    public static int THUMB_MINI_SIZE  = 116*3;
//
//    public static void setThumbMiniSize(int thumbMiniSize) {
//        THUMB_MINI_SIZE = thumbMiniSize;
//    }

    private String mWeChatAppId;
    private Context context;


    public ShareWeiXin(Context context, String appId) {

        mWeChatAppId = appId;
        this.context = context;
        if (!TextUtils.isEmpty(mWeChatAppId)) {
            initWeixinShare(context);
        }

    }


    private void initWeixinShare(Context context) {
        WeiXinHandlerActivity.mIWXAPI = WXAPIFactory.createWXAPI(context.getApplicationContext(), mWeChatAppId, true);


        if (!WeiXinHandlerActivity.mIWXAPI.isWXAppInstalled()) {

            String hint = CanShare.getInstance().getNoInstallWeiXin();
            if (TextUtils.isEmpty(hint)) {

                hint = context.getString(R.string.share_install_wechat_tips);
            }

            ShareListener shareListener = CanShare.getInstance().getShareListener();

            if (shareListener != null) {
                shareListener.onNoInstall(ShareType.WEIXIN, hint);
            }

        } else {
            WeiXinHandlerActivity.mIWXAPI.registerApp(mWeChatAppId);
        }
    }


    private void shareText(int shareType, ShareContent shareContent) {

        String text = shareContent.getContent();
        //初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        //用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //transaction字段用于唯一标识一个请求
        req.transaction = ShareUtil.buildTransaction("textshare");
        req.message = msg;
        //发送的目标场景， 可以选择发送到会话 WXSceneSession 或者朋友圈 WXSceneTimeline。 默认发送到会话。
        req.scene = shareType;
        WeiXinHandlerActivity.mIWXAPI.sendReq(req);
    }


    private void sharePicture(int shareType, ShareContent shareContent) {
        WXImageObject imgObj = new WXImageObject();
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = ShareUtil.buildTransaction("imgshareappdata");
        req.message = msg;
        req.scene = shareType;
        sendShare(shareContent, req);
    }


    private void shareWebPage(final int shareType, final ShareContent shareContent) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareContent.getURL();
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareContent.getTitle();
        msg.description = shareContent.getContent();

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = ShareUtil.buildTransaction("webpage");
        req.message = msg;
        req.scene = shareType;
        sendShare(shareContent, req);
    }


    private void shareMusic(int shareType, ShareContent shareContent) {

        WXMusicObject music = new WXMusicObject();
        //Str1+"#wechat_music_url="+str2 ;str1是网页地址，str2是音乐地址。

        music.musicUrl = shareContent.getURL() + "#wechat_music_url=" + shareContent.getMusicUrl();
        WXMediaMessage msg = new WXMediaMessage(music);
        msg.title = shareContent.getTitle();
        msg.description = shareContent.getContent();

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = ShareUtil.buildTransaction("music");
        req.message = msg;
        req.scene = shareType;
        sendShare(shareContent, req);
    }


    public void shareMini(int shareType, ShareContent shareContent) {
        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        miniProgramObj.webpageUrl = shareContent.getURL(); // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = shareContent.getMiniProgramUserName();     // 小程序原始id
        miniProgramObj.path = shareContent.getMiniProgramPath();            //小程序页面路径
        WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
        msg.title = shareContent.getTitle();
        msg.description = shareContent.getContent();

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = ShareUtil.buildTransaction("webpage");
        req.message = msg;
        req.scene = shareType;
        sendShare(shareContent, req);


    }

    public void openWxMini(ShareContent shareContent) {

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = shareContent.miniProgramUserName; // 填小程序原始id
        req.path = shareContent.miniProgramPath;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        WeiXinHandlerActivity.mIWXAPI.sendReq(req);

    }


    public void share(ShareContent content, @WeiChatShareType int shareType) {
        switch (content.getShareWay()) {
            case ShareConstants.SHARE_WAY_TEXT:
                shareText(shareType, content);
                break;
            case ShareConstants.SHARE_WAY_PIC:
                sharePicture(shareType, content);
                break;
            case ShareConstants.SHARE_WAY_WEBPAGE:
                shareWebPage(shareType, content);
                break;
            case ShareConstants.SHARE_WAY_MUSIC:
                shareMusic(shareType, content);
                break;
            case ShareConstants.SHARE_WAY_MINI:
                shareMini(shareType, content);
                break;
        }
    }

    private void sendShare(ShareContent shareContent, final SendMessageToWX.Req req) {

        try {
            Bitmap bitmap = shareContent.getShareImageBitmap();

            if (bitmap != null) {
                if (req.message.mediaObject instanceof WXImageObject) {
                    WXImageObject imageObject = null;

                    String imageUrl = shareContent.getImageUrl();
                    if (!TextUtils.isEmpty(imageUrl) && imageUrl.startsWith("file://")) {
                        imageObject = new WXImageObject();
                        imageObject.imagePath = imageUrl.replace("file://", "");
                        if(shareContent.isUserProvider){
                            if (checkVersionValid(context) && checkAndroidNotBelowN()) {
                                File file = new File(imageObject.imagePath);
                                imageObject.imagePath = getFileUri(context, file);
                                KLog.i("shareToWeixin:"+imageObject.imagePath);
                            }
                        }

                    } else {
                        imageObject = new WXImageObject(bitmap);
                    }
                    req.message.mediaObject = imageObject;
                }

                if (shareContent.getShareWay() == ShareConstants.SHARE_WAY_MINI) {
                    bitmap = BitmapUtil.scaleCenterCrop(bitmap);
                    req.message.thumbData = ShareUtil.getCompressBitmap(bitmap, 128);

                } else {
                    req.message.thumbData = ShareUtil.getCompressBitmap(bitmap, 32);
                }

            }
            // 就算图片没有了 尽量能发出分享
            WeiXinHandlerActivity.isWeixinCircle = req.scene == TIME_LINE;
            WeiXinHandlerActivity.mIWXAPI.sendReq(req);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    // 判断微信版本是否为7.0.13及以上
    public boolean checkVersionValid(Context context) {
        return WeiXinHandlerActivity.mIWXAPI.getWXAppSupportAPI() >= 0x27000D00;
    }

    // 判断Android版本是否7.0及以上
    public boolean checkAndroidNotBelowN() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N;
    }

    public String getFileUri(Context context, File file) {
        if (file == null || !file.exists()) {
            return null;
        }

        Uri contentUri = FileProvider.getUriForFile(context,
                context.getPackageName() + ".fileprovider",  // 要与`AndroidManifest.xml`里配置的`authorities`一致，假设你的应用包名为com.example.app
                file);

        // 授权给微信访问路径
        context.grantUriPermission("com.tencent.mm",  // 这里填微信包名
                contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        return contentUri.toString();   // contentUri.toString() 即是以"content://"开头的用于共享的路径
    }

}
