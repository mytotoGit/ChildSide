package com.ishuinzu.childside.object;

public class LatitudeLongitude {
    private Boolean is_allowed;
    private double latitude;
    private double longitude;

    public LatitudeLongitude() {
    }

    public LatitudeLongitude(Boolean is_allowed, double latitude, double longitude) {
        this.is_allowed = is_allowed;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Boolean getIs_allowed() {
        return is_allowed;
    }

    public void setIs_allowed(Boolean is_allowed) {
        this.is_allowed = is_allowed;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}