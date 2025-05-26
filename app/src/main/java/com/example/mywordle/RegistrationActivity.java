package com.example.mywordle;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mywordle.MainActivity;
import com.example.mywordle.R;
import com.example.mywordle.data.Network.CallbackUser;
import com.example.mywordle.data.Network.DataFromUserAPI;
import com.example.mywordle.data.model.PlayerModel;
import com.example.mywordle.data.model.PlayerSettingsModel;
import com.example.mywordle.data.repository.PlayerRepository;
import com.example.mywordle.data.repository.PlayerSettingsRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextLogin, editTextPassword;
    private TextView textViewMessage;
    private PlayerRepository playerRepository;
    private PlayerSettingsRepository playerSettingsRepository;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private DataFromUserAPI dataFromUserAPI = new DataFromUserAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_registration);


        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnLog = findViewById(R.id.btnLog);
        textViewMessage = findViewById(R.id.textViewMessage);

        playerRepository = PlayerRepository.getInstance(this);
        playerSettingsRepository= PlayerSettingsRepository.getInstance(this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void registerUser() {
        String login = editTextLogin.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Инициализируем переменную для хранения изображения (по умолчанию null)


        if (login.isEmpty() || password.isEmpty()) {
            textViewMessage.setText("Введите логин и пароль!");
            return;
        }
        // todo Loading UI
        executor.execute(()->dataFromUserAPI.getUser(login, new CallbackUser() {
            @Override
            public void onSuccess(PlayerModel playerModel) {
                saveToRepository(playerModel);
            }

            @Override
            public void onError(Throwable throwable) {
                // todo erorr UI
            }
        }));

        if (playerRepository.isValidUser(login)) {
            textViewMessage.setText("Логин уже существует!");
        } else {


        }
    }
    private void loginUser() {
        String login = editTextLogin.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (login.isEmpty() || password.isEmpty()) {
            textViewMessage.setText("Введите логин и пароль!");
            return;
        }

        // todo Loading UI
        executor.execute(()->dataFromUserAPI.getEnter(login,password, new CallbackUser() {
            @Override
            public void onSuccess(PlayerModel playerModel) {
                saveToRepository(playerModel);
            }

            @Override
            public void onError(Throwable throwable) {
                runOnUiThread(() -> {
                    Toast.makeText(RegistrationActivity.this, "Ошибка входа: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }));

    }

    private void saveToRepository(PlayerModel playerModel){
        playerRepository.userRegistration(playerModel, this);
        PlayerRepository playerRepository = PlayerRepository.getInstance(getApplicationContext());
        int userId = playerRepository.getCurrentUserId();
        playerSettingsRepository.userSettingsRegistration(userId, this);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    // Метод для сохранения ID пользователя
    private void saveUserId(int userId) {
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userId", userId);
        editor.apply();
    }



}