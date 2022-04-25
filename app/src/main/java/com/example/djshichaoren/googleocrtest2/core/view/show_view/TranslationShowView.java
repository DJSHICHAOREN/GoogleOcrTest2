package com.example.djshichaoren.googleocrtest2.core.view.show_view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.djshichaoren.googleocrtest2.core.view.suspend_view.FloatContainer;
import com.example.djshichaoren.googleocrtest2.models.TranslateResult;

import androidx.annotation.NonNull;

public class TranslationShowView extends FloatContainer {

    private LinearLayout mTranslateLinearLayout;

    public TranslationShowView(@NonNull Context context) {
        super(context);

        mTranslateLinearLayout = new LinearLayout(context);
        mTranslateLinearLayout.setOrientation(LinearLayout.VERTICAL);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.BOTTOM;
        this.addView(mTranslateLinearLayout, layoutParams);
    }


    public void addTranslateResult(TranslateResult translateResult){
        if(translateResult == null || translateResult.mTranslationList.size() == 0){
            return;
        }
        String content  = translateResult.mWord + ":" + translateResult.mTranslationList.get(0);

        TextView translateTextView = new TextView(mContext);
        translateTextView.setText(content);
        translateTextView.setTextSize(18);
        translateTextView.setTextColor(Color.parseColor("#ffffff"));

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mTranslateLinearLayout.addView(translateTextView, mTranslateLinearLayout.getChildCount(), layoutParams);

    }

    @Override
    public WindowManager.LayoutParams generateLayoutParams() {
        WindowManager.LayoutParams layoutParams = super.generateLayoutParams();

        layoutParams.width = mScreenWidth - 100;
        layoutParams.height = 300;
        layoutParams.x = 0;
        layoutParams.y = mScreenHeight / 3 * 1;


        return layoutParams;
    }
}
