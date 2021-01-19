package com.example.madcamp_week3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity {

    int position;

    String userid;
    ListView listview;
    public static PlaylistAdapter adapter;

    View view;

    View dialogView;
    TextView tv_tracklist;
    ImageView rotatorAbove;
    ImageView rotatorBelow;
    ImageButton tracklistButton;
    ImageButton sendButton;
    Button editButton;
    Button playButton;
    Button stopButton;

    Boolean isPlaying;

    List<Animator> animations;
    ObjectAnimator imageViewObjectAnimator1;
    ObjectAnimator imageViewObjectAnimator2;
    final AnimatorSet animatorSet = new AnimatorSet();

    public static PlaylistAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        view = getWindow().getDecorView().getRootView();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userid = sharedPreferences.getString("userid", null);

        position = getIntent().getIntExtra("position", 0);

        isPlaying = userid.equals(BackgroundSoundService.userid) && position == BackgroundSoundService.position;

        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.tracklist_dialog, null);
        listview = (ListView) dialogView.findViewById(R.id.tracklist_list_view);
        tv_tracklist = (TextView) dialogView.findViewById(R.id.tv_tracklist);
        tv_tracklist.setText(PlayListList.getPlaylist(position, userid).getDescription());

        adapter = new PlaylistAdapter(getApplicationContext(), PlayListList.getPlaylist(position, userid));
        listview.setAdapter(adapter);

        rotatorAbove = findViewById(R.id.rotator_above);
        rotatorBelow = findViewById(R.id.rotator_below);

        animations = new ArrayList<Animator>();

        imageViewObjectAnimator1 = ObjectAnimator.ofFloat(rotatorAbove,
                "rotation", 0f, 360f);
        imageViewObjectAnimator1.setRepeatCount(ObjectAnimator.INFINITE);
        imageViewObjectAnimator1.setRepeatMode(ObjectAnimator.RESTART);
        imageViewObjectAnimator1.setInterpolator(new AccelerateInterpolator());


        animations.add(imageViewObjectAnimator1);

        imageViewObjectAnimator2 = ObjectAnimator.ofFloat(rotatorBelow,
                "rotation", 0f, 360f);
        imageViewObjectAnimator2.setRepeatCount(ObjectAnimator.INFINITE);
        imageViewObjectAnimator2.setRepeatMode(ObjectAnimator.RESTART);
        imageViewObjectAnimator2.setInterpolator(new AccelerateInterpolator());

        animations.add(imageViewObjectAnimator2);

        animatorSet.playTogether(animations);
        animatorSet.start();


        if (preprocessor() == 1) {
            setRotatorVisibility(true);
        } else {
            setRotatorVisibility(false);
        }


        tracklistButton = findViewById(R.id.tracklist_buton);
        sendButton = findViewById(R.id.send_button);
        editButton = findViewById(R.id.edit_button);
        playButton = findViewById(R.id.play_button);
        stopButton = findViewById(R.id.stop_button);


        tracklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTracklistDialog(position);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SendActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mp_search;
                try {
                    mp_search = MediaPlayer.create(getApplicationContext(), R.raw.press);
                    if (mp_search.isPlaying()) {
                        mp_search.stop();
                        mp_search.release();
                    }
                    mp_search.start();
                } catch(Exception e) { e.printStackTrace(); }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(PlayActivity.this, SearchActivity.class);
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }
                }, 1000);

                Intent intent = new Intent(PlayActivity.this, SearchActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mp_play;
                try {
                    mp_play = MediaPlayer.create(getApplicationContext(), R.raw.press);
                    if (mp_play.isPlaying()) {
                        mp_play.stop();
                        mp_play.release();
                    }
                    mp_play.start();
                } catch(Exception e) { e.printStackTrace(); }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clickstart(view);
                    }
                }, 1000);


                clickstart(view);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mp_pause;
                try {
                    mp_pause = MediaPlayer.create(getApplicationContext(), R.raw.press);
                    if (mp_pause.isPlaying()) {
                        mp_pause.stop();
                        mp_pause.release();
                    }
                    mp_pause.start();
                } catch(Exception e) { e.printStackTrace(); }


                clickpause(view);
            }
        });

        stopButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MediaPlayer mp_stop;
                try {
                    mp_stop = MediaPlayer.create(getApplicationContext(), R.raw.stop_taping);
                    if (mp_stop.isPlaying()) {
                        mp_stop.stop();
                        mp_stop.release();
                    }
                    mp_stop.start();
                } catch(Exception e) { e.printStackTrace(); }



                clickstop(view);
                return true;
            }
        });
    }


    public void showTracklistDialog(int position) {
        Log.d("TAG", "show dialog executing");
        System.out.println("show dialog executing");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userid = sharedPreferences.getString("userid", null);


        AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
        /*
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.tracklist_dialog, null);
         */

        if (dialogView.getParent() != null)
            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
        builder.setView(dialogView);

        listview = (ListView) dialogView.findViewById(R.id.tracklist_list_view);
        adapter = new PlaylistAdapter(getApplicationContext(), PlayListList.getPlaylist(position, userid));
        listview.setAdapter(adapter);

        Button closeButton = dialogView.findViewById(R.id.close_button);
        AlertDialog dialog = builder.create();
        dialog.show();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void clickstart(View view) {
        MediaPlayer mediaPlayer = BackgroundSoundService.mediaPlayer;
        if (preprocessor() == 0) {
            Intent intent = new Intent(this, BackgroundSoundService.class);
            intent.putExtra("position", position);
            startService(intent);
            setRotatorVisibility(true);
        } else if (preprocessor() == 1) {
            setRotatorVisibility(true);
        } else if (preprocessor() == 2) {
            mediaPlayer.start();
            setRotatorVisibility(true);
        } else if (preprocessor() == 3 || preprocessor() == 4) {
            setRotatorVisibility(false);
        }
    }

    public void clickpause(View view) {
        MediaPlayer mediaPlayer = BackgroundSoundService.mediaPlayer;
        if (preprocessor() == 0 || preprocessor() == 2 || preprocessor() == 3 || preprocessor() == 4) {
            setRotatorVisibility(false);
        } else if (preprocessor() == 1) {
            mediaPlayer.pause();
            setRotatorVisibility(false);
        }
    }

    public void clickstop(View view) {
        MediaPlayer mediaPlayer = BackgroundSoundService.mediaPlayer;
        if (preprocessor() == 0 || preprocessor() == 3 || preprocessor() == 4) {
            setRotatorVisibility(false);
        } else if (preprocessor() == 1 || preprocessor() == 2) {
            setRotatorVisibility(false);
            mediaPlayer.stop();
            mediaPlayer.release();
            BackgroundSoundService.mediaPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent (PlayActivity.this, GridActivity.class);
        startActivity(intent);
    }

    public int preprocessor() {
        int result;
        MediaPlayer mediaPlayer = BackgroundSoundService.mediaPlayer;
        String mpUserid = BackgroundSoundService.userid;
        int mpPosition = BackgroundSoundService.position;
        String userid = this.userid;
        int position = this.position;

        if (mediaPlayer == null) {
            result = 0;
        } else {
            if (userid.equals(mpUserid) && position == mpPosition) {
                if (mediaPlayer.isPlaying()) {
                    result = 1;
                } else {
                    result = 2;
                }
            } else {
                if (mediaPlayer.isPlaying()) {
                    result = 3;
                } else {
                    result = 4;
                }
            }
        }

        return result;
    }

    public void setRotatorVisibility(Boolean b) {
        if (b) {
            rotatorAbove.setVisibility(View.VISIBLE);
            rotatorBelow.setVisibility(View.VISIBLE);
        } else {
            rotatorAbove.setVisibility(View.INVISIBLE);
            rotatorBelow.setVisibility(View.INVISIBLE);
        }
    }
}