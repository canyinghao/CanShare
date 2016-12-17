package com.canyinghao.canshare.model;

import java.io.Serializable;

/**
 *
 * 第三方登录返回数据
 */

public class OauthInfo implements Serializable {


    public  String nickname;
    public  String sex;
    public  String headimgurl;
    public  String unionid;
    public  String accesstoken;
    public  String refreshtoken;

    public  String openid;


    @Override
    public String toString() {
        return "OauthInfo{" +
                "nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", unionid='" + unionid + '\'' +
                ", accesstoken='" + accesstoken + '\'' +
                ", refreshtoken='" + refreshtoken + '\'' +
                ", openid='" + openid + '\'' +
                '}';
    }
}
