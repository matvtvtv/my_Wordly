package com.example.mywordle.data.Network;

import com.example.mywordle.data.model.PlayerModel;

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
}
