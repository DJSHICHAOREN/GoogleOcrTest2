package com.example.djshichaoren.googleocrtest2.core.screenshot;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.example.djshichaoren.googleocrtest2.util.image.BitmapBinaryzationTool;
import com.example.djshichaoren.googleocrtest2.util.image.ImageRenderTool;

import java.nio.ByteBuffer;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/7/19 23:46
 * 修改备注：
 */
public class ScreenShotter {
    static Handler handler = new Handler();
    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenDpi;
    private ImageReader mImageReader;
    private MediaProjection mMediaProjection;
    private static final String TAG = "lwd";
    private VirtualDisplay mVirtualDisplay = null;
    private Bitmap mScreenShotImage;
    private BitmapBinaryzationTool mBitmapBinaryzationTool;
    private ImageRenderTool mImageRenderTool;
    private static ScreenShotter mScreenShotter;

    public ScreenShotter() {
        mBitmapBinaryzationTool = new BitmapBinaryzationTool();
        mImageRenderTool = new ImageRenderTool();
    }

    public void setWindowManager(final WindowManager windowManager){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //获取屏幕的宽高和DPI
                Display display = windowManager.getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics();
                display.getRealMetrics(metrics);
                mScreenWidth = metrics.widthPixels;
                mScreenHeight = metrics.heightPixels;
                mScreenDpi = metrics.densityDpi;
                //初始化ImageReader实例
                mImageReader = ImageReader.newInstance(mScreenWidth, mScreenHeight, PixelFormat.RGBA_8888, 2);

            }
        }, 1000);
    }

    public void setMediaProjection(MediaProjection mediaProjection) {
        if(mediaProjection != null) {
            Log.i(TAG, "setMediaProjection");

            mMediaProjection = mediaProjection;

            if(mImageReader != null){
                mVirtualDisplay = mediaProjection.createVirtualDisplay("ScreenCapture",
                        mScreenWidth, mScreenHeight, mScreenDpi,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        mImageReader.getSurface(), null, null);
            }
        }
    }

    public boolean isSupportScreenShot(){
        if(mImageReader != null && mMediaProjection != null){
            return true;
        }
        return false;
    }


    public Bitmap takeScreenshot() {
        if(mImageReader == null || mMediaProjection == null){
            return null;
        }

        Image image = mImageReader.acquireLatestImage();
        if(image == null){
            return null;
        }

        int width = image.getWidth();
        int height = image.getHeight();
//        Log.d("lwd", "width:" + image.getWidth() + " height:" + image.getHeight());

        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width+rowPadding/pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0,width, height);
        image.close();

//        bitmap = ImageRenderTool.removePictureTransparencyPart(bitmap);

//        double cropHeight = 1.0 / 2;
//        int yStart = (int)(bitmap.getHeight() * (1 - cropHeight));
//        int yHeight = (int)(bitmap.getHeight() - yStart);

//        bitmap = ImageRenderTool.whiteRestPartOfImage(bitmap, yStart, yStart + yHeight);
//        bitmap = Bitmap.createBitmap(bitmap, 0, 1354,
//                bitmap.getWidth(), 28);
//        bitmap = mBitmapBinaryzationTool.binaryzation(bitmap, 1354, 1354+28);
//        bitmap = mImageRenderTool.whiteRestPartOfImage(bitmap, 1354, 1354+28);



        mScreenShotImage = bitmap;

        return bitmap;
    }

    public Bitmap getScreenShotImage(){
        return mScreenShotImage;
    }

    public static ScreenShotter newInstance(){
        if(mScreenShotter == null){
            mScreenShotter = new ScreenShotter();
        }

        return mScreenShotter;
    }

    public void unbindScreenShot(){
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
    }


}