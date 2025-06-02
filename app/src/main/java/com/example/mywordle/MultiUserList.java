package com.example.mywordle;

import static android.app.PendingIntent.getActivity;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywordle.Adapter.MultiUserAdapter;
import com.example.mywordle.R;
import com.example.mywordle.data.Network.CallbackMultiUser;
import com.example.mywordle.data.Network.DataFromMultiUserAPI;
import com.example.mywordle.data.model.MultiUserModel;
import com.example.mywordle.data.model.PlayerModel;
import com.example.mywordle.data.repository.PlayerRepository;

public class MultiUserList extends Fragment {

    public MultiUserList() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_multi_user, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_multi_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        PlayerRepository playerRepository = PlayerRepository.getInstance(requireContext());
        int userId = playerRepository.getCurrentUserId();
        PlayerModel user = playerRepository.getUserData(userId);

        DataFromMultiUserAPI api = new DataFromMultiUserAPI();
        api.getMultiUserGames(user.getLogin(), new CallbackMultiUser() {
            @Override
            public void onSuccess(MultiUserModel[] multiUsers) {
                if (getActivity() == null) return;
                requireActivity().runOnUiThread(() -> {
                    recyclerView.setAdapter(new MultiUserAdapter(multiUsers));
                });
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        return view;
    }
}
