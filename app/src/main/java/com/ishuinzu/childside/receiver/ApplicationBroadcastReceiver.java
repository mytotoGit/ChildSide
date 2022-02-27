package com.ishuinzu.childside.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Objects;

public class ApplicationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = Objects.requireNonNull(intent.getData()).getEncodedSchemeSpecificPart();
        Toast.makeText(context, "USER UNINSTALLED : " + packageName, Toast.LENGTH_SHORT).show();
    }
}