package com.example.mywordle;

import static java.security.AccessController.getContext;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import androidx.activity.EdgeToEdge;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.mywordle.data.repository.DatabaseHelper;
import com.example.mywordle.data.repository.PlayerRepository;
import com.example.mywordle.data.repository.WordsRepository;
import com.example.mywordle.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private ImageView settings_button_main;
    private ImageView home_button_main;
    private ImageView profile_button_main;

    private int frame = 1; // 0 - profile, 1 - home, 2 - settings

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        WordsRepository wordsRepository = new WordsRepository(db);
        // Enable edge-to-edge content
         SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                boolean isFirstRun = preferences.getBoolean("isFirstRun", true);
         if (isFirstRun) {
             wordsRepository.importWordsFromFile(this);
                    // Если это первый запуск, открываем RegistrationActivity
             Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
             startActivity(intent);


             // После первого запуска изменяем флаг, чтобы не запускать RegistrationActivity снова
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isFirstRun", false);
                    editor.apply();
                }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set up the window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Create an instance of DatabaseHelper for accessing the database

        // Create an instance of WordsRepository and pass the context


        if(wordsRepository.isTableEmpty()){

        }

        // Initialize view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getAllId(); // Get references to the buttons





        // Change fragment initially to FragmentMain
        change(new FragmentMain());
        home_button_main.setScaleX(1.5f);
        home_button_main.setScaleY(1.5f);

        // Set up listeners for buttons with animations
        settings_button_main.setOnClickListener(v -> handleFragmentChange(v, new FragmentSettings(), 2));
        home_button_main.setOnClickListener(v -> handleFragmentChange(v, new FragmentMain(), 1));
        profile_button_main.setOnClickListener(v -> handleFragmentChange(v, new FragmentProfile(), 0));
    }

    // Generic method for changing fragments and handling icon animations
    private void handleFragmentChange(View v, Fragment fragment, int targetFrame) {
        Animation clickAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.button_click);
        v.startAnimation(clickAnim);
        clickAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (frame != targetFrame) {
                    resetIcons(home_button_main, 0);
                    resetIcons(profile_button_main, 0);
                    resetIcons(settings_button_main, 0);
                    animIcon(v); // Animate the icon for the clicked button
                    change(fragment); // Change the fragment
                    frame = targetFrame; // Update the frame state
                } else {
                    resetIcons(home_button_main, 300);
                    resetIcons(profile_button_main, 300);
                    resetIcons(settings_button_main, 300);
                }
            }
        });
    }

    // Change the displayed fragment
    private void change(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

    // Get references to the buttons
    private void getAllId() {
        settings_button_main = findViewById(R.id.settings_button_main);
        home_button_main = findViewById(R.id.home_button_main);
        profile_button_main = findViewById(R.id.profile_button_main);
    }

    // Reset icon's color and size when switching
    @SuppressLint("UseCompatLoadingForDrawables")
    private void resetIcons(View v, int duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.5f, 1f);
        scaleX.setDuration(duration);
        scaleY.setDuration(duration);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }

    // Animate the selected icon (enlarge and change color)
    private void animIcon(View v) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.5f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.5f);
        scaleX.setDuration(300);
        scaleY.setDuration(300);

        ValueAnimator colorAnimator = getColorAnimator(v, R.color.original_color, R.color.pressed_color);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, colorAnimator);
        animatorSet.start();
    }

    // Create a color transition animation
    private ValueAnimator getColorAnimator(View v, @ColorRes int from, @ColorRes int to) {
        int startColor = ContextCompat.getColor(this, from);
        int endColor = ContextCompat.getColor(this, to);
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
        colorAnimator.setDuration(300);
        colorAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            ((ImageView) v).setColorFilter(animatedValue);
        });
        return colorAnimator;
    }
}
