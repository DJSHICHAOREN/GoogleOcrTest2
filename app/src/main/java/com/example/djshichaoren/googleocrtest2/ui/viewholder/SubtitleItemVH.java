package com.example.djshichaoren.googleocrtest2.ui.viewholder;

import android.graphics.Color;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.core.word.translate.Translator;
import com.example.djshichaoren.googleocrtest2.database.SubtitleDatabase;
import com.example.djshichaoren.googleocrtest2.database.SubtitleDatabaseUtil;
import com.example.djshichaoren.googleocrtest2.database.entity.NewWordEntity;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleSentenceEntity;
import com.example.djshichaoren.googleocrtest2.database.entity.WordSceneEntity;
import com.example.djshichaoren.googleocrtest2.models.TranslateResult;
import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.srt.SRTLine;
import com.example.djshichaoren.googleocrtest2.ui.view.TranslationResultView;
import com.example.djshichaoren.googleocrtest2.util.text.StringCleaner;
import com.google.android.gms.vision.text.Line;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SubtitleItemVH extends BaseVH {

    private TextView tv_time;
    private TextView tv_id;
    private TextView tv_chinese;
    private LinearLayout ll_word_line;
    private LinearLayout ll_whole;
    private TranslationResultView translation_result_view;

    private boolean mIsFigureDown = false;
    private static final int mWordPaddingRight = 15;

    private int SELECTED_TEXT_COLOR = Color.parseColor("#FF0000");
    private int UNSELECTED_TEXT_COLOR = Color.parseColor("#000000");

    private Translator mTranslator;
    private SubtitleEntity mSubtitleEntity;
    private TextView mLastSelectedWordTextView;
    private Handler mHandler = new Handler();

    public SubtitleItemVH(@NonNull View itemView, Translator translator, SubtitleEntity subtitleEntity) {
        super(itemView);

        tv_time = itemView.findViewById(R.id.tv_time);
        tv_id = itemView.findViewById(R.id.tv_id);
        tv_chinese = itemView.findViewById(R.id.tv_chinese);
        ll_word_line = itemView.findViewById(R.id.ll_word_line);
        ll_whole = itemView.findViewById(R.id.ll_whole);
        translation_result_view = itemView.findViewById(R.id.translation_result_view);

        mTranslator = translator;
        mSubtitleEntity = subtitleEntity;
    }

    @Override
    public void bind(SRTLine srtLine) {
        if(srtLine == null) return;

        int sentencePosition = (int)itemView.getTag();
        tv_id.setText(srtLine.getId() + "");
        tv_time.setText(srtLine.getTimeString());
        tv_chinese.setText(srtLine.getChineseString());

        tv_chinese.setAlpha(0);
        tv_chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getAlpha() == 0){
                    view.setAlpha(1);
                }
                else{
                    view.setAlpha(0);
                }
            }
        });

        // ???????????????????????????
        List<WordSceneEntity> wordSceneEntityList = srtLine.getWordSceneEntityList();
        List<String> newWordList = new ArrayList<>();
        if(wordSceneEntityList != null){
            for(WordSceneEntity wordSceneEntity : wordSceneEntityList){
                newWordList.add(wordSceneEntity.word);
            }
        }

        translation_result_view.clearAllViews();

        ll_whole.post(new Runnable() {
            @Override
            public void run() {
                int freeWidth = 0;
                LinearLayout wordLinearLayout = null;
                ll_word_line.removeAllViews();
                if(srtLine.getEnglishString() != null){

                    String[] wordsArray = srtLine.getEnglishString().split("\\s+");
                    for(String word : wordsArray){
                        // ??????????????????
                        TextView wordView = new TextView(SubtitleItemVH.this.itemView.getContext());
                        LinearLayout.LayoutParams wordLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        wordView.setText(word);
                        wordView.setTextSize(20);
                        wordView.setPadding(0, 0, mWordPaddingRight, 0);

                        // ?????????????????????????????????
                        final String pureWord = StringCleaner.cleanWordString(word);

                        if(!newWordList.contains(pureWord)){
                            // ????????????????????????
                            wordView.setTextColor(UNSELECTED_TEXT_COLOR);
                            wordView.setTag(0);
                        }
                        else{
                            // ??????????????????
                            wordView.setTextColor(SELECTED_TEXT_COLOR);
                            wordView.setTag(1);
                        }

                        wordView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                // ????????????????????????
                                SubtitleSentenceEntity subtitleSentenceEntity = SubtitleDatabaseUtil.getSubtitleSentenceEntity(v.getContext(), mSubtitleEntity.id, srtLine.getId());
                                if(subtitleSentenceEntity == null){
                                    subtitleSentenceEntity = SubtitleDatabaseUtil.insertSubtitleSentenceEntity(v.getContext(), mSubtitleEntity.id, srtLine.getId()
                                            , srtLine.getTime().getStart(), srtLine.getTime().getEnd(), srtLine.getEnglishString(), srtLine.getChineseString());
                                }
                                final int subtitleSentenceId = subtitleSentenceEntity.id;

                                // ????????????entity
                                WordSceneEntity wordSceneEntity = SubtitleDatabaseUtil.getWordSceneEntity(v.getContext(), mSubtitleEntity.getId(), subtitleSentenceId, pureWord);
                                if(wordSceneEntity == null){
                                    wordSceneEntity = SubtitleDatabaseUtil.insertWordSceneEntity(v.getContext(), subtitleSentenceId, mSubtitleEntity.id
                                            , "", true, sentencePosition, pureWord);
                                }
                                // ????????????
                                if((int)(wordView.getTag()) == 0) {

                                    if(!translation_result_view.getCurrentTranslationWordName().equals(pureWord)){
                                        translateAndShow(pureWord, wordSceneEntity);
                                    }

                                    wordView.setTag(1);
                                    wordView.setTextColor(SELECTED_TEXT_COLOR);
                                    translation_result_view.setVisibility(View.VISIBLE);

                                    if(wordSceneEntity.isNew == false){
                                        wordSceneEntity.isNew = true;
                                        SubtitleDatabaseUtil.updateWordSceneEntity(v.getContext(), wordSceneEntity);
                                    }

                                    // ?????????????????????????????????
//                                    if(mLastSelectedWordTextView != wordView && mLastSelectedWordTextView != null){
//                                        mLastSelectedWordTextView.setTag(0);
//                                        mLastSelectedWordTextView.setTextColor(UNSELECTED_TEXT_COLOR);
//                                    }
                                    mLastSelectedWordTextView = wordView;
                                }
                                else{
                                    // ???????????????????????????
                                    if(!translation_result_view.getCurrentTranslationWordName().equals(pureWord)){
                                        // ???????????????????????????????????????
                                        translateAndShow(pureWord, wordSceneEntity);
                                        translation_result_view.setVisibility(View.VISIBLE);

                                    }
                                    else{
                                        // ?????????????????????????????????
                                        // ??????????????????
                                        wordView.setTag(0);
                                        wordView.setTextColor(UNSELECTED_TEXT_COLOR);
                                        translation_result_view.setVisibility(View.GONE);

                                        if(wordSceneEntity.isNew == true){
                                            wordSceneEntity.isNew = false;
                                            SubtitleDatabaseUtil.updateWordSceneEntity(v.getContext(), wordSceneEntity);
                                        }

                                        mLastSelectedWordTextView = null;
                                    }
                                }

                            }
                        });

                        // ????????????????????????????????????????????????
                        TextPaint textPaint = wordView.getPaint();
                        float wordWidth = textPaint.measureText(word);
                        if(wordWidth > freeWidth){
                            wordLinearLayout = new LinearLayout(SubtitleItemVH.this.itemView.getContext());
                            LinearLayout.LayoutParams wordLinearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            ll_word_line.addView(wordLinearLayout, wordLinearLayoutParams);
                            freeWidth = ll_whole.getWidth();
                        }

                        // ????????????
                        if(wordLinearLayout != null){
                            wordLinearLayout.addView(wordView, wordLayoutParams);
                            freeWidth -= wordWidth + mWordPaddingRight;
                        }
                        else{
                            int a = 1;
                        }


                    }
                }
            }
        });

    }

    public void translateAndShow(String word, final WordSceneEntity wordSceneEntity){
        mTranslator.translate(word, new Translator.TranslateCallback() {
            @Override
            public void success(TranslateResult translateResult) {
                Log.d("lwd", "get word:" + word);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        translation_result_view.setData(translateResult, wordSceneEntity);
                    }
                });
            }
        });
    }



}
