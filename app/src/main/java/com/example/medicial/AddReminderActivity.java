package com.example.medicial;

import android.os.Bundle;
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
    EditText med_name, med_amount;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

//        {Full screen activity}
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        {Toolbar}
        toolbar = findViewById(R.id.rem_toolbar);
        setSupportActionBar(toolbar);

//        {Setting up action bar}
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_arrow_back));

//        {Add medicine}
        med_name = findViewById(R.id.edt_medicineName);
        med_amount = findViewById(R.id.edt_amount);
        save = findViewById(R.id.btn_upload);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMedicine();
            }
        });
    }

    public void addMedicine(){
        DBHelper dbHelper = new DBHelper(AddReminderActivity.this);

        String medicine = med_name.getText().toString().trim();
        Integer amount =  Integer.valueOf(med_amount.getText().toString().trim());

        if (TextUtils.isEmpty(medicine) || (amount == null)){
            med_name.setHint("required field");
            med_name.setHintTextColor(getResources().getColor(R.color.red));
            med_amount.setHint("required field");
            med_amount.setHintTextColor(getResources().getColor(R.color.red));
        }
        else{
            dbHelper.insertMedicineData(medicine, amount);
        }
    }
}
