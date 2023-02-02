package com.example.medicial.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.medicial.R;
import com.example.medicial.database.DBHelper;

public class UpdateUserActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    EditText id, username, firstName, lastName, email, password, re_password;
    Button _Update_user;
    DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        init();
        _Update_user.setOnClickListener(v -> UpdateUser());
    }

    private void init() {
        // {Toolbar}
        toolbar = findViewById(R.id.up_toolbar);
        setSupportActionBar(toolbar);

        // {Setting up action bar}
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back));
        }

        // {Hook Id}
        id = findViewById(R.id.edt_up_userId);
        username = findViewById(R.id.edt_up_userName);
        firstName = findViewById(R.id.edt_up_firstName);
        lastName = findViewById(R.id.edt_up_lastName);
        email = findViewById(R.id.edt_up_email);
        password = findViewById(R.id.edt_up_password);
        re_password = findViewById(R.id.edt_up_re_password);
        _Update_user = findViewById(R.id.btn_update_user);
    }

    private void UpdateUser() {
        String _Str_Id = id.getText().toString();
        int _Id = Integer.parseInt(_Str_Id);
        String _Username = username.getText().toString();
        String _Firstname = firstName.getText().toString();
        String _Lastname = lastName.getText().toString();
        String _Email = email.getText().toString();
        String _Password = password.getText().toString();
        String _Re_password = re_password.getText().toString();

        boolean check = validateInfo(_Str_Id, _Username, _Firstname, _Lastname, _Email, _Password, _Re_password);
        if (check) {
            boolean update = dbHelper.updateUser(_Id, _Username, _Firstname, _Lastname, _Password, _Email);
            if (!update) {
                id.requestFocus();
                id.setError("wrong id");
            } else {
                dbHelper.updateUser(_Id, _Username, _Firstname, _Lastname, _Password, _Email);
                Toast.makeText(UpdateUserActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInfo(String _Str_Id, String _Username, String _Firstname, String _Lastname, String _Email, String _Password, String _Re_password) {
        if (_Str_Id.length() == 0) {
            id.requestFocus();
            id.setError(getResources().getString(R.string.empty));
            return false;

        } else if (_Username.length() == 0) {
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