package com.example.medicial.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicial.Database.DBHelper;
import com.example.medicial.R;

public class LoginActivity extends AppCompatActivity {
    DBHelper dbHelper = new DBHelper(this);
    Button login;
    EditText username, password;
    TextView txtv_invalid, register_now;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        RegisterNow();
        rememberMe();
        login.setOnClickListener(view -> Login());
    }

    private void init() {
        // {Hook id}
        username = findViewById(R.id.edt_login_username);
        password = findViewById(R.id.edt_login_password);
        txtv_invalid = findViewById(R.id.txtv_invalid);
        login = findViewById(R.id.btn_login);
        register_now = findViewById(R.id.txtv_register_now);

        // {Remember me}
        checkBox = findViewById(R.id.checkBox);
        checkBox.setChecked(true);
    }

    private void RegisterNow() {
        register_now.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });
    }

    public void Login() {
        String _Username = username.getText().toString();
        String _Password = password.getText().toString();

        boolean check = validateInfo(_Username, _Password);
        if (check) {
            boolean check_name_pass = dbHelper.checkUserPassword(_Username, _Password);
            if (!check_name_pass) {
                txtv_invalid.setVisibility(View.VISIBLE);

            } else {
                if (checkBox.isChecked()) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username", username.getText().toString());
                    editor.putString("password", password.getText().toString());
                    editor.apply();
                } else {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.apply();
                }
                passUserName();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private boolean validateInfo(String _Username, String _Password) {
        if (_Username.length() == 0) {
            username.requestFocus();
            username.setError(getResources().getString(R.string.empty));
            return false;

        } else if (_Password.length() == 0) {
            password.requestFocus();
            password.setError(getResources().getString(R.string.empty));
            return false;

        } else {
            return true;
        }
    }

    public void passUserName() {
        SharedPreferences sharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE);
        String _StrUser = username.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key_user", _StrUser);
        editor.apply();
    }

    private void rememberMe() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String _Username = pref.getString("username", "");
        String _Password = pref.getString("password", "");
        username.setText(_Username);
        password.setText(_Password);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage("Are you sure to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> finish())
                .setNegativeButton("No", null)
                .show();
    }
}