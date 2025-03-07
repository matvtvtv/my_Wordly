package com.example.mywordle.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.mywordle.data.model.WordsModel;
import com.example.mywordle.data.repository.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class WordsRepository {
    private final List<WordsModel> wordsList = new ArrayList<>();

    private SQLiteDatabase db;

    public WordsRepository(SQLiteDatabase db) {
        this.db = db;
    }

    // Импорт слов из файла в БД
    public void importWordsFromFile(Context context) {
        WordsFileReader fileReader = new WordsFileReader(context);
        List<WordsModel> wordsList = fileReader.getWordsList(); // Загружаем слова из файла

        for (WordsModel word : wordsList) {
            ContentValues values = new ContentValues();
            values.put("id", word.getId());
            values.put("word", word.getWord());
            values.put("difficulty", word.getDifficulty());
            values.put("length", word.getLength());

            db.insert("words", null, values);
        }
    }


    // Получить слова по длине и сложности
//    public List<WordsModel> getFilteredWordsTravel(int  difficulty, int length) {
//
//
//    }

    public List<WordsModel> getFilteredWordsFree(int length) {
        String query = "SELECT * FROM " + DatabaseHelper.WORD_TABLE + " WHERE " + DatabaseHelper.COLUMN_LENGTH_WORDS + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(length)});

        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String word = cursor.getString(cursor.getColumnIndexOrThrow("word"));
                int diff = cursor.getInt(cursor.getColumnIndexOrThrow("difficulty"));
                int len = cursor.getInt(cursor.getColumnIndexOrThrow("length"));

                wordsList.add(new WordsModel(id, word, diff, len));
            }
        }
        cursor.close();
        return wordsList;
    }
    public boolean isTableEmpty() {
        int wordIndex = -1;
        Cursor cursor = db.query(DatabaseHelper.WORD_TABLE,new String[]{DatabaseHelper.COLUMN_WORD_WORDS},
                null,null,null,null,null);
        while (cursor.moveToNext()){
            wordIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_WORDS);

        }
        if(wordIndex != -1){
            return false;
        }
        return true;
    }

    // Проверка наличия слова
    public boolean isValidWord(String guess) {
        for(int i = 0; i<wordsList.size(); i++){
            if(guess.equalsIgnoreCase(wordsList.get(i).getWord()))return true;
        }

    return false;
    }



}
