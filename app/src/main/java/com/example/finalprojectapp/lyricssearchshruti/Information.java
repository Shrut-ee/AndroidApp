package com.example.finalprojectapp.lyricssearchshruti;

public class Information {

    long id;
    String title;
    String artist;
    String information;

    public Information(long id, String title, String artist, String information) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.information = information;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
