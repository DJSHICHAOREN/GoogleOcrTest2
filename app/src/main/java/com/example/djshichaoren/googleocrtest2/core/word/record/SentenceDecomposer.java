package com.example.djshichaoren.googleocrtest2.core.word.record;

import android.content.Context;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.util.FileUtil;
import com.example.djshichaoren.googleocrtest2.util.text.StringCleaner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SentenceDecomposer {

    private List<WordFilter> mWordFilterList;

    public SentenceDecomposer(Context context){

        mWordFilterList = new ArrayList<>();
        mWordFilterList.add(new FamiliarWordFilter(context));
        mWordFilterList.add(new DigitalWordFilter());
    }


    public List<String> filter(String sentence){
        if(sentence == null) return null;

        sentence = StringCleaner.cleanSentenceString(sentence);
        String[] wordList = sentence.split(" ");

        List<String> strangeWordList = new ArrayList<>();
        for(String word : wordList){

            for(WordFilter wordFilter : mWordFilterList){
                word = wordFilter.filter(word);
                if(word == null){
                    break;
                }
            }

            if(word != null){
                strangeWordList.add(word);
            }
        }

        return strangeWordList;
    }
}
