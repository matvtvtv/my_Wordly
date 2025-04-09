package com.example.mywordle.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.mywordle.OnDataUpdateListener;
import com.example.mywordle.R;

import com.example.mywordle.data.model.PlayerSettingsModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PlayerSettingsRepository {
    private static PlayerSettingsRepository instance;
    private SQLiteDatabase db;
    private Context context;
    List<OnDataUpdateListener> onDataUpdateListeners = new ArrayList<>();
    private PlayerSettingsRepository(Context context) {
        this.context = context;
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public static synchronized PlayerSettingsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PlayerSettingsRepository(context.getApplicationContext());
        }
        return instance;
    }
    public void userSettingsRegistration(Context context) {
        ContentValues values = new ContentValues();

        values.put("userId", 1);
        values.put("sound", 1);
        values.put("vibration", 1);
        values.put("profileImage",getDefaultProfileImage(context));
        int userId = (int) db.insert("settings", null, values);
        if (userId != -1) {
            saveUserId(userId); // Сохраняем ID нового пользователя
        }
    }
    public void getUserIdByLogin(String login) {
        int userId = -1;

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        String query = "SELECT * FROM " + DatabaseHelper.USER_TABLE + " WHERE "+ DatabaseHelper.COLUMN_USER_SETTINGS_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{login});
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        else {userId = -1;}

        saveUserId(userId); // Сохраняем ID нового пользователя
        cursor.close();
    }
    public PlayerSettingsModel getUserData(int userId) {
        PlayerSettingsModel player = null;
        String query = "SELECT * FROM " + DatabaseHelper.USER_SETTINGS_TABLE + " WHERE " + DatabaseHelper.COLUMN_USER_SETTINGS_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
//
        if (cursor.moveToFirst()) {

            player = new PlayerSettingsModel(
                    cursor.getInt(0),  // ID
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getBlob(3)

            );
        }
        cursor.close();
        return player;
    }
    public int getCurrentUserId() {
        SharedPreferences preferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return preferences.getInt("userId", -1);
    }
    private void saveUserId(int userId) {
        SharedPreferences preferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userId", userId);
        editor.apply();
    }
    private byte[] getDefaultProfileImage(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_profile);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    public void updateUserData(int userId, ContentValues values) {
        db.update("user", values,  DatabaseHelper.COLUMN_USER_SETTINGS_ID + " = ?", new String[]{String.valueOf(userId)});
        notifyDataUpdated(values);
    }
    public void notifyDataUpdated(ContentValues values) {
        for (OnDataUpdateListener listener: onDataUpdateListeners) {
            listener.onUpdate(values);
        }
    }
}
