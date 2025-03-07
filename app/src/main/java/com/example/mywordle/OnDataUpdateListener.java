package com.example.mywordle;

import android.content.ContentValues;

@FunctionalInterface
public interface OnDataUpdateListener {
    public void onUpdate(ContentValues values);
}
