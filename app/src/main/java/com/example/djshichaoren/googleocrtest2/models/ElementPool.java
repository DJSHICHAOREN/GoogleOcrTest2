package com.example.djshichaoren.googleocrtest2.models;

import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.djshichaoren.googleocrtest2.ui.element.OutlineTextView;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 类描述：负责提供Element
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/7/24 17:04
 * 修改备注：
 */
public class ElementPool<ElementType> {
    private int mMaxElementCount;
    Class<ElementType> mElementClass;
    private Set<ElementType> mElementPool = new HashSet<ElementType>();
    private Context mContext;
    private List<ElementType> mDisplayedElementList = new ArrayList<>();

    public ElementPool(Class<ElementType> elementClass, Context context, int maxElementCount){
        mMaxElementCount = maxElementCount;
        mElementClass = elementClass;
        mContext = context;
        initElementPool();
    }

    private void addOneElementToElementPool(){
        ElementType element = (ElementType) new OutlineTextView(mContext);
        mElementPool.add(element);
    }

    private void initElementPool() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<mMaxElementCount; i++) {
                    addOneElementToElementPool();
                }
            }
        }, 0);

    }

    public ElementType getFloatElement(){
        ElementType element;
        if(mElementPool.iterator().hasNext()){
            element = mElementPool.iterator().next();
        }
        else{
            addOneElementToElementPool();
            Log.d("lwd", "add one element");
            return getFloatElement();
//            return null;
        }
        mElementPool.remove(element);
        return element;
    }

    public void deleteElement(View view){
        mElementPool.add((ElementType)view);
    }
}
