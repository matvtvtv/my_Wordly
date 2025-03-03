package com.example.mywordle;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class FragmentMain extends Fragment {

   private ImageView travel_button;
    private ImageView letter_4_free;
    private ImageView letter_5_free;
    private ImageView letter_6_free;
    private ImageView letter_7_free;
   private int WORD_LENGTH=0;

@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        getAllId(view);

        travel_button.setOnClickListener(v->{

            Intent intent = new Intent(getContext(), GameActivity.class);
            WORD_LENGTH=5;
            intent.putExtra("WORD_LENGTH", WORD_LENGTH); // Передаем значение
            startActivity(intent);
        });
        letter_4_free.setOnClickListener(v->{

            Intent intent = new Intent(getContext(), GameActivity.class);
            WORD_LENGTH=4;
            intent.putExtra("WORD_LENGTH", WORD_LENGTH); // Передаем значение
            startActivity(intent);
        });
    letter_5_free.setOnClickListener(v->{

        Intent intent = new Intent(getContext(), GameActivity.class);
        WORD_LENGTH=5;
        intent.putExtra("WORD_LENGTH", WORD_LENGTH); // Передаем значение
        startActivity(intent);
    });

    letter_6_free.setOnClickListener(v->{

        Intent intent = new Intent(getContext(), GameActivity.class);
        WORD_LENGTH=6;
        intent.putExtra("WORD_LENGTH", WORD_LENGTH); // Передаем значение
        startActivity(intent);
    });
    letter_7_free.setOnClickListener(v->{

        Intent intent = new Intent(getContext(), GameActivity.class);
        WORD_LENGTH=7;
        intent.putExtra("WORD_LENGTH", WORD_LENGTH); // Передаем значение
        startActivity(intent);
    });



    return view;
    }
    private void getAllId(View view) {

        travel_button = view.findViewById(R.id.travel_button);
        letter_4_free= view.findViewById(R.id.letter_4_free);
        letter_5_free= view.findViewById(R.id.letter_5_free);
        letter_6_free= view.findViewById(R.id.letter_6_free);
        letter_7_free= view.findViewById(R.id.letter_7_free);

    }






}