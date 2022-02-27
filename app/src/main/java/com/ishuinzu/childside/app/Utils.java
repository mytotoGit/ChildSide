package com.ishuinzu.childside.app;

import static android.content.Context.ACTIVITY_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.ishuinzu.childside.R;
import com.ishuinzu.childside.core.SortEnum;
import com.ishuinzu.childside.object.AppObject;
import com.ishuinzu.childside.object.CallObject;
import com.ishuinzu.childside.object.ChildAppObject;
import com.ishuinzu.childside.object.ChildLocation;
import com.ishuinzu.childside.object.SMSDateObject;
import com.ishuinzu.childside.object.SMSObject;
import com.ishuinzu.childside.provider.calllog.Call;
import com.ishuinzu.childside.provider.telephony.SMS;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Utils {
    private static final long A_DAY = 86400 * 1000;

    public static Boolean shouldMatchLocation(ChildLocation childLocation) {
        if (childLocation.getTracking()) {
            Log.d("ChildLocationMonitoring", "Tracking -> TRUE");

            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int currentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);

            if (currentYear == childLocation.getDate().getYear()) {
                Log.d("ChildLocationMonitoring", "Year -> TRUE");

                if (currentMonth == childLocation.getDate().getMonth()) {
                    Log.d("ChildLocationMonitoring", "Month -> TRUE");

                    if (currentDate == childLocation.getDate().getDay()) {
                        Log.d("ChildLocationMonitoring", "Date -> TRUE");

                        if (currentHour == childLocation.getStart_time().getHour()) {
                            Log.d("ChildLocationMonitoring", "Start Hour -> TRUE");

                            if (currentMinute >= childLocation.getStart_time().getMinute()) {
                                Log.d("ChildLocationMonitoring", "Start Minute -> TRUE");

                                if (currentHour <= childLocation.getEnd_time().getHour()) {
                                    Log.d("ChildLocationMonitoring", "End Hour -> TRUE");

                                    if (currentMinute <= childLocation.getEnd_time().getMinute()) {
                                        Log.d("ChildLocationMonitoring", "End Minute -> TRUE");

                                        return true;
                                    } else {
                                        return false;
                                    }
                                } else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        Log.d("ChildLocationMonitoring", "Tracking -> FALSE");
        return false;
    }

    public static String getStringValue(int intValue) {
        return String.valueOf(intValue);
    }

    public static String getHourStringValue(String am_pm, String hour) {
        String newHour = "";
        if (am_pm.equals("0")) {
            switch (hour) {
                case "0":
                case "12":
                    newHour = "12";
                    break;
                case "1":
                    newHour = "01";
                    break;
                case "2":
                    newHour = "02";
                    break;
                case "3":
                    newHour = "03";
                    break;
                case "4":
                    newHour = "04";
                    break;
                case "5":
                    newHour = "05";
                    break;
                case "6":
                    newHour = "06";
                    break;
                case "7":
                    newHour = "07";
                    break;
                case "8":
                    newHour = "08";
                    break;
                case "9":
                    newHour = "09";
                    break;
                case "10":
                    newHour = "10";
                    break;
                case "11":
                    newHour = "11";
                    break;
            }
        } else {
            newHour = hour;
        }
        return newHour;
    }

    public static Boolean isServiceRunning(Context context, String servicePath) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (servicePath.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getMonthName(int month) {
        switch (month) {
            case 1:
                return "January";

            case 2:
                return "February";

            case 3:
                return "March";

            case 4:
                return "April";

            case 5:
                return "May";

            case 6:
                return "June";

            case 7:
                return "July";

            case 8:
                return "August";

            case 9:
                return "September";

            case 10:
                return "October";

            case 11:
                return "November";

            case 12:
                return "December";
        }
        return "January";
    }

    public static String getDayName(int day) {
        switch (day) {
            case 1:
                return "Sunday";

            case 2:
                return "Monday";

            case 3:
                return "Tuesday";

            case 4:
                return "Wednesday";

            case 5:
                return "Thursday";

            case 6:
                return "Friday";

            case 7:
                return "Saturday";
        }
        return "Sunday";
    }

    public static void setWindowsFlags(Activity activity, final int[] flags, boolean isOn) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        if (isOn) {
            for (int flag : flags) {
                layoutParams.flags |= flag;
            }
        } else {
            for (int flag : flags) {
                layoutParams.flags &= ~flag;
            }
        }
        window.setAttributes(layoutParams);
    }

    public static List<ChildAppObject> getInstalledAppList(Context context) {
        List<ChildAppObject> childAppObjects = new ArrayList<>();

        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applicationInfoPackages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : applicationInfoPackages) {
            String name = applicationInfo.loadLabel(packageManager).toString();
            String package_name = applicationInfo.packageName;
            Drawable icon = applicationInfo.loadIcon(packageManager);

            if (context.getPackageManager().getLaunchIntentForPackage(package_name) != null) {
                ChildAppObject app = new ChildAppObject(icon, "true", "false", name, package_name);
                if (!childAppObjects.contains(app)) {
                    childAppObjects.add(app);
                }
            }
        }
        return childAppObjects;
    }

    public static boolean isSystemPackage(ApplicationInfo applicationInfo) {
        return (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    public static boolean isSystemPackage(ResolveInfo resolveInfo) {
        return (resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    public static boolean isSystemPackage(PackageInfo packageInfo) {
        return ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public static String parsePackageName(PackageManager pckManager, String data) {
        ApplicationInfo applicationInformation;
        try {
            applicationInformation = pckManager.getApplicationInfo(data, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInformation = null;
        }
        return (String) (applicationInformation != null ? pckManager.getApplicationLabel(applicationInformation) : data);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable getPackageIcon(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            return manager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return context.getResources().getDrawable(R.drawable.img_logo);
    }

    public static String formatMilliSeconds(long milliSeconds) {
        long second = milliSeconds / 1000L;
        if (second < 60) {
            return String.format("%ss", second);
        } else if (second < 60 * 60) {
            return String.format("%sm %ss", second / 60, second % 60);
        } else {
            return String.format("%sh %sm %ss", second / 3600, second % (3600) / 60, second % (3600) % 60);
        }
    }

    public static boolean isSystemApp(PackageManager manager, String packageName) {

        boolean isSystemApp = false;
        try {
            ApplicationInfo applicationInfo = manager.getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                isSystemApp = (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0
                        || (applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return isSystemApp;
    }

    public static boolean isInstalled(PackageManager packageManager, String packageName) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return applicationInfo != null;
    }

    public static boolean openable(PackageManager packageManager, String packageName) {
        return packageManager.getLaunchIntentForPackage(packageName) != null;
    }

    public static int getAppUid(PackageManager packageManager, String packageName) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            return applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long[] getTimeRange(SortEnum sort) {
        long[] range;
        switch (sort) {
            case YESTERDAY:
                range = getYesterday();
                break;
            case THIS_WEEK:
                range = getThisWeek();
                break;
            case THIS_MONTH:
                range = getThisMonth();
                break;
            case THIS_YEAR:
                range = getThisYear();
                break;
            default:
                range = getTodayRange();
        }
        return range;
    }


    private static long[] getTodayRange() {
        long timeNow = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new long[]{cal.getTimeInMillis(), timeNow};
    }

    public static long getYesterdayTimestamp() {
        long timeNow = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeNow - A_DAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private static long[] getYesterday() {
        long timeNow = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeNow - A_DAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long start = cal.getTimeInMillis();
        long end = start + A_DAY > timeNow ? timeNow : start + A_DAY;
        return new long[]{start, end};
    }

    private static long[] getThisWeek() {
        long timeNow = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long start = cal.getTimeInMillis();
        long end = start + A_DAY > timeNow ? timeNow : start + A_DAY;
        return new long[]{start, end};
    }

    private static long[] getThisMonth() {
        long timeNow = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new long[]{cal.getTimeInMillis(), timeNow};
    }

    private static long[] getThisYear() {
        long timeNow = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new long[]{cal.getTimeInMillis(), timeNow};
    }

    public static String humanReadableByteCount(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format(Locale.getDefault(), "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static SMSObject convertToSMSObject(SMS sms) {
        SMSObject smsObject = new SMSObject();
        smsObject.setDate(new SMSDateObject());

        smsObject.setAddress("" + sms.address);
        smsObject.setBody("" + sms.body);
        smsObject.getDate().setReceived("" + sms.receivedDate);
        smsObject.getDate().setSent("" + sms.sentDate);
        smsObject.setRead("" + sms.read);
        smsObject.setSeen("" + sms.seen);
        smsObject.setType("" + sms.type);

        return smsObject;
    }

    public static CallObject convertToCallObject(Call call) {
        CallObject callObject = new CallObject();
        callObject.setCall_date("" + call.call_date);
        callObject.setDuration("" + call.duration);
        callObject.setGeo_code_location("" + call.geo_code_location);
        callObject.setId("" + call.id);
        callObject.setIs_read("" + call.is_read);
        callObject.setName("" + call.name);
        callObject.setNumber("" + call.number);
        callObject.setType("" + call.type);

        return callObject;
    }

    public static Uri getImageUri(Context context, Bitmap bitmap, String name) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "" + name, null);
        return Uri.parse(path);
    }

    public static AppObject getAppObject(ChildAppObject childAppObject, String link) {
        return new AppObject(link, childAppObject.getIs_enabled(), childAppObject.getIs_selected_lock(), childAppObject.getName(), childAppObject.getPackage_name());
    }

    public static String getDateID() {
        return getValueInDoubleFigure(Calendar.getInstance().get(Calendar.MONTH) + 1) + getValueInDoubleFigure(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + Calendar.getInstance().get(Calendar.YEAR);
    }

    private static String getValueInDoubleFigure(int value) {
        if (value <= 9) {
            return "0" + value;
        } else {
            return "" + value;
        }
    }
}