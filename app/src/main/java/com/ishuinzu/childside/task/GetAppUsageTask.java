package com.ishuinzu.childside.task;

import android.annotation.SuppressLint;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.ishuinzu.childside.app.Preferences;
import com.ishuinzu.childside.app.Utils;
import com.ishuinzu.childside.core.SortEnum;

@SuppressLint("StaticFieldLeak")
public class GetAppUsageTask extends AsyncTask<String, Void, Long[]> {
    private Context context;
    private String name;

    public GetAppUsageTask(Context context, String name) {
        this.context = context;
        this.name = name;
    }

    @Override
    protected Long[] doInBackground(String... strings) {
        long totalWifi = 0;
        long totalMobile = 0;

        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        int targetUid = Utils.getAppUid(context.getPackageManager(), strings[0]);

        long[] range = Utils.getTimeRange(SortEnum.TODAY);
        try {
            if (networkStatsManager != null) {
                NetworkStats networkStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_WIFI, "", range[0], range[1]);
                if (networkStats != null) {
                    while (networkStats.hasNextBucket()) {
                        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                        networkStats.getNextBucket(bucket);
                        if (bucket.getUid() == targetUid) {
                            totalWifi += bucket.getTxBytes() + bucket.getRxBytes();
                        }
                    }
                }
                NetworkStats networkStatsM = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE, null, range[0], range[1]);
                if (networkStatsM != null) {
                    while (networkStatsM.hasNextBucket()) {
                        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                        networkStatsM.getNextBucket(bucket);
                        if (bucket.getUid() == targetUid) {
                            totalMobile += bucket.getTxBytes() + bucket.getRxBytes();
                        }
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.d("NETWORK", strings[0] + " " + Utils.humanReadableByteCount(totalWifi) + " " + Utils.humanReadableByteCount(totalMobile));
        return new Long[]{totalWifi, totalMobile};
    }

    @Override
    protected void onPostExecute(Long[] aLong) {
        Log.d("NETWORK", "RETRIEVED");
        // Update Usages
        FirebaseDatabase.getInstance().getReference().child("app_usages").child(Preferences.getInstance(context).getParent().getId()).child(Utils.getDateID()).child(name).child("mobile_data").setValue(Utils.humanReadableByteCount(aLong[1]));
        FirebaseDatabase.getInstance().getReference().child("app_usages").child(Preferences.getInstance(context).getParent().getId()).child(Utils.getDateID()).child(name).child("wifi_data").setValue(Utils.humanReadableByteCount(aLong[0]));
    }
}