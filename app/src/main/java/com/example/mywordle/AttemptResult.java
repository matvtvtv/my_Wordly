package com.example.mywordle;
public class AttemptResult {
    private String guess;
    private LetterStatus[] statuses;

    public AttemptResult(String guess, LetterStatus[] statuses) {
        this.guess = guess;
        this.statuses = statuses;
    }

    public String getGuess() {
        return guess;
    }

    public LetterStatus[] getStatuses() {
        return statuses;
    }
}