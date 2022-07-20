package com.example.djshichaoren.googleocrtest2;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.djshichaoren.googleocrtest2.ui.fragment.SubtitleFragment;

public class SubtitleActivity extends AppCompatActivity {

    FrameLayout fl_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtitle);

        fl_content = findViewById(R.id.fl_content);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_content, SubtitleFragment.newInstance());
        fragmentTransaction.commit();
    }
}
