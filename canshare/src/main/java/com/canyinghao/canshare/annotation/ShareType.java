package com.canyinghao.canshare.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 分享类型
 *
 * @author canyinghao
 */
@IntDef({ShareType.QQ, ShareType.QZONE, ShareType.WEIXIN, ShareType.WEIXIN_CIRCLE, ShareType.SINA})
@Retention(RetentionPolicy.SOURCE)
public @interface ShareType {
    int QQ = 0;
    int QZONE = 1;
    int WEIXIN = 2;
    int WEIXIN_CIRCLE = 3;
    int SINA = 4;

}