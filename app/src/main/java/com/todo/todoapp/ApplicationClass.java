package com.todo.todoapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ApplicationClass extends Application {

    public static void backPressIntentAnim(Activity activity){
        activity.overridePendingTransition(R.anim.slide_from_left,
                R.anim.slide_to_right);
    }

    public static void enterIntentAnim(Activity activity){
        activity.overridePendingTransition(R.anim.slide_from_right,
                R.anim.slide_to_left);
    }

    public void setAlarm(long time, int ticks, String title, Context context) {
        String shortTitle = title.trim();
        if (shortTitle.length()>20){
            shortTitle = shortTitle.substring(0, 15);
        }
        Intent intent = new Intent(context.getApplicationContext(), MyReceiver.class);
        intent.putExtra("TITLE", shortTitle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), ticks, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
    }

    public void cancelAlarm(int ticks, String title, Context context) {
        String shortTitle = title.trim();
        if (shortTitle.length()>20){
            shortTitle = shortTitle.substring(0, 15);
        }
        Intent i = new Intent(context.getApplicationContext(), MyReceiver.class);
        i.putExtra("TITLE", shortTitle);
        PendingIntent pi = PendingIntent.getBroadcast(context.getApplicationContext(), ticks, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pi);
        }
    }

}
