package com.example.medicial.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.medicial.Database.DBHelper;
import com.example.medicial.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScheduleActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    EditText get_date, get_time;
    DBHelper dbHelper = new DBHelper(this);
    DatePickerDialog.OnDateSetListener dateSetListener;
    int get_hour, get_minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        init();

    }

    private void init() {
        // {Full screen activity}
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // {Toolbar}
        toolbar = findViewById(R.id.sch_toolbar);
        setSupportActionBar(toolbar);

        // {Setting up action bar}
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back));
        }

        // {Hook id}
        get_time = findViewById(R.id.edt_time);
        get_date = findViewById(R.id.edt_date);

        getTime();
        getDate();
    }

    private void getTime() {
        // {Get time}
        get_time.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(ScheduleActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    (timePicker, hourOfDay, minute) -> {
                        get_hour = hourOfDay;
                        get_minute = minute;

                        String time = get_hour + ":" + get_minute;
                        SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = f24Hours.parse(time);
                            SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                            if (date != null) {
                                get_time.setText(f12Hours.format(date));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }, 12, 0, false);
            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.updateTime(get_hour, get_minute);
            timePickerDialog.show();
        });
    }

    private void getDate() {
        // {Get date}
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        get_date.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ScheduleActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener,
                    year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });

        dateSetListener = (datePicker, y, m, d) -> {
            String date = d + "/" + (m + 1) + "/" + y;
            get_date.setText(date);
        };
    }

    public void AddNewReminder() {
        // get medicine data from reminder activity
        Bundle bundle = getIntent().getExtras();
        String receive_med_name = bundle.getString("key_medName");
        String receive_med_amount = bundle.getString("key_medAmount");
        String receive_med_image = bundle.getString("key_image");

        int amountInt = Integer.parseInt(receive_med_amount);
        String time = get_time.getText().toString();
        String date = get_date.getText().toString();

        int redColor = ContextCompat.getColor(this, R.color.red);

        if (time.isEmpty()) {
            get_time.setHint("required");
            get_time.setHintTextColor(redColor);
        }

        if (date.isEmpty()) {
            get_date.setHint("required");
            get_date.setHintTextColor(redColor);
        }

        // invalid inputs
        if (time.isEmpty() || date.isEmpty()) return;
        int newMedicineID = dbHelper.insertMedicineData(receive_med_name, amountInt, receive_med_image);
        if (newMedicineID != -1) {
            int newAlertID = dbHelper.insertDateTime(newMedicineID, time, date);
        }

        Intent intent = new Intent(ScheduleActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.date_schedule_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            AddNewReminder();
        }
        return super.onOptionsItemSelected(item);
    }
}