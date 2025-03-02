package com.example.mywordle;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mywordle.Adapter.KeyboardAdapter;
import com.example.mywordle.Keyboard.Keyboard;
import com.example.mywordle.data.model.WordsModel;
import com.example.mywordle.data.repository.WordsFileReader;
import com.example.mywordle.databinding.ActivityGameBinding;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    ActivityGameBinding binding;
    private ImageView exit_button;
    private GridLayout gridLetters;
    private List<TextView> letterCells;
    private int currentCellIndex = 0;
    private int currentAttemptIndex = 0;
    private int wordLength;
    private final int MAX_ATTEMPTS = 6;
    private GameLogic gameLogic;
    private Keyboard keyboard;
    private WordsFileReader fileReader;

    // Инициализация клавиатуры
    private final List<Keyboard.Key> keyList = java.util.Arrays.asList(
            new Keyboard.Key("Й"),
            new Keyboard.Key("Ц"),
            new Keyboard.Key("У"),
            new Keyboard.Key("К"),
            new Keyboard.Key("Е"),
            new Keyboard.Key("Н"),
            new Keyboard.Key("Г"),
            new Keyboard.Key("Ш"),
            new Keyboard.Key("Щ"),
            new Keyboard.Key("З"),
            new Keyboard.Key("Х"),
            new Keyboard.Key("Ъ"),
            new Keyboard.Key("Ф"),
            new Keyboard.Key("Ы"),
            new Keyboard.Key("В"),
            new Keyboard.Key("А"),
            new Keyboard.Key("П"),
            new Keyboard.Key("Р"),
            new Keyboard.Key("О"),
            new Keyboard.Key("Л"),
            new Keyboard.Key("Д"),
            new Keyboard.Key("Ж"),
            new Keyboard.Key("Э"),
            new Keyboard.Key("Я"),
            new Keyboard.Key("Ч"),
            new Keyboard.Key("С"),
            new Keyboard.Key("М"),
            new Keyboard.Key("И"),
            new Keyboard.Key("Т"),
            new Keyboard.Key("Б"),
            new Keyboard.Key("Ю"),
            new Keyboard.Key("Ь"),
            new Keyboard.Key("Del", LetterStatus.GRAY)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Получаем параметр длины слова через Intent
        wordLength = getIntent().getIntExtra("WORD_LENGTH", 5);
        // Получаем сложность слова. Если сложность не требуется, передавайте -1
        int wordDifficulty = getIntent().getIntExtra("WORD_DIFFICULTY", -1);

        // Инициализируем WordsFileReader
        fileReader = new WordsFileReader(this);

        // Универсальная фильтрация: если wordDifficulty < 0, фильтруем только по длине,
        // иначе по сложности и длине.
        List<WordsModel> validWords = fileReader.getFilteredWords(wordLength, wordDifficulty);
        if (validWords.isEmpty()) {
            String msg = wordDifficulty < 0
                    ? "Нет слов с длиной " + wordLength + " букв!"
                    : "Нет слов с сложностью " + wordDifficulty + " и длиной " + wordLength + " букв!";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Выбираем случайное слово
        int randomIndex = (int) (Math.random() * validWords.size());
        WordsModel selectedWord = validWords.get(randomIndex);

        // Инициализируем игровую логику
        gameLogic = new GameLogic(MAX_ATTEMPTS);
        gameLogic.startNewGame(selectedWord.getWord());

        // Инициализация сетки для ввода букв
        gridLetters = findViewById(R.id.gridLetters);
        letterCells = new ArrayList<>();
        for (int i = 0; i < MAX_ATTEMPTS * wordLength; i++) {
            TextView cell = new TextView(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenHeight = displayMetrics.heightPixels;
            int screenWidth = displayMetrics.widthPixels;

            float availableHeight = screenHeight * 0.7f;
            float availableWidth = screenWidth * 0.9f;
            int cubeWidth;
            int cubeHeight = (int) (availableHeight / MAX_ATTEMPTS);
            if (4==wordLength){
                cubeWidth = (int) (availableWidth / (wordLength + 1));;}
            else{ cubeWidth = (int) (availableWidth / wordLength);}
            int cubeSize = Math.min(cubeWidth, cubeHeight);
            int padding = (int) (cubeSize * 0.05f);
            cubeSize -= padding;

            params.width = cubeSize;
            params.height = cubeSize;
            params.setMargins(6, 8, 6, 8);
            cell.setLayoutParams(params);
            cell.setGravity(Gravity.CENTER);
            cell.setTextSize(28);
            cell.setBackgroundResource(R.drawable.cell_background_undefined);
            gridLetters.addView(cell);
            letterCells.add(cell);
        }

        GridLayout layout = findViewById(R.id.gridLetters);
        layout.setColumnCount(wordLength);

        exit_button = findViewById(R.id.exit_button);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Настройка клавиатуры
        keyboard = new Keyboard(findViewById(R.id.keyboard), keyList);
        keyboard.setOnKeyClickListener(v -> {
            Button btn = (Button) v;
            String letter = btn.getText().toString();
            if (letter.equals("Del")) {
                if (currentCellIndex > currentAttemptIndex * wordLength) {
                    currentCellIndex--;
                    letterCells.get(currentCellIndex).setText("");
                }
            } else {
                if (currentCellIndex < (currentAttemptIndex + 1) * wordLength) {
                    letterCells.get(currentCellIndex).setText(letter);
                    currentCellIndex++;
                }
            }
        });
        keyboard.create(this, binding.getRoot());

        // Обработка нажатия кнопки "Проверить"
        Button btnCheck = findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int start = currentAttemptIndex * wordLength;
                int end = start + wordLength;
                StringBuilder guessBuilder = new StringBuilder();
                for (int i = start; i < end; i++) {
                    guessBuilder.append(letterCells.get(i).getText());
                }
                String guess = guessBuilder.toString().trim();

                if (guess.length() != wordLength) {
                    Toast.makeText(getApplicationContext(), "Введите слово из " + wordLength + " букв!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Проверка: существует ли введённое слово в файле
                if (!fileReader.isValidWord(guess)) {
                    Toast.makeText(getApplicationContext(), "Похоже, я не знаю такого слова", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    AttemptResult result = gameLogic.checkWord(guess);
                    for (int i = 0; i < wordLength; i++) {
                        TextView cell = letterCells.get(start + i);
                        Keyboard.Key key = keyboard.findByKeyText(String.valueOf(result.getGuess().charAt(i)));
                        switch (result.getStatuses()[i]) {
                            case GREEN:
                                if (key != null && (key.getStatus() == LetterStatus.UNDEFINED || key.getStatus() == LetterStatus.YELLOW)) {
                                    key.setStatus(LetterStatus.GREEN);
                                    keyboard.notifyKeyChanged(key);
                                }
                                cell.setBackgroundResource(R.drawable.cell_background_green);
                                break;
                            case YELLOW:
                                if (key != null && key.getStatus() == LetterStatus.UNDEFINED) {
                                    key.setStatus(LetterStatus.YELLOW);
                                    keyboard.notifyKeyChanged(key);
                                }
                                cell.setBackgroundResource(R.drawable.cell_background_yellow);
                                break;
                            case GRAY:
                                if (key != null && key.getStatus() == LetterStatus.UNDEFINED) {
                                    key.setStatus(LetterStatus.GRAY);
                                    keyboard.notifyKeyChanged(key);
                                }
                                cell.setBackgroundResource(R.drawable.cell_background_grey);
                                break;
                        }
                    }

                    if (gameLogic.isGameWon()) {
                        Toast.makeText(getApplicationContext(), "Поздравляем, вы выиграли!", Toast.LENGTH_LONG).show();
                        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.e377e9b8d135e68);
                        mediaPlayer.start();
                    } else if (gameLogic.isGameOver()) {
                        Toast.makeText(getApplicationContext(), "Попытки закончились! Загаданное слово: " + gameLogic.getHiddenWord(), Toast.LENGTH_LONG).show();
                    } else {
                        currentAttemptIndex++;
                    }
                } catch (IllegalStateException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
