package com.ishuinzu.childside.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ishuinzu.childside.R;
import com.ishuinzu.childside.animation.PushDownAnimation;
import com.ishuinzu.childside.app.Constant;
import com.ishuinzu.childside.core.AppUsageManager;
import com.ishuinzu.childside.databinding.ActivityPermissionsBinding;

public class PermissionsActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "PermissionsActivity";
    private ActivityPermissionsBinding binding;
    private LocationManager locationManager;
    private Boolean isGPSEnabled, isMediaProjection, isOverlay, isLocation, isAppUsage, isCamera, isStorage, isSMS, isCall, isMicrophone, isContacts, isFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        isFirstRun = false;
        isGPSEnabled = false;
        isMediaProjection = false;
        isOverlay = false;
        isLocation = false;
        isAppUsage = false;
        isCamera = false;
        isStorage = false;
        isSMS = false;
        isCall = false;
        isMicrophone = false;
        isContacts = false;

        PushDownAnimation.setPushDownAnimationTo(binding.cardGPS).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardScreenCaptureRecord).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardDisplayOverApps).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardLocation).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardAppUsage).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardCamera).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardStorage).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardSMS).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardCall).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardMicrophone).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardContacts).setOnClickListener(this);
        binding.switchGPS.setOnCheckedChangeListener(this);
        binding.switchScreenCaptureRecord.setOnCheckedChangeListener(this);
        binding.switchDisplayOverApps.setOnCheckedChangeListener(this);
        binding.switchLocation.setOnCheckedChangeListener(this);
        binding.switchAppUsage.setOnCheckedChangeListener(this);
        binding.switchCamera.setOnCheckedChangeListener(this);
        binding.switchStorage.setOnCheckedChangeListener(this);
        binding.switchSMS.setOnCheckedChangeListener(this);
        binding.switchCall.setOnCheckedChangeListener(this);
        binding.switchMicrophone.setOnCheckedChangeListener(this);
        binding.switchContacts.setOnCheckedChangeListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.btnCloseScreen).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardContinue).setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        checkAllPermissions();
    }

    private void checkAllPermissions() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isGPSEnabled = true;
            isFirstRun = true;
            binding.switchGPS.setChecked(true);
        }
        if (Settings.canDrawOverlays(this)) {
            isOverlay = true;
            binding.switchDisplayOverApps.setChecked(true);
        }
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            isLocation = true;
            binding.switchLocation.setChecked(true);
        }
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            isCamera = true;
            binding.switchCamera.setChecked(true);
        }
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            isStorage = true;
            binding.switchStorage.setChecked(true);
        }
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            isSMS = true;
            binding.switchSMS.setChecked(true);
        }
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            isCall = true;
            binding.switchCall.setChecked(true);
        }
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            isContacts = true;
            binding.switchContacts.setChecked(true);
        }
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            isMicrophone = true;
            binding.switchMicrophone.setChecked(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardGPS:
                requestGPSProvider();
                break;

            case R.id.cardScreenCaptureRecord:
                requestMediaProjection();
                break;

            case R.id.cardDisplayOverApps:
                requestOverlayPermission();
                break;

            case R.id.cardLocation:
                requestLocationPermission();
                break;

            case R.id.cardAppUsage:
                requestAppUsagePermission();
                break;

            case R.id.cardCamera:
                requestCameraPermission();
                break;

            case R.id.cardStorage:
                requestStoragePermission();
                break;

            case R.id.cardSMS:
                requestSMSPermission();
                break;

            case R.id.cardCall:
                requestCallPermission();
                break;

            case R.id.cardContacts:
                requestContactsPermission();
                break;

            case R.id.cardMicrophone:
                requestMicrophonePermission();
                break;

            case R.id.btnCloseScreen:
                onBackPressed();
                break;

            case R.id.cardContinue:
                if (isLocation && isCamera && isStorage && isSMS && isCall && isContacts && isMicrophone) {
                    startActivity(new Intent(PermissionsActivity.this, ScanQRCodeActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Please Accept All Permissions", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchGPS:
                requestGPSProvider();
                break;

            case R.id.switchScreenCaptureRecord:
                requestMediaProjection();
                break;

            case R.id.switchDisplayOverApps:
                requestOverlayPermission();
                break;

            case R.id.switchLocation:
                if (isChecked) {
                    requestLocationPermission();
                }
                break;

            case R.id.switchAppUsage:
                if (isChecked) {
                    requestAppUsagePermission();
                }
                break;

            case R.id.switchCamera:
                if (isChecked) {
                    requestCameraPermission();
                }
                break;

            case R.id.switchStorage:
                if (isChecked) {
                    requestStoragePermission();
                }
                break;

            case R.id.switchSMS:
                if (isChecked) {
                    requestSMSPermission();
                }
                break;

            case R.id.switchCall:
                if (isChecked) {
                    requestCallPermission();
                }
                break;

            case R.id.switchContacts:
                if (isChecked) {
                    requestContactsPermission();
                }
                break;

            case R.id.switchMicrophone:
                if (isChecked) {
                    requestMicrophonePermission();
                }
                break;
        }
    }

    private void requestAppUsagePermission() {
        if (new AppUsageManager(PermissionsActivity.this).hasPermission(PermissionsActivity.this)) {
            isAppUsage = true;
            binding.switchAppUsage.setChecked(true);
        } else {
            new AppUsageManager(PermissionsActivity.this).requestPermission(PermissionsActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (new AppUsageManager(PermissionsActivity.this).hasPermission(PermissionsActivity.this)) {
            isAppUsage = true;
            binding.switchAppUsage.setChecked(true);
        } else {
            isAppUsage = false;
            binding.switchAppUsage.setChecked(false);
        }
    }

    private void requestGPSProvider() {
        if (!isFirstRun) {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    private void requestOverlayPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, Constant.DRAW_OVER_OTHER_APPS_PERMISSION_CODE);
    }

    private void requestMediaProjection() {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = mediaProjectionManager != null ? mediaProjectionManager.createScreenCaptureIntent() : null;
        startActivityForResult(permissionIntent, Constant.MEDIA_PROJECTION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.MEDIA_PROJECTION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Permission Accepted
                isMediaProjection = true;
                binding.switchScreenCaptureRecord.setChecked(true);
            } else {
                // Permission Denied
                isMediaProjection = false;
                binding.switchScreenCaptureRecord.setChecked(false);
            }
        }
        if (requestCode == Constant.DRAW_OVER_OTHER_APPS_PERMISSION_CODE) {
            if (Settings.canDrawOverlays(this)) {
                // Permission Accepted
                isOverlay = true;
                binding.switchDisplayOverApps.setChecked(true);
            } else {
                // Permission Denied
                isOverlay = false;
                binding.switchDisplayOverApps.setChecked(false);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            isMicrophone = true;
            binding.switchMicrophone.setChecked(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, Constant.MICROPHONE_PERMISSION_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestContactsPermission() {
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            isContacts = true;
            binding.switchContacts.setChecked(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, Constant.CONTACTS_PERMISSION_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCallPermission() {
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            isCall = true;
            binding.switchCall.setChecked(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG}, Constant.CALL_PERMISSION_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestSMSPermission() {
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            isSMS = true;
            binding.switchSMS.setChecked(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, Constant.SMS_PERMISSION_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            isStorage = true;
            binding.switchStorage.setChecked(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.STORAGE_PERMISSION_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            isCamera = true;
            binding.switchCamera.setChecked(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, Constant.CAMERA_PERMISSION_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            isLocation = true;
            binding.switchLocation.setChecked(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constant.LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constant.LOCATION_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isLocation = true;
                    binding.switchLocation.setChecked(true);
                } else {
                    isLocation = false;
                    binding.switchLocation.setChecked(false);
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;

            case Constant.CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isCamera = true;
                    binding.switchCamera.setChecked(true);
                } else {
                    isCamera = false;
                    binding.switchCamera.setChecked(false);
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;

            case Constant.STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isStorage = true;
                    binding.switchStorage.setChecked(true);
                } else {
                    isStorage = false;
                    binding.switchStorage.setChecked(false);
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;

            case Constant.SMS_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isSMS = true;
                    binding.switchSMS.setChecked(true);
                } else {
                    isSMS = false;
                    binding.switchSMS.setChecked(false);
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;

            case Constant.CALL_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isCall = true;
                    binding.switchCall.setChecked(true);
                } else {
                    isCall = false;
                    binding.switchCall.setChecked(false);
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;

            case Constant.CONTACTS_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isContacts = true;
                    binding.switchContacts.setChecked(true);
                } else {
                    isContacts = false;
                    binding.switchContacts.setChecked(false);
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;

            case Constant.MICROPHONE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isMicrophone = true;
                    binding.switchMicrophone.setChecked(true);
                } else {
                    isMicrophone = false;
                    binding.switchMicrophone.setChecked(false);
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}