package com.example.mywordle.data.Network;

import com.example.mywordle.data.model.MultiUserModel;

public class DataFromMultiUserAPI {

    private final MultiUserAPI multiUserAPI;

    public DataFromMultiUserAPI() {
        this.multiUserAPI = new MultiUserAPI();
    }

    public void getMultiUserGames(String loginGuessing, CallbackMultiUserS callbackMultiUserS) {
        multiUserAPI.getMultiUserGames(loginGuessing,new CallbackMultiUserS() {
            @Override
            public void onSuccess(MultiUserModel[] multiUserModel) {
                callbackMultiUserS.onSuccess(multiUserModel);
            }

            @Override
            public void onError(Throwable throwable) {
                callbackMultiUserS.onError(throwable);
            }
        });
    }

    public void saveMultiUser(MultiUserModel multiUserModel, CallbackMultiUser callbackMultiUser){
        multiUserAPI.saveMultiUser(multiUserModel, new CallbackMultiUser() {
            @Override
            public void onSuccess(MultiUserModel multiUserModel) {
                callbackMultiUser.onSuccess(multiUserModel);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });


    }





}
