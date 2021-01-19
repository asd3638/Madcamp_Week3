package com.example.madcamp_week3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class PlayListPref {
    private static FileWriter fileWriter;
    private static FileReader fileReader;


    public PlayListPref() {}

    public void setPlayListPref(Context context) {
        Log.d("TAG", "setting playlist preferences");

        // convert arraylist<playlist> to jsonarray
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(PlayListList.getPlaylistarrayWithoutUserid()).getAsJsonArray();

        // convert jsonarray to string
        String json = myCustomArray.toString();
        /*
        Gson gson = new Gson();
        String json = gson.toJson(PlayListList.getPlaylistarrayWithoutUserid());

         */

        // write string to the json file and save
        try {
            fileWriter = new FileWriter(Environment.getExternalStorageDirectory() + "/Music/PlayListList.json");
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*

        SharedPreferences sharedPreferences = context.getSharedPreferences("playlists", Context.MODE_PRIVATE);
        // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putString("playlists", json);
        editor.commit();

         */
        /*
        ArrayList<PlayList> playlists = PlayListList.getPlaylistarray();
        ArrayList<JSONObject> jsonPlaylists = new ArrayList<JSONObject>();

        try {
            for (int i = 0; i < playlists.size(); i++) {
                PlayList obj = playlists.get(i);
                Log.d("TAG", obj.getTitle());
                Gson gson = new Gson();
                String json = gson.toJson(obj);
                JSONObject jsonObj = new JSONObject(json);
                jsonPlaylists.add(jsonObj);
            }
        } catch (JSONException e) {
            Log.d("TAG", "jsonexception");
            e.printStackTrace();
        }

        String key = "playlists";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonObject = gson.toJson(jsonPlaylists);

        if (playlists != null) {
            editor.putString(key, jsonObject.toString());
        } else {
            editor.putString(key, null);
        }

        editor.apply();

         */
    }

    public void getPlayListPref(Context context) {

        Log.d("TAG", "getting playlist preferences");

        ArrayList<PlayList> arrayObj = new ArrayList<PlayList>();

        // retrieve string from the json file, and convert it to arraylist directly using gson
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = new JsonArray();

        try {
            Object obj = parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/Music/PlayListList.json"));
            jsonArray = (JsonArray) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // convert jsonarray to arraylist<playlist>
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                PlayList playlist = new Gson().fromJson(jsonArray.get(i).toString(), PlayList.class);
                arrayObj.add(playlist);
            }
        }
        /*

        SharedPreferences sharedPreferences = context.getSharedPreferences("playlists", Context.MODE_PRIVATE);
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String serializedObject = sharedPreferences.getString("playlists", null);
        if (serializedObject != null) {
            Log.d("TAG", "it is not null!");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<PlayList>>(){}.getType();
            arrayObj = gson.fromJson(serializedObject, type);
            Log.d("TAG", String.valueOf(arrayObj));
        } else {
            Log.d("TAG", "it is null...");
        }

        String userid = sharedPreferences.getString("userid", null);

         */

        PlayListList.setPlaylist(arrayObj);
        /*
        String key = "playlists";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonArrayString = sharedPreferences.getString(key, null);
        ArrayList<PlayList> values = new ArrayList<PlayList>();

        if (jsonArrayString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonArrayString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    Gson gson = new Gson();
                    PlayList obj = gson.fromJson(jsonObject.toString(), PlayList.class);
                    values.add(obj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        PlayListList.clear();

        for (int i = 0 ; i < values.size(); i++) {
            PlayListList.addPlaylist(values.get(i));
        }
    }
    /*
    public ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonArrayString = sharedPreferences.getString(key, null);
        ArrayList<String> values = new ArrayList<String>();

        if (jsonArrayString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonArrayString);
                for (int i = 0; i< jsonArray.length(); i++) {
                    String value = jsonArray.optString(i);
                    values.add(value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return values;

         */
    }


}
