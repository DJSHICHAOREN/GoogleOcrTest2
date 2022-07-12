package com.example.djshichaoren.googleocrtest2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.djshichaoren.googleocrtest2.config.Constants;
import com.example.djshichaoren.googleocrtest2.core.word.translate.Translator;
import com.example.djshichaoren.googleocrtest2.database.SubtitleDatabase;
import com.example.djshichaoren.googleocrtest2.database.SubtitleDatabaseUtil;
import com.example.djshichaoren.googleocrtest2.database.dao.SubtitleDao;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;
import com.example.djshichaoren.googleocrtest2.subtitle_api.parser.SRTParser;
import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.srt.SRTSub;

import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.djshichaoren.googleocrtest2.ui.adapter.SubtitleRecyclerViewAdapter;
import com.example.djshichaoren.googleocrtest2.util.JinshanTranslator;

public class SubtitleFragment extends Fragment {

    private TextView tv_content;
    private RecyclerView rc_subtitle;
    private SubtitleRecyclerViewAdapter mSubtitleRecyclerViewAdapter;
    private Translator mTranslator = new JinshanTranslator();
    private LinearLayoutManager mLinearLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_subtitle, container, false);
        tv_content = root.findViewById(R.id.tv_content);
        rc_subtitle = root.findViewById(R.id.rc_subtitle);

//        File file = new File("subtitle.ass");
//
//        ASSParser parser = new ASSParser();
//        ASSSub subtitle = parser.parse(file);
//
//        System.out.println(subtitle.toString());

//        File inputStreamfile = new File("subtitle.srt");
        //        SRTSub subtitle = parser.parse(file, null);


        SRTParser parser = new SRTParser();
        InputStream inputStream = getContext().getResources().openRawResource(R.raw.subtitle);
        SRTSub subtitle = parser.parse(inputStream, "subtitle.srt", null);

        SubtitleDatabase subtitleDatabase = SubtitleDatabase.getInstance(getContext());
        SubtitleDao subtitleDao = subtitleDatabase.getSubtitleDao();

        String subtitleName = "subtitle.srt";

        SubtitleEntity subtitleEntity = SubtitleDatabaseUtil.getSubtitleEntity(getContext(), subtitleName);
        if(subtitleEntity == null){
            subtitleEntity = SubtitleDatabaseUtil.insertSubtitleEntity(getContext(), subtitleName);
        }

//        long id = subtitleDao.insertSubtitle(new SubtitleEntity(subtitleName));
//        tv_content.setText(subtitle.toString());

        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rc_subtitle.setLayoutManager(mLinearLayoutManager);
        mSubtitleRecyclerViewAdapter = new SubtitleRecyclerViewAdapter(getContext(), subtitle, mTranslator, subtitleEntity);
        rc_subtitle.setAdapter(mSubtitleRecyclerViewAdapter);
        rc_subtitle.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 60;
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sp = getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        int firstVisiblePosition = sp.getInt(Constants.SP_SUBTITLE_SCROLL_DEPTH_KEY, 0);
        rc_subtitle.scrollToPosition(firstVisiblePosition);


    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(Constants.SP_SUBTITLE_SCROLL_DEPTH_KEY, mLinearLayoutManager.findFirstVisibleItemPosition());
        editor.apply();
    }

    public static SubtitleFragment newInstance(){
        return new SubtitleFragment();
    }
}
