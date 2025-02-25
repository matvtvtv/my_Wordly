package com.example.mywordle;

public enum LetterStatus {
    GREEN,  // буква на правильном месте
    YELLOW, // буква присутствует, но в другой позиции
    GRAY,    // буквы нет в слове
    UNDEFINED
}
