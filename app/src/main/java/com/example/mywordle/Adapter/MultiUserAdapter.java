package com.example.mywordle.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywordle.R;
import com.example.mywordle.data.model.MultiUserModel;

public class MultiUserAdapter extends RecyclerView.Adapter<MultiUserAdapter.MultiUserViewHolder> {

    private final MultiUserModel[] multiUser;

    public MultiUserAdapter(MultiUserModel[] multiUser) {
        this.multiUser = multiUser;
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
        holder.login.setText("Логин: ");
        holder.level.setText("Уровень: " );
        holder.word_check.setText("# " + (position + 1));


    }

    @Override
    public int getItemCount() {
        return multiUser.length;
    }

    static class MultiUserViewHolder extends RecyclerView.ViewHolder {
        TextView login, level, word_check;


        public MultiUserViewHolder(@NonNull View itemView) {
            super(itemView);
            login = itemView.findViewById(R.id.player_login_guess);
            level = itemView.findViewById(R.id.word_lenght);
            word_check = itemView.findViewById(R.id.check_of_word); // убедись, что в XML id = player_position
        }
    }
}
