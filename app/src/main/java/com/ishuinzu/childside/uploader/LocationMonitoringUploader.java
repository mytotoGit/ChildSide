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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ishuinzu.childside.app.Constant;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.app.Utils;
import com.ishuinzu.childside.notification.NotificationSender;
import com.ishuinzu.childside.object.ChildLocation;
import com.ishuinzu.childside.object.LatitudeLongitude;
import com.ishuinzu.childside.object.ParentObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

@SuppressLint("StaticFieldLeak")
public class LocationMonitoringUploader extends TimerTask {
    private static final String TAG = "LocationMonitoringUploader";
    private final Context context;
    private static LocationMonitoringUploader locationUploader;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<ChildLocation> childLocations;
    private LocationRequest locationRequest;
    private ParentObject parentObject;

    private LocationMonitoringUploader(Context context) {
        this.context = context;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        this.parentObject = Preferences.getInstance(context).getParent();
        this.childLocations = new ArrayList<>();
    }

    public static synchronized LocationMonitoringUploader getInstance(Context context) {
        if (locationUploader == null) {
            locationUploader = new LocationMonitoringUploader(context);
        }
        return locationUploader;
    }

    @Override
    public void run() {
        getChildLocations();
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
            if (childLocations != null) {
                matchLocation(locationResult.getLastLocation());
            }
        }
    };

    private void matchLocation(final Location location) {
        for (ChildLocation childLocation : childLocations) {
            if (Utils.shouldMatchLocation(childLocation)) {
                Log.d("ChildLocationMonitoring", "Tracking Started");
                checkAndSetStartLocation(childLocation, location);
            }
        }
    }

    private void checkAndSetStartLocation(ChildLocation childLocation, Location location) {
        LatitudeLongitude latitudeLongitude = new LatitudeLongitude(true, location.getLatitude(), location.getLongitude());

        // Start Location
        FirebaseDatabase.getInstance().getReference().child("locations")
                .child(Preferences.getInstance(context).getParent().getId())
                .child(childLocation.getSlot_id())
                .child("start_location")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() != 0) {
                            LatitudeLongitude startLocation = snapshot.getValue(LatitudeLongitude.class);

                            if (startLocation != null) {
                                if (startLocation.getLatitude() == 0 && startLocation.getLongitude() == 0) {
                                    FirebaseDatabase.getInstance().getReference().child("locations")
                                            .child(Preferences.getInstance(context).getParent().getId())
                                            .child(childLocation.getSlot_id())
                                            .child("start_location")
                                            .setValue(latitudeLongitude);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        // Current Location
        FirebaseDatabase.getInstance().getReference().child("locations")
                .child(Preferences.getInstance(context).getParent().getId())
                .child(childLocation.getSlot_id())
                .child("current_location")
                .setValue(latitudeLongitude);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int currentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);

        if (currentHour == childLocation.getEnd_time().getHour()) {
            if (currentMinute == childLocation.getEnd_time().getMinute()) {
                // Last Location
                FirebaseDatabase.getInstance().getReference().child("locations")
                        .child(Preferences.getInstance(context).getParent().getId())
                        .child(childLocation.getSlot_id())
                        .child("last_location")
                        .setValue(latitudeLongitude);

                // Send Notification
                new NotificationSender(context, Preferences.getInstance(context).getParent().getFcm_token(), "Location Monitoring", "Your child has been successfully monitored according to the Location provided by you. Please check Location section for more details.");

                // Tracking -> FALSE
                FirebaseDatabase.getInstance().getReference().child("locations")
                        .child(Preferences.getInstance(context).getParent().getId())
                        .child(childLocation.getSlot_id())
                        .child("tracking")
                        .setValue(false);
            }
        }

        // Tracking Location
        double trackingLocationLatitude = childLocation.getTracking_location().getLatitude();
        double trackingLocationLongitude = childLocation.getTracking_location().getLongitude();

        // Current Location
        double currentLocationLatitude = location.getLatitude();
        double currentLocationLongitude = location.getLongitude();

        // Statuses
        FirebaseDatabase.getInstance().getReference().child("monitoring_statuses")
                .child(Preferences.getInstance(context).getParent().getId())
                .child(childLocation.getSlot_id())
                .child("status" + System.currentTimeMillis())
                .setValue(getDistance(trackingLocationLatitude, trackingLocationLongitude, currentLocationLatitude, currentLocationLongitude) < 0.1);
    }

    private double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sinLat = Math.sin(dLat / 2);
        double sinLng = Math.sin(dLng / 2);
        double a = Math.pow(sinLat, 2) + Math.pow(sinLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    private void getChildLocations() {
        FirebaseDatabase.getInstance().getReference()
                .child("locations")
                .child(parentObject.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        childLocations.clear();

                        if (snapshot.getChildrenCount() != 0) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                ChildLocation childLocation = dataSnapshot.getValue(ChildLocation.class);
                                if (childLocation != null) {
                                    childLocations.add(childLocation);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, error.getMessage());
                    }
                });
    }
}