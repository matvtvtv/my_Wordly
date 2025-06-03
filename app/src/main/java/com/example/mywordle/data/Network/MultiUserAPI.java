package com.example.mywordle.data.Network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mywordle.data.model.MultiUserModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MultiUserAPI {
    private static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client;
    private final Gson gson;

    public MultiUserAPI() {
        client = new OkHttpClient();
        gson = new GsonBuilder().create();
    }

    //public static final String Api="http://192.168.31.174:8080/api/v1/multi_users";
    public static final String Api="http://192.168.0.102:8080/api/v1/multi_users";
    //public static final String Api="http://172.20.10.2:8080/api/v1/multi_users";





    public void getMultiUserGames(String loginGuessing, CallbackMultiUserS callbackMultiUserS) {
        Request request = new Request.Builder()
                .url(Api + "/guess_word/" + loginGuessing)
                .build();

        Log.d("MultiUserApi", "GET /guess_word/" + loginGuessing);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callbackMultiUserS.onError(e);
                Log.e("MultiUserApi", "getMultiUserGames onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    Log.d("MultiUserApi", "GET response: " + jsonResponse);
                    try {
                        MultiUserModel[] multiUserArray = gson.fromJson(jsonResponse, MultiUserModel[].class);
                        callbackMultiUserS.onSuccess(multiUserArray);
                    } catch (Exception ex) {
                        callbackMultiUserS.onError(ex);
                        Log.e("MultiUserApi", "JSON parse error: " + ex.getMessage());
                    }
                } else {
                    callbackMultiUserS.onError(new Exception("GET failed: code " + response.code()));
                    Log.e("MultiUserApi", "GET failed: code " + response.code());
                }
            }
        });
    }


    public void saveMultiUser(MultiUserModel newWord, CallbackMultiUser callbackMultiUser) {
        // Сериализуем MultiUserModel в JSON
        String jsonBody = gson.toJson(newWord);

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(Api + "/new_guess_word")
                .put(body)
                .build();

        Log.d("MultiUserApi", "PUT /new_guess_word body: " + jsonBody);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callbackMultiUser.onError(e);
                Log.e("MultiUserApi", "saveMultiUser onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    Log.d("MultiUserApi", "PUT response: " + jsonResponse);
                    try {
                        // Сервер возвращает сохранённый объект MultiUser (один)
                        MultiUserModel saved = gson.fromJson(jsonResponse, MultiUserModel.class);
                        callbackMultiUser.onSuccess(saved);
                    } catch (Exception ex) {
                        callbackMultiUser.onError(ex);
                        Log.e("MultiUserApi", "JSON parse error: " + ex.getMessage());
                    }
                } else {
                    callbackMultiUser.onError(new Exception("PUT failed: code " + response.code()));
                    Log.e("MultiUserApi", "PUT failed: code " + response.code());
                }
            }
        });
    }


}
