package com.canyinghao.canshare.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * bitmap工具
 */
public class BitmapUtil {

    // 32k 缩略图的限制，所以采用 RGB_565 比较小
    private static final Bitmap.Config CONFIG = Bitmap.Config.RGB_565;


    // 根据给定的高宽居中缩放+裁剪
    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        Bitmap dest = source;
        try {
            int sourceWidth = source.getWidth();
            int sourceHeight = source.getHeight();

            // Compute the scaling factors to fit the new height and width, respectively.
            // To cover the final image, the final scaling will be the bigger
            // of these two.
            float xScale = (float) newWidth / sourceWidth;
            float yScale = (float) newHeight / sourceHeight;
            float scale = Math.max(xScale, yScale);

            // Now get the size of the source bitmap when scaled
            float scaledWidth = scale * sourceWidth;
            float scaledHeight = scale * sourceHeight;

            // Let's find out the upper left coordinates if the scaled bitmap
            // should be centered in the new size give by the parameters
            float left = (newWidth - scaledWidth) / 2;
            float top = (newHeight - scaledHeight) / 2;

            // The target rectangle for the new, scaled version of the source bitmap will now
            // be
            RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

            // Finally, we create a new bitmap of the specified size and draw our new,
            // scaled bitmap onto it.
            dest = Bitmap.createBitmap(newWidth, newHeight, CONFIG);
            Canvas canvas = new Canvas(dest);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            canvas.drawBitmap(source, null, targetRect, paint);

        } catch (Throwable e) {
            e.printStackTrace();
        }


        return dest;
    }


    // 根据给定的高宽居中缩放+裁剪
    public static Bitmap scaleCenterCrop(Bitmap source) {
        Bitmap dest = source;

        try {
            int sourceWidth = source.getWidth();
            int sourceHeight = source.getHeight();

            if(sourceHeight == sourceWidth){
                return dest;
            }

            int newWidth = Math.min(sourceWidth,sourceHeight);
            int newHeight = Math.min(sourceWidth,sourceHeight);


            // Compute the scaling factors to fit the new height and width, respectively.
            // To cover the final image, the final scaling will be the bigger
            // of these two.
            float xScale = (float) newWidth / sourceWidth;
            float yScale = (float) newHeight / sourceHeight;
            float scale = Math.max(xScale, yScale);

            // Now get the size of the source bitmap when scaled
            float scaledWidth = scale * sourceWidth;
            float scaledHeight = scale * sourceHeight;

            // Let's find out the upper left coordinates if the scaled bitmap
            // should be centered in the new size give by the parameters
            float left = (newWidth - scaledWidth) / 2;
            float top = (newHeight - scaledHeight) / 2;

            // The target rectangle for the new, scaled version of the source bitmap will now
            // be
            RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

            // Finally, we create a new bitmap of the specified size and draw our new,
            // scaled bitmap onto it.
            dest = Bitmap.createBitmap(newWidth, newHeight, CONFIG);
            Canvas canvas = new Canvas(dest);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            canvas.drawBitmap(source, null, targetRect, paint);

        } catch (Throwable e) {
            e.printStackTrace();
        }


        return dest;
    }

}
