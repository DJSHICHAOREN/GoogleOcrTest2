package com.example.djshichaoren.googleocrtest2.core.word.translate;

import com.example.djshichaoren.googleocrtest2.models.TranslateResult;

public interface Translator {

     void translate(String word, TranslateCallback translateCallback);

    interface TranslateCallback {
        public void success(TranslateResult translateResult);
    }
}
