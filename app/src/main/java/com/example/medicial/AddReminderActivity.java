package com.example.medicial;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddReminderActivity extends AppCompatActivity{

    Toolbar toolbar;
    ActionBar actionBar;
    EditText med_name, med_amount;
    TextInputLayout inputLayoutMed_name, inputLayoutMed_amount;
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


        med_name = findViewById(R.id.edt_medicineName);
        med_amount = findViewById(R.id.edt_amount);

        inputLayoutMed_name = findViewById(R.id.til_medicineName);
        inputLayoutMed_amount = findViewById(R.id.til_amount);
    }

    public void addMedicine(){
        DBHelper dbHelper = new DBHelper(AddReminderActivity.this);
        String medicine = med_name.getText().toString();
        String chk_amount = med_amount.getText().toString();
        Integer amount =  Integer.valueOf(med_amount.getText().toString().trim());

        boolean isValid = true;

        if (TextUtils.isEmpty(medicine)){
            inputLayoutMed_name.setError("required");
            inputLayoutMed_name.setBoxStrokeColor(getResources().getColor(R.color.red));
            isValid = false;
        }
        else{
            inputLayoutMed_name.setErrorEnabled(false);
        }
        if (TextUtils.isEmpty(chk_amount)){
            inputLayoutMed_amount.setError("required");
            inputLayoutMed_amount.setBoxStrokeColor(getResources().getColor(R.color.red));
        }else{
            inputLayoutMed_amount.setErrorEnabled(false);
        }

        if(isValid){
            dbHelper.insertMedicineData(medicine, amount);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reminder_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                    addMedicine();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
