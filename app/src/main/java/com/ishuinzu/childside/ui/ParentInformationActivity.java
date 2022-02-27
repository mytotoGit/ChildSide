package com.ishuinzu.childside.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ishuinzu.childside.R;
import com.ishuinzu.childside.animation.PushDownAnimation;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.databinding.ActivityParentInformationBinding;
import com.ishuinzu.childside.object.ParentObject;
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
    }
}