package com.example.djshichaoren.googleocrtest2.core.word.record;

import com.example.djshichaoren.googleocrtest2.util.text.StringCleaner;

import java.util.List;

public class WordFilter {

    public static String[] filter(String sentence){
        if(sentence == null) return null;

        sentence = StringCleaner.cleanSentenceString(sentence);
        String[] wordList = sentence.split(" ");

        return wordList;
    }
}
