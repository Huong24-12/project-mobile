package com.huong.bpnhmnh.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarm(context);
        Log.d("__alone", "boot received");
    }

    private void setAlarm(Context context) {
        cancelAlarm(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        int flag = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flag = PendingIntent.FLAG_IMMUTABLE;

        }
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 111, intent, flag);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 30);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, alarmIntent);
    }

    private void cancelAlarm(Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        int flag = PendingIntent.FLAG_NO_CREATE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flag = PendingIntent.FLAG_IMMUTABLE;

        }
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 111, intent, flag);
        manager.cancel(alarmIntent);
    }
}
