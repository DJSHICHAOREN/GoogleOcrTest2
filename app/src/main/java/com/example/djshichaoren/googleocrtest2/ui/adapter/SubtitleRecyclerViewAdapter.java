package com.example.djshichaoren.googleocrtest2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.core.word.translate.Translator;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;
import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.srt.SRTSub;
import com.example.djshichaoren.googleocrtest2.ui.viewholder.BaseVH;
import com.example.djshichaoren.googleocrtest2.ui.viewholder.SubtitleItemVH;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubtitleRecyclerViewAdapter extends RecyclerView.Adapter<BaseVH> {

    private SRTSub mSubtitle;
    private Context mContext;
    private Translator mTranslator;
    private SubtitleEntity mSubtitleEntity;

    public SubtitleRecyclerViewAdapter(Context context, SRTSub subtitle, Translator translator, SubtitleEntity subtitleEntity){
        mSubtitle = subtitle;
        mContext = context;
        mTranslator = translator;
        mSubtitleEntity = subtitleEntity;
    }

    @NonNull
    @Override
    public BaseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubtitleItemVH(LayoutInflater.from(mContext).inflate(R.layout.layout_subtitle_item_vh, parent, false), mTranslator, mSubtitleEntity);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseVH holder, int position) {
        // 对于SubtitleItemVH，position指的是sentence在subtitle中的位置
        holder.itemView.setTag(position);
        holder.bind(mSubtitle.getSrtLine(position));
    }

    @Override
    public int getItemCount() {
        return mSubtitle.getLength();
    }

}
