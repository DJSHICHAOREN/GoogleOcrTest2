package com.example.djshichaoren.googleocrtest2.core.recogonize;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.djshichaoren.googleocrtest2.models.RecognitionResult;

import java.util.ArrayList;

public class OcrProxyImpl implements OcrProxy {

    public static OcrProxy mOcrProxy;

    public OcrProxyImpl(Context context) {
//        ocrProxy = GoogleOcrImpl.newInstance(context);
        mOcrProxy = PaddleOcrImpl.newInstance(context);
    }

    @Override
    public ArrayList<RecognitionResult> recognize(Bitmap image) {
        return mOcrProxy.recognize(image);
    }

    public static OcrProxy newInstance(Context context){
        if(mOcrProxy == null){
            mOcrProxy = new PaddleOcrImpl(context);
        }
        return mOcrProxy;
    }


}
