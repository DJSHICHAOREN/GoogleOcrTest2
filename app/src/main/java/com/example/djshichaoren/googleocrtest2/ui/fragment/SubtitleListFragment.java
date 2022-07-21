package com.example.djshichaoren.googleocrtest2.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.database.SubtitleDatabaseUtil;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;
import com.example.djshichaoren.googleocrtest2.ui.adapter.SubtitleListRecyclerViewAdapter;

import java.util.List;

public class SubtitleListFragment extends Fragment {

    private RecyclerView rv_subtitle_list;
    SubtitleListRecyclerViewAdapter mSubtitleListRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subtitle_list, container, false);
        rv_subtitle_list = view.findViewById(R.id.rv_subtitle_list);

        rv_subtitle_list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        List<SubtitleEntity> subtitleEntityList = SubtitleDatabaseUtil.getAllSubtitle(getContext());
        if(subtitleEntityList != null){
            mSubtitleListRecyclerViewAdapter = new SubtitleListRecyclerViewAdapter(subtitleEntityList);
            rv_subtitle_list.setAdapter(mSubtitleListRecyclerViewAdapter);
        }

        return view;
    }

    public static SubtitleListFragment newInstance(){
        return new SubtitleListFragment();
    }
}
