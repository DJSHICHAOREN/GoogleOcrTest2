package com.example.djshichaoren.googleocrtest2.util;

import android.graphics.Bitmap;

import com.example.djshichaoren.googleocrtest2.models.BoundingBox;

public class ImageCuttingUtil {

    /**
     * 截取图片y一下的部分
     * @param bitmap
     * @param boundingBox
     * @return
     */
    public static Bitmap cutBottomImageFromY(Bitmap bitmap, BoundingBox boundingBox){
        if(bitmap == null || boundingBox == null)
            return null;

        int y = boundingBox.getY() + boundingBox.getHeight();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight() - y - 1;

        if(width <= 0 || height <= 0) return null;
        bitmap = Bitmap.createBitmap(bitmap, 0, y, width, height);
        return bitmap;
    }
}
