package com.example.medicial.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.medicial.R;
import com.example.medicial.broadcast.AlarmReceiver;
import com.example.medicial.database.DBHelper;
import com.example.medicial.model.Data;
import com.example.medicial.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    CheckBox checkBox_Once, checkBox_Daily;

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

        checkBox_Once = findViewById(R.id.checkBox_once);
        checkBox_Daily = findViewById(R.id.checkBox_daily);

        // {Call time picker & date picker}
        getTime();
        getDate();

        // {Alarm Receiver}
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.createNotificationChannel(this);
    }

    public void AddNewReminder() {
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
            if (newMedicineID != -1) {
                dbHelper.insertDateTime(newMedicineID, _Time, _Date);
            }
            setAlarm();
            Intent intent = new Intent(ScheduleActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getTime() {
        Calendar calendar = Calendar.getInstance();
        _Hour = calendar.get(Calendar.HOUR_OF_DAY);
        _Minute = calendar.get(Calendar.MINUTE);

        imgBtnTime.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(ScheduleActivity.this, R.style.DateTimePickerTheme, (timePicker, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                _Time.setText(sdf.format(calendar.getTime()));
                _Hour = hourOfDay;
                _Minute = minute;
            }, _Hour, _Minute, false);
            timePickerDialog.show();
        });
    }

    private void getDate() {
        Calendar calendar = Calendar.getInstance();
        _Year = calendar.get(Calendar.YEAR);
        _Month = calendar.get(Calendar.MONTH);
        _Day = calendar.get(Calendar.DAY_OF_MONTH);

        imgBtnDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleActivity.this, R.style.DateTimePickerTheme, (datePicker, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = dateFormat.format(calendar.getTime());
                _Date.setText(formattedDate);
                _Year = year;
                _Month = month;
                _Day = dayOfMonth;
            }, _Year, _Month, _Day);
            datePickerDialog.show();
        });
    }

    public void setAlarm() {
        ArrayList<Data> _Data = dbHelper.getReminderData();
        ArrayList<User> _User = dbHelper.getUserData();
        Calendar calendar = Calendar.getInstance(), calendarDate = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        Date time;
        String date;
        int requestCode = 0, amount = 0;

        // to get user name and pass it.
        User user = null;
        for (int j = 0; j < _User.size(); j++) {
            user = _User.get(j);
        }

        for (int i = 0; i < _Data.size(); i++) {
            Data data = _Data.get(i);
            requestCode = _Data.get(i).get_Med_Id();
            amount = _Data.get(i).get_Med_Amount();

            try {
                date = data.get_Date();
                time = tf.parse(data.get_Time());

                if (time != null) {
                    calendarDate.setTime(time);
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR, calendarDate.get(Calendar.HOUR));
                    calendar.set(Calendar.MINUTE, calendarDate.get(Calendar.MINUTE));
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                }
                if (date != null) {
                    String[] dateArray = date.split("-");
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[0]));
                    calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1); // month Jan is represented by the value "0", month Feb --> 1 { dateArray[1] = 2 - 1 }
                    calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[2]));
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        // to get medicine name and pass it.
        Bundle bundle = getIntent().getExtras();
        String receive_medName = bundle.getString("key_medName");

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("key_medName", receive_medName);
        if (user != null) {
            intent.putExtra("key_userName", user.getUsername());
        }

        @SuppressLint("InlinedApi")
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (checkBox_Once.isChecked()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Reminder is set for once time", Toast.LENGTH_SHORT).show();

        } else if (checkBox_Daily.isChecked()) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            Toast.makeText(this, "Reminder is set for daily time", Toast.LENGTH_SHORT).show();

        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Reminder is set for once time", Toast.LENGTH_SHORT).show();
        }

        // Sends the current notification Med_Id and Med_Amount to TakeActivity
        SharedPreferences sp = getSharedPreferences("ID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("requestCode", requestCode);
        editor.putInt("amount", amount);
        editor.apply();
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
        getMenuInflater().inflate(R.menu.schedule_menu, menu);
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