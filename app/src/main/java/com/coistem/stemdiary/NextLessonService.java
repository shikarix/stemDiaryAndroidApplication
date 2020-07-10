package com.coistem.stemdiary;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.coistem.stemdiary.activities.LoginActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NextLessonService extends Service {

    public static PendingIntent pendingIntent = null;

    private void serviceMessageStart(String[] dates) {
        for (int i = 0; i < dates.length; i++) {
            String date = dates[i];
            try {
                Date parsedDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(date);
                parsedDate.setMonth(parsedDate.getMonth());
                System.out.println(i+" date: "+parsedDate.getDate()+"."+(parsedDate.getMonth())+" "+parsedDate.getHours()+":"+parsedDate.getMinutes());
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
//                calendar.add(Calendar.SECOND, 10);
                calendar.set(Calendar.MONTH, 7);
                calendar.set(Calendar.DAY_OF_MONTH, 12);
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                long time = calendar.getTimeInMillis();
                System.out.println(parsedDate.getTime()/1000);
                Intent alarmIntent = new Intent(NextLessonService.this, AlarmBroadcastReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(NextLessonService.this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                if (pendingIntent != null) {
                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    manager.set(AlarmManager.RTC_WAKEUP, parsedDate.getTime(), pendingIntent);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            break;
        }

    }
    public NextLessonService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Служба создана!", Toast.LENGTH_SHORT).show();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceMessageStart(intent.getStringArrayExtra("date"));
        Toast.makeText(NextLessonService.this, "Ура! Пашет!", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "Служба остановлена!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
