package com.example.mywordle.Keyboard;

import static com.google.android.flexbox.JustifyContent.CENTER;
import static com.google.android.flexbox.JustifyContent.FLEX_START;

import android.content.Context;
import android.util.Size;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mywordle.Adapter.KeyboardAdapter;
import com.example.mywordle.LetterStatus;
import com.example.mywordle.R;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class Keyboard {

    private final RecyclerView keyboard;
    private final List<Key> keys;
    @Getter private KeyboardAdapter keyboardAdapter;
    @Setter private View.OnClickListener onKeyClickListener;

    public Keyboard(RecyclerView keyboard, List<Key> keys) {
        this.keys = keys;
        this.keyboard = keyboard;
    }

    public void create(Context context, View view) {
        keyboardAdapter = new KeyboardAdapter(context, keys);
        if (onKeyClickListener != null) keyboardAdapter.setOnClickListener(onKeyClickListener);

        keyboard.setAdapter(keyboardAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(CENTER);
        keyboard.setLayoutManager(layoutManager);
    }

    public void notifyKeyChanged(int position) {
        keyboardAdapter.notifyItemChanged(position);
    }

    public void notifyKeyChanged(Key key) {
        keyboardAdapter.notifyItemChanged(getKeyPosition(key));
    }

    public Key findByKeyText(String text) {
        for (Key key: keys) {
            if(key.getKeyText().equals(text)) return key;
        }
        return null;
    }

    public int getKeyPosition(Key key) {
        return keys.indexOf(key);
    }

    @Data
    public static class Key {
        String keyText;
        LetterStatus status;

        public Key(String keyText) {
            this.keyText = keyText;
            this.status = LetterStatus.UNDEFINED;
        }


        public Key(String keyText, LetterStatus status) {
            this.keyText = keyText;
            this.status = status;
        }
    }
}
