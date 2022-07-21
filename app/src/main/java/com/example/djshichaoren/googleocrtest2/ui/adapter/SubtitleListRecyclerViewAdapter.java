package com.example.djshichaoren.googleocrtest2.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;
import com.example.djshichaoren.googleocrtest2.ui.viewholder.SubtitleListItemVH;

import java.util.List;

public class SubtitleListRecyclerViewAdapter extends RecyclerView.Adapter<SubtitleListItemVH> {

    private List<SubtitleEntity> mSubtitleEntityList;

    public SubtitleListRecyclerViewAdapter(List<SubtitleEntity> subtitleEntityList){
        mSubtitleEntityList = subtitleEntityList;
    }

    @NonNull
    @Override
    public SubtitleListItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubtitleListItemVH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_subtitle_list_item_vh, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubtitleListItemVH holder, int position) {
        holder.bind(mSubtitleEntityList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSubtitleEntityList.size();
    }
}
