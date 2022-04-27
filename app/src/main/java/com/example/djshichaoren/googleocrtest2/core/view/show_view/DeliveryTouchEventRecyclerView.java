package com.example.djshichaoren.googleocrtest2.core.view.show_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class DeliveryTouchEventRecyclerView extends RecyclerView {
    public DeliveryTouchEventRecyclerView(@NonNull Context context) {
        super(context);
    }

    public DeliveryTouchEventRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }
}
