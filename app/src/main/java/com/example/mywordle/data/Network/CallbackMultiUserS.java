package com.example.mywordle.data.Network;

import com.example.mywordle.data.model.MultiUserModel;

public interface CallbackMultiUserS {

        void onSuccess (MultiUserModel[] multiUserModel);
        void onError(Throwable throwable);


}
