package com.example.medicial.Broadcast;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.medicial.Activity.ScheduleActivity;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get id and message from intent
        int notification_id = intent.getIntExtra("notification_id", 0);
        String message = intent.getStringExtra("medicine_name");

        Intent mainIntent = new Intent(context, ScheduleActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("Take your medicine")
                .setContentText("Hello, its time to take your medicine" + message)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent);

        notificationManager.notify(notification_id, builder.build());

    }
}