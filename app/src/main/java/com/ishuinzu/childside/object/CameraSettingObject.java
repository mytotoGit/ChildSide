package com.ishuinzu.childside.object;

public class CameraSettingObject {
    private String format;
    private String resolution;
    private String rotation;
    private String side;

    public CameraSettingObject() {}

    public CameraSettingObject(String format, String resolution, String rotation, String side) {
        this.format = format;
        this.resolution = resolution;
        this.rotation = rotation;
        this.side = side;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
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
}