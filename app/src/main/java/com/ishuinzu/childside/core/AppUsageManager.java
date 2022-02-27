package com.ishuinzu.childside.core;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import com.ishuinzu.childside.app.Constant;
import com.ishuinzu.childside.app.Utils;
import com.ishuinzu.childside.object.AppUsageObject;
import com.ishuinzu.childside.object.UsageObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUsageManager {
    private Context context;

    public AppUsageManager(Context context) {
        this.context = context;
    }

    public void requestPermission(Context context) {
        Intent intent = new Intent(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public boolean hasPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (appOps != null) {
            int mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), context.getPackageName());
            return mode == AppOpsManager.MODE_ALLOWED;
        }
        return false;
    }

    public List<UsageObject> getPackageDetails(Context context, String package_name) {
        List<UsageObject> usageObjects = new ArrayList<>();

        UsageStatsManager manager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        if (manager != null) {
            long[] range = Utils.getTimeRange(SortEnum.TODAY);
            UsageEvents events = manager.queryEvents(range[0], range[1]);
            UsageEvents.Event event = new UsageEvents.Event();

            UsageObject usageObject = new UsageObject();
            usageObject.setPackage_name(package_name);
            usageObject.setName(Utils.parsePackageName(context.getPackageManager(), package_name));

            ClonedEvent prevEndEvent = null;
            long start = 0;

            while (events.hasNextEvent()) {
                events.getNextEvent(event);
                String currentPackage = event.getPackageName();
                int eventType = event.getEventType();
                long eventTime = event.getTimeStamp();

                if (currentPackage.equals(package_name)) {
                    if (eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        if (start == 0) {
                            start = eventTime;
                            usageObject.setEvent_time(eventTime);
                            usageObject.setEvent_type(eventType);
                            usageObject.setUsage_time(0);
                            usageObjects.add(usageObject.copy());
                        }
                    } else if (eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                        if (start > 0) {
                            prevEndEvent = new ClonedEvent(event);
                        }
                    }
                } else {
                    if (prevEndEvent != null) {
                        usageObject.setEvent_time(prevEndEvent.timeStamp);
                        usageObject.setEvent_type(prevEndEvent.eventType);
                        usageObject.setUsage_time(prevEndEvent.timeStamp - start);
                        if (usageObject.getUsage_time() <= 0) {
                            usageObject.setUsage_time(0);
                        }
                        if (usageObject.getUsage_time() > Constant.USAGE_TIME_MIX) {
                            usageObject.setCount(usageObject.getCount() + 1);
                        }
                        usageObjects.add(usageObject.copy());
                        start = 0;
                        prevEndEvent = null;
                    }
                }
            }
        }
        return usageObjects;
    }

    public List<AppUsageObject> getAppUsageDetails(Context context) {
        List<AppUsageObject> appUsageObjects = new ArrayList<>();
        List<AppUsageObject> appUsageObjectsNew = new ArrayList<>();
        UsageStatsManager manager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        if (manager != null) {
            String prevPackage = "";
            Map<String, Long> startPoints = new HashMap<>();
            Map<String, ClonedEvent> endPoints = new HashMap<>();

            long[] range = Utils.getTimeRange(SortEnum.TODAY);
            UsageEvents events = manager.queryEvents(range[0], range[1]);
            UsageEvents.Event event = new UsageEvents.Event();

            while (events.hasNextEvent()) {
                events.getNextEvent(event);
                int event_type = event.getEventType();
                long event_time = event.getTimeStamp();
                String package_name = event.getPackageName();

                if (event_type == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    AppUsageObject appUsageObject = containItem(appUsageObjects, package_name);
                    if (appUsageObject == null) {
                        appUsageObject = new AppUsageObject();
                        appUsageObject.setPackage_name(package_name);
                        appUsageObjects.add(appUsageObject);
                    }
                    if (!startPoints.containsKey(package_name)) {
                        startPoints.put(package_name, event_time);
                    }
                }

                if (event_type == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    if (startPoints.size() > 0 && startPoints.containsKey(package_name)) {
                        endPoints.put(package_name, new ClonedEvent(event));
                    }
                }

                if (TextUtils.isEmpty(prevPackage)) {
                    prevPackage = package_name;
                }
                if (!prevPackage.equals(package_name)) {
                    if (startPoints.containsKey(prevPackage) && endPoints.containsKey(prevPackage)) {
                        ClonedEvent lastEndEvent = endPoints.get(prevPackage);
                        AppUsageObject listAppUsageObject = containItem(appUsageObjects, prevPackage);
                        if (listAppUsageObject != null) {
                            assert lastEndEvent != null;
                            listAppUsageObject.setEvent_time(lastEndEvent.timeStamp);
                            long duration = lastEndEvent.timeStamp - startPoints.get(prevPackage);
                            if (duration <= 0) {
                                duration = 0;
                            }
                            listAppUsageObject.setUsage_time(listAppUsageObject.getUsage_time() + duration);
                            if (duration > Constant.USAGE_TIME_MIX) {
                                listAppUsageObject.setCount(listAppUsageObject.getCount() + 1);
                            }
                        }
                        startPoints.remove(prevPackage);
                        endPoints.remove(prevPackage);
                    }
                    prevPackage = package_name;
                }
            }
        }

        if (appUsageObjects.size() > 0) {
            Map<String, Long> mobileData;
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
            mobileData = getMobileData(context, networkStatsManager);

            PackageManager packageManager = context.getPackageManager();
            for (AppUsageObject appUsageObject : appUsageObjects) {
                if (!Utils.openable(packageManager, appUsageObject.getPackage_name())) {
                    continue;
                }
                if (Utils.isSystemApp(packageManager, appUsageObject.getPackage_name())) {
                    continue;
                }
                if (!Utils.isInstalled(packageManager, appUsageObject.getPackage_name())) {
                    continue;
                }

                String key = "u" + Utils.getAppUid(packageManager, appUsageObject.getPackage_name());
                if (mobileData.size() > 0 && mobileData.containsKey(key)) {
                    appUsageObject.setMobile(mobileData.get(key));
                }
                appUsageObject.setName(Utils.parsePackageName(packageManager, appUsageObject.getPackage_name()));
                appUsageObjectsNew.add(appUsageObject);
            }

            // Sort - Usage Frequency
            Collections.sort(appUsageObjectsNew, (left, right) -> right.getCount() - left.getCount());
        }
        return appUsageObjectsNew;
    }

    private Map<String, Long> getMobileData(Context context, NetworkStatsManager networkStatsManager) {
        Map<String, Long> result = new HashMap<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            long[] range = Utils.getTimeRange(SortEnum.TODAY);
            NetworkStats networkStats;
            try {
                networkStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE, null, range[0], range[1]);
                if (networkStats != null) {
                    while (networkStats.hasNextBucket()) {
                        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                        networkStats.getNextBucket(bucket);
                        String key = "u" + bucket.getUid();
                        if (result.containsKey(key)) {
                            result.put(key, result.get(key) + bucket.getTxBytes() + bucket.getRxBytes());
                        } else {
                            result.put(key, bucket.getTxBytes() + bucket.getRxBytes());
                        }
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private AppUsageObject containItem(List<AppUsageObject> appUsageObjects, String package_name) {
        for (AppUsageObject appUsageObject : appUsageObjects) {
            if (appUsageObject.getPackage_name().equals(package_name)) {
                return appUsageObject;
            }
        }
        return null;
    }

    static class ClonedEvent {
        String packageName;
        String eventClass;
        long timeStamp;
        int eventType;

        ClonedEvent(UsageEvents.Event event) {
            packageName = event.getPackageName();
            eventClass = event.getClassName();
            timeStamp = event.getTimeStamp();
            eventType = event.getEventType();
        }
    }
}