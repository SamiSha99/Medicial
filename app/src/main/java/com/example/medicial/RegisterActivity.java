package com.example.medicial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    DBHelper dbHelper = new DBHelper(this);
    EditText name, email, password, re_password;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //This code to show activity in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        name = findViewById(R.id.edt_reg_firstName);
        email = findViewById(R.id.edt_reg_email);
        password = findViewById(R.id.edt_reg_password);
        re_password = findViewById(R.id.edt_reg_repassword);
        register = findViewById(R.id.btn_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    public void Register(){
        String Name = name.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String Re_password = re_password.getText().toString();

        // To check if fields are empty or not
        if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(Re_password)) {
            Toast.makeText(RegisterActivity.this, "All filed required", Toast.LENGTH_SHORT).show();
        }else {
            if (Password.equals(Re_password)){
                boolean check_user_name = dbHelper.CheckUserName(Name);
                if (check_user_name == false){
                    boolean insert_User_Data = dbHelper.insertUserData(Name, Password, Email);
                    if (insert_User_Data == true) {
                        Toast.makeText(RegisterActivity.this, "REGISTERED SUCCESSFULLY", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(RegisterActivity.this, "REGISTRATION FAILED", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RegisterActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegisterActivity.this, "Password not matching", Toast.LENGTH_SHORT).show();
            }
        }
    }
}