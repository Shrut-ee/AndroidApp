package com.example.finalprojectapp;

public class Information {

    long id;
    String title;
    String information;

    public Information(Long id, String title, String information)
    {
        this.id = id;
        this.title = title;
        this.information = information;

    }

    public Information() {
        title = null;
        information = null;

    }

    public static void setText(String information) {
    }

    public Long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information){
        this.information = information;
    }
}
