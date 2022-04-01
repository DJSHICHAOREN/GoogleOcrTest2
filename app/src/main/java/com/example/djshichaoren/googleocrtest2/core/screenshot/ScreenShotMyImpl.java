package com.example.djshichaoren.googleocrtest2.core.screenshot;

import android.app.Activity;
import android.content.Context;
import android.media.projection.MediaProjectionManager;

public class ScreenShotMyImpl {
    private MediaProjectionManager mMediaProjectionManager;


    public ScreenShotMyImpl(Context context){
        mMediaProjectionManager = (MediaProjectionManager)context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);

    }

    public void getScreenShotPermission(){

    }


}
