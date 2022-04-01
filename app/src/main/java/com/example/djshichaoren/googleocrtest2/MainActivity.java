package com.example.djshichaoren.googleocrtest2;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.djshichaoren.googleocrtest2.services.ShowTranslationService;
import com.example.djshichaoren.googleocrtest2.test.GoogleOcrTester;
import com.example.djshichaoren.googleocrtest2.util.GoogleOcr;
import com.example.djshichaoren.googleocrtest2.util.OrientationChangeCallback;
import com.example.djshichaoren.googleocrtest2.util.ScreenLocationCalculator;
import com.example.djshichaoren.googleocrtest2.util.ScreenShotter;
import com.example.djshichaoren.googleocrtest2.util.image.BitmapSaver;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private GoogleOcr mGoogleOcr;
    private ShowTranslationService mShowTranslationService;
    private boolean mBound = false;
    private ImageView mImageView;
    private ScreenShotter mScreenShotter;
    private static final String TAG = "lwd";
    private static final int REQUEST_DRAW_OVERLAY = 0;
    private static final int REQUEST_MEDIA_PROJECTION_PERMISSION = 3;

    private MediaProjectionManager mMediaProjectionManager;
    private GoogleOcrTester mGoogleOcrTester;

    Button btn_show_translation;
    Button btn_show_screen_shot_button;
    Button btn_show_screen_shot_image;
    Button btn_start_recognize_screen_service;

    private RequestPermissionResult mRequestMediaProjectionResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.iv_image);
        btn_show_translation = findViewById(R.id.btn_show_translation);
        btn_show_screen_shot_button = findViewById(R.id.btn_show_screen_shot_button);
        btn_show_screen_shot_image = findViewById(R.id.btn_show_screen_shot_image);
        btn_start_recognize_screen_service = findViewById(R.id.btn_start_recognize_screen_service);
        // 去掉state bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mContext = getApplicationContext();
        mGoogleOcr = new GoogleOcr(mContext);
        //获取MediaProjectionManager实例
        mMediaProjectionManager = (MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        mScreenShotter = new ScreenShotter(getWindowManager());

        // 屏幕旋转监听
        OrientationChangeCallback oritationChangeCallback = new OrientationChangeCallback(getApplicationContext(), mScreenShotter);
        oritationChangeCallback.enable();
        ScreenLocationCalculator.setWindowsManager(getWindowManager());

        mGoogleOcrTester = new GoogleOcrTester(mGoogleOcr, mContext);
        requestOverlayPermission();

        // 测试识别组件
        btn_show_translation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleOcrTester.testRec();
            }
        });

        // 测试截屏
        btn_show_screen_shot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取监听屏幕权限
                requestMediaProjectPermission(new RequestPermissionResult() {
                    @Override
                    public void successCallback(int resultCode, Intent data) {
                        startRecognizeService(resultCode, data);
                    }
                });

                // 创建截屏按钮
                mShowTranslationService.createScreenShotButton();
            }
        });

        //
        btn_show_screen_shot_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap screenShotImage = mScreenShotter.getScreenShotImage();
                mImageView.setImageBitmap(screenShotImage);

                String bitmapPath = BitmapSaver.save(mContext, screenShotImage);
                Log.d("lwd", "bitmapPath:" + bitmapPath);

            }
        });

        btn_start_recognize_screen_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取截屏权限
                requestMediaProjectPermission(new RequestPermissionResult() {
                    @Override
                    public void successCallback(int resultCode, Intent data) {
                        if(!canDrawOverlay()){
                            requestOverlayPermission();
                        }
                        else{
                            startRecognizeService(resultCode, data);
                        }
                    }
                });
            }
        });

    }


    // 请求屏幕截图权限
    private void requestMediaProjectPermission(RequestPermissionResult requestMediaProjectionResult){
        mRequestMediaProjectionResult = requestMediaProjectionResult;

        startActivityForResult(
                mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION_PERMISSION);
    }

    // 请求显示在屏幕权限
    private void requestOverlayPermission(){
        if(!canDrawOverlay()){
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), REQUEST_DRAW_OVERLAY);
        }
    }

    // 开启截屏服务
    private void startRecognizeService(int resultCode, Intent data){
        MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
        mScreenShotter.bindSystemScreenShot(mediaProjection);
        mShowTranslationService.startRecognizeScreen();

    }

    private boolean canDrawOverlay(){
        return Settings.canDrawOverlays(this);
    }


    private void getStatusBarHeight(){
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        Log.i(TAG, "status bar height:" + result);
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap)
    {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap != bitmap && bitmap != null && !bitmap.isRecycled())
        {
            bitmap.recycle();
            bitmap = null;
        }
        return resizedBitmap;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DRAW_OVERLAY) {
            if (!canDrawOverlay()) {
                Toast.makeText(mContext, "当前无悬浮窗权限，软件无法运行", Toast.LENGTH_SHORT);
            }
            else{
                Toast.makeText(mContext, "已获取悬浮窗权限，点击开始学习", Toast.LENGTH_SHORT);
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

    interface RequestPermissionResult{
        void successCallback(int resultCode, Intent data);
    }


}
