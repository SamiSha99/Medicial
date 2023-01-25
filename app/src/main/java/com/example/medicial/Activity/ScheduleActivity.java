package com.example.medicial.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.medicial.Broadcast.AlarmReceiver;
import com.example.medicial.Database.DBHelper;
import com.example.medicial.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    TextView _Date, _Time;
    ImageButton imgBtnDate, imgBtnTime;
    DBHelper dbHelper = new DBHelper(this);
    private int _Year, _Month, _Day, _Hour, _Minute;
    private AlarmReceiver alarmReceiver;
    String _StrTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        init();
    }

    private void init() {
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
        imgBtnDate = findViewById(R.id.img_btn_date);
        imgBtnTime = findViewById(R.id.img_btn_time);
        _Time = findViewById(R.id.txtv_set_time);
        _Date = findViewById(R.id.txtv_set_date);

        alarmReceiver = new AlarmReceiver();
        // {Call time picker & date picker}
        getTime();
        getDate();
    }

    private void getTime() {
        Calendar calendar = Calendar.getInstance();
        _Hour = calendar.get(Calendar.HOUR_OF_DAY);
        _Minute = calendar.get(Calendar.MINUTE);

        imgBtnTime.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(ScheduleActivity.this, R.style.DateTimePickerTheme, (timePicker, hourOfDay, minute) -> {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                _Time.setText(sdf.format(new Date(0, 0, 0, hourOfDay, minute)));
                _StrTime = String.format("%02d:%02d", hourOfDay, minute); // format time for alarm
                _Hour = hourOfDay;
                _Minute = minute;
            }, _Hour, _Minute, false);
            timePickerDialog.show();
        });
    }

    // {Get date}
    private void getDate() {
        Calendar calendar = Calendar.getInstance();
        _Year = calendar.get(Calendar.YEAR);
        _Month = calendar.get(Calendar.MONTH);
        _Day = calendar.get(Calendar.DAY_OF_MONTH);

        imgBtnDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleActivity.this, R.style.DateTimePickerTheme, (datePicker, year, month, dayOfMonth) -> {
                _Date.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
                _Year = year;
                _Month = month;
                _Day = dayOfMonth;
            }, _Year, _Month, _Day);
            datePickerDialog.show();
        });
    }

    public void setAlarm() {
        Bundle bundle = getIntent().getExtras();
        String receive_medName = bundle.getString("key_medName");
        String message = "Hello, its time to take your medicine " + receive_medName;
        alarmReceiver.setOneTimeAlarm(ScheduleActivity.this, AlarmReceiver.TYPE_ONE_TIME,
                _Date.getText().toString(),
                _StrTime,
                message);
    }

    public void AddNewReminder() {
        // get medicine data from reminder activity
        Bundle bundle = getIntent().getExtras();
        String receive_medName = bundle.getString("key_medName");
        String receive_medAmount = bundle.getString("key_medAmount");
        String receive_medDesc = bundle.getString("key_medDesc");
        String receive_medImage = bundle.getString("key_medImage");

        int amountInt = Integer.parseInt(receive_medAmount);
        String _Date = this._Date.getText().toString();
        String _Time = this._Time.getText().toString();

        if (_Date.length() == 0) {
            this._Date.requestFocus();
            this._Date.setError(getResources().getString(R.string.empty));

        } else if (_Time.length() == 0) {
            this._Time.requestFocus();
            this._Time.setError(getResources().getString(R.string.empty));

        } else {
            int newMedicineID = dbHelper.insertMedicineData(receive_medName, amountInt, receive_medDesc, receive_medImage);
            setAlarm();
            if (newMedicineID != -1) {
                int newAlertID = dbHelper.insertDateTime(newMedicineID, _Time, _Date);  // newAlertID it not used!!
            }
            Intent intent = new Intent(ScheduleActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
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