package com.ishuinzu.childside.object;

public class TrackingStatusObject {
    private String is_matched;
    private String result;
    private String tracking;

    public TrackingStatusObject() {
    }

    public TrackingStatusObject(String is_matched, String result, String tracking) {
        this.is_matched = is_matched;
        this.result = result;
        this.tracking = tracking;
    }

    public String getIs_matched() {
        return is_matched;
    }

    public void setIs_matched(String is_matched) {
        this.is_matched = is_matched;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }
}