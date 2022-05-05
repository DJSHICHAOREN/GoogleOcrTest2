package com.example.djshichaoren.googleocrtest2.core.view.suspend_view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.djshichaoren.googleocrtest2.models.BoundingBox;
import com.example.djshichaoren.googleocrtest2.util.OrientationChangeListener;
import com.example.djshichaoren.googleocrtest2.util.ScreenUtil;

import androidx.annotation.NonNull;

public class FloatContainer extends FrameLayout {
    protected int mScreenWidth;
    protected int mScreenHeight;

    protected Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;

    private boolean mCanBeMove = true;



    public FloatContainer(@NonNull Context context) {
        super(context);
        mContext = context;
        mWindowManager = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        setBackgroundColor(Color.parseColor("#000000"));

        mScreenWidth = ScreenUtil.getScreenWidth(mContext);
        mScreenHeight = ScreenUtil.getScreenHeight(mContext);

        // 屏幕旋转监听
//        OrientationChangeListener orientationChangeListener = new OrientationChangeListener(context);
        OrientationChangeListener.addChangeCallback(new OrientationChangeListener.ChangeCallback() {
            @Override
            public void onOrientationChanged(boolean isHorizontal) {
                if(!isHorizontal){
                    mScreenWidth = Math.min(ScreenUtil.getScreenWidth(mContext), ScreenUtil.getScreenHeight(mContext));
                    mScreenHeight = Math.max(ScreenUtil.getScreenWidth(mContext), ScreenUtil.getScreenHeight(mContext));
                }
                else{
                    mScreenWidth = Math.max(ScreenUtil.getScreenWidth(mContext), ScreenUtil.getScreenHeight(mContext));
                    mScreenHeight = Math.min(ScreenUtil.getScreenWidth(mContext), ScreenUtil.getScreenHeight(mContext));
                }
                if(FloatContainer.this.getLayoutParams() instanceof WindowManager.LayoutParams){
                    showOrUpdate();
                }
            }
        });
//        orientationChangeListener.enable();

    }



    private float downX;
    private float downY;
    // 由于mWindowManagerParams是所有控件共用的，所以记录此实例的x和y
    private int thisX;
    private int thisY;
    private int thisWidth;
    private int thisHeight;

    private boolean isTwoFigureTouch = false;


    private float twoFigureDistance;
    private float twoFigureDistanceY;



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if(mWindowManagerParams == null){
            return false;
        }

        if (this.isEnabled()) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getRawX();
                    downY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(!isTwoFigureTouch){
                        if(!mCanBeMove){
                            return false;
                        }

                        // 单指操作滑动控件
                        final float xDistance = event.getRawX() - downX;
                        final float yDistance = event.getRawY() - downY;
                        //当水平或者垂直滑动距离大于10,才算拖动事件
                        if (Math.abs(xDistance) >1 ||Math.abs(yDistance)>1) {

                            thisX += xDistance;
                            thisY += yDistance;

                            mWindowManagerParams.x = thisX;
                            mWindowManagerParams.y = thisY;


                            mWindowManager.updateViewLayout(this, mWindowManagerParams);
//                        Log.d("lwd","move distanceX:" + xDistance + " distanceY:" + yDistance);

                            downX = event.getRawX();
                            downY = event.getRawY();
                        }
                    }
                    else{
                        // 多指操作缩放控件
                        float twoFigureDistanceNew = getTwoFigureDistance(event);
                        float scale = twoFigureDistanceNew / twoFigureDistance;

                        float widthOffset = (scale - 1) * thisWidth / 2;
                        float heightOffset = (scale - 1) * thisHeight / 2;

                        thisX -= widthOffset;
                        thisY -= heightOffset;

                        // 如果使用默认的向下取整，小的那个边会越来越小，因为它被约掉的部分占的比例更大
                        thisWidth = Math.round(thisWidth * scale);
                        thisHeight = Math.round(thisHeight * scale);

                        mWindowManagerParams.x = thisX;
                        mWindowManagerParams.y = thisY;
//                        mWindowManagerParams.width = thisWidth;
                        mWindowManagerParams.height = thisHeight;

//                        Log.d("lwd", "scale:" + scale + " thisWidth:" + thisWidth + " thisHeight:" + thisHeight);
                        mWindowManager.updateViewLayout(this, mWindowManagerParams);

                        twoFigureDistance = twoFigureDistanceNew;

                    }

//                    Log.d("lwd", "ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:

                    isTwoFigureTouch = true;

                    twoFigureDistance = getTwoFigureDistance(event);

//                    Log.d("lwd", "ACTION_POINTER_DOWN");
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    isTwoFigureTouch = false;

                    Log.d("lwd", "FloatContainer ACTION_POINTER_UP");

                    break;
            }
            return true;
        }


        return false;
    }

    /**
     * 计算两个手指间的距离
     *
     * @param event 触摸事件
     * @return 放回两个手指之间的距离
     */
    private float getTwoFigureDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);//两点间距离公式

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置控件的大小、位置等信息
     * @return
     */
    public WindowManager.LayoutParams generateLayoutParams(){
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= 26) {
            //8.0新特性
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = mScreenWidth - 100;
        layoutParams.height = 100;
        layoutParams.x = 0;
        layoutParams.y = mScreenHeight / 3 * 2;

        Log.d("lwd", "change orientation width:" + layoutParams.width);
        return layoutParams;
    }

    public void showOrUpdate(){
        WindowManager.LayoutParams layoutParams = generateLayoutParams();

        // 设置窗口支持透明度
        layoutParams.format = PixelFormat.TRANSPARENT;

        if(mWindowManagerParams == null){
            mWindowManager.addView(this, layoutParams);
        }
        else{
            mWindowManager.updateViewLayout(this, layoutParams);
        }

        mWindowManagerParams = (WindowManager.LayoutParams) this.getLayoutParams();
        thisX = mWindowManagerParams.x;
        thisY = mWindowManagerParams.y;
        thisWidth = mWindowManagerParams.width;
        thisHeight = mWindowManagerParams.height;
    }

    public BoundingBox getMyBoundingBox(){
        if(mWindowManagerParams != null){
            return new BoundingBox(mWindowManagerParams.x, mWindowManagerParams.y, mWindowManagerParams.width, mWindowManagerParams.height);
        }
        return new BoundingBox();
    }

    /**
     * 设置是否可以移动此控件
     * @param canBeMove
     */
    public void setCanBeMoved(boolean canBeMove){
        mCanBeMove = canBeMove;
    }
}
