package com.example.mywordle.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywordle.R;
import com.example.mywordle.data.model.PlayerModel;

public class PlayerTopAdapter extends RecyclerView.Adapter<PlayerTopAdapter.PlayerViewHolder> {
    private final PlayerModel[] players;

    public PlayerTopAdapter(PlayerModel[] players) {
        this.players = players;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        PlayerModel player = players[position];
        holder.login.setText("Логин: " + player.getLogin());
        holder.level.setText("Уровень: " + player.getLevel());
    }

    @Override
    public int getItemCount() {
        return players.length;
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView login, level;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            login = itemView.findViewById(R.id.player_login);
            level = itemView.findViewById(R.id.player_level);
        }
    }
}
