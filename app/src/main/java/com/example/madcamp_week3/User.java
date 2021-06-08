package com.example.madcamp_week3;

import android.content.SharedPreferences;

public class User {
    private String userId;
    private String password;

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;

    }

    public String getUserid() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setUserid(String userid) {
        this.userId = userid;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
