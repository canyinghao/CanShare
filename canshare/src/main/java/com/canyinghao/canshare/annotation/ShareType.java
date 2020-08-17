package com.canyinghao.canshare.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * 分享类型
 *
 * @author canyinghao
 */
@IntDef({ShareType.QQ, ShareType.QZONE, ShareType.WEIXIN, ShareType.WEIXIN_CIRCLE, ShareType.SINA, ShareType.WEIXIN_MINI, ShareType.QQ_MINI})
@Retention(RetentionPolicy.SOURCE)
public @interface ShareType {
    int QQ = 0;
    int QZONE = 1;
    int WEIXIN = 2;
    int WEIXIN_CIRCLE = 3;
    int SINA = 4;
    int WEIXIN_MINI = 5;
    int QQ_MINI = 6;


}