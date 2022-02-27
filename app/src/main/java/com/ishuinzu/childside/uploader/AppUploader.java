package com.ishuinzu.childside.uploader;

import static com.ishuinzu.childside.app.Utils.getAppObject;
import static com.ishuinzu.childside.app.Utils.getImageUri;
import static com.ishuinzu.childside.app.Utils.getInstalledAppList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.app.Utils;
import com.ishuinzu.childside.core.AppUsageManager;
import com.ishuinzu.childside.object.AppObject;
import com.ishuinzu.childside.object.AppUsageObject;
import com.ishuinzu.childside.object.ChildAppObject;
import com.ishuinzu.childside.object.ParentObject;
import com.ishuinzu.childside.object.UsageObject;
import com.ishuinzu.childside.task.GetAppUsageTask;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

@SuppressLint("StaticFieldLeak")
public class AppUploader extends TimerTask {
    private static final String TAG = "AppUploader";
    private final Context context;
    private static AppUploader appUploader;
    private ParentObject parentObject;
    private List<ChildAppObject> lists;
    private AppUsageManager appUsageManager;

    private AppUploader(Context context) {
        this.context = context;
        this.parentObject = Preferences.getInstance(context).getParent();
        this.lists = new ArrayList<>();
        this.appUsageManager = new AppUsageManager(context);
    }

    public static synchronized AppUploader getInstance(Context context) {
        if (appUploader == null) {
            appUploader = new AppUploader(context);
        }
        return appUploader;
    }

    @Override
    public void run() {
        lists = getInstalledAppList(context);
        Log.d(TAG, "APPS -> UPLOADING");
        uploadAppList(lists);
        uploadAppUsages();
    }

    private void uploadAppList(List<ChildAppObject> lists) {
        for (int i = 0; i < lists.size(); i++) {
            String icon_name = "icon" + lists.get(i).getName();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("icons").child(parentObject.getId()).child(icon_name + ".jpeg");
            int finalI = i;
            storageReference.putFile(getImageUri(context, ((BitmapDrawable) lists.get(i).getIcon()).getBitmap(), lists.get(i).getName() + ".jpeg"))
                    .addOnCompleteListener(taskUpload -> {
                        if (taskUpload.isSuccessful()) {
                            storageReference.getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        String icon_link = uri.toString();

                                        try {
                                            AppObject appObject = getAppObject(lists.get(finalI), icon_link);
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("apps")
                                                    .child(parentObject.getId())
                                                    .child(appObject.getName())
                                                    .setValue(appObject)
                                                    .addOnCompleteListener(taskLink -> {
                                                        if (taskLink.isSuccessful()) {
                                                            Log.d(TAG, "APP -> UPLOADED");

                                                            if (finalI == lists.size() - 1) {
                                                                Log.d(TAG, "All Apps Uploaded");

                                                                // Change Status
                                                                FirebaseDatabase.getInstance().getReference().child("features")
                                                                        .child(Preferences.getInstance(context).getParent().getId())
                                                                        .child("apps_and_usage")
                                                                        .child("is_set")
                                                                        .setValue("false");
                                                            }
                                                        }
                                                    });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                        }
                    });
        }
    }

    private void uploadAppUsages() {
        if (appUsageManager.hasPermission(context)) {
            // AppUsagesObjects
            List<AppUsageObject> appUsageObjects = appUsageManager.getAppUsageDetails(context);
            for (AppUsageObject appUsageObject : appUsageObjects) {
                // HERE WITH PACKAGE
                new GetAppUsageTask(context, appUsageObject.getName()).execute(appUsageObject.getPackage_name());
                // Upload AppUsageObject Details
                FirebaseDatabase.getInstance().getReference()
                        .child("app_usages")
                        .child(Preferences.getInstance(context).getParent().getId())
                        .child(Utils.getDateID())
                        .child(appUsageObject.getName())
                        .setValue(appUsageObject)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Object -> " + appUsageObject.toString());

                                // Usages
                                List<UsageObject> usageObjects = appUsageManager.getPackageDetails(context, appUsageObject.getPackage_name());
                                int usage_index = 1;
                                for (UsageObject usageObject : usageObjects) {
                                    // Set usage_object in appUsageObject
                                    appUsageObject.setUsage_object(usageObject);

                                    // index
                                    String index = "usage_" + usage_index;

                                    // Upload Usages
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("app_usages")
                                            .child(Preferences.getInstance(context).getParent().getId())
                                            .child(Utils.getDateID())
                                            .child(appUsageObject.getName())
                                            .child("usages")
                                            .child(index)
                                            .setValue(appUsageObject.getUsage_object())
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Log.d(TAG, "Usage -> " + usageObject.toString());
                                                }
                                            });
                                    // usage_index++
                                    usage_index++;
                                }
                            }
                        });
            }
        } else {
            // No Permission
            FirebaseDatabase.getInstance().getReference().child("features").child(Preferences.getInstance(context).getParent().getId()).child("apps_and_usage").child("is_allowed").setValue("false");
        }
    }

    @Override
    public boolean cancel() {
        lists.clear();
        return super.cancel();
    }
}