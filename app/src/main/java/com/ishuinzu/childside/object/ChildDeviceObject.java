package com.ishuinzu.childside.object;

public class ChildDeviceObject {
    private BatteryObject battery;
    private String brand;
    private String display;
    private String hardware;
    private LatitudeLongitude location;
    private String manufacturer;
    private String model;
    private String parent;
    private String sdk_number;

    public ChildDeviceObject() {}

    public ChildDeviceObject(BatteryObject battery, String brand, String display, String hardware, LatitudeLongitude location, String manufacturer, String model, String parent, String sdk_number) {
        this.battery = battery;
        this.brand = brand;
        this.display = display;
        this.hardware = hardware;
        this.location = location;
        this.manufacturer = manufacturer;
        this.model = model;
        this.parent = parent;
        this.sdk_number = sdk_number;
    }

    public BatteryObject getBattery() {
        return battery;
    }

    public void setBattery(BatteryObject battery) {
        this.battery = battery;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public LatitudeLongitude getLocation() {
        return location;
    }

    public void setLocation(LatitudeLongitude location) {
        this.location = location;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getSdk_number() {
        return sdk_number;
    }

    public void setSdk_number(String sdk_number) {
        this.sdk_number = sdk_number;
    }
}