package com.example.djshichaoren.googleocrtest2.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.djshichaoren.googleocrtest2.http.bean.JinshanTranslation;
import com.example.djshichaoren.googleocrtest2.models.RecognitionResult;
import com.example.djshichaoren.googleocrtest2.ui.element.InteractionMessageFactory;
import com.example.djshichaoren.googleocrtest2.ui.element.TranslationResultFactory;
import com.example.djshichaoren.googleocrtest2.util.GoogleOcr;
import com.example.djshichaoren.googleocrtest2.util.JinshanTranslator;
import com.example.djshichaoren.googleocrtest2.util.RecognitionResultFilter;
import com.example.djshichaoren.googleocrtest2.util.ScreenLocationCalculator;
import com.example.djshichaoren.googleocrtest2.util.ScreenShotter;
import com.example.djshichaoren.googleocrtest2.util.text.StringCleaner;

import java.util.ArrayList;


/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/7/13 21:51
 * 修改备注：
 */
public class ShowTranslationService extends Service {
    public static boolean isStarted = false;
    private WindowManager windowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private ShowTranslationBinder mBinder = new ShowTranslationBinder();
    private ScreenShotter mScreenShotter;
    private GoogleOcr mGoogleOcr;
    private InteractionMessageFactory mInteractionMessageFactory;
    private View mInteractionMessageView;
    private RecognitionResultFilter mRecognitionResultFilter;
    private ScreenLocationCalculator mScreenLocationCalculator;
    private TranslationResultFactory mTranslationResultFactory;
    private View mTranslationResultView;
    private ImageView mScreenShotView;
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
        //获取MediaProjectionManager实例
//        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
//        mScreenShotter = new ScreenShotter(windowManager);
        Log.w("lwd", "Create translation service");
        mGoogleOcr = new GoogleOcr(getApplicationContext());
        mInteractionMessageFactory = new InteractionMessageFactory(getApplicationContext());
        mRecognitionResultFilter = new RecognitionResultFilter();

        mScreenLocationCalculator = new ScreenLocationCalculator();
        mTranslationResultFactory = new TranslationResultFactory(getApplicationContext());

    }

    /**
     * 实时地显示字幕截图
     * @param bitmap
     */
    public void showScreenShot(Bitmap bitmap){
        if (Settings.canDrawOverlays(this)) {
            //TODO:这里需要根据屏幕计算：注意屏幕方向影响长宽
            mLayoutParams.format = PixelFormat.RGBA_8888;
            mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            mLayoutParams.x = 0;
            mLayoutParams.y = 0;
            mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

            if(mScreenShotView == null){
                mScreenShotView = new ImageView(getApplicationContext());
                mScreenShotView.setImageBitmap(bitmap);
                windowManager.addView(mScreenShotView, mLayoutParams);
            }
            else{
                mScreenShotView.setImageBitmap(bitmap);
                windowManager.updateViewLayout(mScreenShotView, mLayoutParams);
            }

        }
    }

    /**
     * 显示交互控件
     * @param recognitionResult
     */
    private void showInteractionMessage(RecognitionResult recognitionResult){
        if(recognitionResult == null){
            return;
        }
        Log.d("lwd", "recognize result content: "+ recognitionResult.mContent +
                " top:" + recognitionResult.mTop + " left:" + recognitionResult.mLeft +
                " height:" + recognitionResult.mHeight + " width:" + recognitionResult.mWidth);

        if(mInteractionMessageView != null){
            windowManager.removeView(mInteractionMessageView);
        }
        mInteractionMessageView = mInteractionMessageFactory.getInteractionMessage(recognitionResult.mContent, new TranslationResultDisplayer());

        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        ScreenLocationCalculator.Region region = mScreenLocationCalculator.getTopRegion();
        mLayoutParams.x = region.startPoint.x;
        mLayoutParams.y = region.height  / 2;
        Log.d("lwd", "LayoutParams x:" + mLayoutParams.x + " y:" + mLayoutParams.y);
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        windowManager.addView(mInteractionMessageView, mLayoutParams);
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

    public void createScreenShotButton(){
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = 500;
        mLayoutParams.height = 100;
        mLayoutParams.x = 300;
        mLayoutParams.y = 300;
        Button button = new Button(getApplicationContext());
        button.setText("screen shot");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap mScreenShotImage = mScreenShotter.takeScreenshot();
            }
        });
        windowManager.addView(button, mLayoutParams);
    }

    /**
     * 开始进行识别，显示Service
     */
    public void startRecognizeScreen(){
        final int TIME = 300;
        final Handler mRecognizeHandler = new Handler();
        mRecognizeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("lwd", "recognize screen once");

                Bitmap screenImage = mScreenShotter.takeScreenshot();

                if(screenImage != null){
//                    showScreenShot(screenImage);
                    ArrayList<RecognitionResult> recognitionResultsList = mGoogleOcr.recognize(screenImage);
                    RecognitionResult recognitionResult = mRecognitionResultFilter.filter(recognitionResultsList);
                    showInteractionMessage(recognitionResult);
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
        public ShowTranslationService getService(ScreenShotter screenShotter){
            mScreenShotter = screenShotter;
            return ShowTranslationService.this;
        }
    }

}
