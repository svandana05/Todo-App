package com.example.todoapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

public class MyReceiver extends BroadcastReceiver {

    private static final int DAILY_REMINDER_REQUEST_CODE = 1;


    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("TITLE");
        long todoId = intent.getLongExtra("TODO_ID", 0);
        showNotification(context,CreateTodoActivity.class,"Todo task reminder", title, todoId);
    }

    public static void showNotification(Context context,Class<?> cls,String title, String content, long todoId) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.putExtra("TODO_ID", todoId);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.notification_channel))
                .setContentTitle(title)
                .setSound(alarmSound).setSmallIcon(R.mipmap.ic_launcher_round)
                .setSmallIcon(androidx.core.R.drawable.notification_icon_background)
                .setAutoCancel(true)
                .setContentText(content)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(DAILY_REMINDER_REQUEST_CODE, builder.build());
//
//        Notification notification = builder.setContentTitle(title)
//                .setContentText(content).setAutoCancel(true)
//                .setSound(alarmSound).setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentIntent(pendingIntent).build();
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification);

    }
}
