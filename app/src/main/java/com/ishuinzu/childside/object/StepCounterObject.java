package com.ishuinzu.childside.object;

public class StepCounterObject {
    private String calories;
    private String count;
    private String distance;
    private String speed;
    private long startTime;
    private long stopTime;
    private String time;

    public StepCounterObject() {}

    public StepCounterObject(String calories, String count, String distance, String speed, long startTime, long stopTime, String time) {
        this.calories = calories;
        this.count = count;
        this.distance = distance;
        this.speed = speed;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.time = time;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}