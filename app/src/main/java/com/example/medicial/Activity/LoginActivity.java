package com.example.medicial.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.medicial.Database.DBHelper;
import com.example.medicial.R;

public class LoginActivity extends AppCompatActivity {
    DBHelper dbHelper = new DBHelper(this);
    Button login;
    EditText username, password;
    TextView txtv_invalid, register_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        RegisterNow();

        login.setOnClickListener(view -> Login());
    }

    private void init() {
        // {Full screen activity}
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //  {Hook id}
        username = findViewById(R.id.edt_login_username);
        password = findViewById(R.id.edt_login_password);
        txtv_invalid = findViewById(R.id.txtv_invalid);
        login = findViewById(R.id.btn_login);
        register_now = findViewById(R.id.txtv_register_now);
    }

    private void RegisterNow() {
        register_now.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });
    }

    public void Login() {
        String Username = username.getText().toString();
        String Password = password.getText().toString();
        boolean check_name_pass = dbHelper.checkUserPassword(Username, Password);
        int color_red = ContextCompat.getColor(this, R.color.red);

        if (TextUtils.isEmpty(Username)) {
            username.setHint("Required field");
            username.setHintTextColor(color_red);
        }

        if (TextUtils.isEmpty(Password)) {
            password.setHint("Required field");
            password.setHintTextColor(color_red);
        }

        if (!check_name_pass) {
            txtv_invalid.setVisibility(View.VISIBLE);
            return;
        }

        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
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