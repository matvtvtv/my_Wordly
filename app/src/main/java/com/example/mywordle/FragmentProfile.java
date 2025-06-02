package com.example.mywordle;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
    private ProgressBar progressBar_1_attempt;
    private ProgressBar progressBar_2_attempt;
    private ProgressBar progressBar_3_attempt;
    private ProgressBar progressBar_4_attempt;
    private ProgressBar progressBar_5_attempt;
    private ProgressBar progressBar_6_attempt;


@SuppressLint("SetTextI18n")
@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contaner, Bundle savedInstanceState){
    View view = inflater.inflate(R.layout.fragment_statistics,contaner,false);
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

    progressBar_1_attempt.setProgress((int) (user.getOneAttempt() * 100.0 / gamesWin));
    progressBar_2_attempt.setProgress((int) (user.getTwoAttempt() * 100.0 / gamesWin));
    progressBar_3_attempt.setProgress((int) (user.getThreeAttempt() * 100.0 / gamesWin));
    progressBar_4_attempt.setProgress((int) (user.getFourAttempt() * 100.0 / gamesWin));
    progressBar_5_attempt.setProgress((int) (user.getFiveAttempt() * 100.0 / gamesWin));
    progressBar_6_attempt.setProgress((int) (user.getSixAttempt() * 100.0 / gamesWin));
    Button button = view.findViewById(R.id.button);
    button.setOnClickListener(v -> {
        // Создаем новый экземпляр фрагмента с топ-игроками
        Statistics_Of_Best bestFragment = new Statistics_Of_Best();

        // Выполняем замену фрагмента через FragmentManager
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lay_statistics, bestFragment)  // R.id.fragment_container — контейнер для фрагментов в твоей activity
                .addToBackStack(null)  // чтобы можно было вернуться назад кнопкой "Назад"
                .commit();
    });

    return view;

}
    private void getAllId(View view) {
        allGamesText       = view.findViewById(R.id.all_games_text);
        gamesWonText       =view.findViewById(R.id.gamesWonText);
        percentOfWinText       =view.findViewById(R.id.percentOfWinText);
        bestAttemptText       =view.findViewById(R.id.bestAttemptText);
        bestEpisodeText       =view.findViewById(R.id.bestEpisodeText);
        currentEpisodeText       =view.findViewById(R.id.currentSeriesText);
        progressBar_1_attempt =view.findViewById(R.id.progressBar_1_attempt);
        progressBar_2_attempt =view.findViewById(R.id.progressBar_2_attempt);
        progressBar_3_attempt =view.findViewById(R.id.progressBar_3_attempt);
        progressBar_4_attempt =view.findViewById(R.id.progressBar_4_attempt);
        progressBar_5_attempt =view.findViewById(R.id.progressBar_5_attempt);
        progressBar_6_attempt =view.findViewById(R.id.progressBar_6_attempt);


    }


}