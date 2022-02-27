package com.ishuinzu.childside.object;

public class TimeStamp {
    private int hour;
    private int minute;

    public TimeStamp() {
    }

    public TimeStamp(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}