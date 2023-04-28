package com.example.realtimechat.infor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimechat.R;
import com.example.realtimechat.databinding.LayoutBottomSheetMoreBinding;
import com.example.realtimechat.login.LoginActivity;
import com.example.realtimechat.model.User;
import com.example.realtimechat.server.ApiService;
import com.example.realtimechat.server.ResultPatch;
import com.example.realtimechat.utils.DataUtil;
import com.example.realtimechat.utils.SharedPreferenceUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class ChangeInformationActivity extends AppCompatActivity {

    EditText edtName, edtPhone, edtAddress, edtJob;
    TextView txtNgaySinh, txtSave;

    ImageView  imgBack;

    RadioGroup radioGroup;
    RadioButton rbNam, rbNu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);
        anhXa();

        edtName.setText(DataUtil.user.getName());
        if (Objects.equals(DataUtil.user.getGender(), "Female")) {
            rbNam.setChecked(false);
            rbNu.setChecked(true);
        } else {
            rbNam.setChecked(true);
            rbNu.setChecked(false);
        }
        txtNgaySinh.setText(DataUtil.user.getDob());
        edtPhone.setText(DataUtil.user.getPhone());
        edtAddress.setText(DataUtil.user.getAddress());
        edtJob.setText(DataUtil.user.getJob());

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNotNull()) {
                    updateInformation();
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });
    }

    private void updateInformation() {
        User user = DataUtil.user;
        user.setName(edtName.getText().toString());
        if (rbNam.isChecked()) {
            user.setGender("Male");
        } else {
            user.setGender("Female");
        }
        user.setDob(txtNgaySinh.getText().toString());
        user.setPhone(edtPhone.getText().toString());
        user.setAddress(edtAddress.getText().toString());
        user.setJob(edtJob.getText().toString());
        ApiService.apiService.updateUser(user).enqueue(new Callback<ResultPatch>() {
            @Override
            public void onResponse(Call<ResultPatch> call, retrofit2.Response<ResultPatch> response) {
                DataUtil.user = user;
                Toast.makeText(ChangeInformationActivity.this, getText(R.string.change_ok), Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResultPatch> call, Throwable t) {
                System.out.println("Error");
                t.printStackTrace();
            }
        });
    }

    static String regName = "^[a-zA-Z ]+$";
    private boolean checkNotNull() {
        if (edtName.getText().toString().trim().isEmpty()) {
            Toast.makeText(ChangeInformationActivity.this, getText(R.string.name_cannot_be_empty), Toast.LENGTH_LONG).show();
            return false;
        }
        if (txtNgaySinh.getText().toString().trim().isEmpty()) {
            Toast.makeText(ChangeInformationActivity.this, getText(R.string.date_of_birth_cannot_be_empty), Toast.LENGTH_LONG).show();
            return false;
        }
        if (edtPhone.getText().toString().trim().isEmpty()) {
            Toast.makeText(ChangeInformationActivity.this, getText(R.string.phone_cannot_be_empty), Toast.LENGTH_LONG).show();
            return false;
        }
        if (!edtName.getText().toString().matches(regName)) {
            Toast.makeText(this, "Name must have not number!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void anhXa() {
        edtName = findViewById(R.id.edtName);
        radioGroup = findViewById(R.id.radioGroup);
        rbNam = findViewById(R.id.rbNam);
        rbNu = findViewById(R.id.rbNu);
        txtNgaySinh = findViewById(R.id.txtNgaySinh);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtJob = findViewById(R.id.edtJob);
        txtSave = findViewById(R.id.txtSave);
        imgBack = findViewById(R.id.imgBack);
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
        bottomSheetBinding.thangSinh.setValue(myCalendar.get(Calendar.MONTH)+1);
        bottomSheetBinding.thangSinh.setWrapSelectorWheel(false);

        isNhuan = false;

        bottomSheetBinding.namSinh.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                int values = bottomSheetBinding.namSinh.getValue();
                if (values % 100 == 0) {
                    isNhuan = values % 400 == 0;
                } else {
                    isNhuan =  values % 4 == 0;
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
        }) ;

        moreBottomSheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                String ngayThangNam = bottomSheetBinding.namSinh.getValue()+"-";
                if (bottomSheetBinding.thangSinh.getValue() < 10) {
                    ngayThangNam+="0"+bottomSheetBinding.thangSinh.getValue()+"-";
                } else {
                    ngayThangNam+=bottomSheetBinding.thangSinh.getValue()+"-";
                }

                if (bottomSheetBinding.ngaySinh.getValue() < 10) {
                    ngayThangNam+="0"+bottomSheetBinding.ngaySinh.getValue();
                } else {
                    ngayThangNam+=bottomSheetBinding.ngaySinh.getValue();
                }

                txtNgaySinh.setText(ngayThangNam);

            }
        });

        moreBottomSheet.show();
    }
}