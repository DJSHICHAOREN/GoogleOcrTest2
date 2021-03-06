package com.example.djshichaoren.googleocrtest2;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import com.example.djshichaoren.googleocrtest2.permission.FloatPermissionManager;
import com.example.djshichaoren.googleocrtest2.permission.UphoneCallback;
import com.example.djshichaoren.googleocrtest2.services.WorkService;
import com.example.djshichaoren.googleocrtest2.test.GoogleOcrTester;
import com.example.djshichaoren.googleocrtest2.util.OrientationChangeListener;
import com.example.djshichaoren.googleocrtest2.util.ScreenLocationCalculator;
import com.example.djshichaoren.googleocrtest2.core.screenshot.ScreenShotter;
import com.example.djshichaoren.googleocrtest2.util.image.BitmapSaver;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private WorkService mWorkService;
    private boolean mBound = false;
    private ImageView iv_image;
    private ScreenShotter mScreenShotter;
    private static final String TAG = "lwd";
    private static final int REQUEST_DRAW_OVERLAY = 0;
    private static final int REQUEST_MEDIA_PROJECTION_PERMISSION = 3;

    private MediaProjectionManager mMediaProjectionManager;

    Button btn_show_translation;
    Button btn_show_screen_shot_button;
    Button btn_show_screen_shot_image;
    Button btn_start_recognize_screen_service;
    Button btn_subtitle;

    private RequestPermissionResult mRequestMediaProjectionResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_image = findViewById(R.id.iv_image);
        btn_show_translation = findViewById(R.id.btn_show_translation);
        btn_show_screen_shot_button = findViewById(R.id.btn_show_screen_shot_button);
        btn_show_screen_shot_image = findViewById(R.id.btn_show_screen_shot_image);
        btn_start_recognize_screen_service = findViewById(R.id.btn_start_recognize_screen_service);
        btn_subtitle = findViewById(R.id.btn_subtitle);

        // ??????state bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.parseColor("#000000"));

        mContext = getApplicationContext();
        //??????MediaProjectionManager??????
        mMediaProjectionManager = (MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        mScreenShotter = ScreenShotter.newInstance();
        mScreenShotter.setWindowManager(getWindowManager());

        // ??????????????????
        OrientationChangeListener orientationChangeListener = new OrientationChangeListener(getApplicationContext());
        OrientationChangeListener.addChangeCallback(new OrientationChangeListener.ChangeCallback() {
            @Override
            public void onOrientationChanged(boolean isHorizontal) {
                // ??????????????????????????????
                if(isHorizontal){
                    ScreenLocationCalculator.setOrientationHorizontal();
                }
                else{
                    ScreenLocationCalculator.setOrientationVertical();
                }
//                Log.d("lwd", "change orientation MainActivity isHorizontal:" + isHorizontal);

            }
        });
        orientationChangeListener.enable();
        ScreenLocationCalculator.setWindowsManager(getWindowManager());

        // ????????????
//        requestMediaProjectPermission();

//        requestOverlayPermission();

        // ??????????????????
        btn_show_translation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleOcrTester googleOcrTester = new GoogleOcrTester(mContext);

                googleOcrTester.testRec();
            }
        });

        // ????????????
        btn_show_screen_shot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ??????????????????
                requestOverlayPermission();
//                mWorkService.createScreenShotButton();
                mWorkService.createInteractionShowView();
            }
        });

        // ????????????
        btn_show_screen_shot_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap screenShotImage = mScreenShotter.getScreenShotImage();
                if(screenShotImage != null){
                    iv_image.setImageBitmap(screenShotImage);
                    String bitmapPath = BitmapSaver.save(mContext, screenShotImage);
                    Log.d("lwd", "bitmapPath:" + bitmapPath);
                }

            }
        });

        btn_start_recognize_screen_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ??????????????????
//                requestMediaProjectPermission();
//                requestOverlayPermission();

                boolean hasPermission = FloatPermissionManager.getInstance().applyFloatWindow(MainActivity.this, new UphoneCallback() {
                    @Override
                    public void invoke(boolean granted, String msg) {
                        if (granted) {
                            startRecognizeService();
                        }
                    }

                });
                if(hasPermission){
                    startRecognizeService();
                }

            }
        });

        btn_subtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SubtitleActivity.class);
                startActivity(intent);
            }
        });

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
        }
    }

    // ??????????????????
    private void startRecognizeService(){
        mWorkService.startAll();
    }

    // ??????????????????????????????
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DRAW_OVERLAY) {
            if (!canDrawOverlay()) {
                Toast.makeText(mContext, "?????????????????????????????????????????????", Toast.LENGTH_SHORT);
            }
            else{
                Toast.makeText(mContext, "?????????????????????????????????????????????", Toast.LENGTH_SHORT);
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

    /**
     * ????????????
     *
     * @param angle
     * @param bitmap
     * @return
     */
//    public static Bitmap rotaingImageView(int angle, Bitmap bitmap)
//    {
//        // ???????????? ??????
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//
//        // ??????????????????
//        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
//                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        if (resizedBitmap != bitmap && bitmap != null && !bitmap.isRecycled())
//        {
//            bitmap.recycle();
//            bitmap = null;
//        }
//        return resizedBitmap;
//    }


}
