package com.example.realtimechat.login;


import static com.example.realtimechat.chat.Config.IP_CONFIG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.realtimechat.R;
import com.example.realtimechat.home.HomeActivity;
import com.example.realtimechat.utils.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText edtTaiKhoan, edtMatKhau;
    TextView txtQuenPass, txtLogup;
    AppCompatButton btnDangNhap, btnQuayLai;
    CheckBox cbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViewByID();
        
        initListener();

    }

    private void initListener() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNotNull()) {
                    getLogin(
                        edtTaiKhoan.getText().toString().trim(),
                        edtMatKhau.getText().toString().trim()
                    );
                }
            }
        });

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtQuenPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, QuenPassActivity.class));
            }
        });

        txtLogup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LogUpActivity.class));
                finish();
            }
        });
    }

    private boolean checkNotNull() {
        if (edtTaiKhoan.getText().toString().trim().isEmpty()) {
            Toast.makeText(LoginActivity.this, getText(R.string.email_cannot_be_empty), Toast.LENGTH_LONG).show();
            return false;
        }
        if (edtMatKhau.getText().toString().trim().isEmpty()) {
            Toast.makeText(LoginActivity.this, getText(R.string.password_cannot_be_blank), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private void startHome(String password) {
        Intent intent;
        if (Objects.equals(password, "Abc@1234")) {
            intent = new Intent(LoginActivity.this, ChangePassActivity.class);
            intent.putExtra("isDat", true);
            finish();
        } else {
            intent = new Intent(LoginActivity.this, HomeActivity.class);
        }
        startActivity(intent);
        finish();
        //Toast.makeText(LoginActivity.this, "Logged in successfully!", Toast.LENGTH_LONG).show();
    }

    private void initViewByID() {
        edtTaiKhoan = findViewById(R.id.edtTaiKhoan);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        cbRemember = findViewById(R.id.cbRemember);
        txtQuenPass = findViewById(R.id.txtQuenMK);
        txtLogup = findViewById(R.id.txtSignUp);
    }

    private boolean clickBack = false;
    @Override
    public void onBackPressed() {
        if (clickBack) {
            finish();
        } else {
            clickBack = true;
            Toast.makeText(this, getText(R.string.click_back), Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    clickBack = false;
                }
            }, 1000L);
        }
    }

    private void getLogin(String account, String pass) {
        String url = IP_CONFIG+"/auth/login?email="+account+"&password="+pass;
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (requestQueue != null) {
                                int id = response.getInt("id");
                                String email = response.getString("email");
                                String password = response.getString("password");
                                SharedPreferenceUtil.setLogin(cbRemember.isChecked());
                                SharedPreferenceUtil.setUserId(id);
                                SharedPreferenceUtil.setAccount(email);
                                SharedPreferenceUtil.setPassword(pass);
                                startHome(pass);
                            } else {
                                Toast.makeText(LoginActivity.this, getText(R.string.incorrect_information), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, getText(R.string.connection_errors), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Gmail or password is incorrect", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);
    }
}