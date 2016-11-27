package com.pkh.sopt_19th_6.application;

import android.app.Application;

import com.pkh.sopt_19th_6.network.NetworkService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kh on 2016. 11. 21..
 */
public class ApplicationController extends Application {

    private static ApplicationController instance;

    private static String baseUrl = "serverIP";

    private NetworkService networkService;

    public static ApplicationController getInstance() {
        return instance;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;

        buildService();
    }

    public void buildService() {


        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        networkService = retrofit.create(NetworkService.class);
    }
}
