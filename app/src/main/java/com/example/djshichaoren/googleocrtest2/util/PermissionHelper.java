package com.example.djshichaoren.googleocrtest2.util;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.SparseArray;

import com.example.djshichaoren.googleocrtest2.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2018/10/26 15:46
 * 修改备注：
 */
public class PermissionHelper {

    public interface Code {
        int CAMERA = 1001;
        int WRITE = 1002;
        int READ = 1003;
        int FINE_LOCATION = 1004;
        int COARSE_LOCATION = 1005;
        int CHANGE_WIFI_STATE = 1006;
        int CHANGE_NETWORK_STATE = 1007;
    }

    public interface Permission {
        String CAMERA = Manifest.permission.CAMERA;
        String WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String READ = Manifest.permission.READ_EXTERNAL_STORAGE;
        String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
        String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
        String CHANGE_WIFI_STATE = Manifest.permission.CHANGE_WIFI_STATE;
        String CHANGE_NETWORK_STATE = Manifest.permission.CHANGE_NETWORK_STATE;
    }

    public interface OnGrantListener {
        void onGranted();
    }

    public interface OnDenyListener {
        void onDenied();
    }

    private interface OnRequestListener {
        void onGranted();

        void onDenied();
    }

    private final SparseArray<OnRequestListener> requestMap;
    private final AppCompatActivity activity;

    public PermissionHelper(AppCompatActivity activity) {
        this.activity = activity;
        requestMap = new SparseArray<>();
    }

    public boolean hasPermission(@NonNull String permission) {
        return ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void request(final int code,
                        @NonNull final String permission,
                        @NonNull final OnGrantListener onGrant,
                        @NonNull final OnDenyListener onDeny,
                        @Nullable final String message) {
        OnRequestListener listener = new OnRequestListener() {
            @Override
            public void onGranted() {
                onGrant.onGranted();
            }

            @Override
            public void onDenied() {
                onDeny.onDenied();
            }
        };
        requestMap.put(code, listener);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            listener.onGranted();
            return;
        }

        if (hasPermission(permission)) {
            listener.onGranted();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                if (message == null) {
                    listener.onDenied();
                    return;
                }
                activity.runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                showRationaleDialog(code, permission, message);
                            }
                        }
                );
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
            }
        }
    }

    private void showRationaleDialog(int code,
                                     @NonNull final String permission, @Nullable String message) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.permission_title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{permission}, which);
                            }
                        }
                )
                .show();
    }

    public void handleResult(int requestCode, int[] grantResults) {
        OnRequestListener listener = requestMap.get(requestCode);
        if (listener == null) return;

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            listener.onGranted();
        } else {
            listener.onDenied();
        }
    }

}
