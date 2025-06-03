package com.example.mywordle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mywordle.Adapter.MultiUserAdapter;
import com.example.mywordle.data.Network.CallbackMultiUserS;
import com.example.mywordle.data.Network.DataFromMultiUserAPI;
import com.example.mywordle.data.model.MultiUserModel;
import com.example.mywordle.data.model.PlayerModel;
import com.example.mywordle.data.repository.PlayerRepository;

public class MultiUserList extends Fragment {

    private ImageView plus;
    private RecyclerView recyclerView;

    public MultiUserList() {}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_multi_user, container, false);

        recyclerView = view.findViewById(R.id.recycler_multi_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        plus = view.findViewById(R.id.plus);
        plus.setOnClickListener(v -> {
            // Если вам нужно по «плюсу» открывать экран создания нового слова друга,
            // оставляем как есть или запускаем нужный фрагмент:
            MultiUserNewWordForFriend newFrag = new MultiUserNewWordForFriend();
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.lay_multi_user_list, newFrag)
                    .addToBackStack(null)
                    .commit();
        });

        // Получаем список загаданных слов от API
        PlayerRepository playerRepository = PlayerRepository.getInstance(requireContext());
        int userId = playerRepository.getCurrentUserId();
        PlayerModel user = playerRepository.getUserData(userId);

        DataFromMultiUserAPI api = new DataFromMultiUserAPI();
        api.getMultiUserGames(user.getLogin(), new CallbackMultiUserS() {
            @Override
            public void onSuccess(MultiUserModel[] multiUsers) {
                if (getActivity() == null) return;
                requireActivity().runOnUiThread(() -> {
                    // создаём адаптер с listener-ом, который будет открывать GameActivity
                    MultiUserAdapter adapter = new MultiUserAdapter(multiUsers, item -> {
                        // вот здесь, item — выбранный MultiUserModel,
                        // у него есть метод getWord() и getLogin()
                        openGameActivity(item);
                    });
                    recyclerView.setAdapter(adapter);
                });
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        return view;
    }

    // Запускаем GameActivity, передавая слово друга
    private void openGameActivity(MultiUserModel chosenGame) {
        Intent intent = new Intent(requireContext(), GameActivity.class);


        intent.putExtra("GAME_MODE", 4);
        intent.putExtra("CHECK_OF_WORD",chosenGame.getFlagOfCheck());
        intent.putExtra("WORD_LENGTH",chosenGame.getWord().length());
        intent.putExtra("FRIEND_WORD", chosenGame.getWord());


        startActivity(intent);

    }
}
