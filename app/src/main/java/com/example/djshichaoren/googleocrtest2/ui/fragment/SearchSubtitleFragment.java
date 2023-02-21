package com.example.djshichaoren.googleocrtest2.ui.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.http.bean.SubtitleSearchResult;
import com.example.djshichaoren.googleocrtest2.ui.adapter.SearchSubtitleResultRecyclerViewAdapter;
import com.example.djshichaoren.googleocrtest2.util.SubtitleHttpUtil;

public class SearchSubtitleFragment extends DialogFragment {

    private EditText et_subtitle_name;
    private Button btn_search;
    private LinearLayout ll_whole;
    private RecyclerView rv_search_result;

    private SubtitleHttpUtil mSubtitleHttpUtil = new SubtitleHttpUtil();
    private SearchSubtitleResultRecyclerViewAdapter mSearchSubtitleResultRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_search_subtitle_fragment, container, true);

        et_subtitle_name = rootView.findViewById(R.id.et_subtitle_name);
        btn_search = rootView.findViewById(R.id.btn_search);
        ll_whole = rootView.findViewById(R.id.ll_whole);
        rv_search_result = rootView.findViewById(R.id.rv_search_result);

        rv_search_result.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 20;
            }
        });

        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = dialog.getWindow();
        if(window != null){
            window.getDecorView().setPadding(0,0,0,0);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //去除默认白色边框
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                window.setElevation(0f);
            }

        }


        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        et_subtitle_name.requestFocusFromTouch();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_search_result.setLayoutManager(linearLayoutManager);

        mSearchSubtitleResultRecyclerViewAdapter = new SearchSubtitleResultRecyclerViewAdapter(mSubtitleHttpUtil);
        rv_search_result.setAdapter(mSearchSubtitleResultRecyclerViewAdapter);
        rv_search_result.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 30;
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSubtitleHttpUtil.search(et_subtitle_name.getText().toString(), new SubtitleHttpUtil.SearchSubtitleCallback() {
                    @Override
                    public void success(SubtitleSearchResult subtitleSearchResult) {
                        if(subtitleSearchResult.sub != null && subtitleSearchResult.sub.subs != null){
                            mSearchSubtitleResultRecyclerViewAdapter.setSubList(subtitleSearchResult.sub.subs);
                            mSearchSubtitleResultRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void error() {

                    }
                });
            }
        });

        ll_whole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchSubtitleFragment.this.dismiss();
            }
        });
    }
}
