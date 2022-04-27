package com.example.djshichaoren.googleocrtest2.models;

import com.example.djshichaoren.googleocrtest2.http.bean.JinshanTranslation;

import java.util.ArrayList;
import java.util.List;

public class TranslateResult {

    public String mWord;

    public List<String> mTranslationList = new ArrayList<>();


    public TranslateResult(){

    }

    public TranslateResult(String word, String translation){
        mWord = word;
        mTranslationList.add(translation);
    }

    public TranslateResult(JinshanTranslation jinshanTranslation){
        if(jinshanTranslation == null) return;

        mWord = jinshanTranslation.getWord_name();

        if(jinshanTranslation.getSymbols() != null
                && jinshanTranslation.getSymbols().size() > 0
                && jinshanTranslation.getSymbols().get(0) != null
                && jinshanTranslation.getSymbols().get(0).getParts() != null
                && jinshanTranslation.getSymbols().get(0).getParts().size() > 0
                && jinshanTranslation.getSymbols().get(0).getParts().get(0) != null
                && jinshanTranslation.getSymbols().get(0).getParts().get(0).getMeans() != null
                && jinshanTranslation.getSymbols().get(0).getParts().get(0).getMeans().size() > 0
                && jinshanTranslation.getSymbols().get(0).getParts().get(0).getMeans().get(0) != null){

            String translation = jinshanTranslation.getSymbols().get(0).getParts().get(0).getMeans().get(0);
            mTranslationList.add(translation);

        }




    }


}
