package com.coistem.stemdiary;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.coistem.stemdiary.activities.LoginActivity;


public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private static final int NOTIFY_ID = 101;
    private NotificationManager nm;
    // Идентификатор канала
    public static String CHANNEL_ID = "Cat channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        //посылаем уведомление здесь
        nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context, LoginActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 01234, intent1, 0);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.stem_logo)
                        .setContentTitle("Скоро урок!")
                        .setContentText("Напоминаем, что у вас урок скоро. Не забудьте придти!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pi)
                        .setAutoCancel(true);
        nm.notify(NOTIFY_ID, builder.build());
        context.stopService(new Intent(context, NextLessonService.class));
    }
}
