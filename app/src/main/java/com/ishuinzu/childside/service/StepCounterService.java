package com.ishuinzu.childside.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class StepCounterService extends Service implements SensorEventListener {
    private static final String TAG = "StepCounterService";
    public static final String BROADCAST_ACTION = ".StepCounterService";

    SensorManager sensorManager;
    Sensor stepDetectorSensor;
    Intent intent;

    int currentStepsDetected;
    int stepCounter;
    int newStepCounter;
    boolean serviceStopped;

    private final Handler handler = new Handler();
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this, stepDetectorSensor, 0);

        currentStepsDetected = 0;
        stepCounter = 0;
        newStepCounter = 0;
        serviceStopped = false;

        handler.removeCallbacks(updateBroadcastData);
        handler.post(updateBroadcastData);

        Log.d("STEPS", "onStartCommand()");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceStopped = true;
        Log.d("STEPS", "onDestroy()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int countSteps = (int) event.values[0];
            if (stepCounter == 0) {
                stepCounter = (int) event.values[0];
            }
            newStepCounter = countSteps - stepCounter;
        }
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            int detectSteps = (int) event.values[0];
            currentStepsDetected += detectSteps;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private final Runnable updateBroadcastData = new Runnable() {
        public void run() {
            if (!serviceStopped) {
                broadcastSensorValue();
                handler.postDelayed(this, 10000);
            }
        }
    };

    private void broadcastSensorValue() {
        intent.putExtra("COUNTED_STEPS_INTEGER", newStepCounter);
        intent.putExtra("COUNTED_STEPS", String.valueOf(newStepCounter));
        intent.putExtra("DETECTED_STEPS_INTEGER", currentStepsDetected);
        intent.putExtra("DETECTED_STEPS", String.valueOf(currentStepsDetected));
        sendBroadcast(intent);
    }
}