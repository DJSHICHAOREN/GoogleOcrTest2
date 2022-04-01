package com.example.djshichaoren.googleocrtest2.models;

import android.util.Log;

import java.util.HashSet;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/7/16 23:05
 * 修改备注：
 */
public class RecognitionResult {
    public int mTop;
    public int mLeft;
    public int mWidth;
    public int mHeight;
    public String mContent;
    public HashSet<Integer> mContentHashSet = new HashSet<>();

    public RecognitionResult(int top, int left, int width, int height, String content){
        mTop = top;
        mLeft = left;
        mWidth = width;
        mHeight = height;
        mContent = content;

        calculateContentHashSet();
    }

    private int calculateWordHashValue(String word){
        int hashValue = 0;
        for(int i=0; i<word.length(); i++){
            hashValue += (i+1) * word.charAt(i);
        }

        return hashValue;
    }

    /**
     * 计算英文句子的哈希集合
     */
    private void calculateContentHashSet(){
        if(mContent == null){
            return;
        }

        String[]  wordsArray = mContent.split("\\s+");
        for(String word : wordsArray){
            mContentHashSet.add(calculateWordHashValue(word));
        }
    }

    public boolean isSimilar(RecognitionResult recognitionResult){
        HashSet<Integer> unionResult = (HashSet<Integer>)mContentHashSet.clone();
        HashSet<Integer> interResult = (HashSet<Integer>)mContentHashSet.clone();
        unionResult.addAll(recognitionResult.mContentHashSet);
        interResult.retainAll(recognitionResult.mContentHashSet);
//        Log.d("lwd", "unionResult:" + unionResult.size() + " interResult:" + interResult.size());

        if(unionResult.size() == 0){
            return false;
        }
        float IoU = (float)interResult.size() / unionResult.size();
        if(IoU > 0.3){
            return true;
        }
        return false;
    }

    public int getBottom(){
        return mTop + mHeight;
    }
}
