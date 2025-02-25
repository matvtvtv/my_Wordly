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

@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        getAllId(view);

        travel_button.setOnClickListener(v->{

            Intent intent = new Intent(getContext(), GameActivity.class);
            startActivity(intent);
        });

        return view;
    }
    private void getAllId(View view) {

        travel_button = view.findViewById(R.id.travel_button);
    }






}