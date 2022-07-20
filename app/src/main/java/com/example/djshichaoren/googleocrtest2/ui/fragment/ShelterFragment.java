package com.example.djshichaoren.googleocrtest2.ui.fragment;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.RealMainActivity;
import com.example.djshichaoren.googleocrtest2.core.screenshot.ScreenShotter;
import com.example.djshichaoren.googleocrtest2.services.WorkService;
import com.example.djshichaoren.googleocrtest2.util.OrientationChangeListener;
import com.example.djshichaoren.googleocrtest2.util.ScreenLocationCalculator;

public class ShelterFragment extends Fragment {

    private MediaProjectionManager mMediaProjectionManager;
    private ScreenShotter mScreenShotter;
    private WorkService mWorkService;
    private boolean mBound = false;
    private static final String TAG = "lwd";
    private static final int GET_SCREENSHOT_REQUEST_CODE = 0;
    private static final int DRAW_OVERLAY_REQUEST_CODE = 1;
    private Button btn_start;

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

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 允许在其他应用上层显示
//                if (!Settings.canDrawOverlays(getContext())) {
//                    Toast.makeText(getContext(), "请授权在其他应用上层显示", Toast.LENGTH_LONG);
//                    startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                            Uri.parse("package:" + getContext().getPackageName())), DRAW_OVERLAY_REQUEST_CODE);
//                } else {
//                    // 申请截屏权限
//                    startActivityForResult(
//                            mMediaProjectionManager.createScreenCaptureIntent(),
//                            GET_SCREENSHOT_REQUEST_CODE);
//                }

                if(getActivity() instanceof RealMainActivity){
                    RealMainActivity realMainActivity = (RealMainActivity) getActivity();
                    realMainActivity.startRecognitionWithPermission();
                }

            }
        });

        return view;
    }

    public static ShelterFragment newInstance(){
        return new ShelterFragment();
    }
}
