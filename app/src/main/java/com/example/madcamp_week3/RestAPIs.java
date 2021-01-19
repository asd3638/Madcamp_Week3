package com.example.madcamp_week3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class RestAPIs {
    JsonPlaceHolderAPI mAPI = NetRetrofit.getInstance().getService();
    Context context;
    Intent intent;
    EditText useridEditText;
    EditText passwordEditText;

    public RestAPIs(Context context, Intent intent, EditText useridEditText, EditText passwordEditText) {
        this.context = context;
        this.intent = intent;
        this.useridEditText = useridEditText;
        this.passwordEditText = passwordEditText;
    }

    public void register(User user) {
        Call<User> registerCall = mAPI.register(user);
        registerCall.enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "successfully registered", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(context, "enter valid username or password, the length should be 20 at maximum", Toast.LENGTH_SHORT).show();
                        useridEditText.setText("");
                        passwordEditText.setText("");
                    } else if (response.code() == 404) {
                        Toast.makeText(context, "the user already exists", Toast.LENGTH_SHORT).show();
                        useridEditText.setText("");
                        passwordEditText.setText("");
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "register failed", Toast.LENGTH_SHORT).show();
                useridEditText.setText("");
                passwordEditText.setText("");
            }
        });
    }

    public void signIn(User user) {
        Call<User> signInCall = mAPI.signIn(user);
        signInCall.enqueue(new Callback<User> () {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 201) {
                    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(context, "user does not exist", Toast.LENGTH_SHORT).show();
                        useridEditText.setText("");
                        passwordEditText.setText("");
                    } else if (response.code() == 401) {
                        Toast.makeText(context, " unknown error", Toast.LENGTH_SHORT).show();
                        passwordEditText.setText("");
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show();
                passwordEditText.setText("");
            }
        });
    }

    public void send(JsonPlayList jsonPlayList) {
        Call<JsonPlayList> sendCall = mAPI.send(jsonPlayList);
        sendCall.enqueue(new Callback<JsonPlayList>() {
            @Override
            public void onResponse(Call<JsonPlayList> call, Response<JsonPlayList> response) {
                if (response.code() == 201) {
                    Toast.makeText(context, "successfully sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonPlayList> call, Throwable t) {
                Toast.makeText(context, "error occurred while sending", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<JsonPlayList> receive() {
        ArrayList<JsonPlayList> jsonPlayLists = new ArrayList<JsonPlayList>();
        Call<ArrayList<JsonPlayList>> receiveCall = mAPI.receive();
        /*
        receiveCall.enqueue(new Callback<ArrayList<JsonPlayList>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonPlayList>> call, Response<ArrayList<JsonPlayList>> response) {
                if (response.code() == 201) {
                    Toast.makeText(context, "successfully received", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JsonPlayList>> call, Throwable t) {
                Toast.makeText(context, "error occurred while receiving", Toast.LENGTH_SHORT).show();
            }
        });
         */
        try {
            jsonPlayLists = receiveCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonPlayLists;
    }

    public void delete(User user) {
        Call<User> deleteCall = mAPI.delete(user);
        deleteCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

}