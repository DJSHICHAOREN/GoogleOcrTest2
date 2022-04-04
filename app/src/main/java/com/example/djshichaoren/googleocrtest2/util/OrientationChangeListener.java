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
public class OrientationChangeListener extends OrientationEventListener {
    static int mOrientation = 0;
    static boolean mIsHorizontal = false;

    private ChangeCallback mChangeCallback;
    public OrientationChangeListener(Context context) {
        super(context);
    }

    @Override
    public void onOrientationChanged(int orientation) {
        if(orientation != mOrientation){
            mOrientation = orientation;
            Log.d("lwd", "orientation:" + orientation);
            // 当前屏幕是否横着
            boolean isHorizontal = false;
            if( (mOrientation > 45 && mOrientation < 135) || (mOrientation > 225 && mOrientation < 315) ){
                isHorizontal = true;
            }
            // 判断屏幕方向是否改变
            if(mIsHorizontal != isHorizontal){
                Log.d("lwd", "change orientation is horizontal：" + isHorizontal);

                mIsHorizontal = isHorizontal;

                if(mChangeCallback != null){
                    mChangeCallback.onOrientationChanged(isHorizontal);
                }

            }
        }
    }

    public void setChangeCallback(ChangeCallback changeCallback){
        mChangeCallback = changeCallback;
    }

    public interface ChangeCallback{
        void onOrientationChanged(boolean isHorizontal);
    }
}
