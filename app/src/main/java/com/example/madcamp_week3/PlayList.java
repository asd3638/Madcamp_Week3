package com.example.madcamp_week3;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PlayList {
    //user id 추가

    private String userid;
    //private String filepath;
    private String title;
    private String description;
    private ArrayList<Song> songs= new ArrayList<Song>();

    public PlayList(String userid, String title, String description) {
        this.userid = userid;
        this.title = title;
        this.description = description;
    }

    public void addSong (Song song) {
        songs.add(song);
    }
    public Song getSong (int position) {
        return songs.get(position);
    }
    public void deleteSong (int position) {
        if (songs.size() != 0) {
            songs.remove(position);
        } else {

        }
    }

    public String getUserid() {
        return userid;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public ArrayList<String> getUrlList() {
        ArrayList<String> urlList = new ArrayList<String>();
        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            String url = song.getUrl();
            urlList.add(url);
        }
        return urlList;
    }

    public int size() {
        return songs.size();
    }
}

