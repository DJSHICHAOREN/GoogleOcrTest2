package com.example.djshichaoren.googleocrtest2.util;

import android.content.Context;
import android.util.Log;
import android.view.OrientationEventListener;

import com.example.djshichaoren.googleocrtest2.core.screenshot.ScreenShotter;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/7/26 18:14
 * 修改备注：
 */
public class OrientationChangeCallback extends OrientationEventListener {
    ScreenShotter mScreenShotter;
    static int mOrientation = 0;
    static int mIsHorizontalOrientation = 0;
    public OrientationChangeCallback(Context context, ScreenShotter screenShotter) {
        super(context);

        mScreenShotter = screenShotter;
    }

    @Override
    public void onOrientationChanged(int orientation) {
        if(orientation != mOrientation){
            mOrientation = orientation;
//            Log.d("lwd", "orientation:" + orientation);
            // 当前屏幕是否横着
            int isHorizontalOrientation = 0;
            if( (mOrientation > 45 && mOrientation < 135) || (mOrientation > 225 && mOrientation < 315) ){
                isHorizontalOrientation = 1;
            }
            // 判断屏幕方向是否改变
            if(mIsHorizontalOrientation != isHorizontalOrientation){
                Log.d("lwd", "change orientation is horizontal：" + isHorizontalOrientation);
//                mScreenShotter.unbindScreenShot();
//                mScreenShotter.setMediaProjection(null);

                mIsHorizontalOrientation = isHorizontalOrientation;
                // 设置获取屏幕区域对象
                if(isHorizontalOrientation == 1){
                    ScreenLocationCalculator.setOrientationHorizontal();
                }
                else{
                    ScreenLocationCalculator.setOrientationVertical();
                }

            }


        }
    }
}
