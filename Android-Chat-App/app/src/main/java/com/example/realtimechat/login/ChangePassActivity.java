package com.example.realtimechat.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimechat.R;
import com.example.realtimechat.home.HomeActivity;
import com.example.realtimechat.infor.ChangeInformationActivity;
import com.example.realtimechat.model.ResetPassModel;
import com.example.realtimechat.server.ApiService;
import com.example.realtimechat.server.ResultPatch;
import com.example.realtimechat.utils.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;

public class ChangePassActivity extends AppCompatActivity {

    EditText edtMatKhauCu, edtMatKhau, edtXacNhanMatKhau;
    RelativeLayout llOldPass;
    AppCompatButton btnDoiMk, btnQuayLai;
    TextView txtTitle;

    private Boolean isDatMatKhau = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        isDatMatKhau = getIntent().getBooleanExtra("isDat", false);

        initViewByID();

        initListener();
    }

    private void initListener() {
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnDoiMk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDatMatKhau) {
                    if (edtMatKhau.getText().toString().trim().isEmpty()) {
                        Toast.makeText(ChangePassActivity.this, getText(R.string.new_password_cannot_be_blank), Toast.LENGTH_LONG).show();
                    } else if (edtXacNhanMatKhau.getText().toString().trim().isEmpty()) {
                        Toast.makeText(ChangePassActivity.this, getText(R.string.confirm_password_cannot_be_blank), Toast.LENGTH_LONG).show();
                    } else if (checkEqualPass()) {
                        updatePassword();
                    }
                } else {
                    if (edtMatKhauCu.getText().toString().trim().isEmpty()) {
                        Toast.makeText(ChangePassActivity.this, getText(R.string.password_cannot_be_blank), Toast.LENGTH_LONG).show();
                    } else if (!edtMatKhauCu.getText().toString().trim().equals(SharedPreferenceUtil.getPassword().trim())) {
                        Toast.makeText(ChangePassActivity.this, getText(R.string.old_pass_not_same), Toast.LENGTH_LONG).show();
                    } else if (edtMatKhau.getText().toString().trim().isEmpty()) {
                        Toast.makeText(ChangePassActivity.this, getText(R.string.new_password_cannot_be_blank), Toast.LENGTH_LONG).show();
                    } else if (edtXacNhanMatKhau.getText().toString().trim().isEmpty()) {
                        Toast.makeText(ChangePassActivity.this, getText(R.string.confirm_password_cannot_be_blank), Toast.LENGTH_LONG).show();
                    } else if (checkEqualPass()) {
                        updatePassword();
                    }
                }
            }
        });
    }

    private void updatePassword() {
        ApiService.apiService.resetPassword(new ResetPassModel(
                SharedPreferenceUtil.getAccount(),
                edtMatKhau.getText().toString()
        )).enqueue(new Callback<ResultPatch>() {
            @Override
            public void onResponse(Call<ResultPatch> call, retrofit2.Response<ResultPatch> response) {
                SharedPreferenceUtil.setPassword(edtMatKhau.getText().toString());
                Toast.makeText(ChangePassActivity.this, getText(R.string.change_ok), Toast.LENGTH_LONG).show();
                if (isDatMatKhau) {
                    startActivity(new Intent(ChangePassActivity.this, HomeActivity.class));
                }
                finish();
            }

            @Override
            public void onFailure(Call<ResultPatch> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private boolean checkEqualPass() {
        if (edtMatKhau.getText().toString().equals(edtXacNhanMatKhau.getText().toString())) {
            return true;
        }
        Toast.makeText(ChangePassActivity.this, getText(R.string.confirm_pass_not_same), Toast.LENGTH_LONG).show();
        return false;
    }

    @SuppressLint("SetTextI18n")
    private void initViewByID() {
        edtMatKhauCu = findViewById(R.id.edtMatKhauCu);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtXacNhanMatKhau = findViewById(R.id.edtXacNhanMatKhau);
        btnDoiMk = findViewById(R.id.btnDoiMk);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        txtTitle = findViewById(R.id.txtTitle);
        llOldPass = findViewById(R.id.llOldPass);

        if (isDatMatKhau) {
            btnQuayLai.setVisibility(View.GONE);
            llOldPass.setVisibility(View.GONE);
            txtTitle.setText(getText(R.string.reset_password));
        }

    }

    @Override
    public void onBackPressed() {
        if (!isDatMatKhau)
            finish();
    }
}