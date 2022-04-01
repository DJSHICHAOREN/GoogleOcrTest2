package com.example.djshichaoren.googleocrtest2.ui.element;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.djshichaoren.googleocrtest2.models.ElementPool;
import com.example.djshichaoren.googleocrtest2.services.ShowTranslationService;

/**
 * 类描述：生产显示识别结果的控件
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/7/31 16:59
 * 修改备注：
 */
public class InteractionMessageFactory {
    private Context mContext;
    private ElementPool<OutlineTextView> mElementPool;
    private LinearLayout llWordList;
    private RelativeLayout rlWholeWidthLayout;

    public InteractionMessageFactory(Context context){
        mContext = context;
        mElementPool = new ElementPool<>(OutlineTextView.class, context, 30);
        initContainer();

    }

    /**
     * 初始化外部包围的控件
     */
    public void initContainer(){
        rlWholeWidthLayout = new RelativeLayout(mContext);

        llWordList = new LinearLayout(mContext);
        llWordList.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        rlWholeWidthLayout.addView(llWordList, layoutParams);
    }

    public View getInteractionMessage(String englishSentence,
                                      final ShowTranslationService.TranslationResultDisplayer translationResultDisplayer){
        // 翻译句子
        // translationResultDisplayer.translateSentence(englishSentence);

        //TODO:可以通过改变LinearLayout中的Element内容的方法避免对Element操作
        int wordListSize = llWordList.getChildCount();
        for(int i = 0; i < wordListSize; i++){
            mElementPool.deleteElement(llWordList.getChildAt(i));
        }

        llWordList.removeAllViews();

        String[] wordsArray = englishSentence.split("\\s+");
        for(final String word : wordsArray){
            OutlineTextView wordView = mElementPool.getFloatElement();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            wordView.setText(word);
            wordView.setTextSize(18);
            wordView.setOutlineWidth(2);
            wordView.setTextColor(Color.WHITE);
            wordView.setPadding(0, 0, 20, 0);
            wordView.setTextColor(Color.WHITE);
            wordView.setOutlineColor(Color.BLACK);
            wordView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    translationResultDisplayer.translateWord(word);

                }
            });
            // 添加到LinearLayout
            llWordList.addView(wordView, layoutParams);
        }

        return rlWholeWidthLayout;
    }
}
