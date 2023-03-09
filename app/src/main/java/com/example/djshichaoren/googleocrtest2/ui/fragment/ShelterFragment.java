package com.example.djshichaoren.googleocrtest2.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.djshichaoren.googleocrtest2.MainActivity;
import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.RealMainActivity;
import com.example.djshichaoren.googleocrtest2.config.Constants;
import com.example.djshichaoren.googleocrtest2.core.screenshot.ScreenShotter;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;
import com.example.djshichaoren.googleocrtest2.permission.FloatPermissionManager;
import com.example.djshichaoren.googleocrtest2.permission.UphoneCallback;
import com.example.djshichaoren.googleocrtest2.services.WorkService;
import com.example.djshichaoren.googleocrtest2.util.OrientationChangeListener;
import com.example.djshichaoren.googleocrtest2.util.ScreenLocationCalculator;

public class ShelterFragment extends BaseFragment {

    private MediaProjectionManager mMediaProjectionManager;
    private ScreenShotter mScreenShotter;
    private WorkService mWorkService;
    private boolean mBound = false;
    private static final String TAG = "lwd";
    private static final int GET_SCREENSHOT_REQUEST_CODE = 0;
    private static final int DRAW_OVERLAY_REQUEST_CODE = 1;
    private Button btn_start;
    private Button btn_choose_assist_subtitle;
    private TextView tv_assist_subtitle;

    private static final String START_RECOGNIZE_BUTTON_TEXT = "开始识别";
    private static final String STOP_RECOGNIZE_BUTTON_TEXT = "结束识别";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取MediaProjectionManager实例
        mMediaProjectionManager = (MediaProjectionManager)getActivity().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mScreenShotter = ScreenShotter.newInstance();

        // 屏幕旋转监听
        ScreenLocationCalculator.setWindowsManager(getActivity().getWindowManager());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shelter, container, false);
        btn_start = view.findViewById(R.id.btn_start);
        tv_assist_subtitle = view.findViewById(R.id.tv_assist_subtitle);
        btn_choose_assist_subtitle = view.findViewById(R.id.btn_choose_assist_subtitle);
        btn_start.setTag(0);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() instanceof RealMainActivity){
                    RealMainActivity realMainActivity = (RealMainActivity) getActivity();
                    if((int)btn_start.getTag() == 0){
                        realMainActivity.startRecognitionWithPermission();
                        btn_start.setText(STOP_RECOGNIZE_BUTTON_TEXT);
                        btn_start.setTag(1);
                    }
                    else{
                        realMainActivity.stopRecognizeService();
                        btn_start.setText(START_RECOGNIZE_BUTTON_TEXT);
                        btn_start.setTag(0);
                    }

                }

//                boolean hasPermission = FloatPermissionManager.getInstance().applyFloatWindow(getContext(), new UphoneCallback() {
//                    @Override
//                    public void invoke(boolean granted, String msg) {
//                        if (granted) {
//                            startLearnVideo();
//                        }
//                    }
//
//                });
//                if(hasPermission){
//                    startLearnVideo();
//                }

            }
        });

        btn_choose_assist_subtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof RealMainActivity) {
                    RealMainActivity realMainActivity = (RealMainActivity)getActivity();
                    realMainActivity.changeFragmentToChooseAssistSubtitle();
                }
            }
        });

        return view;
    }

    @Override
    public void onRealResume() {
        if(getArguments() != null) {
            Bundle bundle = getArguments();
            if(bundle.getBoolean(Constants.IS_SET_ASSIST_SUBTITLE_KEY, false)) {
                SubtitleEntity subtitleEntity = (SubtitleEntity) bundle.getSerializable(Constants.SUBTITLE_LIST_ITEM_VH_SUBTITLE_KEY);
                tv_assist_subtitle.setText(subtitleEntity.getName());

                bundle.putSerializable(Constants.SUBTITLE_LIST_ITEM_VH_SUBTITLE_KEY, null);
                bundle.putBoolean(Constants.IS_SET_ASSIST_SUBTITLE_KEY, false);
            }
        }
    }

    @Override
    public void onRealPause() {

    }

    public void startLearnVideo(){
        if(getActivity() instanceof RealMainActivity){
            RealMainActivity realMainActivity = (RealMainActivity) getActivity();
            realMainActivity.startRecognizeService();
        }
    }

    public static ShelterFragment newInstance(){
        return new ShelterFragment();
    }
}
