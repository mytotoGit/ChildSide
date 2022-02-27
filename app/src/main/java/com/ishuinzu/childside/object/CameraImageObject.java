package com.ishuinzu.childside.object;

public class CameraImageObject {
    private String format;
    private String link;
    private String resolution;
    private String rotation;
    private String side;
    private long time;

    public CameraImageObject() {}

    public CameraImageObject(String format, String link, String resolution, String rotation, String side, long time) {
        this.format = format;
        this.link = link;
        this.resolution = resolution;
        this.rotation = rotation;
        this.side = side;
        this.time = time;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}