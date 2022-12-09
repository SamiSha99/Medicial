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
    EditText f_name, l_name, email, password, confirm_password;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //This code to show activity in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        f_name = findViewById(R.id.firstName);
        l_name = findViewById(R.id.lasttName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.c_password);
        register = findViewById(R.id.btn_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    public void Register(){
        String F_Name = f_name.getText().toString();
        String L_Name = l_name.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String Confirm_password = confirm_password.getText().toString();

        // To check if fields are empty or not
        if (TextUtils.isEmpty(F_Name) || TextUtils.isEmpty(L_Name) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(Confirm_password)) {
            Toast.makeText(RegisterActivity.this, "All filed required", Toast.LENGTH_SHORT).show();
        }else {
            if (Password.equals(Confirm_password)){
                boolean check_user_name = dbHelper.CheckUserName(F_Name, L_Name);
                if (check_user_name == false){
                    boolean result = dbHelper.insertUserData(F_Name, L_Name, Email, Password);
                    if (result == true) {
                        Toast.makeText(RegisterActivity.this, "REGISTERED SUCCESSFULLY", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
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