package com.ishuinzu.childside.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.ishuinzu.childside.MainActivity;
import com.ishuinzu.childside.R;
import com.ishuinzu.childside.app.Constant;
import com.ishuinzu.childside.uploader.SMSUploader;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SMSService extends Service {
    private static final String TAG = "SMSService";
    private static final String CHANNEL_ID = "com.ishuinzu.childside.service.SMSService";
    private static final String CHANNEL_NAME = "SMS Service";
    private SMSUploader smsUploader;
    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture<?> future;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Notification
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);

            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.img_logo);
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setChannelId(CHANNEL_ID)
                    .setContentTitle("Child Application")
                    .setContentText("Service running in background")
                    .setSmallIcon(R.drawable.img_logo)
                    .setLargeIcon(icon)
                    .build();

            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            notification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
            startForeground(100, notification);
        } else {
            startForeground(100, new Notification());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        smsUploader = SMSUploader.getInstance(getApplicationContext());
        future = scheduledExecutorService.scheduleAtFixedRate(smsUploader, Constant.UPLOAD_DELAY, Constant.UPLOAD_INTERVAL_APPS, TimeUnit.MILLISECONDS);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        future.cancel(true);
        smsUploader.cancel();

        stopForeground(false);
    }
}