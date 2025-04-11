package com.example.mywordle;

import static com.example.mywordle.Notification.NotificationReceiver.CHANNEL_ID;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.mywordle.Notification.NotificationReceiver;
import com.example.mywordle.Notification.NotificationWorker;
import com.example.mywordle.data.repository.DatabaseHelper;
import com.example.mywordle.data.repository.WordsRepository;
import com.example.mywordle.databinding.ActivityMainBinding;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private ImageView settings_button_main;
    private ImageView home_button_main;
    private ImageView profile_button_main;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 101;

    // 0 - profile, 1 - home, 2 - settings
    private int frame = 1;
    private SharedPreferences preferences;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация SharedPreferences
        preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int isUserLoggedIn = preferences.getInt("userId", -1); // Проверка авторизации
        boolean isFirstRun = preferences.getBoolean("isFirstRun", true);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        WordsRepository wordsRepository = new WordsRepository(db);

        if (isFirstRun) {
            // Создаем канал уведомлений (создается один раз)
            createNotificationChannel();

            // Запрос на игнорирование оптимизаций батареи
            requestIgnoreBatteryOptimizations();

            // Импорт слов (однократно при первом запуске)
            wordsRepository.importWordsFromFile(this);

            // Сохраняем флаг первого запуска
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();
        }

        // Если пользователь не авторизован, переходим на RegistrationActivity
        if (isUserLoggedIn == -1) {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        getAllId();
        change(new FragmentMain());
        home_button_main.setScaleX(1.5f);
        home_button_main.setScaleY(1.5f);

        settings_button_main.setOnClickListener(v -> handleFragmentChange(v, new FragmentSettings(), 2));
        home_button_main.setOnClickListener(v -> handleFragmentChange(v, new FragmentMain(), 1));
        profile_button_main.setOnClickListener(v -> handleFragmentChange(v, new FragmentProfile(), 0));

        // Кнопка для тестового вызова уведомления
        Button testButton = findViewById(R.id.button);
        testButton.setOnClickListener(v -> {
            sendNotification();
        });

        // Планирование ежедневного уведомления через WorkManager
        scheduleDailyNotification();
    }

    private void handleFragmentChange(View v, Fragment fragment, int targetFrame) {
        Animation clickAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.button_click);
        v.startAnimation(clickAnim);
        clickAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (frame != targetFrame) {
                    resetIcons(home_button_main, 100);
                    resetIcons(profile_button_main, 100);
                    resetIcons(settings_button_main, 100);
                    animIcon(v);
                    change(fragment);
                    frame = targetFrame;
                } else {
                    resetIcons(home_button_main, 300);
                    resetIcons(profile_button_main, 300);
                    resetIcons(settings_button_main, 300);
                }
            }
        });
    }

    private void change(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

    private void getAllId() {
        settings_button_main = findViewById(R.id.settings_button_main);
        home_button_main = findViewById(R.id.home_button_main);
        profile_button_main = findViewById(R.id.profile_button_main);
    }

    private void resetIcons(View v, int duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.5f, 1f);
        scaleX.setDuration(duration);
        scaleY.setDuration(duration);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }

    private void animIcon(View v) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.5f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.5f);
        scaleX.setDuration(300);
        scaleY.setDuration(300);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }

    /**
     * Создает NotificationChannel для Android 8.0+
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Ежедневные уведомления",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Ежедневные напоминания для игры.");
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
            channel.enableLights(true);
            channel.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Запрашивает у пользователя отключение оптимизаций батареи для данного приложения.
     */
    private void requestIgnoreBatteryOptimizations() {
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }
    }

    /**
     * Планирует выполнение уведомления в 13:00 следующего дня с использованием WorkManager.
     */
    private void scheduleDailyNotification() {
        WorkManager workManager = WorkManager.getInstance(this);

        // Расчет времени до следующего уведомления в 13:00
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Если текущее время больше или равно 13:00, планируем уведомление на следующий день
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();

        WorkRequest dailyNotificationWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build();

        workManager.enqueue(dailyNotificationWorkRequest);
    }

    /**
     * Метод для отправки уведомления (используется для теста и может быть вызван и из Worker).
     */
    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_prog)
                .setContentTitle("Словесный вызов!")
                .setContentText("Пора сыграть в игру Wordly!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{0, 500, 200, 500});

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }
}
