package com.example.mywordle;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywordle.Adapter.KeyboardAdapter;
import com.example.mywordle.Keyboard.Keyboard;
import com.example.mywordle.databinding.ActivityGameBinding;
import com.example.mywordle.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity {

    ActivityGameBinding binding;

    private GridLayout gridLetters;
    private List<TextView> letterCells; // Список для всех клеток
    private int currentCellIndex = 0;
    private int currentAttemptIndex = 0;
    private final int WORD_LENGTH = 5;
    private final int MAX_ATTEMPTS = 6;

    private GameLogic gameLogic; // Логика игры

    private Keyboard keyboard;
    private final List<Keyboard.Key> keyList = Arrays.asList(
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
            new Keyboard.Key("Ь"),
            new Keyboard.Key("Б"),
            new Keyboard.Key("Ь"),
            new Keyboard.Key("Del", LetterStatus.GRAY)

    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        binding = ActivityGameBinding.inflate(getLayoutInflater());

        // Инициализация логики игры (6 попыток)
        gameLogic = new GameLogic(MAX_ATTEMPTS);
        String[] words = {"птица", // п, т, и, ц, а
                "домик", // д, о, м, и, к
                "котик", // к, о, т, и, к
                "волки", // в, о, л, к, и
                "света", // с, в, е, т, а
                "ягода", // я, г, о, д, а
                "снега", // с, н, е, г, а
                "супер", // с, у, п, е, р
                "белка", // б, е, л, к, а
                "мышка", // м, ы, ш, к, а
                "рыбак", // р, ы, б, а, к
                "песик", // п, е, с, и, к
                "корка", // к, о, р, к, а
                "зайка", // з, а, й, к, а
                "груша", // г, р, у, ш, а
                "финик", // ф, и, н, и, к
                "рубеж", // р, у, б, е, ж
                "нитка", // н, и, т, к, а
                "блоки", // б, л, о, к, и
                "фронт"  // ф, р, о, н, т
        };
        // Пример: загадано слово "ВАГОН"
        gameLogic.startNewGame(words[(int) (Math.random() * words.length)]);

        gridLetters = findViewById(R.id.gridLetters);
        letterCells = new ArrayList<>();
        // Создаём 6 * 5 = 30 клеток для ввода
        for (int i = 0; i < MAX_ATTEMPTS * WORD_LENGTH; i++) {
            TextView cell = new TextView(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenHeight = displayMetrics.heightPixels;
            int screenWidth = displayMetrics.widthPixels;

            int cubeHeight = (int) ((screenHeight * 0.7f) / MAX_ATTEMPTS - (screenWidth / 40));
            int cubeWidth = (int) (screenWidth / WORD_LENGTH - (screenWidth / 30));

            int cubeSize = Math.min(cubeWidth, cubeHeight);

            params.width = cubeSize;
            params.height = cubeSize;
            params.setMargins(8, 8, 8, 8);
            cell.setLayoutParams(params);
            cell.setGravity(Gravity.CENTER);
            cell.setTextSize(24);

            cell.setBackgroundResource(R.drawable.cell_background);
            gridLetters.addView(cell);
            letterCells.add(cell);
        }


        GridLayout layout = findViewById(R.id.gridLetters);

        layout.setColumnCount(WORD_LENGTH);

        View.OnClickListener letterClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCellIndex < (currentAttemptIndex + 1) * WORD_LENGTH) {
                    Button btn = (Button) v;
                    String letter = btn.getText().toString();
                    letterCells.get(currentCellIndex).setText(letter);
                    currentCellIndex++;
                }
            }
        };

        keyboard = new Keyboard(findViewById(R.id.keyboard), keyList);

        keyboard.setOnKeyClickListener(v -> {
            Button btn = (Button) v;
            String letter = btn.getText().toString();

            if (letter.equals("Del")) {
                if (currentCellIndex > currentAttemptIndex * WORD_LENGTH) {
                    currentCellIndex--; // Шаг назад
                    letterCells.get(currentCellIndex).setText(""); // Очищаем букву
                }
            } else {
                if (currentCellIndex < (currentAttemptIndex + 1) * WORD_LENGTH) {
                    letterCells.get(currentCellIndex).setText(letter);
                    currentCellIndex++;
                }
            }
        });

        keyboard.create(this, binding.getRoot());



        // Кнопка удаления


        KeyboardAdapter adapter = keyboard.getKeyboardAdapter();

        // Кнопка проверки
        Button btnCheck = findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Собираем слово из текущей строки
                int start = currentAttemptIndex * WORD_LENGTH;
                int end = start + WORD_LENGTH;
                StringBuilder guessBuilder = new StringBuilder();
                for (int i = start; i < end; i++) {
                    guessBuilder.append(letterCells.get(i).getText());
                }
                String guess = guessBuilder.toString().trim();

                if (guess.length() != WORD_LENGTH) {
                    Toast.makeText(getApplicationContext(), "Введите слово из " + WORD_LENGTH + " букв!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    AttemptResult result = gameLogic.checkWord(guess);
                    // Изменяем цвет клеток в зависимости от результата
                    for (int i = 0; i < WORD_LENGTH; i++) {
                        TextView cell = letterCells.get(start + i);
                        Keyboard.Key key = keyboard.findByKeyText(String.valueOf(result.getGuess().charAt(i)));
                        switch (result.getStatuses()[i]) {
                            case GREEN:
                                if (key != null &&
                                        (key.getStatus() == LetterStatus.UNDEFINED
                                                || key.getStatus() == LetterStatus.YELLOW)) {
                                    key.setStatus(LetterStatus.GREEN);
                                    keyboard.notifyKeyChanged(key);
                                }
                                cell.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                                break;
                            case YELLOW:
                                if (key != null && key.getStatus() == LetterStatus.UNDEFINED) {
                                    key.setStatus(LetterStatus.YELLOW);
                                    keyboard.notifyKeyChanged(key);
                                }
                                cell.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
                                break;
                            case GRAY:
                                if (key != null && key.getStatus() == LetterStatus.UNDEFINED) {
                                    key.setStatus(LetterStatus.GRAY);
                                    keyboard.notifyKeyChanged(key);
                                }
                                cell.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
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
                        // Переход к следующей попытке
                        currentAttemptIndex++;
                    }
                } catch (IllegalStateException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}