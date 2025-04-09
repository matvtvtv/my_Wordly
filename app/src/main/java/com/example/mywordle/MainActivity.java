package com.example.mywordle;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mywordle.data.repository.DatabaseHelper;
import com.example.mywordle.data.repository.WordsRepository;
import com.example.mywordle.databinding.ActivityMainBinding;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private ImageView settings_button_main;
    private ImageView home_button_main;
    private ImageView profile_button_main;
    private int frame = 1; // 0 - profile, 1 - home, 2 - settings
    private SharedPreferences preferences;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int isUserLoggedIn = preferences.getInt("userId", -1); // Проверка авторизации
        boolean isFirstRun = preferences.getBoolean("isFirstRun", true);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        WordsRepository wordsRepository = new WordsRepository(db);
        if (isFirstRun) {
            wordsRepository.importWordsFromFile(this);
            // Обновляем флаг
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();
        }
        if (isUserLoggedIn == -1) {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
            return;  // Прекращаем выполнение onCreate()
        }

        setContentView(R.layout.activity_main);

        getAllId(); // Получаем ссылки на кнопки

        // Изначально загружаем FragmentMain
        change(new FragmentMain());
        home_button_main.setScaleX(1.5f);
        home_button_main.setScaleY(1.5f);

        // Устанавливаем слушатели кнопок
        settings_button_main.setOnClickListener(v -> handleFragmentChange(v, new FragmentSettings(), 2));
        home_button_main.setOnClickListener(v -> handleFragmentChange(v, new FragmentMain(), 1));
        profile_button_main.setOnClickListener(v -> handleFragmentChange(v, new FragmentProfile(), 0));
       // scheduleNotification(); // Устанавливаем уведомление
    }

    // Метод для обработки смены фрагментов и анимации иконок
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

    // Метод для смены фрагментов
    private void change(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

    // Получение ссылок на кнопки
    private void getAllId() {
        settings_button_main = findViewById(R.id.settings_button_main);
        home_button_main = findViewById(R.id.home_button_main);
        profile_button_main = findViewById(R.id.profile_button_main);
    }

    // Метод для сброса анимации иконок
    private void resetIcons(View v, int duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.5f, 1f);
        scaleX.setDuration(duration);
        scaleY.setDuration(duration);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }

    // Метод для анимации выбранной иконки
    private void animIcon(View v) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.5f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.5f);
        scaleX.setDuration(300);
        scaleY.setDuration(300);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }

//    @SuppressLint("ScheduleExactAlarm")
//    private void scheduleNotification() {
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 11);
//        calendar.set(Calendar.MINUTE, 10);
//        calendar.set(Calendar.SECOND, 0);
//
//
//
//        Intent intent = new Intent(this, NotificationReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        if (alarmManager != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//            } else {
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//            }
//        }
//    }
}
