package com.ishuinzu.childside;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ishuinzu.childside.app.Constant;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.databinding.ActivityMainBinding;
import com.ishuinzu.childside.ui.DashboardActivity;
import com.ishuinzu.childside.ui.InstructionsActivity;
import com.ishuinzu.childside.ui.ParentInformationActivity;
import com.ishuinzu.childside.ui.PermissionsActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Preferences.getInstance(MainActivity.this).getIsDefaultLauncher()) {
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish();
            return;
        }
        init();
    }

    private void init() {
        if (Preferences.getInstance(MainActivity.this).getInstructionsSeen()) {
            if (Preferences.getInstance(MainActivity.this).getLoggedIn()) {
                if (Preferences.getInstance(MainActivity.this).getIsDefaultLauncher()) {
                    redirectToDashboard();
                } else {
                    redirectToSetDefaultLauncher();
                }
            } else {
                redirectToPermissions();
            }
        } else {
            redirectToInstructions();
        }
    }

    private void redirectToSetDefaultLauncher() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, ParentInformationActivity.class));
            finish();
        }, Constant.DELAY);
    }

    private void redirectToDashboard() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish();
        }, Constant.DELAY);
    }

    private void redirectToInstructions() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, InstructionsActivity.class));
            finish();
        }, Constant.DELAY);
    }

    private void redirectToPermissions() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, PermissionsActivity.class));
            finish();
        }, Constant.DELAY);
    }

}