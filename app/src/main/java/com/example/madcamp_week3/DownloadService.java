package com.example.madcamp_week3;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import java.util.ArrayList;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;

public class DownloadService extends Service {

    private IBinder binder = new MyBinder();
    public static boolean SERVICE_CONNECTED = false;
    ArrayList<String> downloadLink;
    int downloadLinkIndex;
    int position;
    String newLink;
    String title;
    String userid;
    private long downloadId;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long broadcastedDownloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);

            if (broadcastedDownloadID == downloadId) {
                if (getDownloadStatus() == DownloadManager.STATUS_SUCCESSFUL) {
                    Toast.makeText(DownloadService.this, "Download complete!", Toast.LENGTH_SHORT).show();
                    Intent newIntent = new Intent(DownloadService.this, ExtractService.class);
                    newIntent.putExtra("title", title);
                    newIntent.putExtra("position", position);
                    startService(newIntent);

                    if (downloadLinkIndex < downloadLink.size() - 1) {
                        newIntent = new Intent(DownloadService.this, DownloadService.class);
                        newIntent.putExtra("download_link", downloadLink);
                        newIntent.putExtra("download_link_index", downloadLinkIndex + 1);
                        newIntent.putExtra("position", position);
                        startService(newIntent);
                    }

                    //stopSelf();
                } else {
                    Toast.makeText(DownloadService.this, "Download failed with an error.", Toast.LENGTH_SHORT).show();
                    int size = PlayListList.getPlaylist(position, userid).size();
                    PlayListList.getPlaylist(position, userid).deleteSong(size - 1);
                    Intent selfIntent = new Intent(DownloadService.this, DownloadService.class);
                    selfIntent.putExtra("download_link", downloadLink);
                    selfIntent.putExtra("download_link_index", downloadLinkIndex);
                    selfIntent.putExtra("position", position);
                    startService(selfIntent);
                }
            }
        }
    };

    public DownloadService() {
    }

    public class MyBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG", "Download service on start command");
        downloadLink = intent.getStringArrayListExtra("download_link");
        downloadLinkIndex = intent.getIntExtra("download_link_index", 0);
        position = intent.getIntExtra("position", 0);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userid = sharedPreferences.getString("userid", null);


        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, intentFilter);

        downloadMyVideo();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG", "Download service called and created");

    }

    private int getDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Cursor cursor = downloadManager.query(query);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);

            return status;
        }

        return DownloadManager.ERROR_UNKNOWN;
    }

    public void downloadMyVideo() {
        YouTubeUriExtractor youTubeUriExtractor=new YouTubeUriExtractor(DownloadService.this) {
            @Override
            public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {

                if(ytFiles!=null){

                    int tag=18; // tag for a 360p .mp4 file

                    if (ytFiles.get(tag) == null) {
                        Toast.makeText(DownloadService.this, "Error occurred while trying to download the video!", Toast.LENGTH_SHORT);
                    } else {
                        Log.d("ytFile", ytFiles.get(tag).toString());
                        newLink = ytFiles.get(tag).getUrl();
                        title = urlTrimmer(downloadLink.get(downloadLinkIndex));
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(newLink));
                        request.setTitle(title);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title + ".mp4");
                        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

                        request.allowScanningByMediaScanner();
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                        downloadId = downloadManager.enqueue(request);
                    }
                }
            }

        };

        youTubeUriExtractor.execute(downloadLink.get(downloadLinkIndex));
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public String urlTrimmer(String link) {
        String trimmedUrl = link
                .replace("https://m.youtube.com/watch?v=","")
                .replace("/", "")
                .replace(":", "")
                .replace("https", "")
                .replace("http","")
                .replace("youtube", "")
                .replace("youtu.be","");

        return trimmedUrl;
    }
}