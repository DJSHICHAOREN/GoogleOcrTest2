package com.example.djshichaoren.googleocrtest2.core.word.record;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DigitalWordFilter implements WordFilter {

    @Override
    public String filter(String word) {

        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(word);
        if (matcher.find()) {
            return null;
        }

        return word;
    }
}
