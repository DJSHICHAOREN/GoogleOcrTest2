package com.example.djshichaoren.googleocrtest2.core.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.core.view.item_view.BaseVH;
import com.example.djshichaoren.googleocrtest2.core.view.item_view.TranslationContentVH;
import com.example.djshichaoren.googleocrtest2.models.TranslateResult;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TranslationShowViewAdapter extends RecyclerView.Adapter<BaseVH> {

    private List<TranslateResult> mTranslateResult;
    private Context mContext;

    public TranslationShowViewAdapter(Context context, List<TranslateResult> translateResult){
        mContext = context;
        mTranslateResult = translateResult;

        mTranslateResult.add(new TranslateResult("word", "单词"));
    }

    @NonNull
    @Override
    public BaseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TranslationContentVH(LayoutInflater.from(mContext).inflate(R.layout.layout_translation_content_vh, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseVH holder, int position) {
        holder.bind(mTranslateResult.get(position));
    }

    @Override
    public int getItemCount() {
        return mTranslateResult.size();
    }
}
