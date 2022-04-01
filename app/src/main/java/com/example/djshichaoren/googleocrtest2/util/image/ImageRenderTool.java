package com.example.djshichaoren.googleocrtest2.util.image;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/8/15 20:07
 * 修改备注：
 */
public class ImageRenderTool {

    public static Bitmap removePictureTransparencyPart(Bitmap bitmap){
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int columnIndex = width/2;
        int contentStartRowIndex=0, contentEndRowIndex=0;
        int i=1;
        // 获得图片内容开始行号
        for(int startRowIndex=0; startRowIndex<height; startRowIndex++){
            int startPixel = bitmap.getPixel(columnIndex, startRowIndex);
            int startAlpha = Color.alpha(startPixel);
            if(startAlpha !=0){
                contentStartRowIndex = startRowIndex;
                break;
            }
        }
        // 获得图片内容结束行号
        for(int endRowIndex=height-1; endRowIndex>=0; endRowIndex--, i++){
            int endPixel = bitmap.getPixel(columnIndex, endRowIndex);
            int endAlpha = Color.alpha(endPixel);
            if(endAlpha != 0){
                contentEndRowIndex = endRowIndex;
                break;
            }
//            Log.d("lwd", "i:" + i +" alpha:" + alpha);
        }
        if(contentEndRowIndex-contentStartRowIndex > 0){
            bitmap = Bitmap.createBitmap(bitmap, 0, contentStartRowIndex,
                    bitmap.getWidth(), contentEndRowIndex-contentStartRowIndex);
            Log.d("lwd", "deal with the bitmap");
        }
        return bitmap;
    }


    public static Bitmap whiteRestPartOfImage(Bitmap bitmap, int top, int bottom){
        for(int i=0;i<bitmap.getWidth();i++) {
            for(int c=0;c<bitmap.getHeight();c++) {

                if(shouldBeWhite(c, top, bottom))
                    bitmap.setPixel(i, c, 0xFFAAAAAA);
            }
        }
        return bitmap;
    }

    private static boolean shouldBeWhite(int yIndex, int top, int bottom){
        if(yIndex >= top && yIndex <= bottom){
            return false;
        }
        return true;
    }
}
