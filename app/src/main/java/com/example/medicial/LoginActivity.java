package com.example.medicial;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    DBHelper dbHelper = new DBHelper(this);
    Button login, CreateAccount;
    EditText name, password;
    TextView txtv_inv;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //This code to show activity in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Call register activity after on click create account button
        CreateAccount = findViewById(R.id.btn_createAccount);
        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        name = findViewById(R.id.edt_login_name);
        password = findViewById(R.id.edt_login_password);
        login = findViewById(R.id.btn_login);
        txtv_inv = findViewById(R.id.invalid);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    public void Login(){
        String Name = name.getText().toString();
        String Password = password.getText().toString();

        if (TextUtils.isEmpty(Name)){
            name.setHint("Username field is empty");
            name.setHintTextColor(getResources().getColor(R.color.red));
        }

        if (TextUtils.isEmpty(Password)){
            password.setHint("Password field is empty");
            password.setHintTextColor(getResources().getColor(R.color.red));
        }

        else{
            boolean check_name_pass = dbHelper.CheckUserPassword(Name, Password);
            if (check_name_pass == true){
                Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }else{
                txtv_inv.setText("Invalid Credential");
                txtv_inv.setTextColor(getResources().getColor(R.color.red));
            }
        }
    }
}