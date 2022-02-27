package com.ishuinzu.childside.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;
import com.ishuinzu.childside.MainActivity;
import com.ishuinzu.childside.R;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.object.BatteryObject;
import com.ishuinzu.childside.object.ParentObject;
import com.ishuinzu.childside.receiver.BatteryChangeReceiver;

public class BatteryService extends Service {
    private static final String TAG = "BatteryService";
    private static final String CHANNEL_ID = "com.ishuinzu.childside.service.BatteryService";
    private static final String CHANNEL_NAME = "Battery Service";
    private ParentObject parentObject;
    private BatteryChangeReceiver batteryChangeReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Parent
        parentObject = Preferences.getInstance(getApplicationContext()).getParent();
        // Start Battery Receiver
        batteryChangeReceiver = new BatteryChangeReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);

                int percentage = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                int is_charging = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
                int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
                int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
                String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
                if (percentage != 0) {
                    // Update Values To Firebase
                    BatteryObject batteryObject = new BatteryObject(chargingStatus(is_charging), getStringValue(percentage), getStringValue(scale), technology, getStringValue(temperature), getStringValue(voltage));
                    FirebaseDatabase.getInstance().getReference()
                            .child("child_devices")
                            .child(parentObject.getId())
                            .child("battery")
                            .setValue(batteryObject)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Battery Updated");
                                }
                            });
                }
            }
        };
        registerReceiver(batteryChangeReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return START_NOT_STICKY;
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
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryChangeReceiver);
    }

    private String getStringValue(int value) {
        return String.valueOf(value);
    }

    private String chargingStatus(int is_charging) {
        if (is_charging == 0) {
            return "On Battery";
        }
        return "Charging";
    }
}