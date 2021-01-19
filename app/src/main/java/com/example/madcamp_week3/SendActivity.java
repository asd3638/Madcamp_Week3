package com.example.madcamp_week3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class SendActivity extends AppCompatActivity {
    String userid;
    int position;
    EditText toEditText;
    EditText contentEditText;
    TextView fromTextView;
    ImageButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userid = sharedPreferences.getString("userid", null);
        position = getIntent().getIntExtra("position", 0);

        toEditText = findViewById(R.id.to_edit_text);
        contentEditText = findViewById(R.id.content_edit_text);
        fromTextView = findViewById(R.id.from_text_view);
        sendButton = findViewById(R.id.send_button);

        fromTextView.setText(userid);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayList playList = PlayListList.getPlaylist(position, userid);
                String receivingUser = toEditText.getText().toString();
                String letter = "to. " + receivingUser + "\n\n" + contentEditText.getText().toString() +
                        "\n\n" + "                      from. " + userid;
                JsonPlayList jsonPlaylist = new JsonPlayList(receivingUser, playList, letter);

                RestAPIs restAPI = new RestAPIs(getApplicationContext(), null, null, null);
                restAPI.send(jsonPlaylist);

                PlayListList.deletePlaylist(position, userid);

                PlayListPref playListPref = new PlayListPref();
                playListPref.setPlayListPref(getApplicationContext());

                Intent intent = new Intent(getApplicationContext(), GridActivity.class);
                startActivity(intent);
            }
        });
    }
}