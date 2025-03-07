package com.example.mywordle.data.model;

public class WordsModel {
    private int id;
    private String word;
    private int length;
    private int difficulty;




    // Конструктор с 4 параметрами (если хотите включить ID)
    public WordsModel(int id, String word, int difficulty, int length) {
        this.id = id;
        this.word = word;
        this.length = length;
        this.difficulty = difficulty;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public int getLength() {
        return length;
    }

    public int getDifficulty() {
        return difficulty;
    }
}

