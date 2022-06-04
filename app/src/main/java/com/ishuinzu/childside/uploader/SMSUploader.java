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
import com.ishuinzu.childside.object.ParentObject;
import com.ishuinzu.childside.object.SMSObject;
import com.ishuinzu.childside.provider.telephony.SMS;
import com.ishuinzu.childside.provider.telephony.TelephonyProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

@SuppressLint("StaticFieldLeak")
public class SMSUploader extends TimerTask {
    private static final String TAG = "SMSUploader";
    private final Context context;
    private static SMSUploader smsUploader;
    private ParentObject parentObject;
    TelephonyProvider telephonyProvider;
    List<SMS> smsList;

    private SMSUploader(Context context) {
        this.context = context;
        this.telephonyProvider = new TelephonyProvider(context);
        this.parentObject = Preferences.getInstance(context).getParent();
        this.smsList = new ArrayList<>();
    }

    public static synchronized SMSUploader getInstance(Context context) {
        if (smsUploader == null) {
            smsUploader = new SMSUploader(context);
        }
        return smsUploader;
    }

    @Override
    public void run() {
        uploadSMSs();
    }

    private void uploadSMSs() {
        // Check Permission
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            // Firebase : DB -> features -> {PARENT_ID} -> sms_logs -> is_allowed -> TRUE
            FirebaseDatabase.getInstance().getReference().child("features")
                    .child(parentObject.getId())
                    .child("sms_logs")
                    .child("is_allowed")
                    .setValue("true")
                    .addOnCompleteListener(taskCheck -> {
                        if (taskCheck.isSuccessful()) {
                            Log.d(TAG, "SMS Permission Status Changed -> TRUE");

                            // Get SMS List
                            try {
                                smsList = telephonyProvider.getSms(TelephonyProvider.Filter.ALL).getList();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                for (int i = 0; i < smsList.size(); i++) {
                                    try {
                                    // Convert To SMSObject
                                    SMSObject smsObject = Utils.convertToSMSObject(smsList.get(i));

                                    // SMS ID
                                    String sms_id = "sms_" + smsObject.getDate().getReceived();
                                    int finalI = i;
                                    FirebaseDatabase.getInstance().getReference().child("sms")
                                            .child(parentObject.getId())
                                            .child(Utils.getDateID())
                                            .child(smsObject.getAddress().replaceAll("\\s+", ""))
                                            .child(sms_id)
                                            .setValue(smsObject)
                                            .addOnCompleteListener(taskUpload -> {
                                                if (taskUpload.isSuccessful()) {
                                                    Log.d(TAG, "ID :" + sms_id);

                                                    if (finalI == smsList.size() - 1) {
                                                        // Change Status
                                                        FirebaseDatabase.getInstance().getReference().child("features")
                                                                .child(Preferences.getInstance(context).getParent().getId())
                                                                .child("sms_logs")
                                                                .child("is_set")
                                                                .setValue("false");
                                                    }
                                                }
                                            });
                                    }catch (Exception e){
                                        continue;
                                    }

                                }

                            }
                        }
                    });
        } else {
            // Firebase : DB -> features -> {PARENT_ID} -> sms_logs -> is_allowed -> FALSE
            FirebaseDatabase.getInstance().getReference().child("features")
                    .child(parentObject.getId())
                    .child("sms_logs")
                    .child("is_allowed")
                    .setValue("false")
                    .addOnCompleteListener(taskCheck -> {
                        if (taskCheck.isSuccessful()) {
                            Log.d(TAG, "SMS Permission Status Changed -> FALSE");

                            // Change Status
                            FirebaseDatabase.getInstance().getReference().child("features")
                                    .child(Preferences.getInstance(context).getParent().getId())
                                    .child("sms_logs")
                                    .child("is_set")
                                    .setValue("false");
                        }
                    });
        }
    }

    @Override
    public boolean cancel() {
        smsList.clear();
        return super.cancel();
    }
}