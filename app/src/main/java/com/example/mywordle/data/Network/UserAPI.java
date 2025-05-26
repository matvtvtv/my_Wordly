package com.example.mywordle.data.Network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mywordle.data.model.PlayerModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserAPI {
    public UserAPI() {

    }
    public static final String Api="http://172.20.10.2:8080/api/v1/users";
    public void getUser(String login, CallbackUser callbackUser){
        OkHttpClient client= new OkHttpClient();
        Request request= new Request.Builder()
                .url(Api+"/"+login)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callbackUser.onErorr(e);
                Log.d("UserApi ","Get User: Erorr onFailure"+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    try (response){
                        if (response.body() != null){
                            String jsonResponse=response.body().string();
                            Log.d("UserApi ","Get User: Response: "+jsonResponse);
                            if(response.code()!=200){
                                Log.d("UserApi ","Get User: Code: "+response.code());
                                callbackUser.onErorr(new Exception("response code isn't 200"));
                            }
                            Gson gson= new GsonBuilder().create();
                            try {
                                if (jsonResponse.startsWith("{")){
                                    PlayerModel playerModel= gson.fromJson(jsonResponse, PlayerModel.class);
                                    callbackUser.onSuccess(playerModel);
                                }
                            } catch (Exception exception){
                                callbackUser.onErorr(exception);
                                Log.d("UserApi ","Get User: Erorr converting");

                            }
                        }
                    }catch (Exception exception){
                        callbackUser.onErorr(exception);
                        Log.d("UserApi ","Get User: Erorr response");

                    }
                }
                else{
                    callbackUser.onErorr(new Exception("Failed: "+response.code()));
                    Log.d("UserApi ","Get User: Failed: "+response.code());
                }
            }
        });
    }
}
