package com.example.mywordle;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mywordle.data.model.PlayerModel;
import com.example.mywordle.data.repository.PlayerRepository;

import java.util.HashMap;
import java.util.Map;


public class FragmentSettings extends Fragment {
    private ImageView profile;
    private ImageView gameSound;
    private TextView login;
    private ImageView accountExit;
    private ImageView bugRep;
    private Switch mySwitch;

    private PlayerRepository playerRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        getAllId(view);

        playerRepository = PlayerRepository.getInstance(getContext());
        int userId = playerRepository.getCurrentUserId();
        PlayerModel user = playerRepository.getUserData(userId);


        int sound = (Integer) user.getSound();
        mySwitch.setChecked(sound == 1);
        mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int newSoundValue = isChecked ? 1 : 0; // Если включен — 1, если выключен — 0

            ContentValues values = new ContentValues();
            values.put("sound", newSoundValue); // Используем ContentValues для обновления

            playerRepository.updateUserData(userId, values);
        });
        login.setSelected(true);
        if (user != null) {
            login.setText(user.getLogin());
        }

        playerRepository.addOnDataUpdateListener(values -> {
            String newLogin = (String) values.get("login");
            if (newLogin != null) {
                login.setText(newLogin);
            }
        });

        accountExit.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RegistrationActivity.class);
            startActivity(intent);
        });


        bugRep.setOnClickListener(v -> sendEmailToAgency());

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(50);
        drawable.setColor(Color.TRANSPARENT);

        profile.setBackground(drawable);
        profile.setClipToOutline(true);

        return view;
    }
    private void getAllId(View view) {
        profile =view.findViewById(R.id.profCard);
        accountExit =view.findViewById(R.id.accountExit);
        bugRep =view.findViewById(R.id.bugReport);
       login=view.findViewById(R.id.yourLogin);
        mySwitch=view.findViewById(R.id.gameSoundSwitch);
    }
    private static final String AGENCY_EMAIL = "matveicharniauski@gmail.com";

    private void sendEmailToAgency() {    Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + AGENCY_EMAIL));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Сообщение об ошибке");
        intent.putExtra(Intent.EXTRA_TEXT, "Здравствуйте! Хотел бы сообщить об ошибке...");
        startActivity(Intent.createChooser(intent, "Выберите почтовое приложение"));
    }
}
