package com.ishuinzu.childside.object;

public class CallObject {
    private String call_date;
    private String duration;
    private String geo_code_location;
    private String id;
    private String is_read;
    private String name;
    private String number;
    private String type;

    public CallObject() {
    }

    public CallObject(String call_date, String duration, String geo_code_location, String id, String is_read, String name, String number, String type) {
        this.call_date = call_date;
        this.duration = duration;
        this.geo_code_location = geo_code_location;
        this.id = id;
        this.is_read = is_read;
        this.name = name;
        this.number = number;
        this.type = type;
    }

    public String getCall_date() {
        return call_date;
    }

    public void setCall_date(String call_date) {
        this.call_date = call_date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getGeo_code_location() {
        return geo_code_location;
    }

    public void setGeo_code_location(String geo_code_location) {
        this.geo_code_location = geo_code_location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}