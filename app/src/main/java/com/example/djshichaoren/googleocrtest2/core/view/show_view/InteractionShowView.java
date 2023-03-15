package com.example.djshichaoren.googleocrtest2.core.view.show_view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.djshichaoren.googleocrtest2.core.view.suspend_view.FloatContainer;

import androidx.annotation.NonNull;

public class InteractionShowView extends FloatContainer{

    private LinearLayout mWordLinearLayout;
    private Context mContext;
    private int CLICKED_BACKGROUND_COLOR = Color.parseColor("#33000000");
    private int UNCLICKED_BACKGROUND_COLOR = Color.parseColor("#000000");

    private int CLICKED_TEXT_COLOR = Color.parseColor("#33FFFFFF");
    private int UNCLICKED_TEXT_COLOR = Color.parseColor("#FFFFFF");

    private boolean mIsFigureDown = false;


    public InteractionShowView(@NonNull Context context) {
        super(context);
        mContext = context;

        mWordLinearLayout = new LinearLayout(context);
        mWordLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        this.addView(mWordLinearLayout, layoutParams);

    }

    public void updateSentence(String sentence){
        mWordLinearLayout.removeAllViews();
        if (sentence == null) return;

        String[] wordsArray = sentence.split("\\s+");
        for(String word : wordsArray){
            TextView wordView = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            wordView.setText(word);
            wordView.setTextSize(18);
            wordView.setPadding(0, 0, 20, 0);
            wordView.setTextColor(mIsFigureDown ? CLICKED_TEXT_COLOR : UNCLICKED_TEXT_COLOR);
            wordView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    translationResultDisplayer.translateWord(word);

                }
            });

            // 添加到LinearLayout
            mWordLinearLayout.addView(wordView, layoutParams);
        }

    }

    private void setTextColor(int color){
        if(mWordLinearLayout == null) return;

        int textViewCount = mWordLinearLayout.getChildCount();

        for(int i = 0; i < textViewCount; i++){
            View view = mWordLinearLayout.getChildAt(i);
            if(view instanceof TextView){
                TextView textView = (TextView)view;
                textView.setTextColor(color);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(CLICKED_BACKGROUND_COLOR);
                setTextColor(CLICKED_TEXT_COLOR);
                mIsFigureDown = true;
                break;

            case MotionEvent.ACTION_UP:
                setBackgroundColor(UNCLICKED_BACKGROUND_COLOR);
                setTextColor(UNCLICKED_TEXT_COLOR);
                mIsFigureDown = false;
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public WindowManager.LayoutParams generateLayoutParams() {
        WindowManager.LayoutParams layoutParams = super.generateLayoutParams();

        return layoutParams;
    }

}
