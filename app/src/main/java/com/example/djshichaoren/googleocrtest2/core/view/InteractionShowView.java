package com.example.djshichaoren.googleocrtest2.core.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.djshichaoren.googleocrtest2.core.view.suspend_view.FloatContainer;

import androidx.annotation.NonNull;

public class InteractionShowView extends FloatContainer {

    private LinearLayout mWordLinearLayout;
    private Context mContext;


    public InteractionShowView(@NonNull Context context) {
        super(context);
        mContext = context;

        mWordLinearLayout = new LinearLayout(context);
        mWordLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(mWordLinearLayout, layoutParams);
    }

    public void updateSentence(String sentence){
        mWordLinearLayout.removeAllViews();

        String[] wordsArray = sentence.split("\\s+");
        for(String word : wordsArray){
            TextView wordView = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            wordView.setText(word);
            wordView.setTextSize(18);
            wordView.setTextColor(Color.WHITE);
            wordView.setPadding(0, 0, 20, 0);
            wordView.setTextColor(Color.WHITE);
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
}
