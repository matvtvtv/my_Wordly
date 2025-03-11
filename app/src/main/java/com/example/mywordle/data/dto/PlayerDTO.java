package com.example.mywordle.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
public class PlayerDTO {
    private int userId;
    private String login;
    private String password;
    private int level;
    private int allGames;
    private int gamesWin;
    private int maxSeriesWins;
    private int currentSeriesWins;
    private int bestAttempt;
    private int oneAttempt;
    private int twoAttempt;
    private int threeAttempt;
    private int fourAttempt;
    private int fiveAttempt;
    private int sixAttempt;
    private int money;
    private int sound;



}
