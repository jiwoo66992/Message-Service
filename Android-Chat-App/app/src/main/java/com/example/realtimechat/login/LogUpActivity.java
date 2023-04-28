package com.example.realtimechat.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimechat.R;
import com.example.realtimechat.databinding.LayoutBottomSheetMoreBinding;
import com.example.realtimechat.infor.ChangeInformationActivity;
import com.example.realtimechat.model.User;
import com.example.realtimechat.server.ApiService;
import com.example.realtimechat.server.ResultPatch;
import com.example.realtimechat.utils.DataUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;

public class LogUpActivity extends AppCompatActivity {

    EditText edtTaiKhoan, edtName, edtChucVu, edtGioiTinh, edtPhone;

    TextView txtNgaySinh;
    AppCompatButton btnDangKy, btnQuayLai;


    RadioGroup radioGroup;
    RadioButton rbNam, rbNu;

    static String regEmail = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    static String regName = "^[a-zA-Z ]+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_up);

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

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNull()){
                    dangKyAccount();
                }
            }
        });

        txtNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });
    }

    private void dangKyAccount() {
        User user = new User();
        user.setEmail(edtTaiKhoan.getText().toString());
        user.setName(edtName.getText().toString());
        if (rbNam.isChecked()) {
            user.setGender("Male");
        } else {
            user.setGender("Female");
        }
        user.setDob(txtNgaySinh.getText().toString());
        user.setPhone(edtPhone.getText().toString());
        //user.setJob(edtChucVu.getText().toString());
        user.setPassword("Abc@1234");
        ApiService.apiService.createUser(user).enqueue(new Callback<ResultPatch>() {
            @Override
            public void onResponse(Call<ResultPatch> call, retrofit2.Response<ResultPatch> response) {
                Toast.makeText(LogUpActivity.this, getText(R.string.register_success), Toast.LENGTH_LONG).show();
                onBackPressed();
            }

            @Override
            public void onFailure(Call<ResultPatch> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private boolean checkNull() {
        if (
                edtTaiKhoan.getText().toString().trim().isEmpty()
                        || edtName.getText().toString().trim().isEmpty()
                        || txtNgaySinh.getText().toString().trim().isEmpty()
                        || edtPhone.getText().toString().trim().isEmpty()
        ) {
            Toast.makeText(this, getText(R.string.data_cannot_be_empty), Toast.LENGTH_LONG).show();
            return false;
        }
        if (!edtTaiKhoan.getText().toString().matches(regEmail)) {
            Toast.makeText(this, "Email not validate!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!edtName.getText().toString().matches(regName)) {
            Toast.makeText(this, "Name must have not number!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void initViewByID() {
        edtTaiKhoan = findViewById(R.id.edtTaiKhoan);
        edtName = findViewById(R.id.edtName);
        edtChucVu = findViewById(R.id.edtChucVu);
        radioGroup = findViewById(R.id.radioGroup);
        rbNam = findViewById(R.id.rbNam);
        rbNu = findViewById(R.id.rbNu);
        txtNgaySinh = findViewById(R.id.txtNgaySinh);
        edtPhone = findViewById(R.id.edtPhone);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnQuayLai = findViewById(R.id.btnQuayLai);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LogUpActivity.this, LoginActivity.class));
        finish();
    }

    boolean isNhuan = false;

    private Calendar myCalendar = Calendar.getInstance();

    private void showBottomSheet() {
        LayoutBottomSheetMoreBinding bottomSheetBinding = LayoutBottomSheetMoreBinding.inflate(getLayoutInflater());
        BottomSheetDialog moreBottomSheet =
                new BottomSheetDialog(this);
        moreBottomSheet.setContentView(bottomSheetBinding.getRoot());

        bottomSheetBinding.namSinh.setMaxValue(2023);
        bottomSheetBinding.namSinh.setMinValue(1950);
        bottomSheetBinding.namSinh.setValue(myCalendar.get(Calendar.YEAR));
        bottomSheetBinding.namSinh.setWrapSelectorWheel(false);

        bottomSheetBinding.ngaySinh.setMaxValue(31);
        bottomSheetBinding.ngaySinh.setMinValue(1);
        bottomSheetBinding.ngaySinh.setValue(myCalendar.get(Calendar.DAY_OF_MONTH));
        bottomSheetBinding.ngaySinh.setWrapSelectorWheel(false);

        bottomSheetBinding.thangSinh.setMaxValue(12);
        bottomSheetBinding.thangSinh.setMinValue(1);
        bottomSheetBinding.thangSinh.setValue(myCalendar.get(Calendar.MONTH) + 1);
        bottomSheetBinding.thangSinh.setWrapSelectorWheel(false);

        isNhuan = false;

        bottomSheetBinding.namSinh.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                int values = bottomSheetBinding.namSinh.getValue();
                if (values % 100 == 0) {
                    isNhuan = values % 400 == 0;
                } else {
                    isNhuan = values % 4 == 0;
                }
                if (isNhuan) {
                    if (bottomSheetBinding.thangSinh.getValue() == 2) {
                        bottomSheetBinding.ngaySinh.setMaxValue(29);
                    }
                } else {
                    if (bottomSheetBinding.thangSinh.getValue() == 2) {
                        bottomSheetBinding.ngaySinh.setMaxValue(28);
                    }
                }
            }
        });

        bottomSheetBinding.thangSinh.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                switch (bottomSheetBinding.thangSinh.getValue()) {
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12: {
                        bottomSheetBinding.ngaySinh.setMaxValue(31);
                        break;
                    }
                    case 4:
                    case 6:
                    case 9:
                    case 11: {
                        bottomSheetBinding.ngaySinh.setMaxValue(30);
                        break;
                    }
                    default: {
                        if (isNhuan) {
                            bottomSheetBinding.ngaySinh.setMaxValue(29);
                        } else {
                            bottomSheetBinding.ngaySinh.setMaxValue(28);
                        }
                        break;
                    }
                }
            }
        });

        moreBottomSheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                String ngayThangNam = bottomSheetBinding.namSinh.getValue() + "-";
                if (bottomSheetBinding.thangSinh.getValue() < 10) {
                    ngayThangNam += "0" + bottomSheetBinding.thangSinh.getValue() + "-";
                } else {
                    ngayThangNam += bottomSheetBinding.thangSinh.getValue() + "-";
                }

                if (bottomSheetBinding.ngaySinh.getValue() < 10) {
                    ngayThangNam += "0" + bottomSheetBinding.ngaySinh.getValue();
                } else {
                    ngayThangNam += bottomSheetBinding.ngaySinh.getValue();
                }

                txtNgaySinh.setText(ngayThangNam);

            }
        });

        moreBottomSheet.show();
    }
}