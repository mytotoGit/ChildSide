package com.ishuinzu.childside.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ishuinzu.childside.R;
import com.ishuinzu.childside.adapter.InstructionAdapter;
import com.ishuinzu.childside.animation.PushDownAnimation;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.databinding.ActivityInstructionsBinding;
import com.ishuinzu.childside.object.InstructionObject;
import com.ishuinzu.childside.transformer.PagerTransformer;

import java.util.ArrayList;
import java.util.List;

public class InstructionsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "InstructionsActivity";
    private ActivityInstructionsBinding binding;
    private List<InstructionObject> instructionObjects;
    private InstructionAdapter instructionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstructionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        PushDownAnimation.setPushDownAnimationTo(binding.btnCloseScreen).setOnClickListener(this);
        PushDownAnimation.setPushDownAnimationTo(binding.cardGetStarted).setOnClickListener(this);

        loadInstructions();
    }

    private void loadInstructions() {
        instructionObjects = new ArrayList<>();

        instructionObjects.add(new InstructionObject(R.raw.logo, "Instruction Title", "Instruction Description Here"));
        instructionObjects.add(new InstructionObject(R.raw.logo, "Instruction Title", "Instruction Description Here"));
        instructionObjects.add(new InstructionObject(R.raw.logo, "Instruction Title", "Instruction Description Here"));
        instructionObjects.add(new InstructionObject(R.raw.logo, "Instruction Title", "Instruction Description Here"));
        instructionObjects.add(new InstructionObject(R.raw.logo, "Instruction Title", "Instruction Description Here"));

        instructionAdapter = new InstructionAdapter(InstructionsActivity.this, instructionObjects);
        binding.pagerInstructions.setAdapter(instructionAdapter);
        binding.pagerInstructions.setPageTransformer(true, new PagerTransformer(InstructionsActivity.this));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCloseScreen:
                onBackPressed();
                break;

            case R.id.cardGetStarted:
                Preferences.getInstance(InstructionsActivity.this).setInstructionsSeen(true);
                startActivity(new Intent(InstructionsActivity.this, PermissionsActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}