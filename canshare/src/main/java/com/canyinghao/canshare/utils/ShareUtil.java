package com.canyinghao.canshare.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * 分享工具类
 */
public class ShareUtil {


    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }


    public static byte[] bmpToByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        } else {
            byte[] b = null;
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                b = byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return b;
        }
    }

    /**
     * 图片压缩
     *
     * @param image
     * @return
     */
    public static byte[] getCompressBitmap(Bitmap image, int minSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > minSize && options > 0) {
            options -= 5;
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        return baos.toByteArray();

    }


//    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
//    public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
//        Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//
//        try {
//            options.inJustDecodeBounds = true;
//            Bitmap tmp = BitmapFactory.decodeFile(path, options);
//            if (tmp != null) {
//                tmp.recycle();
//                tmp = null;
//            }
//
//            final double beY = options.outHeight * 1.0 / height;
//            final double beX = options.outWidth * 1.0 / width;
//            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
//            if (options.inSampleSize <= 1) {
//                options.inSampleSize = 1;
//            }
//
//            // NOTE: out of memory error
//            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
//                options.inSampleSize++;
//            }
//
//            int newHeight = height;
//            int newWidth = width;
//            if (crop) {
//                if (beY > beX) {
//                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
//                } else {
//                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
//                }
//            } else {
//                if (beY < beX) {
//                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
//                } else {
//                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
//                }
//            }
//
//            options.inJustDecodeBounds = false;
//            Bitmap bm = BitmapFactory.decodeFile(path, options);
//            if (bm == null) {
//                return null;
//            }
//
//            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
//            if (scale != null) {
//                bm.recycle();
//                bm = scale;
//            }
//
//            if (crop) {
//                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
//                if (cropped == null) {
//                    return bm;
//                }
//
//                bm.recycle();
//                bm = cropped;
//            }
//            return bm;
//
//        } catch (final OutOfMemoryError e) {
//            options = null;
//        }
//
//        return null;
//    }


    public static boolean isWeixinAvilible(Context context) {

        try {
            final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equals("com.tencent.mm")) {
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context Context
     * @return boolean
     */
    public static boolean isQQClientAvailable(Context context) {
        try {
            final PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equals("com.tencent.mobileqq") || pn.equals("com.tencent.tim")) {
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }


}
