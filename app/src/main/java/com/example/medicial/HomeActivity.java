package com.example.medicial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button btn_Call_AddMedicine_Activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //This code to show activity in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_Call_AddMedicine_Activity = findViewById(R.id.btn_addMedicine);
        btn_Call_AddMedicine_Activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call_AddMedicine_Activity(view);
            }

            public void Call_AddMedicine_Activity(View view){
                Intent intent = new Intent(getApplicationContext(), AddMedicineActivity.class);
                startActivity(intent);
            }
        });
    }
}