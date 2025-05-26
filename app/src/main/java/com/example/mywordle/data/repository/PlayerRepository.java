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
import com.example.mywordle.data.model.PlayerModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepository {
    private static PlayerRepository instance;
    private SQLiteDatabase db;
    private Context context;
    List<OnDataUpdateListener> onDataUpdateListeners = new ArrayList<>();

    private PlayerRepository(Context context) {
        this.context = context;
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public static synchronized PlayerRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PlayerRepository(context.getApplicationContext());
        }
        return instance;
    }

    // Регистрация нового пользователя
    public void userRegistration(PlayerModel playerModel, Context context) {
        ContentValues values = new ContentValues();
        values.put("login", playerModel.getLogin());
        values.put("password", playerModel.getPassword());
        values.put("level", playerModel.getLevel());
        values.put("allGames",playerModel.getAllGames());
        values.put("gamesWin", playerModel.getGamesWin());
        values.put("maxSeriesWins",playerModel.getMaxSeriesWins());
        values.put("currentSeriesWins", playerModel.getCurrentSeriesWins());
        values.put("bestAttempt", playerModel.getBestAttempt());
        values.put("oneAttempt", playerModel.getOneAttempt());
        values.put("twoAttempt",playerModel.getTwoAttempt());
        values.put("threeAttempt", playerModel.getThreeAttempt());
        values.put("fourAttempt", playerModel.getFourAttempt());
        values.put("fiveAttempt", playerModel.getFiveAttempt());
        values.put("sixAttempt", playerModel.getSixAttempt());
        values.put("money",playerModel.getMoney());
        values.put("wordDay", playerModel.getWordDay());
//        if (profileImage == null) {
//            profileImage = getDefaultProfileImage(context);
//        }

        int userId = (int) db.insert("user", null, values);
        if (userId != -1) {
            saveUserId(userId); // Сохраняем ID нового пользователя
        }
    }

    // Проверка, существует ли пользователь
    public boolean isValidUser(String login) {
        //  String query = "SELECT * FROM " + DatabaseHelper.WORD_TABLE + " WHERE " + DatabaseHelper.COLUMN_LENGTH_WORDS + " = ?";
        String query = "SELECT * FROM " + DatabaseHelper.USER_TABLE + " WHERE "+ DatabaseHelper.COLUMN_USER_LOGIN + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{login});
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0;
        }
        cursor.close();
        return exists;
    }

    // Получение userId по логину (например, при авторизации)
    public void getUserIdByLogin(String login) {
        int userId = -1;


        String query = "SELECT * FROM " + DatabaseHelper.USER_TABLE + " WHERE "+ DatabaseHelper.COLUMN_USER_LOGIN + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{login});
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        else {userId = -1;}

        saveUserId(userId); // Сохраняем ID нового пользователя
         cursor.close();
    }

    // Сохранение userId в SharedPreferences
    private void saveUserId(int userId) {
        SharedPreferences preferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userId", userId);
        editor.apply();
    }

    // Получение сохраненного userId
    public int getCurrentUserId() {
        SharedPreferences preferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return preferences.getInt("userId", -1);
    }

    // Получение данных пользователя
    public PlayerModel getUserData(int userId) {
        PlayerModel player = null;
        String query = "SELECT * FROM " + DatabaseHelper.USER_TABLE + " WHERE " + DatabaseHelper.COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
//
        if (cursor.moveToFirst()) {

            player = new PlayerModel(
                    cursor.getInt(0),  // ID
                    cursor.getString(1), // Login
                    cursor.getString(2), // Password
                    cursor.getInt(3), // Level
                    cursor.getInt(4), // allGames
                    cursor.getInt(5), // gamesWin
                    cursor.getInt(6), // maxSeriesWins
                    cursor.getInt(7),  // currentSeriesWins
                    cursor.getInt(8), // bestAttempt
                    cursor.getInt(9), // oneAttempt
                    cursor.getInt(10), // twoAttempt
                    cursor.getInt(11),  // threeAttempt
                    cursor.getInt(12), // fourAttempt
                    cursor.getInt(13),  // fiveAttempt
                    cursor.getInt(14),// sixAttempt
                    cursor.getInt(15),//money
                    cursor.getString(16)//word day

            );
        }
        cursor.close();
        return player;
    }

    // Обновление данных пользователя
    public void updateUserData(int userId, ContentValues values) {
        db.update("user", values,  DatabaseHelper.COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        notifyDataUpdated(values);
    }

    public void addOnDataUpdateListener(OnDataUpdateListener listener) {
        onDataUpdateListeners.add(listener);
    }

    public void notifyDataUpdated(ContentValues values) {
        for (OnDataUpdateListener listener: onDataUpdateListeners) {
            listener.onUpdate(values);
        }
    }

}
