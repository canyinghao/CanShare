package com.canyinghao.canshare.model;

import android.graphics.Bitmap;

import com.canyinghao.canshare.constants.ShareConstants;

import java.util.ArrayList;


/**
 * 分享内容
 */
public class ShareContent {


    public String content;
    public String title;
    public String URL;
    public String targetUrl;
    public String imageUrl;
    public ArrayList<String> mImageUrls;
    public String mMusicUrl;

    public Bitmap mShareImageBitmap;

    public int shareWay  = ShareConstants.SHARE_WAY_WEBPAGE;


    public Bitmap getShareImageBitmap() {
        return mShareImageBitmap;
    }

    public void setShareImageBitmap(Bitmap shareImageBitmap) {
        mShareImageBitmap = shareImageBitmap;
    }

    public String getMusicUrl() {
        return mMusicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        mMusicUrl = musicUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public ArrayList<String> getImageUrls() {
        return mImageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        mImageUrls = imageUrls;
    }


    public int getShareWay() {
        return shareWay;
    }

    public void setShareWay(int shareWay) {
        this.shareWay = shareWay;
    }
}
