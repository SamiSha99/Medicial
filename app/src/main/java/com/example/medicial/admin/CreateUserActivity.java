package com.example.medicial.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medicial.R;
import com.example.medicial.database.DBHelper;

public class CreateUserActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    DBHelper dbHelper = new DBHelper(this);
    EditText username, firstName, lastName, email, password, re_password;
    Button _Create_user;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        init();
        _Create_user.setOnClickListener(view -> CreateUser());
    }

    private void init() {
        // {Toolbar}
        toolbar = findViewById(R.id.cr_toolbar);
        setSupportActionBar(toolbar);

        // {Setting up action bar}
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back));
        }

        // {Hook Id}
        username = findViewById(R.id.edt_cre_userName);
        firstName = findViewById(R.id.edt_cre_firstName);
        lastName = findViewById(R.id.edt_cre_lastName);
        email = findViewById(R.id.edt_cre_email);
        password = findViewById(R.id.edt_cre_password);
        re_password = findViewById(R.id.edt_cre_re_password);
        _Create_user = findViewById(R.id.btn_create_user);

        // {Dialog}
        dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
    }

    public void CreateUser() {
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
                username.requestFocus();
                username.setError(getResources().getString(R.string.username_exists));
                return;
            }

            // DB insertion failure
            if (!dbHelper.insertUserData(_Username, _Firstname, _Lastname, _Password, _Email)) {
                Toast.makeText(this, getResources().getString(R.string.create_failed), Toast.LENGTH_SHORT).show();
                return;
            }
            ShowSuccessDialog();
        }
    }

    private boolean validateInfo(String _Username, String _Firstname, String _Lastname, String _Email, String _Password, String _Re_password) {
        if (_Username.length() == 0) {
            username.requestFocus();
            username.setError(getResources().getString(R.string.empty));
            return false;

        } else if (_Username.length() < 6) {
            username.requestFocus();
            username.setError(getResources().getString(R.string.username_length));
            return false;

        } else if (!_Username.matches("^[a-zA-Z\\d]+$")) {
            username.requestFocus();
            username.setError(getResources().getString(R.string.username_inputType));
            return false;

        } else if (_Firstname.length() == 0) {
            firstName.requestFocus();
            firstName.setError(getResources().getString(R.string.empty));
            return false;

        } else if (!_Firstname.matches("[a-zA-Z]+")) {
            firstName.requestFocus();
            firstName.setError(getResources().getString(R.string.username_inputType));
            return false;

        } else if (_Lastname.length() == 0) {
            lastName.requestFocus();
            lastName.setError(getResources().getString(R.string.empty));
            return false;

        } else if (!_Lastname.matches("[a-zA-Z]+")) {
            lastName.requestFocus();
            lastName.setError(getResources().getString(R.string.username_inputType));
            return false;

        } else if (_Email.length() == 0) {
            email.requestFocus();
            email.setError(getResources().getString(R.string.empty));
            return false;

        } else if (!_Email.matches("^[a-zA-Z\\d._%+-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$")) {
            email.requestFocus();
            email.setError(getResources().getString(R.string.invalid_email));
            return false;

        } else if (_Password.length() == 0) {
            password.requestFocus();
            password.setError(getResources().getString(R.string.empty));
            return false;

        } else if (_Password.length() < 6) {
            password.requestFocus();
            password.setError(getResources().getString(R.string.password_length));
            return false;

        } else if (_Re_password.length() == 0) {
            re_password.requestFocus();
            re_password.setError(getResources().getString(R.string.empty));
            return false;

        } else if (!_Re_password.equals(_Password)) {
            re_password.requestFocus();
            re_password.setError(getResources().getString(R.string.password_notMatching));
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
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}