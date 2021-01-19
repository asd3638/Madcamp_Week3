package com.example.madcamp_week3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class SearchActivity extends AppCompatActivity {

    String downloadLink = "https://youtu.be/XH9NyhkXcp4";

    String userid;

    ImageButton btn_search_download;
    ImageButton btn_search_cancel;

    Context context;
    private DownloadService downloadService;
    private ExtractService extractService;
    private boolean downloadBound = false;
    private boolean extractBound = false;


    private ServiceConnection downloadServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            DownloadService.MyBinder binder = (DownloadService.MyBinder) iBinder;
            downloadService = binder.getService();
            downloadBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            downloadBound = false;
        }
    };


    private ServiceConnection extractServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ExtractService.MyBinder binder = (ExtractService.MyBinder) iBinder;
            extractService = binder.getService();
            extractBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            extractBound = false;
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userid = sharedPreferences.getString("userid", null);


        int position = getIntent().getIntExtra("position", 0);

        PlayList playList = PlayListList.getPlaylist(position, userid);

        //PlayListList playListList = PlayListList.getPlayListList();
        //PlayList playList = new PlayList();
        Song song = new Song();
        btn_search_download = (ImageButton) findViewById(R.id.btn_search_download);
        btn_search_cancel = (ImageButton) findViewById(R.id.btn_search_cancel);

        Context context = SearchActivity.this;

        String url = "http://www.youtube.com";

        //webView 만들기
        WebView webView = (WebView) findViewById(R.id.home_webview);
        //webview client 연결
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        btn_search_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = webView.getUrl();

                Log.d("TAG", str);

                Toast url = Toast.makeText(context, str, LENGTH_SHORT);
                url.show();

                String str_title = webView.getTitle();

                showDialog(song, position, str);
            }
        });
        btn_search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //취소 누르면 activity 종료
                finish();
            }
        });
    }


    public void showDialog(Song song, int position, String url) {
        Log.d("TAG", "show dialog executing");
        System.out.println("show dialog executing");

        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.song_dialog, null);
        builder.setView(dialogView);

        EditText artistText = dialogView.findViewById(R.id.artist_edit_text);
        EditText titleText = dialogView.findViewById(R.id.title_edit_text);
        Button confirmButton = dialogView.findViewById(R.id.confirm_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        AlertDialog dialog = builder.create();

        dialog.show();

        System.out.println("here");

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                song.setSinger(artistText.getText().toString());
                song.setTitle(titleText.getText().toString());
                song.setUrl(url);
                PlayListList.getPlaylist(position, userid).addSong(song);

                PlayListPref playListPref = new PlayListPref();
                playListPref.setPlayListPref(getApplicationContext());

                PlayActivity.getAdapter().notifyDataSetChanged();

                dialog.dismiss();

                Intent service = new Intent(getApplicationContext(), DownloadService.class);
                ArrayList<String> urlList = new ArrayList<String>();
                urlList.add(url);
                service.putExtra("download_link", urlList);
                service.putExtra("download_link_index", 0);
                service.putExtra("position", position);
                startService(service);

                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(downloadBound) {
            unbindService(downloadServiceConnection);
            downloadBound = false;
        }
        stopService(new Intent(this, DownloadService.class));

        if(extractBound) {
            unbindService(extractServiceConnection);
            extractBound = false;
        }
        stopService(new Intent(this, ExtractService.class));
    }
}