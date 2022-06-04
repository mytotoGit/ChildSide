package com.ishuinzu.childside.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ishuinzu.childside.BuildConfig;
import com.ishuinzu.childside.R;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.app.Utils;
import com.ishuinzu.childside.core.ImageTransformer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

public class ScreenshotService extends Service {
    public static final String EXTRA_RESULT_CODE = "resultCode";
    public static final String EXTRA_RESULT_INTENT = "resultIntent";
    private static final String CHANNEL_WHATEVER = "channel_whatever";
    private static final int NOTIFY_ID = 9906;
    public static final String ACTION_RECORD = BuildConfig.APPLICATION_ID + ".RECORD";
    public static final String ACTION_SHUTDOWN = BuildConfig.APPLICATION_ID + ".SHUTDOWN";
    static final int VIRT_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
    private MediaProjection projection;
    private VirtualDisplay vdisplay;
    final private HandlerThread handlerThread = new HandlerThread(getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_BACKGROUND);
    private Handler handler;
    private MediaProjectionManager mgr;
    private WindowManager wmgr;
    private ImageTransformer it;
    private int resultCode;
    private Intent resultData;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mgr = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        wmgr = (WindowManager) getSystemService(WINDOW_SERVICE);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {
        if (ACTION_RECORD.equals(i.getAction())) {
            resultCode = i.getIntExtra(EXTRA_RESULT_CODE, 1337);
            resultData = i.getParcelableExtra(EXTRA_RESULT_INTENT);
            foregroundify();

            if (resultData != null) {
                new Handler().postDelayed(this::startCapture, 2000);
            }
        } else if (ACTION_SHUTDOWN.equals(i.getAction())) {
            stopForeground(true);
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            stopCapture();

        // Upload File
        Uri uri = Uri.fromFile(new File(Preferences.getInstance(getApplicationContext()).getFilePath()));
        // Upload On Firebase Storage
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("images").child(Preferences.getInstance(getApplicationContext()).getParent().getId()).child("" + System.currentTimeMillis()).child(uri.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(uri);

        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();

                FirebaseDatabase.getInstance().getReference().child("screen_shots")
                        .child(Preferences.getInstance(getApplicationContext()).getParent().getId())
                        .child(Utils.getDateID())
                        .child("SS_" + System.currentTimeMillis())
                        .child("link")
                        .setValue(downloadUri.toString())
                        .addOnCompleteListener(uploadURLTask -> {
                            if (uploadURLTask.isSuccessful()) {
                                Log.d("Screenshot", "UPLOADED");

                                // Change Status
                                FirebaseDatabase.getInstance().getReference().child("features")
                                        .child(Preferences.getInstance(getApplicationContext()).getParent().getId())
                                        .child("screen_capturing")
                                        .child("is_set")
                                        .setValue("false");

                                // Delete Path
                                Preferences.getInstance(getApplicationContext()).deleteFilePath();
                            }
                        });
            }
        });
        }catch (Exception e){

    }

}

    public WindowManager getWindowManager() {
        return (wmgr);
    }

    public Handler getHandler() {
        return (handler);
    }

    public void processImage(final byte[] png) {
        new Thread() {
            @Override
            public void run() {
                File output = new File(getExternalFilesDir(null) + "/" + "SS.png");

                try {
                    FileOutputStream fos = new FileOutputStream(output);
                    fos.write(png);
                    fos.flush();
                    fos.getFD().sync();
                    fos.close();

                    // Save Path To Preferences
                    Preferences.getInstance(getApplicationContext()).setFilePath(getExternalFilesDir(null) + "/" + "SS.png");
                    MediaScannerConnection.scanFile(ScreenshotService.this, new String[]{output.getAbsolutePath()}, new String[]{"image/png"}, null);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Exception writing out screenshot", e);
                }
            }
        }.start();
        stopCapture();
    }

    private void stopCapture() {
        if (projection != null) {
            projection.stop();
            vdisplay.release();
            projection = null;
        }
    }

    private void startCapture() {
        projection = mgr.getMediaProjection(resultCode, resultData);
        it = new ImageTransformer(this);

        MediaProjection.Callback cb = new MediaProjection.Callback() {
            @Override
            public void onStop() {
                vdisplay.release();
            }
        };

        Log.d("ScreenshotService", "Here");

        vdisplay = projection.createVirtualDisplay("andshooter", it.getWidth(), it.getHeight(), getResources().getDisplayMetrics().densityDpi, VIRT_DISPLAY_FLAGS, it.getSurface(), null, handler);
        projection.registerCallback(cb, handler);
    }

    private void foregroundify() {
        NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mgr.getNotificationChannel(CHANNEL_WHATEVER) == null) {
            mgr.createNotificationChannel(new NotificationChannel(CHANNEL_WHATEVER, "Whatever", NotificationManager.IMPORTANCE_DEFAULT));
        }
        NotificationCompat.Builder b = new NotificationCompat.Builder(this, CHANNEL_WHATEVER);

        b.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL);
        b.setContentTitle("Child Application").setSmallIcon(R.drawable.img_logo).setTicker("Service running in background");

        startForeground(NOTIFY_ID, b.build());
    }
}