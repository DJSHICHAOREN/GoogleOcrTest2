package com.example.djshichaoren.googleocrtest2.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.database.SubtitleDatabaseUtil;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleSentenceEntity;
import com.example.djshichaoren.googleocrtest2.database.entity.WordSceneEntity;
import com.example.djshichaoren.googleocrtest2.http.bean.JinshanTranslation;
import com.example.djshichaoren.googleocrtest2.models.TranslateResult;
import com.example.djshichaoren.googleocrtest2.util.dark.DarkUtil;

import org.apache.commons.lang3.StringUtils;


import androidx.annotation.Nullable;

public class TranslationResultView extends LinearLayout {


    private LinearLayout ll_whole;
    private int mFreeWidth;
    private static final int WORD_PADDING_LEFT  = 20;
    private TranslateResult mTranslateResult;
    private WordSceneEntity mWordSceneEntity;

    private int SELECTED_TEXT_COLOR = Color.parseColor("#FF0000");
    private int SELECTED_TEXT_COLOR_DARK = Color.parseColor("#aaFF0000");
    private int UNSELECTED_TEXT_COLOR = Color.parseColor("#000000");
    private int UNSELECTED_TEXT_COLOR_DARK = Color.parseColor("#aaffffff");

    private TextView mLastSelectedTranslationTextView;

    public TranslationResultView(Context context) {
        super(context);
        init();
    }

    public TranslationResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TranslationResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        View root =  LayoutInflater.from(getContext()).inflate(R.layout.layout_translation_result_view, this, true);
        ll_whole = root.findViewById(R.id.ll_whole);
    }

    public void setData(TranslateResult translateResult, WordSceneEntity wordSceneEntity){
        mTranslateResult = translateResult;
        mWordSceneEntity = wordSceneEntity;

        ll_whole.removeAllViews();

        if(translateResult == null
                || translateResult.getSymbols() == null
                || translateResult.getSymbols().size() < 1) {
            return;
        }

        JinshanTranslation.Symbol symbol = translateResult.getSymbols().get(0);

        // 音标行
        LinearLayout phLinearLayout = new LinearLayout(getContext());
        phLinearLayout.setOrientation(HORIZONTAL);
        if(!StringUtils.isEmpty(symbol.getPh_en())){

            TextView tv_en_tip = new TextView(getContext());
            tv_en_tip.setText("英：");
            tv_en_tip.setTextColor(getTextColor());
            phLinearLayout.addView(tv_en_tip);

            TextView tv_ph_en = new TextView(getContext());
            tv_ph_en.setTextColor(getTextColor());
            tv_ph_en.setText("/" + symbol.getPh_en() + "/");
            phLinearLayout.addView(tv_ph_en);

            View tv_ph_block = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 1);
            phLinearLayout.addView(tv_ph_block, layoutParams);
        }

        if(!StringUtils.isEmpty(symbol.getPh_am())){

            TextView tv_am_tip = new TextView(getContext());
            tv_am_tip.setText("美：");
            tv_am_tip.setTextColor(getTextColor());
            phLinearLayout.addView(tv_am_tip);

            TextView tv_ph_am = new TextView(getContext());
            tv_ph_am.setText("/" + symbol.getPh_am() + "/");
            tv_ph_am.setTextColor(getTextColor());
            phLinearLayout.addView(tv_ph_am);
        }

        ll_whole.addView(phLinearLayout);

        // 翻译
        if(symbol.getParts() == null) return;

        ll_whole.post(new Runnable() {
            @Override
            public void run() {

                for(JinshanTranslation.Symbol.Part part : symbol.getParts()){
                    LinearLayout partLinearLayout = createAndAddTranslationLayout();

                    TextView tvPart = new TextView(getContext());
                    tvPart.setText(part.getPart());
                    tvPart.setTextColor(getTextColor());
                    partLinearLayout.addView(tvPart);

                    mFreeWidth -= tvPart.getPaint().measureText(part.getPart());

                    for(String mean : part.getMeans()){
                        TextView tvMean = new TextView(getContext());
                        tvMean.setText(mean);
                        tvMean.setPadding(WORD_PADDING_LEFT, 0, 0, 0);
                        tvMean.setTag(0);
                        tvMean.setTextColor(getTextColor());
                        tvMean.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if((int)tvMean.getTag() == 0){
                                    tvMean.setTextColor(DarkUtil.isDarkMode(getContext()) ? SELECTED_TEXT_COLOR_DARK : SELECTED_TEXT_COLOR);
                                    tvMean.setTag(1);

                                    wordSceneEntity.isNew = true;
                                    wordSceneEntity.wordTranslation = mean;
                                    SubtitleDatabaseUtil.updateWordSceneEntity(view.getContext(), wordSceneEntity);

                                    // 把其他翻译的标记去掉
                                    if(mLastSelectedTranslationTextView != tvMean && mLastSelectedTranslationTextView != null){
                                        mLastSelectedTranslationTextView.setTextColor(UNSELECTED_TEXT_COLOR);
                                        mLastSelectedTranslationTextView.setTag(0);
                                    }
                                    mLastSelectedTranslationTextView = tvMean;

                                }
                                else{
                                    tvMean.setTextColor(getTextColor());
                                    tvMean.setTag(0);

                                    wordSceneEntity.isNew = false;
                                    SubtitleDatabaseUtil.updateWordSceneEntity(view.getContext(), wordSceneEntity);
                                }
                            }
                        });

                        if(mFreeWidth < tvMean.getPaint().measureText(mean)){
                            partLinearLayout = createAndAddTranslationLayout();
                        }

                        partLinearLayout.addView(tvMean);
                        mFreeWidth -= tvMean.getPaint().measureText(mean);
                        mFreeWidth -= WORD_PADDING_LEFT;

                    }

                }
            }
        });

    }

    private int getTextColor(){
        return DarkUtil.isDarkMode(getContext()) ? UNSELECTED_TEXT_COLOR_DARK : UNSELECTED_TEXT_COLOR;
    }

    private LinearLayout createAndAddTranslationLayout(){
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(HORIZONTAL);
        ll_whole.addView(linearLayout
                , new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mFreeWidth = ll_whole.getWidth();

        return linearLayout;
    }

    public void clearAllViews(){
        ll_whole.removeAllViews();
    }

    public String getCurrentTranslationWordName(){
        if(mTranslateResult == null || mTranslateResult.getWord_name() == null) return "";
        if(mWordSceneEntity == null || mWordSceneEntity.word == null) return "";
        return mWordSceneEntity.word;
    }
}
