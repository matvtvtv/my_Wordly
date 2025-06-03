package com.example.mywordle.Adapter;

import android.health.connect.datatypes.units.Length;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywordle.R;
import com.example.mywordle.data.model.MultiUserModel;

// MultiUserAdapter.java
public class MultiUserAdapter extends RecyclerView.Adapter<MultiUserAdapter.MultiUserViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(MultiUserModel item);
    }

    private final MultiUserModel[] multiUser;
    private final OnItemClickListener listener;

    public MultiUserAdapter(MultiUserModel[] multiUser, OnItemClickListener listener) {
        this.multiUser = multiUser;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MultiUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_multi_user, parent, false);
        return new MultiUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiUserViewHolder holder, int position) {
        MultiUserModel user = multiUser[position];
        holder.login.setText("Логин: " + user.getLoginGuess());
        holder.level.setText("Длина слова: " + user.getWord().length());
        holder.word_check.setText("#" + (position + 1));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return multiUser.length;
    }

    static class MultiUserViewHolder extends RecyclerView.ViewHolder {
        TextView login, level, word_check;

        MultiUserViewHolder(@NonNull View itemView) {
            super(itemView);
            login = itemView.findViewById(R.id.player_login_guess);
            level = itemView.findViewById(R.id.word_lenght);
            word_check = itemView.findViewById(R.id.check_of_word);
        }
    }
}
