package com.example.mywordle;


import android.annotation.SuppressLint;
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
    private TextView gamesWonText;
    private TextView percentOfWinText;
    private TextView bestAttemptText;
    private TextView bestEpisodeText;
    private TextView currentEpisodeText;


@SuppressLint("SetTextI18n")
@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contaner, Bundle savedInstanceState){
    View view = inflater.inflate(R.layout.fragment_profile,contaner,false);
    getAllId(view);
    PlayerRepository playerRepository = PlayerRepository.getInstance(getContext());
    int userId = playerRepository.getCurrentUserId();
    PlayerModel user = playerRepository.getUserData(userId);
    playerRepository.addOnDataUpdateListener(values -> {
        Integer allGames = (Integer) values.get("allGames");
        if (allGames != null) {
            allGamesText.setText(String.valueOf(allGames));
        }

    });
    allGamesText.setText(String.valueOf(user.getAllGames()));
    gamesWonText.setText(String.valueOf(user.getGamesWin()));
    float allGames=user.getAllGames();
    float gamesWin=user.getGamesWin();
    if(allGames!=0){
        percentOfWinText.setText(String.format("%.2f %%", (gamesWin /  allGames) * 100));}
    else{percentOfWinText.setText("-");}
    bestAttemptText.setText(String.valueOf(user.getBestAttempt()));
    bestEpisodeText.setText(String.valueOf(user.getMaxSeriesWins()));
    currentEpisodeText.setText(String.valueOf(user.getCurrentSeriesWins()));
    //percentOfWinText.setText((Integer.parseInt(String.valueOf(user.getGamesWin()))/Integer.parseInt(String.valueOf(user.getAllGames())))*100);

//
//    Switch mySwitch = findViewById(R.id.mySwitch);
//    mySwitch.setChecked(true);
    return view;

}
    private void getAllId(View view) {
        allGamesText       = view.findViewById(R.id.all_games_text);
        gamesWonText       =view.findViewById(R.id.gamesWonText);
        percentOfWinText       =view.findViewById(R.id.percentOfWinText);
        bestAttemptText       =view.findViewById(R.id.bestAttemptText);
        bestEpisodeText       =view.findViewById(R.id.bestEpisodeText);
        currentEpisodeText       =view.findViewById(R.id.currentSeriesText);

    }


}