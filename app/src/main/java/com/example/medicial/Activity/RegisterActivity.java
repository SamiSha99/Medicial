package com.example.medicial.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

        boolean check = validateInfo(_Username, _Firstname, _Lastname, _Email, _Password, _Re_password);
        if (check) {
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
    }

    private boolean validateInfo(String _Username, String _Firstname, String _Lastname, String _Email, String _Password, String _Re_password) {
        if (_Username.length() == 0) {
            username.requestFocus();
            username.setError("Field cannot be empty");
            return false;

        } else if (_Username.length() < 6) {
            username.requestFocus();
            username.setError("Minimum 6 characters required");
            return false;

        } else if (!_Username.matches("^[a-zA-Z0-9]+$")) {
            username.requestFocus();
            username.setError("Enter only letters and numbers");
            return false;

        } else if (_Firstname.length() == 0) {
            firstName.requestFocus();
            firstName.setError("Field cannot be empty");
            return false;

        } else if (!_Firstname.matches("[a-zA-Z]+")) {
            firstName.requestFocus();
            firstName.setError("Enter only letters");
            return false;

        } else if (_Lastname.length() == 0) {
            lastName.requestFocus();
            lastName.setError("Field cannot be empty");
            return false;

        } else if (!_Lastname.matches("[a-zA-Z]+")) {
            lastName.requestFocus();
            lastName.setError("Enter only letters");
            return false;

        } else if (_Email.length() == 0) {
            email.requestFocus();
            email.setError("Field cannot be empty");
            return false;

        } else if (!_Email.matches("[a-zA-Z0-9]+@[a-z]+\\.[a-z]+")) {
            email.requestFocus();
            email.setError("Enter valid email address");
            return false;

        } else if (_Password.length() == 0) {
            password.requestFocus();
            password.setError("Field cannot be empty");
            return false;

        } else if (_Password.length() < 6) {
            password.requestFocus();
            password.setError("Minimum 6 characters required");
            return false;

        } else if (_Re_password.length() == 0) {
            re_password.requestFocus();
            re_password.setError("Field cannot be empty");
            return false;

        } else if (!_Re_password.equals(_Password)) {
            re_password.requestFocus();
            re_password.setError("Password does not match");
            return false;

        } else {
            return true;
        }
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