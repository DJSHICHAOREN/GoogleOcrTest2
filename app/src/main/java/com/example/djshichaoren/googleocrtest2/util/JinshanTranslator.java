package com.example.djshichaoren.googleocrtest2.util;

import android.util.Log;

import com.example.djshichaoren.googleocrtest2.core.word.translate.Translator;
import com.example.djshichaoren.googleocrtest2.http.HttpService;
import com.example.djshichaoren.googleocrtest2.http.bean.JinshanTranslation;
import com.example.djshichaoren.googleocrtest2.models.TranslateResult;
import com.example.djshichaoren.googleocrtest2.services.WorkService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/8/2 20:38
 * 修改备注：
 */
public class JinshanTranslator implements Translator {

    private Retrofit mRetrofit;
    private HttpService mHttpService;
    private static final String JINSHAN_KEY = "9D24A73F992E4B2A00E86A1780814E90";

    public enum StringType{WORD, SENTENCE}

    public JinshanTranslator(){
        OkHttpClient client = new OkHttpClient();
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://dict-co.iciba.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        mHttpService = mRetrofit.create(HttpService.class);
    }

    @Override
    public void translate(String word, TranslateCallback translateCallback) {
        Call<JinshanTranslation> call = mHttpService.jinshanTranslate(word, JINSHAN_KEY, "json");
        call.enqueue(new Callback<JinshanTranslation>() {
            @Override
            public void onResponse(Call<JinshanTranslation> call, Response<JinshanTranslation> response) {
                JinshanTranslation jinshanTranslation = response.body();

                if(jinshanTranslation != null){
                    Log.d("lwd", "translation word:" + jinshanTranslation.getWord_name());
                    translateCallback.success(new TranslateResult(jinshanTranslation));
                }
                else{
                    Log.d("lwd", "onResponse jinshanTranslation is null");
                }
            }

            @Override
            public void onFailure(Call<JinshanTranslation> call, Throwable t) {
                Log.d("lwd", "onFailure get translation msg:" + t.getMessage());
            }
        });
    }



    public void translate(String word,
                          final WorkService.TranslationResultDisplayer translationResultDisplayer,
                          final StringType stringType){
        Call<JinshanTranslation> call = mHttpService.jinshanTranslate(word, JINSHAN_KEY, "json");
        call.enqueue(new Callback<JinshanTranslation>() {
            @Override
            public void onResponse(Call<JinshanTranslation> call, Response<JinshanTranslation> response) {
                JinshanTranslation jinshanTranslation = response.body();

                if(jinshanTranslation != null){
                    Log.d("lwd", "translation word:" + jinshanTranslation.getWord_name());
                    if(stringType == StringType.WORD){
                        translationResultDisplayer.setWordResult(jinshanTranslation);
                    }
                    else if(stringType == StringType.SENTENCE){
                        translationResultDisplayer.setSentenceResult(jinshanTranslation);
                    }
                }
                else{
                    Log.d("lwd", "onResponse jinshanTranslation is null");
                }
            }

            @Override
            public void onFailure(Call<JinshanTranslation> call, Throwable t) {
                Log.d("lwd", "onFailure get translation msg:" + t.getMessage());
            }
        });
    }




}
