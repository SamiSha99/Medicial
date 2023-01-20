package com.example.medicial.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medicial.Database.DBHelper;
import com.example.medicial.R;

public class RegisterActivity extends AppCompatActivity {
    DBHelper dbHelper = new DBHelper(this);
    EditText username, firstName, lastName, email, password, re_password;
    Button register;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        register.setOnClickListener(view -> Register());
    }

    private void init() {
        // {Full screen activity}
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // {Hook Id}
        username = findViewById(R.id.edt_reg_userName);
        firstName = findViewById(R.id.edt_reg_firstName);
        lastName = findViewById(R.id.edt_reg_lastName);
        email = findViewById(R.id.edt_reg_email);
        password = findViewById(R.id.edt_reg_password);
        re_password = findViewById(R.id.edt_reg_re_password);
        register = findViewById(R.id.btn_signUp);

        // {Dialog}
        dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
    }

    public void Register() {
        String _Username = username.getText().toString();
        String _Firstname = firstName.getText().toString();
        String _Lastname = lastName.getText().toString();
        String _Email = email.getText().toString();
        String _Password = password.getText().toString();
        String _Re_password = re_password.getText().toString();

        if (TextUtils.isEmpty(_Username)) {
            username.setHint("required field");
            username.setHintTextColor(getResources().getColor(R.color.red));
        }

        if (TextUtils.isEmpty(_Firstname)) {
            firstName.setHint("required field");
            firstName.setHintTextColor(getResources().getColor(R.color.red));
        }

        if (TextUtils.isEmpty(_Lastname)) {
            lastName.setHint("required field");
            lastName.setHintTextColor(getResources().getColor(R.color.red));
        }

        if (TextUtils.isEmpty(_Email)) {
            email.setHint("required field");
            email.setHintTextColor(getResources().getColor(R.color.red));
        }

        if (TextUtils.isEmpty(_Password)) {
            password.setHint("required field");
            password.setHintTextColor(getResources().getColor(R.color.red));
        }

        if (TextUtils.isEmpty(_Re_password)) {
            re_password.setHint("required field");
            re_password.setHintTextColor(getResources().getColor(R.color.red));
        }

        // Password verification mismatch
        if (!_Password.equals(_Re_password)) {
            Toast.makeText(this, "Password not matching", Toast.LENGTH_SHORT).show();
            return;
        }

        // Username already exists
        if (dbHelper.checkUserName(_Username)) {
            Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        // DB insertion failure
        if (!dbHelper.insertUserData(_Username, _Firstname, _Lastname, _Password, _Email)) {
            Toast.makeText(this, "REGISTRATION FAILED", Toast.LENGTH_SHORT).show();
            return;
        }

        ShowSuccessDialog();
    }

    private void ShowSuccessDialog() {
        dialog.setContentView(R.layout.dialog_success);
        Button done = dialog.findViewById(R.id.btn_done);

        done.setOnClickListener(view -> {
            dialog.dismiss();
            onBackPressed();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}