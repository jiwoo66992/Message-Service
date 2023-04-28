package com.example.realtimechat.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.realtimechat.R;

public class QuenPassActivity extends AppCompatActivity {

    EditText edtTaiKhoan;
    AppCompatButton btnXacNhan, btnQuayLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_pass);

        initViewByID();

        initListener();
    }

    private void initListener() {
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (checkNotNull()) {
                    getLogin(
                            edtTaiKhoan.getText().toString().trim(),
                            edtMatKhau.getText().toString().trim()
                    );
                }*/
            }
        });

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initViewByID() {
        edtTaiKhoan = findViewById(R.id.edtTaiKhoan);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        btnQuayLai = findViewById(R.id.btnQuayLai);
    }
}