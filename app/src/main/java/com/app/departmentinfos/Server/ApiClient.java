package com.app.departmentinfos.Server;

import com.app.departmentinfos.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = Config.BASE_URL;
    public static Retrofit retrofit = null;

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getApiClient() {

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;

    }

}