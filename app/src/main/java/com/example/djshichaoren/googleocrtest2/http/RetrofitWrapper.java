package com.example.djshichaoren.googleocrtest2.http;

import com.example.djshichaoren.googleocrtest2.BuildConfig;
import com.example.djshichaoren.googleocrtest2.config.Config;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/8/2 18:28
 * 修改备注：
 */
public class RetrofitWrapper {
    private static RetrofitWrapper instance;

    private Retrofit retrofit;

    public RetrofitWrapper(){
        OkHttpClient client = new OkHttpClient();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(loggingInterceptor)
                    .build();

        }
        retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    public RetrofitWrapper getInstance(){
        if(instance == null){
            synchronized (RetrofitWrapper.class){
                instance = new RetrofitWrapper();
            }
        }
        return instance;
    }
    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }


}
