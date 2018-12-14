package com.example.wever.myfirebaserealtimedatabaseapp;

public class Movie {
    private String adId;
    private String title;
    private String genre;
    private String description;
    private String director;
    private String cast;
    private String openingDay;

    public Movie(String adId, String title, String genre, String description, String director, String cast, String openingDay) {
        this.adId = adId;
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.director = director;
        this.cast = cast;
        this.openingDay = openingDay;
    }

    public Movie() {
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getOpeningDay() {
        return openingDay;
    }

    public void setOpeningDay(String openingDay) {
        this.openingDay = openingDay;
    }
}

