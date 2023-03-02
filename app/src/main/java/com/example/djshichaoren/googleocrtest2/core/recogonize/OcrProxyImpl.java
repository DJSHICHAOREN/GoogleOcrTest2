package com.example.djshichaoren.googleocrtest2.core.recogonize;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.djshichaoren.googleocrtest2.models.RecognitionResult;

import java.util.ArrayList;

public class OcrProxyImpl implements OcrProxy {

    OcrProxy ocrProxy;

    public OcrProxyImpl(Context context) {
//        ocrProxy = GoogleOcrImpl.newInstance(context);
        ocrProxy = PaddleOcrImpl.newInstance(context);
    }

    @Override
    public ArrayList<RecognitionResult> recognize(Bitmap image) {
        return ocrProxy.recognize(image);
    }
}
