package com.ishuinzu.childside.object;

import androidx.annotation.NonNull;

import java.util.Locale;

public class AppUsageObject {
    private Boolean can_open;
    private int count;
    private long event_time;
    private int event_type;
    private Boolean is_system;
    private long mobile;
    private String name;
    private String package_name;
    private UsageObject usage_object;
    private long usage_time;

    public AppUsageObject() {
    }

    public AppUsageObject(Boolean can_open, int count, long event_time, int event_type, Boolean is_system, long mobile, String name, String package_name, UsageObject usage_object, long usage_time) {
        this.can_open = can_open;
        this.count = count;
        this.event_time = event_time;
        this.event_type = event_type;
        this.is_system = is_system;
        this.mobile = mobile;
        this.name = name;
        this.package_name = package_name;
        this.usage_object = usage_object;
        this.usage_time = usage_time;
    }

    public AppUsageObject copy() {
        return new AppUsageObject(getCan_open(), getCount(), getEvent_time(), getEvent_type(), getIs_system(), getMobile(), getName(), getPackage_name(), getUsage_object(), getUsage_time());
    }

    @NonNull
    public String toString() {
        return String.format(Locale.getDefault(), "name:%s package_name:%s time:%d total:%d type:%d system:%b count:%d", getName(), getPackage_name(), getEvent_time(), getUsage_time(), getEvent_type(), getIs_system(), getCount());
    }

    public Boolean getCan_open() {
        return can_open;
    }

    public void setCan_open(Boolean can_open) {
        this.can_open = can_open;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getEvent_time() {
        return event_time;
    }

    public void setEvent_time(long event_time) {
        this.event_time = event_time;
    }

    public int getEvent_type() {
        return event_type;
    }

    public void setEvent_type(int event_type) {
        this.event_type = event_type;
    }

    public Boolean getIs_system() {
        return is_system;
    }

    public void setIs_system(Boolean is_system) {
        this.is_system = is_system;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public UsageObject getUsage_object() {
        return usage_object;
    }

    public void setUsage_object(UsageObject usage_object) {
        this.usage_object = usage_object;
    }

    public long getUsage_time() {
        return usage_time;
    }

    public void setUsage_time(long usage_time) {
        this.usage_time = usage_time;
    }
}