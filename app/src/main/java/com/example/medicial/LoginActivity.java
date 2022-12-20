package com.example.medicial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    DBHelper dbHelper = new DBHelper(this);
    Button login, CreateAccount;
    EditText username, password;
    TextView txtv_incorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //This code to show activity in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Call register activity after on click create account button
        CreateAccount = findViewById(R.id.btn_createAccount);
        CreateAccount.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });

        username = findViewById(R.id.edt_login_username);
        password = findViewById(R.id.edt_login_password);
        login = findViewById(R.id.btn_login);
        txtv_incorrect = findViewById(R.id.invalid);

        login.setOnClickListener(view -> Login());
    }

    public void Login(){
        String Username = username.getText().toString();
        String Password = password.getText().toString();

        if (TextUtils.isEmpty(Username)){
            username.setHint("Username is required");
            username.setHintTextColor(getResources().getColor(R.color.red));
        }

        if (TextUtils.isEmpty(Password)){
            password.setHint("Password is required");
            password.setHintTextColor(getResources().getColor(R.color.red));
        }

        else{
            boolean check_name_pass = dbHelper.CheckUserPassword(Username, Password);
            if (check_name_pass){
                //Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }else{
                txtv_incorrect.setText("Incorrect information");
                txtv_incorrect.setTextColor(getResources().getColor(R.color.red));
            }
        }
    }
}