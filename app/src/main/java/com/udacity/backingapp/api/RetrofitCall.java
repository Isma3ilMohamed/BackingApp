package com.udacity.backingapp.api;

import android.support.annotation.NonNull;

import com.udacity.backingapp.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by The Dev Wolf on 18-03-2018.
 */

public class RetrofitCall {

    @NonNull
    public static Retrofit getRecipes() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();


        if (BuildConfig.DEBUG_MODE) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);
        }

        return new Retrofit.Builder()
                .baseUrl(Constants.COOKING_DATA)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
//                .client(builder.build())
                .build();

    }
}
