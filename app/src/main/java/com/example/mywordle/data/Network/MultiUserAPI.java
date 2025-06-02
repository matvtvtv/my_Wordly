package com.example.mywordle.data.Network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mywordle.data.model.MultiUserModel;
import com.example.mywordle.data.model.PlayerModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MultiUserAPI {
    public MultiUserAPI() {

    }
    //public static final String Api="http://192.168.31.174:8080/api/v1/multi_users";
    //public static final String Api="http://192.168.0.102:8080/api/v1/multi_users";
    public static final String Api="http://172.20.10.2:8080/api/v1/multi_users";

    public void getMultiUserGames(String loginGuessing , CallbackMultiUser callbackMultiUser){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Api + "/guess_word/"+loginGuessing)
                .build();
        Log.d("MultiUserApi", "loginGuessing"+loginGuessing );

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callbackMultiUser.onError(e);
                Log.d("UserApi", "getTopUsers: Error onFailure " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try (response) {
                        if (response.body() != null) {
                            String jsonResponse = response.body().string();
                            Log.d("MultiUserApi", "getMultiUser: Response: " + jsonResponse);

                            Gson gson = new GsonBuilder().create();
                            try {
                                MultiUserModel[] multiUserArray = gson.fromJson(jsonResponse, MultiUserModel[].class);
                                callbackMultiUser.onSuccess(multiUserArray);
                            } catch (Exception exception) {
                                callbackMultiUser.onError(exception);
                                Log.d("MultiUserApi", "getMultiUser: Error converting JSON");
                            }
                        }
                    } catch (Exception exception) {
                        callbackMultiUser.onError(exception);
                        Log.d("MultiUserApi", "getMultiUser: Error response");
                    }
                } else {
                    callbackMultiUser.onError(new Exception("Failed: " + response.code()));
                    Log.d("MultiUserApi", "getMultiUser: Failed: " + response.code());
                }
            }
        });
    }





}
