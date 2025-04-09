package com.example.mywordle;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
        rescheduleNotification(context); // Перезапуск уведомления на следующий день
    }

    private void showNotification(Context context) {
        String channelId = "daily_notification_channel";
        String channelName = "Daily Notifications";
        int notificationId = 1;

        // Intent для открытия приложения
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Создаем канал уведомлений (для Android 8.0+)
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Канал для ежедневных уведомлений");
            channel.enableLights(true);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.game_win_sound);

        // Создаем уведомление
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "daily_notification_channel")
                //.setSmallIcon(R.drawable.ic_notification)  // Обязательно укажите маленькую иконку
                .setContentTitle("Напоминание")
                .setContentText("Пора сыграть в Wordly!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                //.setSound(soundUri)
                .setAutoCancel(true);
                //.setContentIntent(pendingIntent); // Уведомление откроет MainActivity при клике

        notificationManager.notify(notificationId, builder.build());
    }

    @SuppressLint("ScheduleExactAlarm")
    private void rescheduleNotification(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1); // Устанавливаем на следующий день
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 10);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }
}
