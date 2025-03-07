package com.example.mywordle.data.dto;

public class WordsDTO {
    private int id;
    private String word;
    private int dificult;
    private int lendth;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public int getDificult() {
        return dificult;
    }
    public void setDificult(int dificult) {
        this.dificult = dificult;
    }
    public int getLendth() {
        return lendth;
    }
    public void setLendth(int lendth) {
        this.lendth = lendth;
    }
}
