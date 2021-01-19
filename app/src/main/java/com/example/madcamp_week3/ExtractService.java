package com.example.madcamp_week3;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class ExtractService extends Service {

    private IBinder binder = new ExtractService.MyBinder();
    public static boolean SERVICE_CONNECTED = false;
    String userid;
    int position;
    String title;




    public class MyBinder extends Binder {
        public ExtractService getService() {
            return ExtractService.this;
        }
    }

    public ExtractService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG", "Extract service on start command");
        title = intent.getStringExtra("title");

        Boolean firstSong = true;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userid = sharedPreferences.getString("userid", null);
        position = intent.getIntExtra("position", 0);
        String dstPath;

        try {
            if (new File(Environment.getExternalStorageDirectory() + "/Music/" + userid + "-" + position + ".mp3").exists()) {
                dstPath = Environment.getExternalStorageDirectory() + "/Music/" + userid + "-" + position + "-" + "temp" + ".mp3";
                Log.d("First song", "False");
                firstSong = false;
            } else {
                dstPath = Environment.getExternalStorageDirectory() + "/Music/" + userid + "-" + position + ".mp3";
                firstSong = true;
                Log.d("First song", "True");
            }

            new AudioExtractor().genVideoUsingMuxer(Environment.getExternalStorageDirectory() + "/Download/" + title + ".mp4" ,
                    dstPath,
                    -1,
                    -1,
                    true,
                    false);

            File file = new File(Environment.getExternalStorageDirectory() + "/Download/" + title + ".mp4");
            file.delete();

            if (!firstSong) {
                Intent newIntent = new Intent(ExtractService.this, AppendService.class);
                newIntent.putExtra("first", Environment.getExternalStorageDirectory() + "/Music/" + userid + "-" + position + ".mp3");
                newIntent.putExtra("second", dstPath);
                startService(newIntent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG", "Extract service called and created");

    }
}