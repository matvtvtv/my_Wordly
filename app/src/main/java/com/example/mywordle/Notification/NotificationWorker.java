package com.example.mywordle.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.mywordle.R;

public class NotificationWorker extends Worker {

    private static final String CHANNEL_ID = "daily_notification_channel";

    public NotificationWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public Result doWork() {
        // Создаем NotificationManager
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Для Android 8.0 и выше создаем канал уведомлений
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // Создаём канал уведомлений
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Ежедневные уведомления",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Ежедневные напоминания для игры.");
            channel.setSound(soundUri, null);  // Стандартный звук
            channel.enableLights(true);        // Включение мигания
            channel.enableVibration(true);     // Включение вибрации
            notificationManager.createNotificationChannel(channel);
        }

        // Создаем уведомление
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_prog)  // Иконка уведомления
                .setContentTitle("Словесный вызов!")
                .setContentText("Пора сыграть в игру Wordly!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)  // Высокий приоритет
                .setAutoCancel(true)  // Отключение уведомления при нажатии
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) // Стандартный звук
                .setVibrate(new long[]{0, 500, 200, 500});  // Вибрация

        // Отправляем уведомление
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }

        return Result.success();
    }
}
