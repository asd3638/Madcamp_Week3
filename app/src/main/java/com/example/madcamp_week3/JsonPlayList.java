package com.example.madcamp_week3;

import com.google.gson.Gson;

import java.util.ArrayList;

public class JsonPlayList {
    private String userid;
    private String title;
    private String description;
    private String songs;
    private String letter;

    public JsonPlayList(String userid, PlayList playlist, String letter) {
        this.userid = userid;
        this.title = playlist.getTitle();
        this.description = playlist.getDescription();
        Gson gson = new Gson();
        ArrayList<Song> songsBeforeConverted = playlist.getSongs();
        this.songs = gson.toJson(songsBeforeConverted);
        this.letter = letter;
    }

    public String getUserid() {
        return userid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLetter() {
        return letter;
    }

    public String getSongs() {
        return songs;
    }
}
