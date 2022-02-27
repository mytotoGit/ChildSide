package com.ishuinzu.childside.object;

public class ChildLocation {
    private LatitudeLongitude current_location;
    private DateStamp date;
    private TimeStamp end_time;
    private LatitudeLongitude last_location;
    private String slot_id;
    private LatitudeLongitude start_location;
    private TimeStamp start_time;
    private Boolean tracking;
    private LatitudeLongitude tracking_location;

    public ChildLocation() {
    }

    public ChildLocation(LatitudeLongitude current_location, DateStamp date, TimeStamp end_time, LatitudeLongitude last_location, String slot_id, LatitudeLongitude start_location, TimeStamp start_time, Boolean tracking, LatitudeLongitude tracking_location) {
        this.current_location = current_location;
        this.date = date;
        this.end_time = end_time;
        this.last_location = last_location;
        this.slot_id = slot_id;
        this.start_location = start_location;
        this.start_time = start_time;
        this.tracking = tracking;
        this.tracking_location = tracking_location;
    }

    public LatitudeLongitude getCurrent_location() {
        return current_location;
    }

    public void setCurrent_location(LatitudeLongitude current_location) {
        this.current_location = current_location;
    }

    public DateStamp getDate() {
        return date;
    }

    public void setDate(DateStamp date) {
        this.date = date;
    }

    public TimeStamp getEnd_time() {
        return end_time;
    }

    public void setEnd_time(TimeStamp end_time) {
        this.end_time = end_time;
    }

    public LatitudeLongitude getLast_location() {
        return last_location;
    }

    public void setLast_location(LatitudeLongitude last_location) {
        this.last_location = last_location;
    }

    public String getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(String slot_id) {
        this.slot_id = slot_id;
    }

    public LatitudeLongitude getStart_location() {
        return start_location;
    }

    public void setStart_location(LatitudeLongitude start_location) {
        this.start_location = start_location;
    }

    public TimeStamp getStart_time() {
        return start_time;
    }

    public void setStart_time(TimeStamp start_time) {
        this.start_time = start_time;
    }

    public Boolean getTracking() {
        return tracking;
    }

    public void setTracking(Boolean tracking) {
        this.tracking = tracking;
    }

    public LatitudeLongitude getTracking_location() {
        return tracking_location;
    }

    public void setTracking_location(LatitudeLongitude tracking_location) {
        this.tracking_location = tracking_location;
    }
}