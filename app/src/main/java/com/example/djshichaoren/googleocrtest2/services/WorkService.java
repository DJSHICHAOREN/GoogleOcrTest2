package com.example.djshichaoren.googleocrtest2.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.example.djshichaoren.googleocrtest2.core.view.show_view.InteractionShowView;
import com.example.djshichaoren.googleocrtest2.core.view.show_view.TranslationShowView;
import com.example.djshichaoren.googleocrtest2.core.word.record.WordFilter;
import com.example.djshichaoren.googleocrtest2.core.word.translate.Translator;
import com.example.djshichaoren.googleocrtest2.http.bean.JinshanTranslation;
import com.example.djshichaoren.googleocrtest2.models.BoundingBox;
import com.example.djshichaoren.googleocrtest2.models.RecognitionResult;
import com.example.djshichaoren.googleocrtest2.models.TranslateResult;
import com.example.djshichaoren.googleocrtest2.ui.element.TranslationResultFactory;
import com.example.djshichaoren.googleocrtest2.core.recogonize.GoogleOcrImpl;
import com.example.djshichaoren.googleocrtest2.util.ImageCuttingUtil;
import com.example.djshichaoren.googleocrtest2.util.JinshanTranslator;
import com.example.djshichaoren.googleocrtest2.util.RecognitionResultFilter;
import com.example.djshichaoren.googleocrtest2.util.ScreenLocationCalculator;
import com.example.djshichaoren.googleocrtest2.core.screenshot.ScreenShotter;
import com.example.djshichaoren.googleocrtest2.util.text.StringCleaner;

import java.util.ArrayList;


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
    private GoogleOcrImpl mGoogleOcrImpl;
    private ScreenLocationCalculator mScreenLocationCalculator;
    private TranslationResultFactory mTranslationResultFactory;
    private View mTranslationResultView;
    private InteractionShowView mInteractionShowView;
    private TranslationShowView mTranslationShowView;
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
        mGoogleOcrImpl = GoogleOcrImpl.newInstance(getApplicationContext());

        mScreenLocationCalculator = new ScreenLocationCalculator();

        mTranslationResultFactory = new TranslationResultFactory(getApplicationContext());

    }

    /**
     * 负责显示翻译的结果
     * 用来在得到翻译结果以后回调
     */
    public class TranslationResultDisplayer{
        private JinshanTranslator mJinshanTranslator;
        private JinshanTranslation mSentenceResult;
        private JinshanTranslation mWordResult;
        private StringCleaner mStringCleaner;

        public TranslationResultDisplayer(){
            mJinshanTranslator = new JinshanTranslator();
            mStringCleaner = new StringCleaner();
        }

        public void translateSentence(String sentence){
            sentence = mStringCleaner.cleanSentenceString(sentence);
            mJinshanTranslator.translate(sentence, this, JinshanTranslator.StringType.SENTENCE);
        }

        public void translateWord(String word){
            word = mStringCleaner.cleanWordString(word);
            mJinshanTranslator.translate(word, this, JinshanTranslator.StringType.WORD);

        }

        /**
         * 显示点击单词的翻译
         * @param word
         */
        public void display(String word){
            Log.d("lwd", "display translation result");

            WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= 26) {
                //8.0新特性
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }else {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
            mLayoutParams.format = PixelFormat.RGBA_8888;
            mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            ScreenLocationCalculator.Region region = mScreenLocationCalculator.getTopRegion();
            mLayoutParams.x = region.startPoint.x + 200;
            mLayoutParams.y = region.startPoint.y;


            if(mTranslationResultView == null){
                mTranslationResultView = mTranslationResultFactory.getTranslationResult(word);
                windowManager.addView(mTranslationResultView, mLayoutParams);
            }
            else{
                mTranslationResultView = mTranslationResultFactory.getTranslationResult(word);
                windowManager.updateViewLayout(mTranslationResultView, mLayoutParams);
            }

            // 清空result
            mSentenceResult = null;
            mWordResult = null;

        }

        public void setSentenceResult(JinshanTranslation mSentenceResult) {
            this.mSentenceResult = mSentenceResult;
        }

        public void setWordResult(JinshanTranslation WordResult) {
            this.mWordResult = WordResult;

            // 由于Sentence是在显示整句字幕时就开始翻译，所以大概率会比单词先翻译完成

            if(mWordResult != null &&
                    mWordResult.getSymbols() != null &&
                    mWordResult.getSymbols().get(0).getParts() != null &&
                    mWordResult.getSymbols().get(0).getParts().get(0).getMeans() != null){

                // 在句子的意思中寻找交集
                if(mSentenceResult != null &&
                        mSentenceResult.getSymbols() != null &&
                        mSentenceResult.getSymbols().get(0).getParts() != null &&
                        mSentenceResult.getSymbols().get(0).getParts().get(0).getMeans() != null){

                    String sentenceTranslation = mSentenceResult.getSymbols().get(0).getParts().get(0).getMeans().get(0);
                    Log.d("lwd", "sentence translation:" + sentenceTranslation);
                    for(JinshanTranslation.Symbol symbol : mWordResult.getSymbols()){
                        for(JinshanTranslation.Symbol.Part part : symbol.getParts()){
                            for(String mean : part.getMeans()){
                                Log.d("lwd", "mean:" + mean);
                            }
                        }
                    }

                }
                // 只显示单词的第一个意思
                else{
                    display(mWordResult.getSymbols().get(0).getParts().get(0).getMeans().get(0));
                }
            }
        }

    }

    public void createInteractionShowView(){
        mInteractionShowView = new InteractionShowView(getApplicationContext());
        mInteractionShowView.showOrUpdate();
    }

    public void createTranslationShowView(){
        mTranslationShowView = new TranslationShowView(getApplicationContext());
        mTranslationShowView.showOrUpdate();
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
            createInteractionShowView();
        }

        if(mTranslationShowView == null){
            createTranslationShowView();
        }

        final Handler mRecognizeHandler = new Handler();
        mRecognizeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap screenImage = mScreenShotter.takeScreenshot();

                BoundingBox boundingBox = mInteractionShowView.getMyBoundingBox();

                screenImage = ImageCuttingUtil.cutBottomImageFromY(screenImage, boundingBox);

                RecognitionResult recognitionResult = null;
                if(screenImage != null){
                    ArrayList<RecognitionResult> recognitionResultsList = mGoogleOcrImpl.recognize(screenImage);
                    recognitionResult = RecognitionResultFilter.filter(recognitionResultsList);

                }

                if(recognitionResult != null){
                    mInteractionShowView.updateSentence(recognitionResult.mContent);

                    String[] wordList = WordFilter.filter(recognitionResult.mContent);

                    for(String word : wordList){
                        Translator translator = new JinshanTranslator();
                        translator.translate(word, new Translator.TranslateCallback() {
                            @Override
                            public void success(TranslateResult translateResult) {
                                mTranslationShowView.addTranslateResult(translateResult);
                            }
                        });
                    }

                    Log.d("lwd", "content is :" + recognitionResult.mContent);
                }

                mRecognizeHandler.postDelayed(this, TIME);
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
