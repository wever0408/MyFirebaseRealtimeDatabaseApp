package com.example.wever.myfirebaserealtimedatabaseapp;

public class ClassifiedAd {
    private String adId;
    private String category;
    private String city;
    private String description;
    private String name;
    private String phone;
    private String title;

    public ClassifiedAd(String adId, String category, String city, String description, String name, String phone, String title) {
        this.adId = adId;
        this.category = category;
        this.city = city;
        this.description = description;
        this.name = name;
        this.phone = phone;
        this.title = title;
    }
    public ClassifiedAd(){

    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdId() {
        return adId;
    }

    public String getCategory() {
        return category;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getTitle() {
        return title;
    }
}