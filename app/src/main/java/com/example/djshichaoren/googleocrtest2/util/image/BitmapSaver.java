package com.example.djshichaoren.googleocrtest2.util.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static com.example.djshichaoren.googleocrtest2.config.Config.BASE_FOLDER_NAME;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/8/22 20:10
 * 修改备注：
 */
public class BitmapSaver {
    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/dskqxt/pic/";

    /**
     * 随机生产文件名
     *
     * @return
     */
    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }

    /**
     * 保存图片到本地
     * 需要动态请求存储
     * @param context
     * @param bitmap
     * @return
     */
    public static String save(Context context, Bitmap bitmap){
        // 创建文件夹
        File dir = new File(BASE_FOLDER_NAME);
        if (!dir.exists()) {
            boolean res = dir.mkdirs();
        }
        // 创建输出流
        OutputStream outputStream = null;

        File file = new File(BASE_FOLDER_NAME, "screenshot.png");
        try {
            // 创建图片文件
            file.createNewFile();
            // 将流与图片文件绑定
            outputStream = new FileOutputStream(file);

            bitmap = printBitmapTransparency(bitmap);
            // 将图片写入文件
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            galleryAddPic(context, file);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();

    }


    private static void galleryAddPic(Context context, File f) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    private static Bitmap printBitmapTransparency(Bitmap bitmap){
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
}
