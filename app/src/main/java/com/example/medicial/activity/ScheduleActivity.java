package com.example.medicial.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.medicial.broadcast.AlarmReceiver;
import com.example.medicial.database.DBHelper;
import com.example.medicial.model.Data;
import com.example.medicial.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ScheduleActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    TextView _Date, _Time, _Repeat;
    ImageButton imgBtnDate, imgBtnTime;
    DBHelper dbHelper = new DBHelper(this);
    private int _Year, _Month, _Day, _Hour, _Minute;

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
        _Repeat = findViewById(R.id.txtv_set_repeat);

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
            //setAlarm();
            setAlarm2(_Date, _Time);
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
        Calendar calendar = Calendar.getInstance();
        Calendar calendarDate = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        Date date, time;
        for (int i = 0; i < _Data.size(); i++) {
            Data data = _Data.get(i);
            try {
                date = df.parse(data.get_Date());
                time = tf.parse(data.get_Time());
                // Apply the specified time
                if(time != null) {
                    calendarDate.setTime(time);
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, calendarDate.get(Calendar.HOUR_OF_DAY));
                    calendar.set(Calendar.MINUTE, calendarDate.get(Calendar.MINUTE));
                    calendar.set(Calendar.SECOND, 0);
                    // Date + Specified Hour
                    now.set(Calendar.HOUR_OF_DAY, calendarDate.get(Calendar.HOUR_OF_DAY));
                    now.set(Calendar.MINUTE, calendarDate.get(Calendar.MINUTE));
                    now.set(Calendar.SECOND, 0);
                }
                // Apply as today
                if(date != null) {
                    calendarDate.setTime(now.getTime());
                    calendar.set(Calendar.DAY_OF_MONTH, calendarDate.get(Calendar.DAY_OF_MONTH));
                    calendar.set(Calendar.MONTH, calendarDate.get(Calendar.MONTH));
                    calendar.set(Calendar.YEAR, calendarDate.get(Calendar.YEAR));
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            // Repeat the alarm on weekly (day * 7)
            // Repeat every 2 days? (day * 2)
            //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pendingIntent), pendingIntent);
        }
        Toast.makeText(ScheduleActivity.this, "Reminder set!", Toast.LENGTH_SHORT).show();
    }

    // Redone again for clarity
    // Note: Andriod will always clump up alarms that are "close" to each other or activate alarms that will eventually happen earlier
    // This is a design to help with performance and saving battery
    public void setAlarm2(String date, String time) {
        Calendar currentDate = Calendar.getInstance(TimeZone.getDefault());
        Calendar alarmTime = Calendar.getInstance(TimeZone.getDefault());
        Date dDate, dTime;
        // Parse dates
        try {
            dDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(date);
            dTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(time);
        }
        catch (ParseException e) { throw new RuntimeException(e); }
        Calendar calendarDate = Calendar.getInstance(TimeZone.getDefault());
        Calendar calendarTime = Calendar.getInstance(TimeZone.getDefault());
        calendarDate.setTime(dDate);
        calendarTime.setTime(dTime);
        // Adjust to specified alarm time ("date" already specified via "Calendar.getInstance();")
        currentDate.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
        currentDate.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        // Set up Alarm Date
        alarmTime.setTimeZone(TimeZone.getDefault());
        alarmTime.set(calendarDate.get(Calendar.YEAR), calendarDate.get(Calendar.MONTH), calendarDate.get(Calendar.DAY_OF_MONTH));
        alarmTime.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
        alarmTime.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
        alarmTime.set(Calendar.SECOND, 0);
        alarmTime.set(Calendar.MILLISECOND, 0);
        // If its in the past, adjust to the future
        // Push it to next day
        if(currentDate.getTimeInMillis() > alarmTime.getTimeInMillis()) {
            alarmTime.set(Calendar.DATE, currentDate.get(Calendar.DATE));
            alarmTime.add(Calendar.DATE, 1);
        }
        System.out.println("Alarm Set @ => " + alarmTime.getTime());
        System.out.println("CurrentDate Set @ => " + currentDate.getTime());
        // Compare if alarm is in the past
        //if(alarmTime.getTimeInMillis() < currentDate.getTimeInMillis()
        // Set up Alarm Time
        Intent intent = new Intent(this, AlarmReceiver.class);
        // requestCode should be unique, so it will be based on size (from 1 to infinity)
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,  0, intent, PendingIntent.FLAG_MUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), 1000 * 5, pendingIntent);
        AlarmManager.AlarmClockInfo aci = new AlarmManager.AlarmClockInfo(alarmTime.getTimeInMillis(), pendingIntent);
        alarmManager.setAlarmClock(aci, pendingIntent);
        Date d = new Date(alarmManager.getNextAlarmClock().getTriggerTime());
        Calendar a = Calendar.getInstance();
        a.setTime(d);
        System.out.println("Next Trigger Date => " + a.getTime());
        Toast.makeText(ScheduleActivity.this, "Reminder set!", Toast.LENGTH_SHORT).show();
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