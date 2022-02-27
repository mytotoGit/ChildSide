package com.ishuinzu.childside.object;

public class SMSDateObject {
    private String received;
    private String sent;

    public SMSDateObject() {
    }

    public SMSDateObject(String received, String sent) {
        this.received = received;
        this.sent = sent;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }
}