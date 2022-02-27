package com.ishuinzu.childside.object;

public class SMSObject {
    private String address;
    private String body;
    private SMSDateObject date;
    private String read;
    private String seen;
    private String type;

    public SMSObject() {
    }

    public SMSObject(String address, String body, SMSDateObject date, String read, String seen, String type) {
        this.address = address;
        this.body = body;
        this.date = date;
        this.read = read;
        this.seen = seen;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public SMSDateObject getDate() {
        return date;
    }

    public void setDate(SMSDateObject date) {
        this.date = date;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}