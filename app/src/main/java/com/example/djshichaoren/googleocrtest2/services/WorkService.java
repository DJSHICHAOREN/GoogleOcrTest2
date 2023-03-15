package com.example.djshichaoren.googleocrtest2.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import com.example.djshichaoren.googleocrtest2.core.recogonize.OcrProxy;
import com.example.djshichaoren.googleocrtest2.core.recogonize.OcrProxyImpl;
import com.example.djshichaoren.googleocrtest2.core.view.show_view.InteractionShowView;
import com.example.djshichaoren.googleocrtest2.core.view.show_view.TranslationShowView;
import com.example.djshichaoren.googleocrtest2.core.word.record.SentenceDecomposer;
import com.example.djshichaoren.googleocrtest2.core.word.translate.Translator;
import com.example.djshichaoren.googleocrtest2.database.SubtitleDatabaseUtil;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;
import com.example.djshichaoren.googleocrtest2.database.entity.WordSceneEntity;
import com.example.djshichaoren.googleocrtest2.models.BoundingBox;
import com.example.djshichaoren.googleocrtest2.models.RecognitionResult;
import com.example.djshichaoren.googleocrtest2.core.recogonize.GoogleOcrImpl;
import com.example.djshichaoren.googleocrtest2.subtitle_api.parser.SRTParser;
import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.srt.SRTLine;
import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.srt.SRTSub;
import com.example.djshichaoren.googleocrtest2.util.EnhanceSubtitleUtil;
import com.example.djshichaoren.googleocrtest2.util.FileUtil;
import com.example.djshichaoren.googleocrtest2.util.ImageCuttingUtil;
import com.example.djshichaoren.googleocrtest2.util.JinshanTranslator;
import com.example.djshichaoren.googleocrtest2.util.RecognitionResultFilter;
import com.example.djshichaoren.googleocrtest2.core.screenshot.ScreenShotter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/7/13 21:51
 * 修改备注：
 */
public class WorkService extends Service {
    public static boolean isStarted = false;
    private WindowManager windowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private ShowTranslationBinder mBinder = new ShowTranslationBinder();
    private ScreenShotter mScreenShotter;
    private OcrProxy mOcrProxy;
    private InteractionShowView mInteractionShowView;
    private TranslationShowView mTranslationShowView;
    private Translator mTranslator = new JinshanTranslator();
    private SentenceDecomposer mSentenceDecomposer;
    private Handler mRecognizeHandler = new Handler();
    private boolean isStartRecognize;
    private EnhanceSubtitleUtil mEnhanceSubtitleUtil = new EnhanceSubtitleUtil();
    @Override
    public void onCreate() {
        super.onCreate();
        isStarted = true;
        // 初始化windowManager
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // 初始化layoutParams
        mLayoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        Log.w("lwd", "Create translation service");
        mOcrProxy = OcrProxyImpl.newInstance(getApplicationContext());
        mSentenceDecomposer = new SentenceDecomposer(getApplicationContext());

    }

    public void createInteractionShowView(){
        mInteractionShowView = new InteractionShowView(getApplicationContext());
        mInteractionShowView.show();
    }

    public void createTranslationShowView(){
        mTranslationShowView = new TranslationShowView(getApplicationContext());
        mTranslationShowView.show();
    }

    public void stopAll(){
        if(mInteractionShowView != null){
            mInteractionShowView.stopShow();
        }
        isStartRecognize = false;

    }

    public void setAssistSubtitle(SubtitleEntity subtitleEntity) {
        if (subtitleEntity != null) {
            File subtitleFile = FileUtil.getSubtitleFile(getApplicationContext(), subtitleEntity.name);
            if (subtitleFile != null) {
                SRTParser parser = new SRTParser();
                SRTSub subtitle = parser.parse(subtitleFile, null);

                // 获取生词场景
                List<WordSceneEntity> wordSceneEntityList = SubtitleDatabaseUtil.getAllWordSceneEntityWithSubtitleId(getApplicationContext(), subtitleEntity.id);
//                Collections.sort(wordSceneEntityList);
//                for (WordSceneEntity wordSceneEntity : wordSceneEntityList) {
//                    Log.d("lwd", wordSceneEntity.subtitleSentencePosition + " " + wordSceneEntity.word);
//                }

                // 将生词场景加入字幕数据中
                for (WordSceneEntity wordSceneEntity : wordSceneEntityList) {
                    SRTLine srtLine = subtitle.getSrtLine(wordSceneEntity.subtitleSentencePosition);
                    srtLine.addWordSceneEntity(wordSceneEntity);
                }

                mEnhanceSubtitleUtil.setAssistSubtitle(subtitle);
            }
        }
    }

    /**
     * 开始进行识别，显示Service
     */
    public void startAll(){
        final int TIME = 300;

        if(mScreenShotter == null){
            mScreenShotter = ScreenShotter.newInstance();
        }

        if(mInteractionShowView == null){
            mInteractionShowView = new InteractionShowView(getApplicationContext());
        }
        mInteractionShowView.show();

//        if(mTranslationShowView == null){
//            createTranslationShowView();
//        }
        isStartRecognize = true;
        mRecognizeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap screenImage = mScreenShotter.takeScreenshot();

                BoundingBox boundingBox = mInteractionShowView.getMyBoundingBox();

                screenImage = ImageCuttingUtil.cutBottomImageFromY(screenImage, boundingBox);

                RecognitionResult recognitionResult = null;
                if(screenImage != null){
                    ArrayList<RecognitionResult> recognitionResultsList = mOcrProxy.recognize(screenImage);
                    recognitionResult = RecognitionResultFilter.filter(recognitionResultsList);

                }

                if(recognitionResult != null){
                    Log.d("lwd", "recognitionResult:" + recognitionResult.mContent);
                    String content = mEnhanceSubtitleUtil.enhance(recognitionResult.mContent);
                    mInteractionShowView.updateSentence(content);

//                    List<String> wordList = mSentenceDecomposer.filter(recognitionResult.mContent);
//
//                    for(String word : wordList){
//                        Log.d("lwd", "start require word:" + word);
//                        mTranslator.translate(word, new Translator.TranslateCallback() {
//                            @Override
//                            public void success(TranslateResult translateResult) {
//                                Log.d("lwd", "get word:" + word);
//                                mTranslationShowView.addTranslateResult(translateResult);
//                            }
//                        });
//                    }

//                    Log.d("lwd", "content is :" + recognitionResult.mContent);
                }
                if(isStartRecognize){
                    mRecognizeHandler.postDelayed(this, TIME);
                }
            }
        }, TIME);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class ShowTranslationBinder extends Binder{
        public WorkService getService(){
            return WorkService.this;
        }
    }

}
