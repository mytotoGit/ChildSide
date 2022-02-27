package com.ishuinzu.childside.service;

import static com.ishuinzu.childside.app.Utils.isServiceRunning;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ishuinzu.childside.R;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.object.FeatureObject;
import com.ishuinzu.childside.object.ParentObject;
import com.ishuinzu.childside.ui.DashboardActivity;
import com.ishuinzu.childside.ui.MediaProjectionActivity;

import java.util.ArrayList;
import java.util.List;

public class ApplicationService extends Service {
    private static final String TAG = "ApplicationService";
    private static final String CHANNEL_ID = "com.ishuinzu.childside.service.ApplicationService";
    private static final String CHANNEL_NAME = "Application Service";
    private ParentObject parentObject;
    private List<FeatureObject> featureObjects;

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

            Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            notification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
            startForeground(100, notification);
        } else {
            startForeground(100, new Notification());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Parent
        parentObject = Preferences.getInstance(getApplicationContext()).getParent();
        // Get Features From Database
        featureObjects = new ArrayList<>();
        getFeatures();

        return START_NOT_STICKY;
    }

    private void getFeatures() {
        FirebaseDatabase.getInstance().getReference()
                .child("features")
                .child(parentObject.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            featureObjects.clear();

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                FeatureObject featureObject = dataSnapshot.getValue(FeatureObject.class);

                                if (featureObject != null) {
                                    featureObjects.add(featureObject);
                                }
                            }
                            startServices(featureObjects);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void startServices(@NonNull List<FeatureObject> featureObjects) {
        if (featureObjects.size() >= 11) {
            try {
                // Sync Hidden Camera
                if (featureObjects.get(10).getIs_set().equals("true")) {
                    hiddenCameraService(0);
                } else if (featureObjects.get(10).getIs_set().equals("false")) {
                    hiddenCameraService(1);
                }

                // Sync Child
                if (featureObjects.get(9).getIs_set().equals("true")) {
                    batteryService(0);
                } else if (featureObjects.get(9).getIs_set().equals("false")) {
                    batteryService(1);
                }

                // SMS Logs
                if (featureObjects.get(8).getIs_set().equals("true")) {
                    smsService(0);
                } else if (featureObjects.get(8).getIs_set().equals("false")) {
                    smsService(1);
                }

                // Screen Recording
                if (featureObjects.get(7).getIs_set().equals("true")) {
                    screenRecordService(0);
                } else if (featureObjects.get(7).getIs_set().equals("false")) {
                    screenRecordService(1);
                }

                // Screen Capturing
                if (featureObjects.get(6).getIs_set().equals("true")) {
                    screenCaptureService(0);
                } else if (featureObjects.get(6).getIs_set().equals("false")) {
                    screenCaptureService(1);
                }

                // Real Time Location
                if (featureObjects.get(5).getIs_set().equals("true")) {
                    realTimeLocationService(0);
                } else if (featureObjects.get(5).getIs_set().equals("false")) {
                    realTimeLocationService(1);
                }

                // Location Monitoring
                if (featureObjects.get(4).getIs_set().equals("true")) {
                    locationMonitoringService(0);
                } else if (featureObjects.get(4).getIs_set().equals("false")) {
                    locationMonitoringService(1);
                }

                // Live Lock
                if (featureObjects.get(3).getIs_set().equals("true")) {
                    liveLockService(0);
                } else if (featureObjects.get(3).getIs_set().equals("false")) {
                    liveLockService(1);
                }

                // Call Logs
                if (featureObjects.get(1).getIs_set().equals("true")) {
                    callService(0);
                } else if (featureObjects.get(1).getIs_set().equals("false")) {
                    callService(1);
                }

                // Apps & Usage
                if (featureObjects.get(0).getIs_set().equals("true")) {
                    appService(0);
                } else if (featureObjects.get(0).getIs_set().equals("false")) {
                    appService(1);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void liveLockService(int operation) {
        switch (operation) {
            case 0:
                // Live Lock Service
                if (!isServiceRunning(getApplicationContext(), "com.ishuinzu.childside.service.LiveLockService")) {
                    Intent startIntent = new Intent(getApplicationContext(), LiveLockService.class);
                    startService(startIntent);
                    Log.d(TAG, "LIVE LOCK SERVICE STARTED");
                } else {
                    Log.d(TAG, "LIVE LOCK SERVICE ALREADY RUNNING");
                }
                break;

            case 1:
                // Live Lock Service
                stopService(new Intent(getApplicationContext(), LiveLockService.class));
                Log.d(TAG, "LIVE LOCK SERVICE STOPPED");
                break;
        }
    }

    private void screenCaptureService(int operation) {
        switch (operation) {
            case 0:
                // Screen Capture Service
                if (!isServiceRunning(getApplicationContext(), "com.ishuinzu.childside.service.ScreenshotService")) {
                    startActivity(new Intent(getApplicationContext(), MediaProjectionActivity.class).putExtra("TODO", "START_CAPTURE").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            screenCaptureService(1);
                        }
                    }, 5000);
                }
                break;

            case 1:
                // Screen Capture Service
                stopService(new Intent(getApplicationContext(), ScreenshotService.class).setAction(ScreenshotService.ACTION_SHUTDOWN));
                Log.d(TAG, "SCREEN CAPTURE SERVICE STOPPED");
                break;
        }
    }

    private void screenRecordService(int operation) {
        switch (operation) {
            case 0:
                // Screen Record Service
                if (!isServiceRunning(getApplicationContext(), "com.ishuinzu.childside.service.ScreenRecordService")) {
                    startActivity(new Intent(getApplicationContext(), MediaProjectionActivity.class).putExtra("TODO", "START_RECORD").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            screenRecordService(1);
                        }
                    }, 10000);
                }
                break;

            case 1:
                // Screen Record Service
                stopService(new Intent(getApplicationContext(), ScreenRecordService.class));
                Log.d(TAG, "SCREEN RECORD SERVICE STOPPED");
                break;
        }
    }

    private void appService(int operation) {
        switch (operation) {
            case 0:
                // Apps & Usage Service
                if (!isServiceRunning(getApplicationContext(), "com.ishuinzu.childside.service.AppService")) {
                    Intent startIntent = new Intent(getApplicationContext(), AppService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(startIntent);
                    } else {
                        startService(startIntent);
                    }
                    Log.d(TAG, "APP SERVICE STARTED");
                } else {
                    Log.d(TAG, "APP SERVICE ALREADY RUNNING");
                }
                break;

            case 1:
                // Apps & Usage Service
                stopService(new Intent(getApplicationContext(), AppService.class));
                Log.d(TAG, "APP SERVICE STOPPED");
                break;
        }
    }

    private void callService(int operation) {
        switch (operation) {
            case 0:
                // Start Call Service
                if (!isServiceRunning(getApplicationContext(), "com.ishuinzu.childside.service.CallService")) {
                    Intent startIntent = new Intent(getApplicationContext(), CallService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(startIntent);
                    } else {
                        startService(startIntent);
                    }
                    Log.d(TAG, "CALL SERVICE STARTED");
                } else {
                    Log.d(TAG, "CALL SERVICE ALREADY RUNNING");
                }
                break;

            case 1:
                // Stop Call Service
                stopService(new Intent(getApplicationContext(), CallService.class));
                Log.d(TAG, "CALL SERVICE STOPPED");
                break;
        }
    }

    private void locationMonitoringService(int operation) {
        switch (operation) {
            case 0:
                // Start Location Service
                if (!isServiceRunning(getApplicationContext(), "com.ishuinzu.childside.service.LocationMonitoringService")) {
                    Intent startIntent = new Intent(getApplicationContext(), LocationMonitoringService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(startIntent);
                    } else {
                        startService(startIntent);
                    }
                    Log.d(TAG, "LOCATION MONITORING SERVICE STARTED");
                } else {
                    Log.d(TAG, "LOCATION MONITORING SERVICE ALREADY RUNNING");
                }
                break;

            case 1:
                // Stop Location Service
                stopService(new Intent(getApplicationContext(), LocationMonitoringService.class));
                Log.d(TAG, "LOCATION MONITORING SERVICE STOPPED");
                break;
        }
    }

    private void realTimeLocationService(int operation) {
        switch (operation) {
            case 0:
                // Start Location Service
                if (!isServiceRunning(getApplicationContext(), "com.ishuinzu.childside.service.RealTimeLocationService")) {
                    Intent startIntent = new Intent(getApplicationContext(), RealTimeLocationService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(startIntent);
                    } else {
                        startService(startIntent);
                    }
                    Log.d(TAG, "REAL TIME LOCATION SERVICE STARTED");
                } else {
                    Log.d(TAG, "REAL TIME LOCATION SERVICE ALREADY RUNNING");
                }
                break;

            case 1:
                // Stop Location Service
                stopService(new Intent(getApplicationContext(), RealTimeLocationService.class));
                Log.d(TAG, "REAL TIME LOCATION SERVICE STOPPED");
                break;
        }
    }

    private void smsService(int operation) {
        switch (operation) {
            case 0:
                // Start SMS Service
                if (!isServiceRunning(getApplicationContext(), "com.ishuinzu.childside.service.SMSService")) {
                    Intent startIntent = new Intent(getApplicationContext(), SMSService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(startIntent);
                    } else {
                        startService(startIntent);
                    }
                    Log.d(TAG, "SMS SERVICE STARTED");
                } else {
                    Log.d(TAG, "SMS SERVICE ALREADY RUNNING");
                }
                break;

            case 1:
                // Stop SMS Service
                stopService(new Intent(getApplicationContext(), SMSService.class));
                Log.d(TAG, "SMS SERVICE STOPPED");
                break;
        }
    }

    private void batteryService(int operation) {
        switch (operation) {
            case 0:
                // Start Battery Service
                if (!isServiceRunning(getApplicationContext(), "com.ishuinzu.childside.service.BatteryService")) {
                    Intent startIntent = new Intent(getApplicationContext(), BatteryService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(startIntent);
                    } else {
                        startService(startIntent);
                    }
                    Log.d(TAG, "BATTERY SERVICE STARTED");
                } else {
                    Log.d(TAG, "BATTERY SERVICE ALREADY RUNNING");
                }
                break;

            case 1:
                // Stop Battery Service
                stopService(new Intent(getApplicationContext(), BatteryService.class));
                Log.d(TAG, "BATTERY SERVICE STOPPED");
                break;
        }
    }

    private void hiddenCameraService(int operation) {
        switch (operation) {
            case 0:
                // Start Hidden Camera Service
                if (!isServiceRunning(getApplicationContext(), "com.ishuinzu.childside.service.AccessCameraService")) {
                    Intent startIntent = new Intent(getApplicationContext(), AccessCameraService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(startIntent);
                    } else {
                        startService(startIntent);
                    }
                    Log.d(TAG, "HIDDEN CAMERA SERVICE STARTED");
                } else {
                    Log.d(TAG, "HIDDEN CAMERA SERVICE ALREADY RUNNING");
                }
                break;

            case 1:
                // Stop Hidden Camera Service
                stopService(new Intent(getApplicationContext(), AccessCameraService.class));
                Log.d(TAG, "HIDDEN CAMERA SERVICE STOPPED");
                break;
        }
    }
}