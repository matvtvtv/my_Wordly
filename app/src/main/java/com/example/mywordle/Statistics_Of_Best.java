package com.example.mywordle;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mywordle.Adapter.PlayerTopAdapter;
import com.example.mywordle.data.Network.CallbackUserS;
import com.example.mywordle.data.Network.DataFromUserAPI;
import com.example.mywordle.data.model.PlayerModel;

public class Statistics_Of_Best extends Fragment {

    public Statistics_Of_Best() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics__of__best, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_top_players);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DataFromUserAPI api = new DataFromUserAPI();
        api.getTopUsers(new CallbackUserS() {
            @Override
            public void onSuccess(PlayerModel[] playerModels) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    recyclerView.setAdapter(new PlayerTopAdapter(playerModels));
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
