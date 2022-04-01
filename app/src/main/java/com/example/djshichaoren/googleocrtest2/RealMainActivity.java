package com.example.djshichaoren.googleocrtest2;

import androidx.appcompat.app.AppCompatActivity;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.djshichaoren.googleocrtest2.services.ShowTranslationService;
import com.example.djshichaoren.googleocrtest2.test.GoogleOcrTester;
import com.example.djshichaoren.googleocrtest2.util.OrientationChangeCallback;
import com.example.djshichaoren.googleocrtest2.util.ScreenLocationCalculator;
import com.example.djshichaoren.googleocrtest2.util.ScreenShotter;

public class RealMainActivity extends AppCompatActivity {

    private MediaProjectionManager mMediaProjectionManager;
    private ScreenShotter mScreenShotter;
    private ShowTranslationService mShowTranslationService;
    private boolean mBound = false;
    private static final String TAG = "lwd";
    private static final int GET_SCREENSHOT_REQUEST_CODE = 0;
    private static final int DRAW_OVERLAY_REQUEST_CODE = 1;
    private Button btn_start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_main);

        //获取MediaProjectionManager实例
        mMediaProjectionManager = (MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mScreenShotter = new ScreenShotter(getWindowManager());

        // 屏幕旋转监听
        OrientationChangeCallback oritationChangeCallback = new OrientationChangeCallback(getApplicationContext(), mScreenShotter);
        oritationChangeCallback.enable();
        ScreenLocationCalculator.setWindowsManager(getWindowManager());

        // 获取控件
        btn_start = findViewById(R.id.btn_start);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DRAW_OVERLAY_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, R.string.user_screen_overlay, Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                // 申请截屏权限
                startActivityForResult(
                        mMediaProjectionManager.createScreenCaptureIntent(),
                        GET_SCREENSHOT_REQUEST_CODE);
            }
        }
        else if(requestCode == GET_SCREENSHOT_REQUEST_CODE){
            if(resultCode != Activity.RESULT_OK){
                Toast.makeText(this, R.string.user_canceled_screen_shot, Toast.LENGTH_SHORT).show();
            }
            else{
                recognizeScreen(resultCode, data);
            }

        }
    }

    public void btnStartRecognition(View view){
        // 允许在其他应用上层显示
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "请授权在其他应用上层显示", Toast.LENGTH_LONG);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())), DRAW_OVERLAY_REQUEST_CODE);
        } else {
            // 申请截屏权限
            startActivityForResult(
                    mMediaProjectionManager.createScreenCaptureIntent(),
                    GET_SCREENSHOT_REQUEST_CODE);
        }
    }

    private void recognizeScreen(int resultCode, Intent data){
        MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
        mScreenShotter.bindSystemScreenShot(mediaProjection);
        mShowTranslationService.startRecognizeScreen();

        btn_start.setText("停止识别");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 载入layout时绑定服务
        Intent intent = new Intent(this, ShowTranslationService.class);
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

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ShowTranslationService.ShowTranslationBinder binder = (ShowTranslationService.ShowTranslationBinder)iBinder;
            mShowTranslationService = binder.getService(mScreenShotter);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };
}
