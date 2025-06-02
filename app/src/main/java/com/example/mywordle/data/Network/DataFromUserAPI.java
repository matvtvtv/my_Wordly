package com.example.mywordle.data.Network;

import android.telecom.Call;

import androidx.annotation.NonNull;

import com.example.mywordle.data.model.PlayerModel;
import com.example.mywordle.data.repository.PlayerRepository;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Response;

public class DataFromUserAPI {
    private final UserAPI userAPI;

    public DataFromUserAPI() {
        this.userAPI = new UserAPI();
    }

    public void getUser (String login, CallbackUser callbackUser){
        userAPI.getUser(login, new CallbackUser() {
            @Override
            public void onSuccess(PlayerModel playerModel) {
                callbackUser.onSuccess(playerModel);
            }

            @Override
            public void onError(Throwable throwable) {
                callbackUser.onError(throwable);
            }
        });
    };
    public void getEnter (String login, String password, CallbackUser callbackUser){
        userAPI.getEnter(login,password, new CallbackUser() {
            @Override
            public void onSuccess(PlayerModel playerModel) {
                callbackUser.onSuccess(playerModel);
            }

            @Override
            public void onError(Throwable throwable) {
                callbackUser.onError(throwable);
            }
        });
    };
    public void getRegistration (String login, String password, CallbackUser callbackUser){
        userAPI.getRegistration(login,password, new CallbackUser() {
            @Override
            public void onSuccess(PlayerModel playerModel) {
                callbackUser.onSuccess(playerModel);
            }

            @Override
            public void onError(Throwable throwable) {
                callbackUser.onError(throwable);
            }
        });
    };
    public void getTopUsers(CallbackUserS callbackUsers) {
        userAPI.getTopUsers(new CallbackUserS() {
            @Override
            public void onSuccess(PlayerModel[] playerModels) {
                callbackUsers.onSuccess(playerModels);
            }

            @Override
            public void onError(Throwable throwable) {
                callbackUsers.onError(throwable);
            }
        });
    }

    public void updateUser(PlayerModel player, CallbackUser callbackUser) {
        userAPI.updateUser(player, new CallbackUser() {
            @Override
            public void onSuccess(PlayerModel playerModel) {
                callbackUser.onSuccess(playerModel);
            }

            @Override
            public void onError(Throwable throwable) {
                callbackUser.onError(throwable);
            }
        });
    }




}
