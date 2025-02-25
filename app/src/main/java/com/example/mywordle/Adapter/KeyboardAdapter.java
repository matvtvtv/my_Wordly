package com.example.mywordle.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywordle.Keyboard.Keyboard;
import com.example.mywordle.R;

import java.util.List;

import lombok.Setter;

public class KeyboardAdapter extends RecyclerView.Adapter<KeyboardAdapter.KeyViewHolder> {

    List<Keyboard.Key> keys;
    Context context;
    @Setter View.OnClickListener onClickListener;

    public KeyboardAdapter(Context context, List<Keyboard.Key> keys) {
        this.keys = keys;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(keys.get(position).getKeyText().equals("Del")) return 4;

        if (position < 13) return 1;
        if (position < 25) return 2;
        if (position < 33) return 3;

        return 0;
    }

    @NonNull
    @Override
    public KeyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.key_item, parent, false);
        ViewGroup.LayoutParams params = itemView.getLayoutParams();

        switch (viewType) {
            case 1:
            case 3:
                params.width = parent.getMeasuredWidth()/12;
                break;
            case 2:
                params.width = parent.getMeasuredWidth()/11;
                break;
            case 4:
                params.width = parent.getMeasuredWidth()/5;
        }

        return new KeyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KeyViewHolder holder, int position) {
        Keyboard.Key key = keys.get(position);

        holder.bind(key, context);

        holder.keyBtn.setOnClickListener(view -> {
            if(onClickListener != null) onClickListener.onClick(view);
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha));
        });


    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    public static class KeyViewHolder extends RecyclerView.ViewHolder {
        Button keyBtn;

        public KeyViewHolder(@NonNull View itemView) {
            super(itemView);
            keyBtn = itemView.findViewById(R.id.key);
        }

        public void bind(Keyboard.Key key, Context context) {
            keyBtn.setText(key.getKeyText());

            switch (key.getStatus()) {
                case GRAY:
                    keyBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.cell_background));
                    break;
                case YELLOW:
                    keyBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.yellow));
                    break;
                case GREEN:
                    keyBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
                    break;
                case UNDEFINED:
                    keyBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.key_background));
                    break;
            }
        }
    }
}