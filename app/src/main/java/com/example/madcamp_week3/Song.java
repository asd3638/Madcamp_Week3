package com.example.madcamp_week3;

public class Song {
    private String title;
    private String singer;
    private String url;

    public Song() {
    }

    public String getSinger() {
        return singer;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getString() {
        String returnString = this.singer + " - " + this.title;
        return returnString;
    }

}
