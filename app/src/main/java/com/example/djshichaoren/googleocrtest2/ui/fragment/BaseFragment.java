package com.example.djshichaoren.googleocrtest2.ui.fragment;

import android.util.Log;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    public abstract void onRealResume();
    public abstract void onRealPause();

    @Override
    public void onResume() {
        super.onResume();
        onRealResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        onRealPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            //pause
            onRealPause();
        }else{
            //resume
            onRealResume();
        }
    }

}
