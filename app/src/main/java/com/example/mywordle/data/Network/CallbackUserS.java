package com.example.mywordle.data.Network;

import com.example.mywordle.data.model.PlayerModel;

public interface CallbackUserS {
    void onSuccess (PlayerModel[] playerModel);
    void onError(Throwable throwable);
}
