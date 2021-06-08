package com.example.madcamp_week3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.arthenica.mobileffmpeg.FFmpeg;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    Context context;
    EditText useridEditText;
    EditText passwordEditText;
    String userid;
    String password;
    ImageButton signInButton;
    ImageButton signUpButton;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void checkPermission(Context context, String[] permissions) {
        Boolean b = true;

        for (int i=0; i <permissions.length; i++) {
            b = (ActivityCompat.checkSelfPermission(context, permissions[i]) == PackageManager.PERMISSION_GRANTED) && b;
        }

        if (!b) {
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TAG", "Main activity on create.");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        PlayListPref playListPref = new PlayListPref();
        playListPref.getPlayListPref(getApplicationContext());

        context = getApplicationContext();
        checkPermission(context, permissions);

        signInButton = findViewById(R.id.sign_in_button);
        signUpButton = findViewById(R.id.sign_up_button);
        useridEditText = findViewById(R.id.id_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userid = useridEditText.getText().toString();
                password = passwordEditText.getText().toString();

                User user = new User(userid, password);

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //editor.remove("playlists" );

                editor.putString("userid", userid);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, GridActivity.class);
                RestAPIs restAPI = new RestAPIs(getApplicationContext(), intent, useridEditText, passwordEditText);

                restAPI.logIn(user);

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userid = useridEditText.getText().toString();
                password = passwordEditText.getText().toString();

                User user = new User(userid, password);

                RestAPIs restAPI = new RestAPIs(getApplicationContext(), null, useridEditText, passwordEditText);
                restAPI.signIn(user);
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d("TAG", "Main activity on resume.");

        super.onResume();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        Log.d("TAG", "Main activity on back pressed.");

        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onPause() {
        super.onPause();

        PlayListPref playListPref = new PlayListPref();
        playListPref.setPlayListPref(getApplicationContext());
    }


    @Override
    protected void onDestroy() {
        Log.d("TAG", "Main activity on destroy.");
        super.onDestroy();
    }
}