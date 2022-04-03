package com.example.djshichaoren.googleocrtest2.core.view.suspend_view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.djshichaoren.googleocrtest2.util.ScreenUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FloatContainer extends FrameLayout {
    private boolean isDrag=false;

    private int mInitWidth;
    private int mInitHeight;


    private int mWidth;
    private int mHeight;
    private int screenWidth;
    private int screenHeight;

    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;


    public FloatContainer(@NonNull Context context) {
        super(context);
        mContext = context;
        mWindowManager = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    }

    private float downX;
    private float downY;
    // 由于mWindowManagerParams是所有控件共用的，所以记录此实例的x和y
    private int thisX;
    private int thisY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if(mWindowManagerParams == null){
            return false;
        }

        if (this.isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDrag=false;
                    downX = event.getRawX();
                    downY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:

                    final float xDistance = event.getRawX() - downX;
                    final float yDistance = event.getRawY() - downY;
                    //当水平或者垂直滑动距离大于10,才算拖动事件
                    if (Math.abs(xDistance) >1 ||Math.abs(yDistance)>1) {
                        isDrag=true;

                        thisX += xDistance;
                        thisY += yDistance;

                        mWindowManagerParams.x = thisX;
                        mWindowManagerParams.y = thisY;


                        mWindowManager.updateViewLayout(this, mWindowManagerParams);
//                        Log.d("lwd","move distanceX:" + xDistance + " distanceY:" + yDistance);

                        downX = event.getRawX();
                        downY = event.getRawY();
                    }
                    break;
            }
            return true;
        }


        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth =getMeasuredWidth();
        mHeight =getMeasuredHeight();
        screenWidth= ScreenUtil.getScreenWidth(mContext);
        screenHeight= ScreenUtil.getScreenHeight(mContext);

    }

    public void show(WindowManager.LayoutParams layoutParams){
        mWindowManager.addView(this, layoutParams);
        mWindowManagerParams = (WindowManager.LayoutParams) this.getLayoutParams();
        thisX = mWindowManagerParams.x;
        thisY = mWindowManagerParams.y;
    }
}
