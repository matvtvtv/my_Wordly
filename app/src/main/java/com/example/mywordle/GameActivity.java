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
import androidx.core.content.ContextCompat;

import com.example.mywordle.Adapter.KeyboardAdapter;
import com.example.mywordle.Keyboard.Keyboard;
import com.example.mywordle.databinding.ActivityGameBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    ActivityGameBinding binding;
    private ImageView exit_button;

    private GridLayout gridLetters;
    private List<TextView> letterCells; // Список для всех клеток
    private int currentCellIndex = 0;
    private int currentAttemptIndex = 0;
    //private final int WORD_LENGTH;
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
            new Keyboard.Key("Б"),
            new Keyboard.Key("Ю"),
            new Keyboard.Key("Ь"),
            new Keyboard.Key("Del", LetterStatus.GRAY)

    );


        // Получаем переданное значение
       // 6 - значение по умолчанию


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        int WORD_LENGTH = getIntent().getIntExtra("WORD_LENGTH", 6);
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
                "фронт", // ф, р, о, н, т
                "камни", // к, а, м, н, и
                "дверь", // д, в, е, р, ь
                "птица", // п, т, и, ц, а
                "книга", // к, н, и, г, а
                "ручка", // р, у, ч, к, а
                "карта", // к, а, р, т, а
                "город", // г, о, р, о, д
                "зверь", // з, в, е, р, ь
                "масло", // м, а, с, л, о
                "сонет", // с, о, н, е, т
                "фильм", // ф, и, л, ь, м
                "музей", // м, у, з, е, й
                "театр", // т, е, а, т, р
                "киоск", // к, и, о, с, к
                "парус", // п, а, р, у, с
                "бочка", // б, о, ч, к, а
                "мужик", // м, у, ж, и, к
                "делец", // д, е, л, е, ц
                "диван", // д, и, в, а, н
                "комод", // к, о, м, о, д
                "панда", // п, а, н, д, а
                "мышка", // м, ы, ш, к, а
                "пчела", // п, ч, е, л, а
                "зайка", // з, а, й, к, а
                "огонь", // о, г, о, н, ь
                "песня", // п, е, с, н, я
                "весна", // в, е, с, н, а
                "осень", // о, с, е, н, ь
                "белка", // б, е, л, к, а
                "рыбак", // р, ы, б, а, к
                "берег", // б, е, р, е, г
                "сокол", // с, о, к, о, л
                "молот", // м, о, л, о, т
                "печка", // п, е, ч, к, а
                "завод", // з, а, в, о, д
                "конус", // к, о, н, у, с
                "весло", // в, е, с, л, о
                "стена", // с, т, е, н, а
                "радио", // р, а, д, и, о
                "лапша"  // л, а, п, ш, а

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

            float availableHeight = screenHeight * 0.7f; // 70% высоты экрана
            float availableWidth = screenWidth * 0.9f;   // 90% ширины экрана (оставляем небольшие отступы)

// Вычисляем размер кубика по ширине и высоте
            int cubeHeight = (int) (availableHeight / MAX_ATTEMPTS);
            int cubeWidth;
            if (4==WORD_LENGTH){
                 cubeWidth = (int) (availableWidth / (WORD_LENGTH + 1));;}
            else{ cubeWidth = (int) (availableWidth / WORD_LENGTH);}

// Выбираем минимальное значение, чтобы кубики не выходили за границы
            int cubeSize = Math.min(cubeWidth, cubeHeight);

// Добавляем небольшой отступ между кубиками (например, 5% от cubeSize)
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

        layout.setColumnCount(WORD_LENGTH);
        ImageView exit_button = findViewById(R.id.exit_button);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Завершает текущую активность
            }
        });


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
                int tr=0;
                for (int i=0;i<words.length;i++){
                    if (words[i].equalsIgnoreCase(guess)) {
                        tr=1;
                    }
                }
                if (tr==0) {
                    Toast.makeText(getApplicationContext(), "Похоже я не знаю такого слова", Toast.LENGTH_SHORT).show();
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
                                cell.setBackgroundResource(R.drawable.cell_background_green);                                break;
                            case YELLOW:
                                if (key != null && key.getStatus() == LetterStatus.UNDEFINED) {
                                    key.setStatus(LetterStatus.YELLOW);
                                    keyboard.notifyKeyChanged(key);
                                }
                                cell.setBackgroundResource(R.drawable.cell_background_yellow);                                                break;
                            case GRAY:
                                if (key != null && key.getStatus() == LetterStatus.UNDEFINED) {
                                    key.setStatus(LetterStatus.GRAY);
                                    keyboard.notifyKeyChanged(key);
                                }
                                cell.setBackgroundResource(R.drawable.cell_background_grey);                                                break;
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