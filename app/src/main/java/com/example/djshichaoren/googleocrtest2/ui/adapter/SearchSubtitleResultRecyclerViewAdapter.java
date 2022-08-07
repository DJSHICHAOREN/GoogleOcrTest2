package com.example.djshichaoren.googleocrtest2.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.http.bean.SubtitleSearchResult;
import com.example.djshichaoren.googleocrtest2.ui.viewholder.SearchSubtitleResultItemVH;
import com.example.djshichaoren.googleocrtest2.util.SubtitleHttpUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchSubtitleResultRecyclerViewAdapter extends RecyclerView.Adapter<SearchSubtitleResultItemVH> {

    private List<SubtitleSearchResult.SubEntity> mSubList = new ArrayList<>();
    private SubtitleHttpUtil mSubtitleHttpUtil;

    public SearchSubtitleResultRecyclerViewAdapter(SubtitleHttpUtil subtitleHttpUtil){
        mSubtitleHttpUtil = subtitleHttpUtil;
    }

    @NonNull
    @Override
    public SearchSubtitleResultItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchSubtitleResultItemVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_subtitle_result_item_vh, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchSubtitleResultItemVH holder, int position) {
        holder.setData(mSubList.get(position), mSubtitleHttpUtil);
    }

    @Override
    public int getItemCount() {
        return mSubList.size();
    }

    public void setSubList(List<SubtitleSearchResult.SubEntity> subList){
        mSubList = subList;
    }
}
