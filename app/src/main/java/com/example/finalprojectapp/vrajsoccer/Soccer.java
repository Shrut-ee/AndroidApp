package com.example.finalprojectapp.vrajsoccer;

import java.util.ArrayList;

public class Soccer {
    private long id;
    private String title;
    private String date;
    private String side1;
    private String side2;
    private String videosJsonArray;

    public Soccer(String title) {
        this.title = title;
    }

    public Soccer(String title, String date, String side1, String side2, String videosJsonArray) {
        this.title = title;
        this.date = date;
        this.side1 = side1;
        this.side2 = side2;
        this.videosJsonArray = videosJsonArray;
    }

    public Soccer(long id,String title, String date, String side1, String side2, String videosJsonArray) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.side1 = side1;
        this.side2 = side2;
        this.videosJsonArray = videosJsonArray;
    }

    public String getDate() {
        return date;
    }

    public String getSide1() {
        return side1;
    }

    public String getSide2() {
        return side2;
    }

    public String getTitle() {
        return title;
    }

    public long getId() { return id; }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSide1(String side1) {
        this.side1 = side1;
    }

    public void setSide2(String side2) {
        this.side2 = side2;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(long id) { this.id = id; }

    public String getVideosJsonArray() {
        return videosJsonArray;
    }

    public void setVideosJsonArray(String videosJsonArray) { this.videosJsonArray = videosJsonArray; }
}
