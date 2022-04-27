package com.example.djshichaoren.googleocrtest2.core.view.show_view;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.djshichaoren.googleocrtest2.core.view.adapter.TranslationShowViewAdapter;
import com.example.djshichaoren.googleocrtest2.core.view.suspend_view.FloatContainer;
import com.example.djshichaoren.googleocrtest2.models.TranslateResult;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TranslationShowView extends FloatContainer {

    private LinearLayout mTranslateLinearLayout;
    private RecyclerView mRecyclerView;
    private TranslationShowViewAdapter mTranslationShowViewAdapter;
    private List<TranslateResult> mTranslateResult = new ArrayList<>();

    public TranslationShowView(@NonNull Context context) {
        super(context);

//        mTranslateLinearLayout = new LinearLayout(context);
//        mTranslateLinearLayout.setOrientation(LinearLayout.VERTICAL);
//        mTranslateLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
//
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
//                , ViewGroup.LayoutParams.MATCH_PARENT);
//        mTranslateLinearLayout.setBackgroundColor(Color.parseColor("#00ff00"));
//        this.addView(mTranslateLinearLayout, layoutParams);

        setBackgroundColor(Color.parseColor("#10000000"));

        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mTranslationShowViewAdapter = new TranslationShowViewAdapter(context, mTranslateResult);
        mRecyclerView.setAdapter(mTranslationShowViewAdapter);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.leftMargin = 100;
        layoutParams.rightMargin = 100;
        mRecyclerView.setBackgroundColor(Color.parseColor("#00000000"));
        this.addView(mRecyclerView, layoutParams);

    }


    public void addTranslateResult(TranslateResult translateResult){
        if(translateResult == null || translateResult.mTranslationList.size() == 0){
            return;
        }

        mTranslateResult.add(translateResult);
        mTranslationShowViewAdapter.notifyItemInserted(mTranslateResult.size() - 1);
        mRecyclerView.scrollToPosition(mTranslateResult.size() - 1);

//        String content  = translateResult.mWord + ":" + translateResult.mTranslationList.get(0);
//        TextView translateTextView = new TextView(mContext);
//        translateTextView.setText(content);
//        translateTextView.setTextSize(18);
//        translateTextView.setBackgroundColor(Color.parseColor("#ff0000"));
//
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//
//        mTranslateLinearLayout.addView(translateTextView, mTranslateLinearLayout.getChildCount(), layoutParams);




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
