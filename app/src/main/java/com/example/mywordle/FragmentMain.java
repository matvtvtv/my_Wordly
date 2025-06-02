package com.example.mywordle;

import static java.lang.Math.round;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywordle.data.model.PlayerModel;
import com.example.mywordle.data.repository.PlayerRepository;


public class FragmentMain extends Fragment {

    private ImageView word_day;
   private ImageView travel_button;
    private ImageView letter_4_free;
    private ImageView letter_5_free;
    private ImageView letter_6_free;
    private ImageView letter_7_free;
    private TextView level;
    private TextView moneyText;

    private ImageView multiUser;

    private int GAME_MODE=2;
    private int WORD_LENGTH=0;

@SuppressLint("SetTextI18n")
@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        getAllId(view);
        PlayerRepository playerRepository = PlayerRepository.getInstance(getContext());
        int userId = playerRepository.getCurrentUserId();
        PlayerModel user = playerRepository.getUserData(userId);

        level.setText("Level № "+ String.valueOf(user.getLevel()));
        playerRepository.addOnDataUpdateListener(values -> {
        Integer newLevel = (Integer) values.get("level");
            if (newLevel != null) {
            level.setText("Level № "+ String.valueOf(newLevel));
            }
        });


            level.setText("Level № "+ String.valueOf(user.getLevel()));

            playerRepository.addOnDataUpdateListener(values -> {
            Integer newLevel = (Integer) values.get("level");
            if (newLevel != null) {
                level.setText("Level № "+ String.valueOf(newLevel));
            }
            });

            moneyText.setText(String.valueOf(user.getMoney()));
            playerRepository.addOnDataUpdateListener(values -> {
            Integer newMoney = (Integer) values.get("money");
            if (newMoney != null) {
            moneyText.setText(String.valueOf(newMoney));
            }
            });

         word_day.setOnClickListener(v->{
            ContentValues values = new ContentValues();
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = now.format(formatter);
            if(!Objects.equals(user.getWordDay(), formattedDate)){
                values.put("wordDay",formattedDate);
                playerRepository.updateUserData(userId, values);
                WORD_LENGTH=5;
                Intent intent = new Intent(getContext(), GameActivity.class);
                intent.putExtra("WORD_LENGTH", WORD_LENGTH);
                GAME_MODE=1;
                intent.putExtra("GAME_MODE", GAME_MODE);

                requireActivity().finish();
                startActivity(intent);
            }
            else {
            Toast.makeText(getContext(), "Вы уже играли слово дня", Toast.LENGTH_SHORT).show();
            }

        });

        travel_button.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), GameActivity.class);


            GAME_MODE=2;
            intent.putExtra("GAME_MODE", GAME_MODE);


            requireActivity().finish();
            startActivity(intent);
        });

        letter_4_free.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), GameActivity.class);
            WORD_LENGTH=4;
            intent.putExtra("WORD_LENGTH", WORD_LENGTH); // Передаем значение
            GAME_MODE=3;
            intent.putExtra("GAME_MODE", GAME_MODE);
            requireActivity().finish();
            startActivity(intent);
        });

        letter_5_free.setOnClickListener(v->{

            Intent intent = new Intent(getContext(), GameActivity.class);
            WORD_LENGTH=5;
            intent.putExtra("WORD_LENGTH", WORD_LENGTH); // Передаем значение
            GAME_MODE=3;
            intent.putExtra("GAME_MODE", GAME_MODE);
            requireActivity().finish();
            startActivity(intent);
         });

        letter_6_free.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), GameActivity.class);
            WORD_LENGTH=6;
            intent.putExtra("WORD_LENGTH", WORD_LENGTH); // Передаем значение
            GAME_MODE=3;
            intent.putExtra("GAME_MODE", GAME_MODE);
            requireActivity().finish();
            startActivity(intent);
        });

        letter_7_free.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), GameActivity.class);
            WORD_LENGTH=7;
            intent.putExtra("WORD_LENGTH", WORD_LENGTH); // Передаем значение
            GAME_MODE=3;
            intent.putExtra("GAME_MODE", GAME_MODE);
            requireActivity().finish();
            startActivity(intent);
        });

    multiUser.setOnClickListener(v -> {

        MultiUserList bestFragment = new MultiUserList();

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lay_main, bestFragment)
                .addToBackStack(null)
                .commit();
    });


    return view;
    }
    private void getAllId(View view) {
        travel_button       = view.findViewById(R.id.travel_button);
        letter_4_free       = view.findViewById(R.id.letter_4_free);
        letter_5_free       = view.findViewById(R.id.letter_5_free);
        letter_6_free       = view.findViewById(R.id.letter_6_free);
        letter_7_free       = view.findViewById(R.id.letter_7_free);
        level               = view.findViewById(R.id.Level);
        moneyText           = view.findViewById(R.id.moneyText);
        word_day            = view.findViewById(R.id.word_day);
        multiUser =  view.findViewById(R.id.multiUser);
    }






}