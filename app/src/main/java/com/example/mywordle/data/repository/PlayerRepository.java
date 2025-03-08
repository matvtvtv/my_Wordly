package com.example.mywordle.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.DatePicker;

import com.example.mywordle.OnDataUpdateListener;
import com.example.mywordle.data.model.PlayerModel;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

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
    public void userRegistration(String login, String password) {
        ContentValues values = new ContentValues();
        values.put("login", login);
        values.put("password", password);
        values.put("level", 1);
        values.put("allGames", 0);
        values.put("gamesWin", 0);
        values.put("maxSeriesWins", 0);
        values.put("currentSeriesWins", 0);
        values.put("bestAttempt", 0);
        values.put("oneAttempt", 0);
        values.put("twoAttempt", 0);
        values.put("threeAttempt", 0);
        values.put("fourAttempt", 0);
        values.put("fiveAttempt", 0);
        values.put("sixAttempt", 0);
        values.put("money", 50);

        int userId = (int) db.insert("user", null, values);
        if (userId != -1) {
            saveUserId(userId); // Сохраняем ID нового пользователя
        }
    }

    // Проверка, существует ли пользователь
    public boolean isValidUser(String login) {
        //  String query = "SELECT * FROM " + DatabaseHelper.WORD_TABLE + " WHERE " + DatabaseHelper.COLUMN_LENGTH_WORDS + " = ?";
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.USER_TABLE + " WHERE "+ DatabaseHelper.COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{login});
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0;
        }
        cursor.close();
        return exists;
    }

    // Получение userId по логину (например, при авторизации)
    public long getUserIdByLogin(String login) {
        int userId = -1;
        //  String query = "SELECT * FROM " + DatabaseHelper.WORD_TABLE + " WHERE " + DatabaseHelper.COLUMN_LENGTH_WORDS + " = ?";
        //String query = "SELECT COUNT(*) FROM " + DatabaseHelper.USER_TABLE + " WHERE "+ DatabaseHelper.COLUMN_USER_ID + " = ?";

        String query = "SELECT "+ DatabaseHelper.COLUMN_USER_ID + " FROM " + DatabaseHelper.USER_TABLE + " WHERE "+ DatabaseHelper.COLUMN_USER_LOGIN + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{login});
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        saveUserId(userId); // Сохраняем ID нового пользователя
        return userId;
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
//        Cursor cursor = db.query(DatabaseHelper.USER_TABLE, new String[]{
//                DatabaseHelper.COLUMN_USER_ID, DatabaseHelper.COLUMN_USER_LOGIN,
//                DatabaseHelper.COLUMN_USER_PASSWORD, DatabaseHelper.COLUMN_USER_LEVEL,
//                DatabaseHelper.COLUMN_USER_ALL_GAMES, DatabaseHelper.COLUMN_USER_GAMES_WIN,
//                DatabaseHelper.COLUMN_USER_MAX_SERIES_WINS, DatabaseHelper.COLUMN_USER_CURRENT_SERIES_WINS,
//                DatabaseHelper.COLUMN_USER_BEST_ATTEMPT, DatabaseHelper.COLUMN_USER_ONE_ATTEMPT,
//                DatabaseHelper.COLUMN_USER_TWO_ATTEMPT, DatabaseHelper.COLUMN_USER_THREE_ATTEMPT,
//                DatabaseHelper.COLUMN_USER_FOUR_ATTEMPT, DatabaseHelper.COLUMN_USER_FIVE_ATTEMPT,
//                DatabaseHelper.COLUMN_USER_SIX_ATTEMPT},DatabaseHelper.COLUMN_USER_ID + "=");
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
                    cursor.getInt(15)//money

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
