package com.example.madcamp_week3;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonPlaceHolderAPI {

    @POST("restfulapi/register/")
    Call<User> register(@Body User user);

    @POST("restfulapi/sign_in/")
    Call<User> signIn(@Body User user);

    @POST("restfulapi/send/")
    Call<JsonPlayList> send(@Body JsonPlayList jsonPlayList);

    @GET("restfulapi/receive/")
    Call<ArrayList<JsonPlayList>> receive();

    @POST("restfulapi/delete/")
    Call<User> delete(@Body User user);

}