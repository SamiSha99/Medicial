package com.example.medicial.broadcast;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.medicial.R;
import com.example.medicial.activity.TakeActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "channel_id_1"; // {Every channel need unique id}
    private static final String Title = "Take your medicine";
    public static int notificationId = 0;
    public static int notificationSBId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        // receiving the data sent from ScheduleActivity
        Bundle bundle = intent.getExtras();
        String _Med_Name = bundle.getString("key_medName");
        String _User_Name = bundle.getString("key_userName");
        String Message = "Hello " + _User_Name + ", its time to take your medicine " + _Med_Name;
        String groupKey = "group_key_notifications_" + _User_Name;
        notificationId = (int) (System.currentTimeMillis() & 0xfffffff) + _User_Name.hashCode();

        Intent _TakeIntent = new Intent(context, TakeActivity.class);
        _TakeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent _TakePending = PendingIntent.getActivity(context, 0, _TakeIntent, 0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notification)
                .setContentTitle(Title)
                .setContentText(Message)
                .setGroup(groupKey)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .addAction(0, "Take", _TakePending)
                .setAutoCancel(true);

        // To set the notification in groups
        NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notification)
                .setGroup(groupKey)
                .setGroupSummary(true)
                .setWhen(System.currentTimeMillis());

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
        notificationManager.notify(notificationSBId, summaryBuilder.build());
    }

    public void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarm";
            String description = "Alarm Notification";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

