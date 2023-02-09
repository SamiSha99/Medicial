package com.example.medicial.activity;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicial.R;
import com.example.medicial.broadcast.AlarmReceiver;
import com.example.medicial.database.DBHelper;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class TakeActivity extends AppCompatActivity {
    TextView textView_timer, textView_chk;
    CircularSeekBar circularSeekBar;
    Button button;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take);

        textView_timer = findViewById(R.id.timer);
        textView_chk = findViewById(R.id.txtv_chk);
        circularSeekBar = findViewById(R.id.seekbar);
        button = findViewById(R.id.btn_take);

        dismissNotification();
        timer();
        button.setOnClickListener(v -> confirm());
    }

    private void timer() {
        new CountDownTimer(20000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                circularSeekBar.setProgress((int) (millisUntilFinished / 1000));
                textView_timer.setText("" + millisUntilFinished / 1000);
                textView_chk.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
            }

            public void onFinish() {
                textView_timer.setText("0");
                textView_chk.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void confirm() {
        dbHelper = new DBHelper(TakeActivity.this);
        SharedPreferences sp = getSharedPreferences("ID", Context.MODE_PRIVATE);
        int _MedId = sp.getInt("requestCode", 0);
        int _MedAmount = sp.getInt("amount", 0);

        // If the medicine is taken and confirmed the amount of medicine will be reduced
        if (_MedAmount == 0) {
            _MedAmount = 0;
        } else {
            _MedAmount -= 1;
        }
        dbHelper.reduceAmount(_MedId, _MedAmount);
        // Finish the activity
        finish();
    }

    private void dismissNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        StatusBarNotification[] activeNotifications = new StatusBarNotification[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activeNotifications = notificationManager.getActiveNotifications();
        }
        // Check if the notification group is empty
        if (activeNotifications.length == 0) {
            // The notification group is empty, dismiss group notifications
            notificationManager.cancel(AlarmReceiver.notificationSBId);

        } else if (activeNotifications.length == 1) {
            // if there is only one notification in the group, dismiss both
            notificationManager.cancel(AlarmReceiver.notificationId);
            notificationManager.cancel(AlarmReceiver.notificationSBId);

        } else {
            // The notification group is not empty, dismiss the specified notification
            notificationManager.cancel(AlarmReceiver.notificationId);
        }
    }
}