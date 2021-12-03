package com.ambiguous.ambichat.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestController {
    private static final int HTTP_TIME_OUT = 60;

    static Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(logging);
        }
        return new Retrofit.Builder()
                .baseUrl("http://ambiguous.in/studentApp/")
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
