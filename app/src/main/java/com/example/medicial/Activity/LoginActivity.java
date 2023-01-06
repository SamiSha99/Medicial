package com.example.medicial.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    TextView txtv_incorrect, register_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // {Full screen activity}
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // {Go to activity register}
        register_now = findViewById(R.id.txtv_register_now);
        register_now.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });

        //  {Login}
        username = findViewById(R.id.edt_login_username);
        password = findViewById(R.id.edt_login_password);
        txtv_incorrect = findViewById(R.id.invalid);

        login = findViewById(R.id.btn_login);
        login.setOnClickListener(view -> Login());
    }

    public void Login() {
        String Username = username.getText().toString();
        String Password = password.getText().toString();
        boolean check_name_pass = dbHelper.CheckUserPassword(Username, Password);
        int color_red = ContextCompat.getColor(this, R.color.red);

        if (TextUtils.isEmpty(Username)) {
            username.setHint("Required field");
            username.setHintTextColor(color_red);
        }

        if (TextUtils.isEmpty(Password)) {
            password.setHint("Required field");
            password.setHintTextColor(color_red);
        }

        if(!check_name_pass) {
            txtv_incorrect.setText("Incorrect information");
            txtv_incorrect.setTextColor(color_red);
            return;
        }

        String getUsername = username.getText().toString();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("key", getUsername);
        startActivity(intent);
        finish();
    }
}