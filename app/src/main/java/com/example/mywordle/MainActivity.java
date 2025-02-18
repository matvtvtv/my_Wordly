package com.example.mywordle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

    private ImageView settings_button_main;
    private ImageView home_button_main;

    private ImageView profile_button_main;

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

        getAllId();

        settings_button_main.setOnClickListener(v ->{
            change(new SettingsFragment());
            resetIcons();
            settings_button_main.setImageDrawable(getDrawable(R.drawable.settings_selected));

        });
        home_button_main.setOnClickListener(v ->{
            change(new fragment_main());
            resetIcons();
            home_button_main.setImageDrawable(getDrawable(R.drawable.home_button_selected));
        });
        profile_button_main.setOnClickListener(v ->{
            change(new fragment_profile());
            resetIcons();
            profile_button_main.setImageDrawable(getDrawable(R.drawable.user_selected));

        });
    }

      void change(Fragment fragment){
          FragmentManager manager = getSupportFragmentManager();
          FragmentTransaction ft = manager.beginTransaction();
          ft.replace(R.id.container,fragment);
          ft.commit();
      }

    private void getAllId() {
        settings_button_main = findViewById(R.id.settings_button_main);
        home_button_main= findViewById(R.id.home_button_main);
        profile_button_main= findViewById(R.id.profile_button_main);
    }
    private void resetIcons() {
        home_button_main.setImageDrawable(getDrawable(R.drawable.home_button_uncelected));
        settings_button_main.setImageDrawable(getDrawable(R.drawable.settings_unselect));
        profile_button_main.setImageDrawable(getDrawable(R.drawable.user_unselected));
    }
}