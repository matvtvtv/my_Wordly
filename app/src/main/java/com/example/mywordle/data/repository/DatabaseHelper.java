package com.example.mywordle.data.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "words.db";
    private static final int DATABASE_VERSION = 1;

    public static final String WORD_TABLE = "words";
    public static final String COLUMN_ID_WORDS = "id";
    public static final String COLUMN_WORD_WORDS = "word";
    public static final String COLUMN_DIFFICULT_WORDS = "difficulty";
    public static final String COLUMN_LENGTH_WORDS = "length";

    public static final String USER_TABLE = "user";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_USER_LOGIN = "login";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_LEVEL = "level";
    public static final String COLUMN_USER_ALL_GAMES = "allGames";
    public static final String COLUMN_USER_GAMES_WIN = "gamesWin";
    public static final String COLUMN_USER_MAX_SERIES_WINS = "maxSeriesWins";
    public static final String COLUMN_USER_CURRENT_SERIES_WINS= "currentSeriesWins";
    public static final String COLUMN_USER_BEST_ATTEMPT= "bestAttempt";
    public static final String COLUMN_USER_ONE_ATTEMPT= "oneAttempt";
    public static final String COLUMN_USER_TWO_ATTEMPT= "twoAttempt";
    public static final String COLUMN_USER_THREE_ATTEMPT= "threeAttempt";
    public static final String COLUMN_USER_FOUR_ATTEMPT= "fourAttempt";
    public static final String COLUMN_USER_FIVE_ATTEMPT= "fiveAttempt";
    public static final String COLUMN_USER_SIX_ATTEMPT= "sixAttempt";
    public static final String COLUMN_USER_MONEY = "money";
    public static final String COLUMN_USER_SOUND = "sound";
    public static final String COLUMN_USER_WORDDAY = "wordDay";
    public static final String COLUMN_USER_PICTURE = "profileImage";




    private static DatabaseHelper instance;
    private static final String CREATE_USER_TABLE = "CREATE TABLE " +  USER_TABLE+ " ( " +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_LOGIN + " TEXT, " +
            COLUMN_USER_PASSWORD + " TEXT, " +
            COLUMN_USER_LEVEL + " INTEGER, " +
            COLUMN_USER_ALL_GAMES + " INTEGER, " +
            COLUMN_USER_GAMES_WIN + " INTEGER, " +
            COLUMN_USER_MAX_SERIES_WINS + " INTEGER, " +
            COLUMN_USER_CURRENT_SERIES_WINS + " INTEGER, " +
            COLUMN_USER_BEST_ATTEMPT + " INTEGER, " +
            COLUMN_USER_ONE_ATTEMPT + " INTEGER, " +
            COLUMN_USER_TWO_ATTEMPT + " INTEGER, " +
            COLUMN_USER_THREE_ATTEMPT + " INTEGER, " +
            COLUMN_USER_FOUR_ATTEMPT + " INTEGER, " +
            COLUMN_USER_FIVE_ATTEMPT + " INTEGER, " +
            COLUMN_USER_SIX_ATTEMPT + " INTEGER, " +
            COLUMN_USER_MONEY + " INTEGER, " +
            COLUMN_USER_SOUND + " INTEGER, " +
            COLUMN_USER_WORDDAY + " INTEGER, " +
            COLUMN_USER_PICTURE + " BLOB )";


    private static final String CREATE_TABLE_WORDS= "CREATE TABLE " + WORD_TABLE + " ( " +
            COLUMN_ID_WORDS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_WORD_WORDS + " TEXT, " +
            COLUMN_DIFFICULT_WORDS + " INTEGER, " +
            COLUMN_LENGTH_WORDS + " INTEGER )";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WORDS);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WORD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }
}
