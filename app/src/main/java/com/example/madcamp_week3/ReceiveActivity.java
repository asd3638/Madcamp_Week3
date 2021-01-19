package com.example.madcamp_week3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ReceiveActivity extends AppCompatActivity {
    String userid;
    TextView letterTextView;
    ImageButton confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userid = sharedPreferences.getString("userid", null);

        letterTextView = findViewById(R.id.letter_text_view);
        confirmButton = findViewById(R.id.receive_button);
        confirmButton.setVisibility(View.GONE);

        RestAPIs restAPI = new RestAPIs(getApplicationContext(), null, null, null);
        ArrayList<JsonPlayList> jsonPlayLists = restAPI.receive();
        JsonPlayList jsonPlayList = null;

        if (retrieve(jsonPlayLists) != null) {
            Log.d("TAG", "retrieve(jsonplaylists) is not null");
            jsonPlayList = retrieve(jsonPlayLists);
            letterTextView.setText(jsonPlayList.getLetter());
            confirmButton.setVisibility(View.VISIBLE);

            PlayList playList = new PlayList(userid, jsonPlayList.getTitle(), jsonPlayList.getDescription());
            Gson gson = new Gson();
            ArrayList<Song> songs = gson.fromJson(jsonPlayList.getSongs(), new TypeToken<ArrayList<Song>>(){}.getType());
            playList.setSongs(songs);

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RestAPIs restAPI = new RestAPIs(getApplicationContext(), null, null, null);
                    restAPI.delete(new User(userid, ""));

                    PlayListList.addPlaylist(playList);

                    ArrayList<String> urlList = playList.getUrlList();

                    Intent service = new Intent(getApplicationContext(), DownloadService.class);
                    service.putExtra("download_link", urlList);
                    service.putExtra("download_link_index", 0);
                    service.putExtra("position", PlayListList.getPlaylistArrayForCertainUser(userid).size() - 1);
                    startService(service);

                    PlayListPref playListPref = new PlayListPref();
                    playListPref.setPlayListPref(getApplicationContext());

                    Intent intent = new Intent(getApplicationContext(), GridActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private JsonPlayList retrieve(ArrayList<JsonPlayList> jsonPlayLists) {
        JsonPlayList jsonPlayList = null;
        for (int i = 0; i < jsonPlayLists.size(); i++) {
            jsonPlayList = jsonPlayLists.get(i);
            String packageUserid = jsonPlayList.getUserid();
            if (userid.equals(packageUserid)) {
                return jsonPlayList;
            }
        }
        return jsonPlayList;
    }
}