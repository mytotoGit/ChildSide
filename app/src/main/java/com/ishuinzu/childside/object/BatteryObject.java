package com.ishuinzu.childside.object;

public class BatteryObject {
    private String is_charging;
    private String percentage;
    private String scale;
    private String technology;
    private String temperature;
    private String voltage;

    public BatteryObject() {
    }

    public BatteryObject(String is_charging, String percentage, String scale, String technology, String temperature, String voltage) {
        this.is_charging = is_charging;
        this.percentage = percentage;
        this.scale = scale;
        this.technology = technology;
        this.temperature = temperature;
        this.voltage = voltage;
    }

    public String getIs_charging() {
        return is_charging;
    }

    public void setIs_charging(String is_charging) {
        this.is_charging = is_charging;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }
}