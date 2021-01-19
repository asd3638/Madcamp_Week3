package com.example.madcamp_week3;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlayListList {
    private static ArrayList<PlayList> playlists= new ArrayList<PlayList>();
    private static PlayListList playListList = null;

    private PlayListList() {}

    public static PlayListList getPlayListList(){
        if (playListList == null) {
            // Printer 인스턴스 생성
            playListList = new PlayListList();
        }
        return playListList;
    }

    public static ArrayList<PlayList> getPlaylistArrayForCertainUser (String userid) {
        ArrayList<PlayList> returnArray = new ArrayList<PlayList>();

        for ( int i = 0 ; i < playlists.size(); i++) {
            if (playlists.get(i).getUserid().equals(userid)) {
                returnArray.add(playlists.get(i));
            }
        }

        return returnArray;
    }

    public static void addPlaylist (PlayList playlist) {
        Log.d("TAG", "Playlist added");
        Log.d("TAG", String.valueOf(playlists.size()));
        playlists.add(playlist);
        Log.d("TAG", String.valueOf(playlists.size()));
        /*
        String userid = playlist.getUserid();
        String title = userid + "-" + String.valueOf(PlayListList.getPlaylistArrayForCertainUser(userid).size() - 1);
        File file = new File(Environment.getExternalStorageDirectory() + "/Music/" + title + ".mp3");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

         */
    }

    public static void setPlaylist (ArrayList<PlayList> playlists) {
        PlayListList.playlists = playlists;
    }

    public static PlayList getPlaylist (int position, String userid) {
        ArrayList<PlayList> playlistsForCertainUser = PlayListList.getPlaylistArrayForCertainUser(userid);
        return playlistsForCertainUser.get(position);
    }

    public static void deletePlaylist (int position, String userid) {
        ArrayList<PlayList> playlistsForCertainUser = PlayListList.getPlaylistArrayForCertainUser(userid);
        PlayList obj = playlistsForCertainUser.get(position);

        int index = -1;

        for (int i = 0; i < playlists.size(); i++) {
            PlayList playList = playlists.get(i);
            if (playList.getUserid().equals(obj.getUserid()) && playList.getTitle().equals(obj.getTitle()) && playList.getDescription().equals(obj.getDescription()))
                index = i;

        }

        Log.d("TAG", String.valueOf(index));

        if (playlists.size() != 0) {
            playlists.remove(index);
            String title = userid + "-" + String.valueOf(position);
            String newTitle;
            File file = new File(Environment.getExternalStorageDirectory() + "/Music/" + title + ".mp3");
            File newFile;
            file.delete();

            for (int i = position; i < playlistsForCertainUser.size(); i++) {
                newTitle = userid + "-" + String.valueOf(i + 1);
                newFile = new File(Environment.getExternalStorageDirectory() + "/Music/" + newTitle + ".mp3");
                newFile.renameTo(new File(Environment.getExternalStorageDirectory() + "/Music/" + title + ".mp3"));
                title = newTitle;
            }
        }
    }
    public static ArrayList<PlayList> getPlaylistarray (String userid) {
        return PlayListList.getPlaylistArrayForCertainUser(userid);
    }

    public static ArrayList<PlayList> getPlaylistarrayWithoutUserid() {
        return playlists;
    }

    // 범석 추가
    public static ArrayList<String> getTitleList(String userid) {
        ArrayList<String> titleList = new ArrayList<String>();
        String title;

        ArrayList<PlayList> playlistsForCertainUser = PlayListList.getPlaylistArrayForCertainUser(userid);

        for (int i = 0; i< playlistsForCertainUser.size(); i++) {
            title = playlistsForCertainUser.get(i).getTitle();
            titleList.add(title);
        }

        return titleList;
    }


    public static void editPlaylist(int position, String userid, String title, String description) {
        ArrayList<PlayList> playlistsForCertainUser = PlayListList.getPlaylistArrayForCertainUser(userid);
        PlayList obj = playlistsForCertainUser.get(position);
        int index = -1;

        for (int i = 0; i < playlists.size(); i++) {
            PlayList playList = playlists.get(i);
            if (playList.getUserid().equals(obj.getUserid()) && playList.getTitle().equals(obj.getTitle()) && playList.getDescription().equals(obj.getDescription()))
                index = i;
        }

        PlayList newPlayList = obj;
        newPlayList.setTitle(title);
        newPlayList.setDescription(description);
        playlists.set(index, newPlayList);
    }


    // 지혜 추가
    public static ArrayList<String> getDescriptionList(String userid) {
        ArrayList<String> descriptionList = new ArrayList<String>();
        String description;

        ArrayList<PlayList> playlistForCertainUser = PlayListList.getPlaylistArrayForCertainUser(userid);

        for (int i = 0; i < playlists.size(); i++) {
            description = playlists.get(i).getDescription();
            descriptionList.add(description);
        }
        return descriptionList;
    }

    public static int getSize(String userid) {
        return PlayListList.getPlaylistArrayForCertainUser(userid).size();
    }

    public static void clear() {
        playlists.clear();
    }
}

