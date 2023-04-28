package com.example.realtimechat.splash;

import static com.example.realtimechat.chat.Config.IP_CONFIG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.realtimechat.R;
import com.example.realtimechat.home.HomeActivity;
import com.example.realtimechat.login.LoginActivity;
import com.example.realtimechat.utils.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    long delay = 1500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (SharedPreferenceUtil.isLogin()) {
            getLogin(SharedPreferenceUtil.getAccount(), SharedPreferenceUtil.getPassword());
        } else {
            startLogIn();
        }
    }

    private void getLogin(String account, String pass) {
        String url = IP_CONFIG+"/auth/login?email="+account+"&password="+pass;
        RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (requestQueue != null) {
                            startHome();
                        } else {
                            startLogIn();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashActivity.this, getText(R.string.connection_errors), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);
    }

    private void startHome() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
        }, delay);
    }

    private void startLogIn() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, delay);
    }

    @Override
    public void onBackPressed() {

    }
}