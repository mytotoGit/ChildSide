package com.ishuinzu.childside.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BatteryChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "BatteryChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive()");
    }
}