package com.ishuinzu.childside.ui;

import static com.ishuinzu.childside.app.Utils.getDayName;
import static com.ishuinzu.childside.app.Utils.getInstalledAppList;
import static com.ishuinzu.childside.app.Utils.getMonthName;
import static com.ishuinzu.childside.app.Utils.isServiceRunning;
import static com.ishuinzu.childside.app.Utils.setWindowsFlags;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.ishuinzu.childside.R;
import com.ishuinzu.childside.adapter.ChildAppAdapter;
import com.ishuinzu.childside.animation.PushDownAnimation;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.app.Utils;
import com.ishuinzu.childside.databinding.ActivityDashboardBinding;
import com.ishuinzu.childside.object.AppObject;
import com.ishuinzu.childside.object.ChildAppObject;
import com.ishuinzu.childside.object.StepCounterObject;
import com.ishuinzu.childside.service.ApplicationService;
import com.ishuinzu.childside.service.StepCounterService;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import eightbitlab.com.blurview.RenderScriptBlur;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DashboardActivity";
    private ActivityDashboardBinding binding;

    // Drawer
    private static final int DRAWER_PEEK_HEIGHT = 0;
    private static final int NUM_ROWS = 7;
    private int cellHeight;
    private BottomSheetBehavior mBottomSheetBehavior;
    private List<ChildAppObject> childAppObjects;
    private ChildAppAdapter childAppAdapter;

    // Step Counter
    private Boolean isServiceStarted = false;
    private String[] saveText = new String[4];
    private long startTime;
    private long stopTime;
    private String countedSteps, detectedSteps, distanceFeet, timeLapsed, averageSpeed, caloriesBurn;

    // Firebase Existing Step Counter Values
    private double fbCalories;
    private int fbCount;
    private double fbDistance;
    private long fbStartTime;
    private double fbTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {
        // Transparent Status Bar
        setWindowsFlags(DashboardActivity.this, new int[]{WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION}, true);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // Start Application Service
        if (!isServiceRunning(getApplicationContext(), "com.ishuinzu.childside.service.ApplicationService")) {
            Intent startIntent = new Intent(getApplicationContext(), ApplicationService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(startIntent);
            } else {
                startService(startIntent);
            }
            Log.d(TAG, "APPLICATION SERVICE STARTED");
        } else {
            Log.d(TAG, "APPLICATION SERVICE ALREADY RUNNING");
        }

        // Click Listener
        PushDownAnimation.setPushDownAnimationTo(binding.openAppDrawer).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardHealthTip).setOnClickListener(this);

        setDefaults();
        initializeDrawer();
        getCurrentStepCounts();
    }

    private void getCurrentStepCounts() {
        // Update Firebase Values
        DatabaseReference stepCountReference = FirebaseDatabase.getInstance().getReference().child("step_counter").child(Preferences.getInstance(DashboardActivity.this).getParent().getId()).child(Utils.getDateID());
        stepCountReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                StepCounterObject stepCounter = currentData.getValue(StepCounterObject.class);

                if (stepCounter == null) {
                    currentData.setValue(new StepCounterObject("0.00 cal", "0 steps", "0.00 feet", "0.00 m/sec", stopTime, 0, "0.0 min"));
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (currentData != null) {
                    StepCounterObject stepCounter = currentData.getValue(StepCounterObject.class);

                    if (stepCounter != null) {
                        fbCalories = Double.parseDouble(stepCounter.getCalories().substring(0, stepCounter.getCalories().length() - 4));
                        fbCount = Integer.parseInt(stepCounter.getCount().substring(0, stepCounter.getCount().length() - 6));
                        fbDistance = Double.parseDouble(stepCounter.getDistance().substring(0, stepCounter.getDistance().length() - 5));
                        fbStartTime = stepCounter.getStartTime();
                        fbTime = Double.parseDouble(stepCounter.getTime().substring(0, stepCounter.getTime().length() - 4));

                        Log.d("STEPS", "" + fbCalories);
                        Log.d("STEPS", "" + fbCount);
                        Log.d("STEPS", "" + fbDistance);
                        Log.d("STEPS", "" + fbStartTime);
                        Log.d("STEPS", "" + fbTime);

                        startStepCounter();
                    }
                }
            }
        });
    }

    private void startStepCounter() {
        // Recording Date & Time
        saveText[0] = DateFormat.getDateTimeInstance().format(new Date());
        startTime = Calendar.getInstance().getTimeInMillis();

        // Start Step Counter Service
        startService(new Intent(getBaseContext(), StepCounterService.class));

        // Register Receiver For Values
        registerReceiver(broadcastReceiver, new IntentFilter(StepCounterService.BROADCAST_ACTION));

        // Service Status TRUE
        isServiceStarted = true;
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void updateUI(Intent intent) {
        stopTime = Calendar.getInstance().getTimeInMillis();
        countedSteps = intent.getStringExtra("COUNTED_STEPS");
        detectedSteps = intent.getStringExtra("DETECTED_STEPS");

        String forCaloriesSteps = "" + detectedSteps;
        String forCaloriesTime = "" + Double.parseDouble(String.valueOf(((stopTime - startTime) / 60000)));

        double detectedDistanceNow = (0.0833 * Double.parseDouble(String.valueOf(fbCount)) * Double.parseDouble("10")) + 0.0833 * Double.parseDouble(detectedSteps) * Double.parseDouble("10");
        String detectedDistance = "" + detectedDistanceNow;

        double detectedTimeNow = Double.parseDouble(((stopTime - startTime) / 60000) + "." + (stopTime - startTime) % 60000) + fbTime;
        String detectedTime = "" + detectedTimeNow;

        int detectedStepsNow = Integer.parseInt(detectedSteps) + fbCount;
        detectedSteps = "" + detectedStepsNow;

        // STEPS
        saveText[1] = detectedSteps;
        // DISTANCE (feet)
        saveText[2] = detectedDistance;
        // TIME (min)
        saveText[3] = detectedTime;

        // Average Speed
        averageSpeed = String.format("%.2f", (Double.parseDouble(saveText[2]) / Double.parseDouble(saveText[3])) * 0.00508) + " m/sec";

        // Calculating Calories Burnt
        double fbCaloriesNow = (Double.parseDouble(detectedSteps) * (Double.parseDouble(detectedDistance) / 63));
        caloriesBurn = String.format("%.2f", fbCaloriesNow) + " cal";

        binding.progressStepCounts.setProgress(Double.parseDouble(saveText[1]), 1000);
        binding.txtDistanceCalculated.setText(saveText[2] + " feet");
        binding.txtTimeCalculated.setText(saveText[3] + " min");
        binding.txtSpeedCalculated.setText(averageSpeed);
        binding.txtCaloriesCalculated.setText(caloriesBurn);

        // Step Counter Object
        StepCounterObject stepCounterObject = new StepCounterObject();
        stepCounterObject.setCount(detectedSteps + " steps");
        stepCounterObject.setCalories(binding.txtCaloriesCalculated.getText().toString());
        stepCounterObject.setDistance(binding.txtDistanceCalculated.getText().toString());
        stepCounterObject.setSpeed(binding.txtSpeedCalculated.getText().toString());
        stepCounterObject.setStopTime(stopTime);
        stepCounterObject.setStartTime(startTime);
        stepCounterObject.setTime(binding.txtTimeCalculated.getText().toString());

        // Update Firebase Values
        DatabaseReference stepCountReference = FirebaseDatabase.getInstance().getReference().child("step_counter").child(Preferences.getInstance(DashboardActivity.this).getParent().getId()).child(Utils.getDateID());
        stepCountReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                StepCounterObject stepCounter = currentData.getValue(StepCounterObject.class);

                if (stepCounter == null) {
                    currentData.setValue(new StepCounterObject("0.00 cal", "0 steps", "0.00 feet", "0.00 m/sec", stopTime, 0, "0.0 min"));
                } else {
                    currentData.setValue(stepCounterObject);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDefaults() {
        // Get Default Wallpaper
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(DashboardActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Drawable drawable = wallpaperManager.getDrawable();
        findViewById(R.id.mainLayout).setBackground(drawable);

        cellHeight = (getDisplayContentHeight() - DRAWER_PEEK_HEIGHT) / NUM_ROWS;
        Calendar calendar = Calendar.getInstance();
        binding.txtDate.setText(getDayName(calendar.get(Calendar.DAY_OF_WEEK)) + ", " + getMonthName(calendar.get(Calendar.MONTH) + 1) + " " + calendar.get(Calendar.DATE));
        binding.bottomSheet.setupWith(getWindow().getDecorView().findViewById(android.R.id.content))
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(20f)
                .setBlurEnabled(true)
                .setFrameClearDrawable(drawable)
                .setBlurAutoUpdate(true)
                .setHasFixedTransformationMatrix(false);
    }

    private void loadApps() {
        childAppObjects = new ArrayList<>();
        childAppObjects = getInstalledAppList(DashboardActivity.this);
        Collections.sort(childAppObjects, new Comparator<ChildAppObject>() {
            @Override
            public int compare(ChildAppObject childAppObject, ChildAppObject t1) {
                return childAppObject.getName().compareTo(t1.getName());
            }
        });
        childAppAdapter = new ChildAppAdapter(DashboardActivity.this, childAppObjects, cellHeight);
        binding.drawerGrid.setAdapter(childAppAdapter);

        getApps();
    }

    private void getApps() {
        FirebaseDatabase.getInstance().getReference().child("apps")
                .child(Preferences.getInstance(DashboardActivity.this).getParent().getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() != 0) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                AppObject appObject = dataSnapshot.getValue(AppObject.class);

                                if (appObject != null) {
                                    for (int i = 0; i < childAppObjects.size(); i++) {
                                        if (appObject.getPackage_name().equals(childAppObjects.get(i).getPackage_name())) {
                                            // Exist At Both End
                                            if (appObject.getIs_selected_lock().equals("true")) {
                                                childAppObjects.get(i).setIs_selected_lock("true");
                                            } else {
                                                childAppObjects.get(i).setIs_selected_lock("false");
                                            }
                                            childAppAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void initializeDrawer() {
        View bottomSheet = findViewById(R.id.bottomSheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setPeekHeight(DRAWER_PEEK_HEIGHT);

        // Load Apps
        loadApps();

        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED && binding.drawerGrid.getChildAt(0).getY() != 0)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (newState == BottomSheetBehavior.STATE_DRAGGING && binding.drawerGrid.getChildAt(0).getY() != 0)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openAppDrawer:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case R.id.cardHealthTip:
                startActivity(new Intent(DashboardActivity.this, HealthTipActivity.class).putExtra("CALORIES", binding.txtCaloriesCalculated.getText()));
                break;
        }
    }

    private int getDisplayContentHeight() {
        final WindowManager windowManager = getWindowManager();
        final Point size = new Point();
        int screenHeight = 0, actionBarHeight = 0, statusBarHeight = 0;
        if (getActionBar() != null) {
            actionBarHeight = getActionBar().getHeight();
        }

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        int contentTop = (findViewById(android.R.id.content)).getTop();
        windowManager.getDefaultDisplay().getSize(size);
        screenHeight = size.y;
        return screenHeight - contentTop - actionBarHeight - statusBarHeight;
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.blurLayoutHealth.startBlur();
    }
}