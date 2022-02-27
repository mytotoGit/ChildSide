package com.ishuinzu.childside.object;

public class NotificationObject {
    private String body;
    private Boolean status;
    private long time;
    private String title;

    public NotificationObject() {}

    public NotificationObject(String body, Boolean status, long time, String title) {
        this.body = body;
        this.status = status;
        this.time = time;
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}