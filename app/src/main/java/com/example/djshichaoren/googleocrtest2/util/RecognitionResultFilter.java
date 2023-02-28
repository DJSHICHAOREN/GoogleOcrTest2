package com.example.djshichaoren.googleocrtest2.util;

import android.util.Log;

import com.example.djshichaoren.googleocrtest2.models.RecognitionResult;

import java.util.List;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/7/31 22:21
 * 修改备注：
 */
public class RecognitionResultFilter {
    private static RecognitionResult mLatestRecognitionResult;
    private static RecognitionResult mDefaultRecognitionResult = new RecognitionResult(0,0,0,0, "empty");
    /**
     * 过滤识别结果
     * 1.避免重复显示
     * 2.得到最有可能是英文字幕的     * @param recognitionResultList
     *
     * 显示上一个句子有两种情况
     * 当前字幕没有结束：识别出的存在和之前相同内容的句子
     * 当前字幕已经结束：识别出的是本软件显示出的提示的句子
     * 记录长度的前两名，如果第一名与上一个相似，且位置差距很大，则采用第二个
     * @return
     */
    public static RecognitionResult filter(List<RecognitionResult> recognitionResultList){
        // 若没有识别结果，则返回null
        if(recognitionResultList == null || recognitionResultList.size() < 1){
            return null;
        }
        // 选出文字出现的位置最低的
        // todo:找到离box最近的文字
        RecognitionResult candidateResult = mDefaultRecognitionResult;
        for(RecognitionResult recognitionResult : recognitionResultList){
            if(recognitionResult.getBottom()  > candidateResult.getBottom()){
                candidateResult = recognitionResult;
            }
        }

        // 当上次的识别值与这次识别值相同时返回null
        if(mLatestRecognitionResult != null &&
                mLatestRecognitionResult.isSimilar(candidateResult)){
//            Log.d("lwd", "rec get same content top:" + candidateResult.mTop);
            return null;
        }

//        Log.d("lwd", "candidateResult top:" + candidateResult.mTop + " left:" + candidateResult.mLeft);

        mLatestRecognitionResult = candidateResult;

        return candidateResult;
    }



    public RecognitionResult oldFilter(List<RecognitionResult> recognitionResultList){
        // 若没有识别结果，则返回null
        if(recognitionResultList.size() < 1){
            return null;
        }
        // 选出在一幅图片中最长的识别结果
        RecognitionResult candidateResult = mDefaultRecognitionResult;
        for(RecognitionResult recognitionResult : recognitionResultList){

//            // 过滤掉上半部的识别结果，因为有可能识别的是显示结果
            if(Math.abs(recognitionResult.mTop - 86) < 10){
                continue;
            }

            Log.d("lwd", "all top：" + candidateResult.mTop);
            if(recognitionResult.mContent.length() > candidateResult.mContent.length()){
                candidateResult = recognitionResult;
            }
        }
        // 如果是默认结果，则返回null
        if(candidateResult == mDefaultRecognitionResult){
            return null;
        }

//        // 当上次的识别值与这次识别值相同时返回null
        if(mLatestRecognitionResult != null &&
                mLatestRecognitionResult.isSimilar(candidateResult)){
            Log.d("lwd", "rec get same content top:" + candidateResult.mTop + " middleHeight:" + ScreenLocationCalculator.getMiddleHeight());
            return null;
        }


        mLatestRecognitionResult = candidateResult;

        return candidateResult;
    }

}
