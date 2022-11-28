package com.example.medicial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Button btn_Call_Register_Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
    }
}