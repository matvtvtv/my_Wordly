package com.example.mywordle.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.mywordle.data.model.WordsModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WordsFileReader {
    private List<WordsModel> wordsList = new ArrayList<>();

    public WordsFileReader(Context context) {
        loadWords(context);
    }
    public List<WordsModel> getWordsList() {
        return wordsList;
    }

    private void loadWords(Context context) {
        try (InputStream is = context.getAssets().open("final.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Формат строки: id,слово,difficulty,length
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0].trim());
                    String word = parts[1].trim();
                    int difficulty = Integer.parseInt(parts[2].trim());
                    int length = Integer.parseInt(parts[3].trim());
                    wordsList.add(new WordsModel(id, word, difficulty, length));

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
