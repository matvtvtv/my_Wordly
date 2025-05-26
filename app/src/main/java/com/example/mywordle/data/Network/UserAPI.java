package com.example.mywordle.data.Network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mywordle.R;
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
    public static final String Api="http://192.168.0.102:8080/api/v1/users";
    //public static final String Api="http://172.20.10.2:8080/api/v1/users";

    public void getEnter(String login,String password, CallbackUser callbackUser){
        OkHttpClient client= new OkHttpClient();
        Request request= new Request.Builder()
                .url(Api+"/entrance/"+login+"/"+password)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callbackUser.onError(e);
                Log.d("UserApi ","Get Enter: Erorr onFailure"+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    try (response){
                        if (response.body() != null){
                            String jsonResponse=response.body().string();
                            Log.d("UserApi ","Get Enter: Response: "+jsonResponse);



                            //todo Сделай диалог с не правильным аккаунтом
                            if(response.code()!=200){
                                Log.d("UserApi ","Get Enter: Code: "+response.code());
                                callbackUser.onError(new Exception("response code isn't 200"));
                            }
                            Gson gson= new GsonBuilder().create();
                            try {
                                if (jsonResponse.startsWith("{")){
                                    PlayerModel playerModel= gson.fromJson(jsonResponse, PlayerModel.class);
                                    callbackUser.onSuccess(playerModel);
                                }
                            } catch (Exception exception){
                                callbackUser.onError(exception);
                                Log.d("UserApi ","Get Enter: Error converting");

                            }
                        }
                    }catch (Exception exception){
                        callbackUser.onError(exception);
                        Log.d("UserApi ","Get Enter: Error response");

                    }
                }
                else{
                    callbackUser.onError(new Exception("Failed: "+response.code()));
                    Log.d("UserApi ","Get Enter: Failed: "+response.code());
                }
            }
        });
    }

    public void getRegistration(String login,String password, CallbackUser callbackUser){
        OkHttpClient client= new OkHttpClient();
        Request request= new Request.Builder()
                .url(Api+"/registration/"+login+"/"+password)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callbackUser.onError(e);
                Log.d("UserApi ","Get Enter: Error onFailure"+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    try (response){
                        if (response.body() != null){
                            String jsonResponse=response.body().string();
                            Log.d("UserApi ","Get Enter: Response: "+jsonResponse);



                            //todo Сделай диалог с не правильным аккаунтом
                            if(response.code()!=200){
                                Log.d("UserApi ","Get Enter: Code: "+response.code());
                                callbackUser.onError(new Exception("response code isn't 200"));
                            }
                            Gson gson= new GsonBuilder().create();
                            try {
                                if (jsonResponse.startsWith("{")){
                                    PlayerModel playerModel= gson.fromJson(jsonResponse, PlayerModel.class);
                                    callbackUser.onSuccess(playerModel);
                                }
                            } catch (Exception exception){
                                callbackUser.onError(exception);
                                Log.d("UserApi ","Get Enter: Error converting");

                            }
                        }
                    }catch (Exception exception){
                        callbackUser.onError(exception);
                        Log.d("UserApi ","Get Enter: Error response");

                    }
                }
                else{
                    callbackUser.onError(new Exception("Failed: "+response.code()));
                    Log.d("UserApi ","Get Enter: Failed: "+response.code());
                }
            }
        });
    }
    public void getUser(String login, CallbackUser callbackUser){
        OkHttpClient client= new OkHttpClient();
        Request request= new Request.Builder()
                .url(Api+"/"+login)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callbackUser.onError(e);
                Log.d("UserApi ","Get User: Error onFailure"+e.getMessage());
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
                                callbackUser.onError(new Exception("response code isn't 200"));
                            }
                            Gson gson= new GsonBuilder().create();
                            try {
                                if (jsonResponse.startsWith("{")){
                                    PlayerModel playerModel= gson.fromJson(jsonResponse, PlayerModel.class);
                                    callbackUser.onSuccess(playerModel);
                                }
                            } catch (Exception exception){
                                callbackUser.onError(exception);
                                Log.d("UserApi ","Get User: Error converting");

                            }
                        }
                    }catch (Exception exception){
                        callbackUser.onError(exception);
                        Log.d("UserApi ","Get User: Error response");

                    }
                }
                else{
                    callbackUser.onError(new Exception("Failed: "+response.code()));
                    Log.d("UserApi ","Get User: Failed: "+response.code());
                }
            }
        });
    }



}
