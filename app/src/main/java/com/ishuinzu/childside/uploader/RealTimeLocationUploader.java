package com.ishuinzu.childside.uploader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.FirebaseDatabase;
import com.ishuinzu.childside.app.Constant;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.object.LatitudeLongitude;
import com.ishuinzu.childside.object.ParentObject;

import java.util.TimerTask;

@SuppressLint("StaticFieldLeak")
public class RealTimeLocationUploader extends TimerTask {
    private static final String TAG = "RealTimeLocationUploader";
    private final Context context;
    private static RealTimeLocationUploader realTimeLocationUploader;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private ParentObject parentObject;

    private RealTimeLocationUploader(Context context) {
        this.context = context;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        this.parentObject = Preferences.getInstance(context).getParent();
    }

    public static synchronized RealTimeLocationUploader getInstance(Context context) {
        if (realTimeLocationUploader == null) {
            realTimeLocationUploader = new RealTimeLocationUploader(context);
        }
        return realTimeLocationUploader;
    }

    @Override
    public void run() {
        getLocation();
    }

    private void getLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(Constant.LOCATION_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(Constant.LOCATION_UPDATE_FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            saveUserLocation(locationResult.getLastLocation());
        }
    };

    private void saveUserLocation(final Location fetchedLocation) {
        try {
            LatitudeLongitude location = new LatitudeLongitude(true, fetchedLocation.getLatitude(), fetchedLocation.getLongitude());
            FirebaseDatabase.getInstance().getReference()
                    .child("child_devices")
                    .child(parentObject.getId())
                    .child("location")
                    .setValue(location)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Location Updated");
                        }
                    });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean cancel() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        return super.cancel();
    }
}