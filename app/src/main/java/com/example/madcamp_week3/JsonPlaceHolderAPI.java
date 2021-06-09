package com.example.madcamp_week3;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderAPI {

    //이 부분이 서버에 요청 보내는 부분!

    @POST("accounts/createAccount")
    Call<User> signIn(@Body User user);

    @POST("accounts/logIn")
    Call<User> logIn(@Body User user);

    @POST("playlists.json")
    Call<JsonPlayList> send(@Body JsonPlayList jsonPlayList);

    @GET("restfulapi/receive/")
    Call<ArrayList<JsonPlayList>> receive();

    @POST("restfulapi/delete/")
    Call<User> delete(@Body User user);

}