package com.example.madcamp_week3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class GridActivity extends AppCompatActivity {
    ImageButton addButton;
    ImageButton receiveButton;
    String userid;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        Log.d("TAG", "grid activity on resume");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userid = sharedPreferences.getString("userid", null);

        Log.d("RESUMMME", userid);

        PlayListPref playListPref = new PlayListPref();
        playListPref.getPlayListPref(getApplicationContext());

        PlayListList.getPlayListList();

        addButton = findViewById(R.id.add_button);
        receiveButton = findViewById(R.id.receive_button);

        ArrayList<String> titleList = PlayListList.getTitleList(userid);
        Log.d("TAG", String.valueOf(PlayListList.getPlaylistarrayWithoutUserid()));
        Log.d("TAG", String.valueOf(titleList));

        int position = PlayListList.getSize(userid);
        String str = Integer.toString(position);

        mPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ViewPagerAdapter(this, R.layout.row, titleList, userid);
        mPager.setAdapter(pagerAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog();
            }
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReceiveActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("TAG", "grid activity on pause");

        PlayListPref playListPref = new PlayListPref();
        playListPref.setPlayListPref(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PlayListPref playListPref = new PlayListPref();
        playListPref.setPlayListPref(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent (GridActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void addDialog() {
        Log.d("TAG", "show dialog executing");
        System.out.println("show dialog executing");

        AlertDialog.Builder builder = new AlertDialog.Builder(GridActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.playlistadd_dialog, null);
        builder.setView(dialogView);

        EditText et_pl = dialogView.findViewById(R.id.et_pl);
        EditText et_dc = dialogView.findViewById(R.id.et_dc);
        ImageButton pl_add_button = dialogView.findViewById(R.id.pl_add_button);
        ImageButton pl_cancel_button = dialogView.findViewById(R.id.pl_cancel_button);


        AlertDialog dialog = builder.create();

        dialog.show();

        System.out.println("here");

        pl_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pl_name = et_pl.getText().toString();
                String pl_des = et_dc.getText().toString();

                if (pl_name.equals("") || pl_des.equals("")) {
                    Toast error_m = Toast.makeText(getApplicationContext(),  "작성을 완료하세요.", LENGTH_SHORT);
                    error_m.show();
                } else {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    userid = sharedPreferences.getString("userid", null);
                    Log.d("Else", userid + " " + et_pl.getText().toString() + " " + et_dc.getText().toString());

                    String title = et_pl.getText().toString();
                    String description = et_dc.getText().toString();

                    PlayListList.addPlaylist(new PlayList(userid, title, description));

                    PlayListPref playListPref = new PlayListPref();
                    playListPref.setPlayListPref(getApplicationContext());

                    pagerAdapter.notifyDataSetChanged();

                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), GridActivity.class);
                    startActivity(intent);
                }
            }
        });

        pl_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void editDialog(int position) {
        Log.d("TAG", "show dialog executing");
        System.out.println("show dialog executing");

        AlertDialog.Builder builder = new AlertDialog.Builder(GridActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogeditView = inflater.inflate(R.layout.playlistedit_dialog, null);
        builder.setView(dialogeditView);

        EditText et_pl_ed = dialogeditView.findViewById(R.id.et_pl_ed);
        EditText et_dc_ed = dialogeditView.findViewById(R.id.et_dc_ed);

        Button pled_edit_button = dialogeditView.findViewById(R.id.pled_edit_button);
        Button pled_delete_button = dialogeditView.findViewById(R.id.pled_delete_button);
        Button pled_cancel_button = dialogeditView.findViewById(R.id.pled_cancel_button);

        et_pl_ed.setText(PlayListList.getPlaylist(position, userid).getTitle());
        et_dc_ed.setText(PlayListList.getPlaylist(position, userid).getDescription());

        AlertDialog dialog = builder.create();
        dialog.show();

        System.out.println("here");

        pled_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pl_name = et_pl_ed.getText().toString();
                String pl_des = et_dc_ed.getText().toString();

                if (pl_name.equals("") || pl_des.equals("")) {
                    Toast error_m = Toast.makeText(getApplicationContext(),  "작성을 완료하세요.", LENGTH_SHORT);
                    error_m.show();
                } else {
                    //수정하는 방법 바꾸기
                    PlayListList.editPlaylist(position, userid, pl_name, pl_des);
                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), GridActivity.class);
                    startActivity(intent);
                }
            }
        });

        pled_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayListList.deletePlaylist(position, userid);
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), GridActivity.class);
                startActivity(intent);
            }
        });

        pled_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}