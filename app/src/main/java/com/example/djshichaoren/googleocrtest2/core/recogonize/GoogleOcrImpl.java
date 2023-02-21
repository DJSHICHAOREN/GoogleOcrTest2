package com.example.djshichaoren.googleocrtest2.core.recogonize;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.example.djshichaoren.googleocrtest2.models.RecognitionResult;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/7/16 19:49
 * 修改备注：
 */
public class GoogleOcrImpl {
    Context mContext;
    TextRecognizer mTextRecognizer;

    private static GoogleOcrImpl mGoogleOcrImpl;


    public GoogleOcrImpl(Context context){
        mContext = context;
        mTextRecognizer = new TextRecognizer.Builder(mContext).build();
    }

    public ArrayList<RecognitionResult> recognize(Bitmap image){
        if (!mTextRecognizer.isOperational()) {
            Log.w("lwd", "Detector dependencies are not yet available.");
            Toast.makeText(mContext, "没有打开识别模块", Toast.LENGTH_SHORT).show();
            return null;
        }

        Frame outFrame = new Frame.Builder().setBitmap(image).build();
        SparseArray<TextBlock> textBlockArray = mTextRecognizer.detect(outFrame);
        ArrayList<RecognitionResult> recognitionResultList = new ArrayList<>();
        for (int i = 0; i < textBlockArray.size(); ++i) {
            TextBlock textBlock = textBlockArray.valueAt(i);

            Rect rect = textBlock.getBoundingBox();
            recognitionResultList.add(new RecognitionResult(rect.top, rect.left, rect.width(), rect.height(), textBlock.getValue()));
        }
        return recognitionResultList;
    }

    public static GoogleOcrImpl newInstance(Context context){
        if(mGoogleOcrImpl == null){
            mGoogleOcrImpl = new GoogleOcrImpl(context);
        }
        return mGoogleOcrImpl;
    }


}
