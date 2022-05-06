package com.example.djshichaoren.googleocrtest2.core.word.record;

import android.content.Context;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.util.FileUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FamiliarWordFilter implements WordFilter {


    private Set<String> mFamiliarWordSet = new HashSet<>();

    public FamiliarWordFilter(Context context){
        String familiarWordStr = FileUtil.readFromResource(context, R.raw.familiar_word);
        String[] familiarWordArray = familiarWordStr.split(",");
        List<String> familiarWordList = Arrays.asList(familiarWordArray);
        mFamiliarWordSet = new HashSet<>(familiarWordList);
    }

    @Override
    public String filter(String word) {
        if(word == null) return null;

        if(!mFamiliarWordSet.contains(word)){
            return word;
        }
        return null;
    }
}
