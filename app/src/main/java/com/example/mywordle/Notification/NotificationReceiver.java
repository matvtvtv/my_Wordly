package com.example.mywordle.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.mywordle.R;

public class NotificationReceiver extends BroadcastReceiver {

    // Константа для канала уведомлений
    public static final String CHANNEL_ID = "daily_notification_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Получаем NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Создание канала уведомлений для Android 8.0 и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Получаем стандартный системный звук для уведомлений
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // Создаем канал уведомлений с включённым звуком
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Ежедневные уведомления",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Ежедневное уведомление для игры.");
            channel.setSound(soundUri, null); // Устанавливаем стандартный звук уведомлений

            // Если канал уже существует, ничего не делаем, иначе создаём новый канал
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Создаем уведомление
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_prog) // Указываем свою иконку
                .setContentTitle("Словесный вызов!")
                .setContentText("Пора сыграть в игру Wordly!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) // Используем стандартный звук
                .setVibrate(new long[]{0, 500, 200, 500}); // Вибрация

        // Отправляем уведомление
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }
}
