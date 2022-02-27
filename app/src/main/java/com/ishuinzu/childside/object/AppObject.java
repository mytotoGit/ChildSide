package com.ishuinzu.childside.object;

public class AppObject {
    private String icon_link;
    private String is_enabled;
    private String is_selected_lock;
    private String name;
    private String package_name;

    public AppObject() {
    }

    public AppObject(String icon_link, String is_enabled, String is_selected_lock, String name, String package_name) {
        this.icon_link = icon_link;
        this.is_enabled = is_enabled;
        this.is_selected_lock = is_selected_lock;
        this.name = name;
        this.package_name = package_name;
    }

    public String getIcon_link() {
        return icon_link;
    }

    public void setIcon_link(String icon_link) {
        this.icon_link = icon_link;
    }

    public String getIs_enabled() {
        return is_enabled;
    }

    public void setIs_enabled(String is_enabled) {
        this.is_enabled = is_enabled;
    }

    public String getIs_selected_lock() {
        return is_selected_lock;
    }

    public void setIs_selected_lock(String is_selected_lock) {
        this.is_selected_lock = is_selected_lock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }
}