package com.example.djshichaoren.googleocrtest2.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/7/26 19:16
 * 修改备注：
 */
public class OrientationReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("lwd", "onReceive:" );
    }
}
