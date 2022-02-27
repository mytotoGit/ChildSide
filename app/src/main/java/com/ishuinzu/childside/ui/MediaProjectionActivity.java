package com.ishuinzu.childside.ui;

import static com.ishuinzu.childside.app.Utils.setWindowsFlags;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ishuinzu.childside.R;
import com.ishuinzu.childside.app.Constant;
import com.ishuinzu.childside.databinding.ActivityMediaProjectionBinding;
import com.ishuinzu.childside.service.ScreenRecordService;
import com.ishuinzu.childside.service.ScreenshotService;

public class MediaProjectionActivity extends AppCompatActivity {
    private static final String TAG = "MediaProjectionActivity";
    private ActivityMediaProjectionBinding binding;
    private String toDo;

    // Screen Recording
    private int screenWidth;
    private int screenHeight;
    private int screenDensity;

    // Screen Capture
    private MediaProjectionManager mediaProjectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMediaProjectionBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_media_projection);

        init();
    }

    private void init() {
        toDo = "";

        if (getIntent().getExtras() != null) {
            toDo = getIntent().getExtras().getString("TODO");
        }

        switch (toDo) {
            case "START_RECORD":
                startScreenRecording();
                break;
            case "START_CAPTURE":
                startScreenshot();
                break;
            default:
                finish();
        }

        // Transparent Status Bar
        setWindowsFlags(MediaProjectionActivity.this, new int[]{WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION}, true);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // Get Default Wallpaper
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(MediaProjectionActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Drawable drawable = wallpaperManager.getDrawable();
        findViewById(R.id.mainLayout).setBackground(drawable);
    }

    private void getScreenBaseInfo() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        screenDensity = metrics.densityDpi;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.MEDIA_PROJECTION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                getScreenBaseInfo();
                Intent service = new Intent(this, ScreenRecordService.class);
                service.putExtra("code", resultCode);
                service.putExtra("data", data);
                service.putExtra("audio", true);
                service.putExtra("width", screenWidth);
                service.putExtra("height", screenHeight);
                service.putExtra("density", screenDensity);
                service.putExtra("quality", true);
                startService(service);
                finish();
            }
        }
        if (requestCode == Constant.MEDIA_PROJECTION_REQUEST_CODE_2) {
            if (resultCode == RESULT_OK) {
                Intent screenShotService = new Intent(this, ScreenshotService.class).putExtra(ScreenshotService.EXTRA_RESULT_CODE, resultCode).putExtra(ScreenshotService.EXTRA_RESULT_INTENT, data);
                screenShotService.setAction(ScreenshotService.ACTION_RECORD);
                startService(screenShotService);
                finish();
            }
        }
    }

    private void startScreenshot() {
        mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), Constant.MEDIA_PROJECTION_REQUEST_CODE_2);
    }

    private void startScreenRecording() {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = mediaProjectionManager != null ? mediaProjectionManager.createScreenCaptureIntent() : null;
        startActivityForResult(permissionIntent, Constant.MEDIA_PROJECTION_REQUEST_CODE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}