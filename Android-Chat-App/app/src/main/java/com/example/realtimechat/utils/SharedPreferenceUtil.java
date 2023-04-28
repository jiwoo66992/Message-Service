package com.example.realtimechat.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
    public static SharedPreferences sharedPreferences;

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences("data_app", Context.MODE_PRIVATE);
    }

    @SuppressLint("CommitPrefEdits")
    public static void setLogin(Boolean data) {
        sharedPreferences.edit().putBoolean("local_login", data).apply();
    }

    public static Boolean isLogin() {
        return sharedPreferences.getBoolean("local_login", false);
    }

    @SuppressLint("CommitPrefEdits")
    public static void setAccount(String data) {
        sharedPreferences.edit().putString("local_account", data).apply();
    }

    public static String getAccount() {
        return sharedPreferences.getString("local_account", "");
    }

    @SuppressLint("CommitPrefEdits")
    public static void setUserId(int data) {
        sharedPreferences.edit().putInt("local_user_id", data).apply();
    }

    public static int getUserId() {
        return sharedPreferences.getInt("local_user_id", 0);
    }

    @SuppressLint("CommitPrefEdits")
    public static void setPassword(String data) {
        sharedPreferences.edit().putString("local_password", data).apply();
    }

    public static String getPassword() {
        return sharedPreferences.getString("local_password", "");
    }


}
