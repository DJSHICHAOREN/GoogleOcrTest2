package com.example.djshichaoren.googleocrtest2.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.models.RecognitionResult;
import com.example.djshichaoren.googleocrtest2.core.recogonize.GoogleOcrImpl;
import com.example.djshichaoren.googleocrtest2.util.RecognitionResultFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/8/22 15:36
 * 修改备注：
 */
public class GoogleOcrTester {
    GoogleOcrImpl mGoogleOcrImpl;
    Context mContext;
    private RecognitionResultFilter mRecognitionResultFilter;
    List<Integer> bitmapKeyList = new ArrayList<>();
    public GoogleOcrTester(Context context){
        mGoogleOcrImpl = GoogleOcrImpl.newInstance(context);
        mContext = context;
        mRecognitionResultFilter = new RecognitionResultFilter();


        bitmapKeyList.add(R.drawable.ss1);
        bitmapKeyList.add(R.drawable.ss2);
//        bitmapKeyList.add(R.drawable.ss3);
//        bitmapKeyList.add(R.drawable.ss4);
//        bitmapKeyList.add(R.drawable.ss5);
//        bitmapKeyList.add(R.drawable.ss6);
//        bitmapKeyList.add(R.drawable.ss7);
    }

    public void testRec(){
        for(int bitmapKey : bitmapKeyList){
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), bitmapKey);

            ArrayList<RecognitionResult> recognitionResultsList = mGoogleOcrImpl.recognize(bitmap);
            RecognitionResult recognitionResult = mRecognitionResultFilter.filter(recognitionResultsList);

            if(recognitionResult == null){
                return;
            }

            Log.d("lwd", "test recognize content: "+ recognitionResult.mContent +
                    " top:" + recognitionResult.mTop + " left:" + recognitionResult.mLeft +
                    " height:" + recognitionResult.mHeight + " width:" + recognitionResult.mWidth);
        }


    }
}
