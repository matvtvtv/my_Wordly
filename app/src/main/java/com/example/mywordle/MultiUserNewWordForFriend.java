package com.example.mywordle;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mywordle.data.Network.CallbackMultiUser;
import com.example.mywordle.data.Network.DataFromMultiUserAPI;
import com.example.mywordle.data.Network.MultiUserAPI;
import com.example.mywordle.data.model.MultiUserModel;
import com.example.mywordle.data.model.PlayerModel;
import com.example.mywordle.data.model.WordsModel;
import com.example.mywordle.data.repository.DatabaseHelper;
import com.example.mywordle.data.repository.PlayerRepository;
import com.example.mywordle.data.repository.WordsRepository;

import java.util.List;

public class MultiUserNewWordForFriend extends Fragment {

    private EditText loginGuessing;
    private EditText word;
    private CheckBox radioButtonFlagOfCheck;
    private Button buttonNew;
    private TextView textError;

    private WordsRepository wordsRepository;

    private static final String RUSSIAN_WORD_PATTERN = "^[А-Яа-яЁё]{4,7}$";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multi_user_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginGuessing = view.findViewById(R.id.loginGuessing);
        word = view.findViewById(R.id.word);
        radioButtonFlagOfCheck = view.findViewById(R.id.radioButton_flag_of_check);
        buttonNew = view.findViewById(R.id.button_new);
        textError = view.findViewById(R.id.text_error);

        buttonNew.setOnClickListener(v -> {
            textError.setText(""); // Сброс предыдущей ошибки

            String loginText = loginGuessing.getText().toString().trim();
            String wordText = word.getText().toString().trim();

            // Проверяем, что логин не пуст
            if (TextUtils.isEmpty(loginText)) {
                textError.setText("Пожалуйста, введите логин.");
                return;
            }

            // Проверяем слово на длину и символы
            if (!wordText.matches(RUSSIAN_WORD_PATTERN)) {
                textError.setText("Слово должно состоять из 4–7 букв русского алфавита без пробелов и спецсимволов.");
                return;
            }

            // Если нужно проверить существование слова
            boolean shouldCheckExistence = radioButtonFlagOfCheck.isChecked();
            if (shouldCheckExistence) {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(requireContext());
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                wordsRepository = new WordsRepository(db);
                if (!wordsRepository.isValidWord(wordText)) {
                    Toast.makeText(requireContext(),
                            "Похоже, я не знаю такого слова", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Получаем текущего пользователя, чтобы подставить его логин как loginGuessing
            PlayerRepository playerRepository = PlayerRepository.getInstance(requireContext());
            int userId = playerRepository.getCurrentUserId();
            PlayerModel user = playerRepository.getUserData(userId);

            MultiUserModel newEntry = new MultiUserModel(
                    null,                      // wordId — пусть сервер сам присвоит
                    user.getLogin(),           // loginGuess (текущий пользователь)
                    loginText,                 // loginGuessing (кому мы загадываем)
                    wordText,                  // слово
                    shouldCheckExistence ? 1 : 0 // flagOfCheck
            );

            Toast.makeText(requireContext(),
                    "Отправляем на сервер: логин друга = " + loginText + ", слово = " + wordText,
                    Toast.LENGTH_SHORT).show();

            // Делаем PUT-запрос на /new_guess_word через DataFromMultiUserAPI
            DataFromMultiUserAPI api = new DataFromMultiUserAPI();
            api.saveMultiUser(newEntry, new CallbackMultiUser() {
                @Override
                public void onSuccess(MultiUserModel savedMultiUser) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(),
                                "Успешно отправлено! Присвоенный ID: " + savedMultiUser.getWordId(),
                                Toast.LENGTH_LONG).show();
                        // Здесь можно закрыть фрагмент или перейти назад:
                        // requireActivity().getSupportFragmentManager().popBackStack();
                    });
                }

                @Override
                public void onError(Throwable t) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(),
                                "Ошибка при отправке: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    });
                }
            });
        });
    }
}
