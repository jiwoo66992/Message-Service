package com.example.realtimechat.server;

import android.net.Uri;

import com.example.realtimechat.chat.Config;
import com.example.realtimechat.model.ResetPassModel;
import com.example.realtimechat.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    ApiService apiService = new Retrofit.Builder()
            .baseUrl(Config.IP_CONFIG)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);

    @GET("/users/info/{email}")
    Call<User> getUser(@Path("email") String email);

    @GET("/users/info/{email}")
    Call<User> getListUser(@Path("email") String email);

    @POST("/auth/register")
    Call<ResultPatch> createUser(@Body User User);

    @PATCH("/users")
    Call<ResultPatch> updateUser(@Body User user);

    @POST("/auth/reset-password")
    Call<ResultPatch> resetPassword(@Body ResetPassModel resetPassModel);

}
