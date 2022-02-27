package com.ishuinzu.childside.object;

public class FoodItemObject {
    private double calories;
    private String image_link;
    private String title;

    public FoodItemObject() {
    }

    public FoodItemObject(double calories, String image_link, String title) {
        this.calories = calories;
        this.image_link = image_link;
        this.title = title;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}