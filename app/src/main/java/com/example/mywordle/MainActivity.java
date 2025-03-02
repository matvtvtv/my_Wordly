package com.example.mywordle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import androidx.activity.EdgeToEdge;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.animation.AnimationUtils;

import com.example.mywordle.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private ImageView settings_button_main;
    private ImageView home_button_main;
    private ImageView profile_button_main;
    private int frame = 1;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getAllId();
        change(new FragmentMain());
        home_button_main.setScaleX(1.5f);
        home_button_main.setScaleY(1.5f);

        settings_button_main.setOnClickListener(v -> {
            Animation clickAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.button_click);
            v.startAnimation(clickAnim);
            clickAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) { }
                @Override public void onAnimationRepeat(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) {

                    if (frame != 2) {
                        resetIcons(home_button_main,0);
                        resetIcons(profile_button_main,0);
                        animIcon(v);

                        change(new FragmentSettings());
                        frame = 2;
                    }
                    else{resetIcons(home_button_main,300);
                         resetIcons(profile_button_main,300);

                    }

                }
            });
        });

        home_button_main.setOnClickListener(v -> {
            Animation clickAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.button_click);
            v.startAnimation(clickAnim);
            clickAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) { }
                @Override public void onAnimationRepeat(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) {

                    if (frame != 1) {
                        animIcon(v);
                        resetIcons(profile_button_main,0);
                        resetIcons(settings_button_main,0);

                        change(new FragmentMain());
                        frame = 1;
                    }
                     else{   resetIcons(profile_button_main,300);
                        resetIcons(settings_button_main,300);
                        }
                }
            });
        });

        profile_button_main.setOnClickListener(v -> {

            Animation clickAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.button_click);
            v.startAnimation(clickAnim);
            clickAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) { }
                @Override public void onAnimationRepeat(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) {

                    if (frame != 0) {
                        resetIcons(home_button_main,0);
                        resetIcons(settings_button_main,0);
                        animIcon(v);
                        change(new FragmentProfile());
                        frame = 0;
                    }
                    else{resetIcons(home_button_main,300);
                        resetIcons(settings_button_main,300);
                    }
                }
            });

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



    @SuppressLint("UseCompatLoadingForDrawables")
    private void resetIcons(View v, int i) {
        // Сброс цветовых фильтров и масштабов для всех иконок


        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.5f, 1f);
        scaleX.setDuration(i);
        scaleY.setDuration(i);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }

    private void animIcon(View v) {
        // Анимация увеличения (scale up)
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.5f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.5f);
        scaleX.setDuration(300);
        scaleY.setDuration(300);

        // Анимация перекрашивания
        ValueAnimator colorAnimator = getColorAnimator(v, R.color.original_color, R.color.pressed_color);

        // Объединяем анимации в AnimatorSet
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, colorAnimator);
        animatorSet.start();
    }

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
