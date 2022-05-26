package com.example.myapplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static RetrofitManager mInstance = new RetrofitManager();

    private BinanceApiService binanceApiService;

    private RetrofitManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.yshyqxx.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        binanceApiService = retrofit.create(BinanceApiService.class);
    }

    public static RetrofitManager getInstance() {
        return mInstance;
    }

    public BinanceApiService getAPI() {
        return binanceApiService;
    }
}