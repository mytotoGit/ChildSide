package com.ishuinzu.childside.uploader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.firebase.database.FirebaseDatabase;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.app.Utils;
import com.ishuinzu.childside.object.CallObject;
import com.ishuinzu.childside.object.ParentObject;
import com.ishuinzu.childside.provider.calllog.Call;
import com.ishuinzu.childside.provider.calllog.CallsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

@SuppressLint("StaticFieldLeak")
public class CallUploader extends TimerTask {
    private static final String TAG = "CallUploader";
    private final Context context;
    private static CallUploader callUploader;
    private ParentObject parentObject;
    private CallsProvider callsProvider;
    private List<Call> calls;

    private CallUploader(Context context) {
        this.context = context;
        this.callsProvider = new CallsProvider(context);
        this.parentObject = Preferences.getInstance(context).getParent();
        this.calls = new ArrayList<>();
    }

    public static synchronized CallUploader getInstance(Context context) {
        if (callUploader == null) {
            callUploader = new CallUploader(context);
        }
        return callUploader;
    }

    @Override
    public void run() {
        uploadCalls();
    }

    private void uploadCalls() {
        // Check Permission
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            // Firebase : DB -> features -> {PARENT_ID} -> call_logs -> is_allowed -> TRUE
            FirebaseDatabase.getInstance().getReference().child("features")
                    .child(parentObject.getId())
                    .child("call_logs")
                    .child("is_allowed")
                    .setValue("true")
                    .addOnCompleteListener(taskCheck -> {
                        if (taskCheck.isSuccessful()) {
                            Log.d(TAG, "Call Permission Status Changed -> TRUE");

                            // Get Calls List
                            try {
                                calls = callsProvider.getCalls().getList();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                for (int i = 0; i < calls.size(); i++) {
                                    // Convert To CallObject
                                    CallObject callObject = Utils.convertToCallObject(calls.get(i));
                                    String call_id = "call_" + callObject.getCall_date();
                                    int finalI = i;
                                    FirebaseDatabase.getInstance().getReference().child("calls")
                                            .child(parentObject.getId())
                                            .child(Utils.getDateID())
                                            .child(callObject.getNumber().replaceAll("\\s+", ""))
                                            .child(call_id)
                                            .setValue(callObject)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "ID :" + call_id);

                                                    if (finalI == calls.size() - 1) {
                                                        // Change Status
                                                        FirebaseDatabase.getInstance().getReference().child("features")
                                                                .child(Preferences.getInstance(context).getParent().getId())
                                                                .child("call_logs")
                                                                .child("is_set")
                                                                .setValue("false");
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });
        } else {
            // Firebase : DB -> features -> {PARENT_ID} -> call_logs -> is_allowed -> FALSE
            FirebaseDatabase.getInstance().getReference().child("features")
                    .child(parentObject.getId())
                    .child("call_logs")
                    .child("is_allowed")
                    .setValue("false")
                    .addOnCompleteListener(taskCheck -> {
                        if (taskCheck.isSuccessful()) {
                            Log.d(TAG, "Call Permission Status Changed -> FALSE");

                            // Change Status
                            FirebaseDatabase.getInstance().getReference().child("features")
                                    .child(Preferences.getInstance(context).getParent().getId())
                                    .child("call_logs")
                                    .child("is_set")
                                    .setValue("false");
                        }
                    });
        }
    }

    @Override
    public boolean cancel() {
        calls.clear();
        return super.cancel();
    }
}