package com.example.mywordle.data.Network;

import com.example.mywordle.data.model.MultiUserModel;
import com.example.mywordle.data.model.PlayerModel;

public class DataFromMultiUserAPI {

    private final MultiUserAPI multiUserAPI;

    public DataFromMultiUserAPI() {
        this.multiUserAPI = new MultiUserAPI();
    }

    public void getMultiUserGames(String loginGuessing, CallbackMultiUser callbackMultiUser) {
        multiUserAPI.getMultiUserGames(loginGuessing,new CallbackMultiUser() {
            @Override
            public void onSuccess(MultiUserModel[] multiUserModel) {
                callbackMultiUser.onSuccess(multiUserModel);
            }

            @Override
            public void onError(Throwable throwable) {
                callbackMultiUser.onError(throwable);
            }
        });
    }





}
