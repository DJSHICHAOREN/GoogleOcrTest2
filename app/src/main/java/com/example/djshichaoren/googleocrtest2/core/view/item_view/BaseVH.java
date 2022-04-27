package com.example.djshichaoren.googleocrtest2.core.view.item_view;

import android.view.View;

import com.example.djshichaoren.googleocrtest2.models.TranslateResult;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseVH extends RecyclerView.ViewHolder {

    public BaseVH(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bind(TranslateResult translateResult);

}
