package com.example.djshichaoren.googleocrtest2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.djshichaoren.googleocrtest2.services.WorkService;
import com.example.djshichaoren.googleocrtest2.ui.fragment.ShelterFragment;
import com.example.djshichaoren.googleocrtest2.ui.fragment.SubtitleFragment;
import com.example.djshichaoren.googleocrtest2.ui.fragment.SubtitleListFragment;
import com.example.djshichaoren.googleocrtest2.util.OrientationChangeListener;
import com.example.djshichaoren.googleocrtest2.util.ScreenLocationCalculator;
import com.example.djshichaoren.googleocrtest2.core.screenshot.ScreenShotter;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class RealMainActivity extends AppCompatActivity {

    private MediaProjectionManager mMediaProjectionManager;
    private ScreenShotter mScreenShotter;
    private WorkService mWorkService;
    private boolean mBound = false;
    private static final String TAG = "lwd";

    private static final int REQUEST_DRAW_OVERLAY = 0;
    private static final int REQUEST_MEDIA_PROJECTION_PERMISSION = 3;

    private RequestPermissionResult mRequestMediaProjectionResult;
    private RequestPermissionResult mRequestDrawOverlaynResult;

    private List<Fragment> mFragmentList = new ArrayList<>();
    private Fragment mCurrentFragment;
    private BottomBar mBottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_main);

        //??????MediaProjectionManager??????
        mMediaProjectionManager = (MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mScreenShotter = ScreenShotter.newInstance();
        mScreenShotter.setWindowManager(getWindowManager());

        // ??????????????????
        OrientationChangeListener oritationChangeCallback = new OrientationChangeListener(getApplicationContext());
        oritationChangeCallback.addChangeCallback(new OrientationChangeListener.ChangeCallback() {
            @Override
            public void onOrientationChanged(boolean isHorizontal) {

            }
        });
        oritationChangeCallback.enable();
        ScreenLocationCalculator.setWindowsManager(getWindowManager());


        // ????????????
        mBottomBar = findViewById(R.id.bottom_bar);

        createFragment();
        changeFragment(0);

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                int index = 0;
                switch (tabId){
                    case R.id.tab_shelter:
                        index = 0;
                        break;
                    case R.id.tab_subtitle:
                        index = 1;
                        break;
                }

                changeFragment(index);
            }
        });

        requestMediaProjectPermission();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DRAW_OVERLAY) {
            if (!canDrawOverlay()) {
                Toast.makeText(this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();

                if(mRequestDrawOverlaynResult != null){
                    mRequestDrawOverlaynResult.successCallback(resultCode, data);
                }
            }
            return;
        }

        if (requestCode == REQUEST_MEDIA_PROJECTION_PERMISSION) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, R.string.user_canceled_screen_shot, Toast.LENGTH_SHORT).show();
                return;
            }
            if(mRequestMediaProjectionResult != null){
                mRequestMediaProjectionResult.successCallback(resultCode, data);
            }
        }
    }

    public void createFragment(){
        ShelterFragment shelterFragment = ShelterFragment.newInstance();
        mFragmentList.add(shelterFragment);

        SubtitleListFragment subtitleListFragment = SubtitleListFragment.newInstance();
        mFragmentList.add(subtitleListFragment);

//        SubtitleFragment subtitleFragment = SubtitleFragment.newInstance();
//        mFragmentList.add(subtitleFragment);
    }

    private void changeFragment(int position) {
        if (isFinishing()) {
            return;
        }
        if(position >= mFragmentList.size()) return;

        Fragment fragment = mFragmentList.get(position);
        if (fragment != mCurrentFragment) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            if (fragment.isAdded()) {
                fragmentTransaction.show(fragment).hide(mCurrentFragment);
            } else {
                fragmentTransaction.add(R.id.fl_container, fragment);
                if (mCurrentFragment != null) {
                    fragmentTransaction.hide(mCurrentFragment);
                }
            }
            fragmentTransaction.commitAllowingStateLoss();
            mCurrentFragment = fragment;
        }
    }

    public void startRecognitionWithPermission(){
        // ??????????????????
        requestMediaProjectPermission();
        requestOverlayPermission();

        startRecognizeService();
    }

    // ??????????????????
    public void startRecognizeService(){
        mWorkService.startAll();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ??????layout???????????????
        Intent intent = new Intent(this, WorkService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBound){
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    // ????????????????????????
    private void requestMediaProjectPermission(){

        if(!mScreenShotter.isSupportScreenShot()){
            startActivityForResult(
                    mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION_PERMISSION);

            mRequestMediaProjectionResult = new RequestPermissionResult() {
                @Override
                public void successCallback(int resultCode, Intent data) {
                    MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
                    mScreenShotter.setMediaProjection(mediaProjection);
                }
            };
        }

    }

    // ???????????????????????????
    private void requestOverlayPermission(){
        if(!canDrawOverlay()){
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), REQUEST_DRAW_OVERLAY);

            mRequestDrawOverlaynResult = new RequestPermissionResult() {
                @Override
                public void successCallback(int resultCode, Intent data) {

                }
            };
        }
    }

    // ??????????????????????????????
    private boolean canDrawOverlay(){
        return Settings.canDrawOverlays(this);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            WorkService.ShowTranslationBinder binder = (WorkService.ShowTranslationBinder)iBinder;
            mWorkService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    interface RequestPermissionResult{
        void successCallback(int resultCode, Intent data);
    }
}
