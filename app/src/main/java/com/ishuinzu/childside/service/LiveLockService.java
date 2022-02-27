package com.ishuinzu.childside.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.FirebaseDatabase;
import com.ishuinzu.childside.R;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.app.Utils;
import com.ishuinzu.childside.object.LiveLockObject;

public class LiveLockService extends Service {
    private WindowManager mWindowManager;
    private View mChatHeadView;
    private LiveLockObject liveLockObject;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"InflateParams", "ClickableViewAccessibility"})
    @Override
    public void onCreate() {
        super.onCreate();

        long currentSnapShot = System.currentTimeMillis();
        liveLockObject = new LiveLockObject();
        liveLockObject.setEnd(0);
        liveLockObject.setStart(currentSnapShot);
        liveLockObject.setId("lock_" + currentSnapShot);

        // Get Current Live Lock Object
        FirebaseDatabase.getInstance().getReference().child("live_lock_history")
                .child(Preferences.getInstance(getApplicationContext()).getParent().getId())
                .child(Utils.getDateID())
                .child(liveLockObject.getId())
                .setValue(liveLockObject)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("LIVELOCK", "Live Lock Added");
                    }
                });

        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.layout_service_head, null);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT
        );

        params.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 0;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mChatHeadView, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWindowManager.removeView(mChatHeadView);

        // Firebase Update End Time
        FirebaseDatabase.getInstance().getReference().child("live_lock_history")
                .child(Preferences.getInstance(getApplicationContext()).getParent().getId())
                .child(Utils.getDateID())
                .child(liveLockObject.getId())
                .child("end")
                .setValue(System.currentTimeMillis())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("LIVELOCK", "Live Lock Updated");
                    }
                });
    }
}