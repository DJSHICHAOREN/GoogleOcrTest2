package com.example.djshichaoren.googleocrtest2.ui.element;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 类描述：生产显示翻译结果的控件
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/8/5 22:25
 * 修改备注：
 */
public class TranslationResultFactory {
    private Context mContext;
    private RelativeLayout rlWholeWidthLayout;
    private TextView mTranslationView;

    public TranslationResultFactory(Context context){
        mContext = context;

        initContainer();
    }

    private void initContainer(){
        rlWholeWidthLayout = new RelativeLayout(mContext);

        mTranslationView = new TextView(mContext);
        mTranslationView.setTextSize(18);
        mTranslationView.setTextColor(Color.WHITE);
        mTranslationView.setPadding(0, 0, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        rlWholeWidthLayout.addView(mTranslationView, layoutParams);
    }

    public View getTranslationResult(String word){
        mTranslationView.setText(word);

        return rlWholeWidthLayout;
    }


}
