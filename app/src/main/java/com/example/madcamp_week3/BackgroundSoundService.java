package com.example.madcamp_week3;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;

public class BackgroundSoundService extends Service {
    public static MediaPlayer mediaPlayer = null;
    public static int position;
    public static String userid;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //position = intent.getIntExtra("position", 50);
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        /*mediaPlayer = MediaPlayer.create(this, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Music/" + Integer.toString(position) + ".mp3")));
        Toast.makeText(getApplicationContext(),  "Playing" + Integer.toString(position) + "in the Background", Toast.LENGTH_SHORT).show();*/

        /*if (file.exists()) {
            mediaPlayer = MediaPlayer.create(this, Uri.fromFile(file));
            mediaPlayer.setLooping(true); // Set looping
            mediaPlayer.setVolume(100, 100);
            mediaPlayer.start();
        }
        else if (file.exists()) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();
            } catch(IOException ie)
            {
                ie.printStackTrace();
            }
            mediaPlayer.seekTo(0);
        }*/
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*if (file.exists()) {
            //Toast.makeText(getApplicationContext(), "Playing Bohemian Rashpody in the Background", Toast.LENGTH_SHORT).show();
        }
        return startId;*/

        Log.d("TAG", "background sound service on start command");


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userid = sharedPreferences.getString("userid", null);
        position = intent.getIntExtra("position", 0);

        String title = PlayListList.getPlaylist(position,userid).getTitle();


        mediaPlayer = MediaPlayer.create(this,
                Uri.fromFile(new File(
                        Environment.getExternalStorageDirectory()
                                + "/Music/" + userid + "-" + Integer.toString(position) + ".mp3")));

        if (new File(Environment.getExternalStorageDirectory()
                + "/Music/" + userid + "-" + Integer.toString(position) + ".mp3").exists()) {
            mediaPlayer.start();

            Log.d("TAG", "Mediaplayer just started in backgroundsound service");

            Toast.makeText(getApplicationContext(),  "Playing " + title, Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
    }

    /*@Override
    public void onDestroy() {
        *//*if (file.exists()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }*//*
    }*/
    public void onStop(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public void onPause(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    public void onDestroy(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    @Override
    public void onLowMemory() {
    }
}
