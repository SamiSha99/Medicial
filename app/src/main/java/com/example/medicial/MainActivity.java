package com.example.medicial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn_Call_Login_Activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Call_Login_Activity = findViewById(R.id.btn_start);
        btn_Call_Login_Activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call_Login_Activity(view);
            }

            public  void Call_Login_Activity(View view){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }

        });
    }


}