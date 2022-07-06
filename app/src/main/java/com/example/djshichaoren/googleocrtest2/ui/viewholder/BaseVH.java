package com.example.djshichaoren.googleocrtest2.ui.viewholder;

import android.view.View;


import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.srt.SRTLine;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseVH extends RecyclerView.ViewHolder {

    public BaseVH(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bind(SRTLine translateResult);

}
