package com.example.medicial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Button btn_Call_Register_Activity;
    Button btn_Call_Home_Activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //This code to show activity in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Call register activity after on click create account button
        btn_Call_Register_Activity = findViewById(R.id.btn_createAccount);
        btn_Call_Register_Activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call_Register_Activity(view);
            }

            public void Call_Register_Activity(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Call home activity after on click login button
        btn_Call_Home_Activity = findViewById(R.id.btn_login);
        btn_Call_Home_Activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call_Home_Activity(view);
            }

            public void Call_Home_Activity (View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}