package com.ishuinzu.childside.ui;

import static com.ishuinzu.childside.app.Utils.isServiceRunning;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ishuinzu.childside.R;
import com.ishuinzu.childside.animation.PushDownAnimation;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.databinding.ActivityParentInformationBinding;
import com.ishuinzu.childside.object.ParentObject;
import com.ishuinzu.childside.service.ApplicationService;
import com.ishuinzu.childside.task.DownloadImageTask;

public class ParentInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityParentInformationBinding binding;
    private ParentObject parentObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        PushDownAnimation.setPushDownAnimationTo(binding.btnCloseScreen).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardSetDefaultLauncher).setOnClickListener(this);

        parentObject = Preferences.getInstance(ParentInformationActivity.this).getParent();

        if (parentObject != null) {
            binding.txtName.setText(parentObject.getName());
            binding.txtEmail.setText(parentObject.getEmail());
            new DownloadImageTask(binding.imgProfile).execute(parentObject.getImg_url());
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCloseScreen:
                onBackPressed();
                break;

            case R.id.cardSetDefaultLauncher:
                setDefaultLauncher();
                break;
        }
    }

    private void setDefaultLauncher() {
        Preferences.getInstance(ParentInformationActivity.this).setIsDefaultLauncher(true);
        startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
/*

        if (!isServiceRunning(getApplicationContext(), "com.ishuinzu.childside.service.ApplicationService")) {
            Intent startIntent = new Intent(getApplicationContext(), ApplicationService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(startIntent);
            } else {
                startService(startIntent);
            }
            Log.d("TAG", "APPLICATION SERVICE STARTED");

            Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);

            startActivity(notificationIntent);
            finish();
        } else {
            Log.d("TAG", "APPLICATION SERVICE ALREADY RUNNING");
        }
*/

    }
}