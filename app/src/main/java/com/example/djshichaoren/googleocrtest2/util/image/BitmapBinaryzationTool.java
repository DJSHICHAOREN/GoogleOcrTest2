package com.example.djshichaoren.googleocrtest2.util.image;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/8/11 15:32
 * 修改备注：
 */
public class BitmapBinaryzationTool {

    public Bitmap binaryzation(Bitmap bitmap, int top, int bottom){
        for(int i=0;i<bitmap.getWidth();i++) {
            for(int c=top;c<bottom;c++) {
                int pixel = bitmap.getPixel(i, c);
                if(!shouldBeWhite(pixel, 240))
                    //bitmap.setPixel(i, c, Color.WHITE);
                    bitmap.setPixel(i, c, 0xFF555555);

            }
        }
        return bitmap;
    }

    private boolean shouldBeWhite(int pixel, int threshValue){
        int alpha = Color.alpha(pixel);
        int redValue = Color.red(pixel);
        int blueValue = Color.blue(pixel);
        int greenValue = Color.green(pixel);
        if(alpha == 0x00) //if this pixel is transparent let me use TRASNPARENT_IS_BLACK
            return true;
        // distance from the white extreme
        double distanceFromWhite = Math.sqrt(Math.pow(0xff - redValue, 2) + Math.pow(0xff - blueValue, 2) + Math.pow(0xff - greenValue, 2));

        double value = Math.sqrt(Math.pow(redValue, 2) + Math.pow(blueValue, 2) + Math.pow(greenValue, 2));
        Log.d("lwd", String.format("distanceFromWhite:%d, value:%d, green:%d, red:%d, blue:%d",
                (int)distanceFromWhite, (int)value, (int)greenValue, (int)redValue, (int)blueValue));
        if(value > threshValue){
            return true;
        }
        else{
            return false;
        }





    }
}
