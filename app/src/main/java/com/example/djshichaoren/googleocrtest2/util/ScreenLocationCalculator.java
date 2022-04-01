package com.example.djshichaoren.googleocrtest2.util;

import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/8/4 0:31
 * 修改备注：
 */
public class ScreenLocationCalculator {
    private static int mScreenHeight, mScreenWidth;
    private static DisplayMetrics mMetrics;

    public ScreenLocationCalculator(){

    }

    public static void setWindowsManager(WindowManager windowManager){
        Display display = windowManager.getDefaultDisplay();
        mMetrics = new DisplayMetrics();
        display.getRealMetrics(mMetrics);
        mScreenWidth = mMetrics.widthPixels;
        mScreenHeight = mMetrics.heightPixels;
    }


    public static void setOrientationHorizontal(){
        mScreenWidth = mMetrics.heightPixels;
        mScreenHeight = mMetrics.widthPixels;
    }

    public static void setOrientationVertical(){
        mScreenWidth = mMetrics.widthPixels;
        mScreenHeight = mMetrics.heightPixels;
    }

    public static int getMiddleHeight(){
        return mScreenHeight / 2;
    }

    public Region getTopRegion(){
        Region topRegin = new Region();
        topRegin.startPoint = new Point(0, 0);
        topRegin.width = mScreenWidth;
        topRegin.height = mScreenHeight / 3;
        return topRegin;
    }

    public Region getMiddleRegion(){
        Region middleRegin = new Region();
        middleRegin.startPoint = new Point(0, mScreenHeight/3);
        middleRegin.width = mScreenWidth;
        middleRegin.height = mScreenHeight / 3;
        return middleRegin;
    }

    public class Region{
        public Point startPoint;
        public int width, height;
    }
}
