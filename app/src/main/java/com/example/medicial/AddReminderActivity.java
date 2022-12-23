package com.example.medicial;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AddReminderActivity extends AppCompatActivity{

    Toolbar toolbar;
    ActionBar actionBar;
    DBHelper dbHelper = new DBHelper(this);
    EditText medName, amount;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        // This code to show activity in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // finding UI widget
        toolbar = findViewById(R.id.rem_toolbar);
        setSupportActionBar(toolbar);

        // setting up action bar
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_arrow_back));

        medName = findViewById(R.id.edt_medicineName);
        amount = findViewById(R.id.edt_amount);
        save = findViewById(R.id.btn_upload);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMedicine();
            }
        });
    }

    public void addMedicine(){
        String med_name = medName.getText().toString();
        String med_amount = amount.getText().toString();

        if (TextUtils.isEmpty(med_name) || (TextUtils.isEmpty(med_amount))){
            medName.setHint("required field");
            medName.setHintTextColor(getResources().getColor(R.color.red));
            amount.setHint("required field");
            amount.setHintTextColor(getResources().getColor(R.color.red));
        }
        else{
            boolean insert = dbHelper.insertMedicineData(med_name, med_amount);
            if (insert){
                Toast.makeText(AddReminderActivity.this, "ADDED", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(AddReminderActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
