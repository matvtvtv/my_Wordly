package com.example.mywordle;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mywordle.data.model.PlayerModel;
import com.example.mywordle.data.repository.PlayerRepository;


public class FragmentProfile extends Fragment {
    private TextView allGamesText;

@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contaner, Bundle savedInstanceState){
    View view = inflater.inflate(R.layout.fragment_profile,contaner,false);
    getAllId(view);
    PlayerRepository playerRepository = PlayerRepository.getInstance(getContext());
    int userId = playerRepository.getCurrentUserId();
    PlayerModel user = playerRepository.getUserData(userId);
    allGamesText.setText(String.valueOf(user.getAllGames()));
    playerRepository.addOnDataUpdateListener(values -> {
        Integer allGames = (Integer) values.get("allGames");
        if (allGames != null) {
            allGamesText.setText(String.valueOf(allGames));
        }
    });
    return view;

}
    private void getAllId(View view) {
        allGamesText       = view.findViewById(R.id.all_games_text);

    }


}