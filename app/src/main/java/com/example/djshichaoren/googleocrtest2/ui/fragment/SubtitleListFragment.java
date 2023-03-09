package com.example.djshichaoren.googleocrtest2.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.config.Constants;
import com.example.djshichaoren.googleocrtest2.database.SubtitleDatabaseUtil;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;
import com.example.djshichaoren.googleocrtest2.models.BaseEvent;
import com.example.djshichaoren.googleocrtest2.subtitle_api.parser.SRTParser;
import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.srt.SRTSub;
import com.example.djshichaoren.googleocrtest2.ui.adapter.SubtitleListRecyclerViewAdapter;
import com.example.djshichaoren.googleocrtest2.util.FileUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.InputStream;
import java.util.List;

public class SubtitleListFragment extends Fragment {

    private RecyclerView rv_subtitle_list;
    private Button btn_add_network_subtitle;
    SubtitleListRecyclerViewAdapter mSubtitleListRecyclerViewAdapter;
    private static final String DEFAULT_SUBTITLE_NAME  = "星际穿越.srt";
    private List<SubtitleEntity> mSubtitleEntityList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subtitle_list, container, false);
        rv_subtitle_list = view.findViewById(R.id.rv_subtitle_list);
        btn_add_network_subtitle = view.findViewById(R.id.btn_add_network_subtitle);

        rv_subtitle_list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mSubtitleEntityList = SubtitleDatabaseUtil.getAllSubtitle(getContext());
        if(mSubtitleEntityList != null){
            mSubtitleListRecyclerViewAdapter = new SubtitleListRecyclerViewAdapter(mSubtitleEntityList);
            rv_subtitle_list.setAdapter(mSubtitleListRecyclerViewAdapter);
        }

        SubtitleEntity subtitleEntity = SubtitleDatabaseUtil.getSubtitleEntity(getContext(), DEFAULT_SUBTITLE_NAME);
        if(subtitleEntity == null){
            subtitleEntity = SubtitleDatabaseUtil.insertSubtitleEntity(getContext(), DEFAULT_SUBTITLE_NAME);

            InputStream inputStream = getContext().getResources().openRawResource(R.raw.subtitle);
            FileUtil.writeTheInputStreamToLocalFile(inputStream, DEFAULT_SUBTITLE_NAME, getContext());

            mSubtitleEntityList.add(subtitleEntity);
            mSubtitleListRecyclerViewAdapter.notifyDataSetChanged();
        }

        btn_add_network_subtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchSubtitleFragment searchSubtitleFragment = new SearchSubtitleFragment();
                searchSubtitleFragment.show(getChildFragmentManager(), "SearchSubtitleFragment");
            }
        });

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        Log.d("lwd", "SubtitleListFragment onCreateView");

        rv_subtitle_list.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;
            }
        });


        return view;
    }

    @Subscribe
    public void onEventMainThread(BaseEvent baseEvent) {
        if(baseEvent == null) return;

        if(Constants.FLUSH_SUBTITLE_LIST_EVENT.equals( baseEvent.getType())){
            mSubtitleEntityList = SubtitleDatabaseUtil.getAllSubtitle(getContext());
            mSubtitleListRecyclerViewAdapter.setSubtitleEntityList(mSubtitleEntityList);
            mSubtitleListRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void beginAnimationToChooseItem() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i < rv_subtitle_list.getChildCount(); i++) {
                    View view = rv_subtitle_list.getChildAt(i);

                    Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.choose_subtitle_animation);
                    animator.setTarget(view);
                    animator.start();
                }
            }
        });

    }

    public static SubtitleListFragment newInstance(){
        return new SubtitleListFragment();
    }

}
