package com.example.mywordle;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mywordle.Keyboard.Keyboard;
import com.example.mywordle.data.model.PlayerModel;
import com.example.mywordle.data.model.WordsModel;
import com.example.mywordle.data.repository.DatabaseHelper;
import com.example.mywordle.data.repository.PlayerRepository;
import com.example.mywordle.data.repository.WordsRepository;
import com.example.mywordle.databinding.ActivityGameBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameActivity extends AppCompatActivity {


    ActivityGameBinding binding;
    private GridLayout gridLetters;
    private List<TextView> letterCells;
    private int currentCellIndex = 0;
    private int currentAttemptIndex = 0;
    private int wordLength;
    private final int MAX_ATTEMPTS = 6;
    private GameLogic gameLogic;
    private WordsRepository wordsRepository;
    private TextView popupGameOver;
    private TextView popupGameWin;

    private TextView moneyText;



    private List<Keyboard.Key> keyList = java.util.Arrays.asList(
            new Keyboard.Key("Й"), new Keyboard.Key("Ц"), new Keyboard.Key("У"), new Keyboard.Key("К"),
            new Keyboard.Key("Е"), new Keyboard.Key("Н"), new Keyboard.Key("Г"), new Keyboard.Key("Ш"),
            new Keyboard.Key("Щ"), new Keyboard.Key("З"), new Keyboard.Key("Х"), new Keyboard.Key("Ъ"),
            new Keyboard.Key("Ф"), new Keyboard.Key("Ы"), new Keyboard.Key("В"), new Keyboard.Key("А"),
            new Keyboard.Key("П"), new Keyboard.Key("Р"), new Keyboard.Key("О"), new Keyboard.Key("Л"),
            new Keyboard.Key("Д"), new Keyboard.Key("Ж"), new Keyboard.Key("Э"), new Keyboard.Key("Я"),
            new Keyboard.Key("Ч"), new Keyboard.Key("С"), new Keyboard.Key("М"), new Keyboard.Key("И"),
            new Keyboard.Key("Т"), new Keyboard.Key("Б"), new Keyboard.Key("Ю"), new Keyboard.Key("Ь"),
            new Keyboard.Key("Del", LetterStatus.GRAY)
    );


    @SuppressLint({"SetTextI18n", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        wordLength = getIntent().getIntExtra("WORD_LENGTH", 5);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        wordsRepository = new WordsRepository(db);

        List<WordsModel> validWords = wordsRepository.getFilteredWordsFree(wordLength);

        PlayerRepository playerRepository = PlayerRepository.getInstance(getApplicationContext());
        int userId = playerRepository.getCurrentUserId();
        PlayerModel user = playerRepository.getUserData(userId);




        int randomIndex = (int) (Math.random() * validWords.size());
        WordsModel selectedWord = validWords.get(randomIndex);

        gameLogic = new GameLogic(MAX_ATTEMPTS);
        gameLogic.startNewGame(selectedWord.getWord());

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


        binding.exitButton.setOnClickListener(v -> finish());
        binding.btnHint.setOnClickListener(v -> showHintDialog());

        TextView level=findViewById(R.id.levelGameText);
        level.setText("Level № "+String.valueOf(user.getLevel()));


        Keyboard keyboard = new Keyboard(binding.keyboard, keyList);
        keyboard.setOnKeyClickListener(v -> {
            if(user.getSound()==1){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.keyboard_sound);
            mediaPlayer.start();}

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

        Button btnCheck = binding.btnCheck;
        btnCheck.setOnClickListener(v -> {
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

            // Проверка: существует ли введённое слово в базе данных
            try {
                if (!wordsRepository.isValidWord(guess)) {
                    Toast.makeText(getApplicationContext(), "Похоже, я не знаю такого слова", Toast.LENGTH_SHORT).show();
                    return;
                }

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
                   playerWin();
                } else if (gameLogic.isGameOver()) {
                    playerLose();
                } else {
                    currentAttemptIndex++;
                }
            } catch (IllegalStateException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void playerWin(){

        PlayerRepository playerRepository = PlayerRepository.getInstance(getApplicationContext());
        int userId = playerRepository.getCurrentUserId();
        PlayerModel user = playerRepository.getUserData(userId);
        user.setLevel(user.getLevel() + 5);
        user.setMoney(user.getMoney() + 15);
        user.setAllGames(user.getAllGames() + 1);
        user.setGamesWin(user.getGamesWin() + 1);
        user.setCurrentSeriesWins(user.getCurrentSeriesWins()+1);
        if(user.getSound()==1) {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.game_win_sound);
            mediaPlayer.start();
        }
        switch (currentAttemptIndex) {
            case 0:user.setOneAttempt(user.getOneAttempt() + 1);break;
            case 1:user.setOneAttempt(user.getTwoAttempt() + 1);break;
            case 2:user.setOneAttempt(user.getThreeAttempt() + 1);break;
            case 3:user.setOneAttempt(user.getFourAttempt() + 1);break;
            case 4:user.setOneAttempt(user.getFiveAttempt() + 1);break;
            case 5:user.setOneAttempt(user.getSixAttempt() + 1);break;
        }
        ContentValues values = new ContentValues();
        if(user.getBestAttempt()!=0 || user.getBestAttempt()< currentAttemptIndex+1){
            user.setBestAttempt(currentAttemptIndex+1);
        }

        values.put("level", user.getLevel());
        values.put("money", user.getMoney());
        values.put("allGames", user.getAllGames());
        values.put("gamesWin", user.getGamesWin());
        values.put("currentSeriesWins",user.getCurrentSeriesWins());
        switch (currentAttemptIndex) {
            case 0: values.put("oneAttempt",user.getOneAttempt());break;
            case 1: values.put("twoAttempt",user.getOneAttempt());break;
            case 2: values.put("threeAttempt",user.getOneAttempt());break;
            case 3: values.put("fourAttempt",user.getOneAttempt());break;
            case 4: values.put("fiveAttempt",user.getOneAttempt());break;
            case 5: values.put("sixAttempt",user.getOneAttempt());break;
        }
        values.put("currentSeriesWins",user.getCurrentSeriesWins());
        values.put("bestAttempt",user.getBestAttempt());

        playerRepository.updateUserData(userId, values);
        showGameWinDialog();
    }
    private void playerLose(){


        PlayerRepository playerRepository = PlayerRepository.getInstance(getApplicationContext());
        int userId = playerRepository.getCurrentUserId();
        PlayerModel user = playerRepository.getUserData(userId);
        if(user.getLevel()>5){user.setLevel(user.getLevel() - 5);}
        else{user.setLevel(0);}
        user.setAllGames(user.getAllGames() + 1);
        if(user.getMaxSeriesWins()<user.getCurrentSeriesWins()){user.setMaxSeriesWins(user.getCurrentSeriesWins());}
        user.setCurrentSeriesWins(0);
        if(user.getSound()==1){
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.game_lose_sound);
        mediaPlayer.start();}

        ContentValues values = new ContentValues();
        values.put("level", user.getLevel());
        values.put("money", user.getMoney());
        values.put("allGames", user.getAllGames());
        values.put("currentSeriesWins", user.getCurrentSeriesWins());
        values.put("maxSeriesWins", user.getMaxSeriesWins());
        playerRepository.updateUserData(userId, values);

        showGameOverDialog();
    }

    private Set<Integer> hintedIndexes = new HashSet<>();

    private void showGameOverDialog() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_game_over);
        dialog.setCancelable(false);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView popupGameOver = dialog.findViewById(R.id.popupGameOverText);
        Button btnRestart = dialog.findViewById(R.id.btnRestart);
        Button btnMainMenu = dialog.findViewById(R.id.btnMainMenu);

        popupGameOver.setText( gameLogic.getHiddenWord());


        btnRestart.setOnClickListener(v -> {
            dialog.dismiss();
            recreate();
        });


        btnMainMenu.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }
    private void showGameWinDialog() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_game_win);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView popupGameWin = dialog.findViewById(R.id.popupGameWinText);
        Button btnRestart = dialog.findViewById(R.id.btnRestart);
        Button btnMainMenu = dialog.findViewById(R.id.btnMainMenu);

        popupGameWin.setText( gameLogic.getHiddenWord());


        btnRestart.setOnClickListener(v -> {
            dialog.dismiss();
            recreate();
        });


        btnMainMenu.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }




    private char[] hintWord;


    private void initializeHintWord(int wordLength) {
        if (wordLength > 0) {
            hintWord = new char[wordLength];
            Arrays.fill(hintWord, '*');
        } else {
            Log.e("GameActivity", "Invalid word length: " + wordLength);
        }
    }
    int hintCount=0;
    private void showHintDialog() {
        if (hintWord == null) {
            Log.e("GameActivity", "hintWord is null! Initializing...");
            initializeHintWord(wordLength);
        }


        Dialog hintDialog = new Dialog(this);
        hintDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        hintDialog.setContentView(R.layout.popup_hint);
        hintDialog.setCancelable(true);
        hintDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView popupHint = hintDialog.findViewById(R.id.popupHintText);
        popupHint.setText("Подсказка: " + new String(hintWord));
        TextView moneyText = hintDialog.findViewById(R.id.moneyCost);
        moneyText.setText("Стоимость подсказки : " + String.valueOf((hintCount+1)*5));
        PlayerRepository playerRepository = PlayerRepository.getInstance(getApplicationContext());
        int userId = playerRepository.getCurrentUserId();
        PlayerModel user = playerRepository.getUserData(userId);

        TextView money2 = hintDialog.findViewById(R.id.textView);
        money2.setText(  String.valueOf(user.getMoney()+"X"));
        hintDialog.show();


        hintDialog.findViewById(R.id.btnUpdateHint).setOnClickListener(v -> {
            hintCount++;
            updateHint(popupHint, moneyText, money2);
        });


        hintDialog.findViewById(R.id.btnCloseHint).setOnClickListener(v ->
                hintDialog.dismiss());


    }


    @SuppressLint("SetTextI18n")
    private void updateHint(TextView popupHint,TextView moneyText,TextView money2) {
        List<Integer> unopenedIndexes = computeUnopenedIndexes();
        if (unopenedIndexes.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Все возможные подсказки использованы!", Toast.LENGTH_SHORT).show();
            return;
        }

        PlayerRepository playerRepository = PlayerRepository.getInstance(getApplicationContext());
        int userId = playerRepository.getCurrentUserId();
        PlayerModel user = playerRepository.getUserData(userId);
        if (user.getMoney() >= (hintCount) * 5) {
        user.setMoney(user.getMoney()-((hintCount)*5));
        }
        else{
            Toast.makeText(getApplicationContext(), "Недостаточно монет для получения подсказки !", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("money", user.getMoney());
        playerRepository.updateUserData(userId, values);


        int hintIndex = unopenedIndexes.get((int) (Math.random() * unopenedIndexes.size()));
        hintWord[hintIndex] = gameLogic.getHiddenWord().charAt(hintIndex);
        hintedIndexes.add(hintIndex);
        popupHint.setText("Подсказка: " + new String(hintWord));

        moneyText.setText("Стоимость подсказки : " + hintCount*5);

        money2.setText(user.getMoney()+"X");
    }


    private List<Integer> computeUnopenedIndexes() {
        List<Integer> unopenedIndexes = new ArrayList<>();
        for (int i = 0; i < wordLength; i++) {
            if (!hintedIndexes.contains(i) && !isLetterGuessed(i)) {
                unopenedIndexes.add(i);
            }
        }
        return unopenedIndexes;
    }
    private boolean isLetterGuessed(int index) {
        return hintWord[index] != '*';
    }








}
