package com.example.djshichaoren.googleocrtest2.util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtil {
    private static int width = 0;
    private static int height = 0;

    public static int getScreenWidth(Context context) {
        if (width == 0) {
            WindowManager manager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            width = display.getWidth();
        }
        return width;
    }

    public static int getScreenHeight(Context context) {
        if (height == 0) {
            WindowManager manager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            height = display.getHeight();
        }
        return height;
    }
}
