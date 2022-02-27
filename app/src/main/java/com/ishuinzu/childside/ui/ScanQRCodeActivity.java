package com.ishuinzu.childside.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ishuinzu.childside.R;
import com.ishuinzu.childside.animation.PushDownAnimation;
import com.ishuinzu.childside.app.Constant;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.databinding.ActivityScanQrcodeBinding;
import com.ishuinzu.childside.dialog.LoadingDialog;
import com.ishuinzu.childside.object.BatteryObject;
import com.ishuinzu.childside.object.ChildDeviceObject;
import com.ishuinzu.childside.object.LatitudeLongitude;
import com.ishuinzu.childside.object.ParentObject;

import java.util.Objects;

public class ScanQRCodeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ScanQRCodeActivity";
    private ActivityScanQrcodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanQrcodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        PushDownAnimation.setPushDownAnimationTo(binding.btnCloseScreen).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardScanNow).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCloseScreen:
                onBackPressed();
                break;

            case R.id.cardScanNow:
                //Check Device Version
                if (ContextCompat.checkSelfPermission(ScanQRCodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    scanNow();
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, Constant.CAMERA_PERMISSION_CODE);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constant.CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanNow();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void scanNow() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(ScanQRCodeActivity.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Scan QR Code");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ScanQRCodeActivity.this, "Scanned : " + intentResult.getContents(), Toast.LENGTH_SHORT).show();
                uploadDeviceInfo(intentResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void uploadDeviceInfo(String id) {
        LoadingDialog.showLoadingDialog(ScanQRCodeActivity.this);

        // Check if Another Child with Parent ID is registered
        FirebaseDatabase.getInstance().getReference().child("child_devices")
                .child(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            ChildDeviceObject childDevice = task.getResult().getValue(ChildDeviceObject.class);

                            if (childDevice != null) {
                                Log.d(TAG, "ONE CHILD FOUND -> " + childDevice.getParent());

                                // Matching Device Details
                                if (childDevice.getManufacturer().equals(Build.MANUFACTURER) && childDevice.getModel().equals(Build.MODEL)) {
                                    Log.d(TAG, "GOOD TO GO -> " + childDevice.getParent());
                                    getDeviceDetailsAndUpload(id);
                                } else {
                                    Log.d(TAG, "NOT GOOD TO GO -> " + childDevice.getParent());
                                    LoadingDialog.closeDialog();
                                    Toast.makeText(ScanQRCodeActivity.this, "Another Child Device Already Registered.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d(TAG, "NO CHILD FOUND");
                                getDeviceDetailsAndUpload(id);
                            }
                        }
                    }
                });
    }

    private void getDeviceDetailsAndUpload(String id) {
        ChildDeviceObject childDeviceObject = new ChildDeviceObject(new BatteryObject("---", "0", "0", "---", "0", "0"), Build.BRAND, Build.DISPLAY, Build.HARDWARE, new LatitudeLongitude(true, 0, 0), Build.MANUFACTURER, Build.MODEL, id, String.valueOf(Build.VERSION.SDK_INT));
        // Upload Details
        FirebaseDatabase.getInstance().getReference()
                .child("child_devices")
                .child(id)
                .setValue(childDeviceObject)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Child Device Data Uploaded");

                        //Update Parents have_child
                        FirebaseDatabase.getInstance().getReference()
                                .child("parents")
                                .child(id)
                                .child("have_child")
                                .setValue("true")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Parent's have_child Updated");

                                            //Get Parent Data
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("parents")
                                                    .child(id)
                                                    .get()
                                                    .addOnCompleteListener(task1 -> {
                                                        if (task1.isSuccessful()) {
                                                            Log.d(TAG, "Parent Data Fetched");

                                                            DataSnapshot dataSnapshot = task1.getResult();
                                                            if (dataSnapshot != null) {
                                                                if (dataSnapshot.getValue(ParentObject.class) != null) {
                                                                    Preferences.getInstance(ScanQRCodeActivity.this).setLoggedIn(true);
                                                                    Preferences.getInstance(ScanQRCodeActivity.this).setParent(Objects.requireNonNull(dataSnapshot.getValue(ParentObject.class)));

                                                                    LoadingDialog.closeDialog();
                                                                    startActivity(new Intent(ScanQRCodeActivity.this, ParentInformationActivity.class));
                                                                }
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });
    }
}