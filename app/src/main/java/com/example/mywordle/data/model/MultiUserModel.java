package com.example.mywordle.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MultiUserModel {


    private Long wordId;
    private String loginGuess;
    private String loginGuessing;
    private String word;

    //private int flag;
    private int flagOfCheck;
}
