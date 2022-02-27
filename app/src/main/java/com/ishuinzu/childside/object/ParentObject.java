package com.ishuinzu.childside.object;

public class ParentObject {
    private String email;
    private String fcm_token;
    private String have_child;
    private String id;
    private String img_url;
    private String name;

    public ParentObject() {
    }

    public ParentObject(String email, String fcm_token, String have_child, String id, String img_url, String name) {
        this.email = email;
        this.fcm_token = fcm_token;
        this.have_child = have_child;
        this.id = id;
        this.img_url = img_url;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getHave_child() {
        return have_child;
    }

    public void setHave_child(String have_child) {
        this.have_child = have_child;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
