package com.example.djshichaoren.googleocrtest2.util.text;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 类描述：整理单词、句子字符串
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/8/6 22:43
 * 修改备注：
 */
public class StringCleaner {
    private List<String> punctuationList;
    public StringCleaner(){
        punctuationList = Arrays.asList(".", "," ,"'s","?","!","@","#","$", "%","&","*","/", ";","-","+","=","[","]");
    }

    public String cleanWordString(String word){
        word = word.replaceAll("\\.|,|'s|!|\\?|@|#|$|%|&|\\*|/|;|-|\\+|=|\\|\\]", "");
        word = word.toLowerCase();
        return word;
    }

    public String cleanSentenceString(String sentence){
        sentence = sentence.replaceAll("\\.|,|'s|!|\\?|@|#|$|%|&|\\*|/|;|-|\\+|=|\\|\\]", " ");
        sentence = sentence.toLowerCase();
        return sentence;
    }
}
