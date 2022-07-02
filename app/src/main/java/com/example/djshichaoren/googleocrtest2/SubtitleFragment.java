package com.example.djshichaoren.googleocrtest2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.djshichaoren.googleocrtest2.subtitle_api.parser.ASSParser;
import com.example.djshichaoren.googleocrtest2.subtitle_api.parser.ParserFactory;
import com.example.djshichaoren.googleocrtest2.subtitle_api.parser.SRTParser;
import com.example.djshichaoren.googleocrtest2.subtitle_api.parser.SubtitleParser;
import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.ass.ASSSub;
import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.common.TimedTextFile;
import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.srt.SRTSub;
import com.example.djshichaoren.googleocrtest2.util.FileUtil;

import java.io.File;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SubtitleFragment extends Fragment {

    private TextView tv_content;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_subtitle, container, false);
        tv_content = root.findViewById(R.id.tv_content);

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


        tv_content.setText(subtitle.toString());


//        File file = new File("subtitle.srt");
//        String extension = FilenameUtils.getExtension(file.getName());
//        SubtitleParser parser = ParserFactory.getParser(extension);
//        TimedTextFile subtitle = parser.parse(file);
//        System.out.println(subtitle.toString());

        return root;
    }


    public static SubtitleFragment newInstance(){
        return new SubtitleFragment();
    }
}
