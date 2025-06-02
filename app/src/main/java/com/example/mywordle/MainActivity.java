package com.example.mywordle;

import static com.example.mywordle.Notification.NotificationReceiver.CHANNEL_ID;

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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.mywordle.Adapter.ViewPagerAdapter;
import com.example.mywordle.Notification.NotificationWorker;
import com.example.mywordle.data.model.PlayerSettingsModel;
import com.example.mywordle.data.repository.DatabaseHelper;
import com.example.mywordle.data.repository.PlayerSettingsRepository;
import com.example.mywordle.data.repository.WordsRepository;
import com.example.mywordle.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 101;



    private SharedPreferences preferences;
    private PlayerSettingsRepository playerSettingsRepository;
    private BottomNavigationView bottomNavigationView;
    private int currentSelectedItemId=-1;
    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;

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


            // Импорт слов (однократно при первом запуске)
            wordsRepository.importWordsFromFile(this);
            requestNotificationPermission();
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
        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(true);
        viewPager.setOffscreenPageLimit(2);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == currentSelectedItemId) {
                return false;
            }
            currentSelectedItemId = itemId;
            Toast.makeText(this, Objects.requireNonNull(item.getTitle()).toString(), Toast.LENGTH_SHORT).show();
            switch (Objects.requireNonNull(item.getTitle()).toString()) {
                case "Settings":
                    viewPager.setCurrentItem(2);
                    return true;
                case "Home":
                    viewPager.setCurrentItem(1);
                    return true;
                case "Stats":
                    viewPager.setCurrentItem(0);
                    return true;
            }
            return false;
        });
        if (savedInstanceState == null) {
            viewPager.setCurrentItem(1, false);
        }


        playerSettingsRepository = PlayerSettingsRepository.getInstance(getApplicationContext());
        int user_Id = playerSettingsRepository.getCurrentUserId();
        PlayerSettingsModel user_Ac = playerSettingsRepository.getUserData(user_Id);
        if(user_Ac.getNotification()==1) {
            scheduleDailyNotification();
        }

    }



    private void getAllId() {

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        viewPager=findViewById(R.id.viewPager);
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
     * Планирует выполнение уведомления в 13:00 следующего дня с использованием WorkManager.
     */
    private void scheduleDailyNotification() {
        WorkManager workManager = WorkManager.getInstance(this);

        // Расчет времени до следующего уведомления в 13:00
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 14);
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



    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE);
            } else {
                Log.d("PermissionCheck", "Уведомления уже разрешены");
            }
        } else {
            Log.d("PermissionCheck", "Запрос разрешения не требуется (Android < 13)");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PermissionCheck", "Разрешение на уведомления получено");
            } else {
                Log.d("PermissionCheck", "Разрешение на уведомления ОТКАЗАНО");
            }
        }
    }
}
