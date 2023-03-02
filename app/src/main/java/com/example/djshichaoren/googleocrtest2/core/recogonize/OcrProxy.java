package com.example.djshichaoren.googleocrtest2.core.recogonize;

import android.graphics.Bitmap;

import com.example.djshichaoren.googleocrtest2.models.RecognitionResult;

import java.util.ArrayList;

public interface OcrProxy {
    public ArrayList<RecognitionResult> recognize(Bitmap image);
}
