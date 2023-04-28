package com.example.realtimechat.infor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.realtimechat.R;
import com.example.realtimechat.home.HomeActivity;
import com.example.realtimechat.login.ChangePassActivity;
import com.example.realtimechat.login.LoginActivity;
import com.example.realtimechat.utils.DataUtil;

import java.util.Objects;

public class InformationActivity extends AppCompatActivity {

    TextView txtName,txtGender, txtDob, txtPhone, txtEmail, txtAddress, txtJob;

    ImageView imgEdit, imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        anhXa();
        update();
    }

    private void anhXa() {
        txtName = findViewById(R.id.txtName);
        txtGender = findViewById(R.id.txtGender);
        txtDob = findViewById(R.id.txtDob);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        txtAddress = findViewById(R.id.txtAddress);
        txtJob = findViewById(R.id.txtJob);
        imgEdit = findViewById(R.id.imgEdit);
        imgBack = findViewById(R.id.imgBack);
    }

    private void update() {

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenuMore(view);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtName.setText(DataUtil.user.getName());
        if (Objects.equals(DataUtil.user.getGender(), "Female")) {
            txtGender.setText(getText(R.string.female));
        } else {
            txtGender.setText(getText(R.string.male));
        }
        txtDob.setText(DataUtil.user.getDob());
        txtPhone.setText(DataUtil.user.getPhone());
        txtEmail.setText(DataUtil.user.getEmail());
        txtAddress.setText(DataUtil.user.getAddress());
        txtJob.setText(DataUtil.user.getJob());
    }

    private void popupMenuMore(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_infor, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.editInfor:
                        startActivity(new Intent(InformationActivity.this, ChangeInformationActivity.class));
                        break;
                    case R.id.editPass:
                        startActivity(new Intent(InformationActivity.this, ChangePassActivity.class));
                        break;
                }
                return true;
            }
        });

        popupMenu.show();
    }
}