package com.seoulapp.findhiddenseoul;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JIYOUNGKIM on 2017-10-10.
 */

public class ApplicationController extends Application {
    private static ApplicationController instance;

    public static ApplicationController getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;
    }

    private NetworkService networkService;

    public NetworkService getNetworkService() {
        return networkService;
    }

    private String baseUrl;

    public void buildNetworkService() {
        synchronized (ApplicationController.class) {
            if (networkService == null) {
                baseUrl = String.format("http://13.124.223.47:3000/");
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                GsonConverterFactory factory = GsonConverterFactory.create(gson);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(factory)
                        .build();
                networkService = retrofit.create(NetworkService.class);
            }
        }
    }
}
