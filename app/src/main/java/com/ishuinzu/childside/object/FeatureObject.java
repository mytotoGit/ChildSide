package com.ishuinzu.childside.object;

public class FeatureObject {
    private String is_allowed;
    private String is_set;
    private DateStamp last_updated;

    public FeatureObject() {
    }

    public FeatureObject(String is_allowed, String is_set, DateStamp last_updated) {
        this.is_allowed = is_allowed;
        this.is_set = is_set;
        this.last_updated = last_updated;
    }

    public void setIs_allowed(String is_allowed) {
        this.is_allowed = is_allowed;
    }

    public void setIs_set(String is_set) {
        this.is_set = is_set;
    }

    public void setLast_updated(DateStamp last_updated) {
        this.last_updated = last_updated;
    }

    public String getIs_allowed() {
        return is_allowed;
    }

    public String getIs_set() {
        return is_set;
    }

    public DateStamp getLast_updated() {
        return last_updated;
    }
}