package com.example.djshichaoren.googleocrtest2.util;

import android.util.Log;

import com.example.djshichaoren.googleocrtest2.http.HttpService;
import com.example.djshichaoren.googleocrtest2.http.bean.JinshanTranslation;
import com.example.djshichaoren.googleocrtest2.http.bean.SubtitleDetailResult;
import com.example.djshichaoren.googleocrtest2.http.bean.SubtitleSearchResult;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubtitleHttpUtil {

    private Retrofit mRetrofit;
    private HttpService mHttpService;
    private static final String SHOOTER_KEY = "PLF8Gi5WChOe4vJThZhmFga8kcs91Ief";

    public SubtitleHttpUtil(){
        OkHttpClient client = new OkHttpClient();
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.assrt.net/v1/sub/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        mHttpService = mRetrofit.create(HttpService.class);
    }

    public void search(String fileName, SearchSubtitleCallback searchSubtitleCallback){
        Call<SubtitleSearchResult> call = mHttpService.searchSubtitle(SHOOTER_KEY, fileName, 0);
        call.enqueue(new Callback<SubtitleSearchResult>() {
            @Override
            public void onResponse(Call<SubtitleSearchResult> call, Response<SubtitleSearchResult> response) {
                SubtitleSearchResult subtitleSearchResult = response.body();
                if(subtitleSearchResult != null){
                    if(subtitleSearchResult.status == 0){
                        searchSubtitleCallback.success(subtitleSearchResult);
                    }
                    else{
                        Log.d("lwd", "search subtitle error status:" + subtitleSearchResult.status);
                    }

                }
            }

            @Override
            public void onFailure(Call<SubtitleSearchResult> call, Throwable t) {
                t.printStackTrace();
                searchSubtitleCallback.error();
            }
        });
    }

    public void queryDetail(int id, QuerySubtitleDetailCallback querySubtitleDetailCallback){
        Call<SubtitleDetailResult> call = mHttpService.querySubtitleDetail(SHOOTER_KEY, id);
        call.enqueue(new Callback<SubtitleDetailResult>() {
            @Override
            public void onResponse(Call<SubtitleDetailResult> call, Response<SubtitleDetailResult> response) {
                SubtitleDetailResult subtitleDetailResult = response.body();
                if(subtitleDetailResult != null){
                    if(subtitleDetailResult.status == 0){
                        querySubtitleDetailCallback.success(subtitleDetailResult);
                    }
                }
            }

            @Override
            public void onFailure(Call<SubtitleDetailResult> call, Throwable t) {
                t.printStackTrace();
                querySubtitleDetailCallback.error();
            }
        });

    }

    public interface SearchSubtitleCallback{
        public void success(SubtitleSearchResult subtitleSearchResult);
        public void error();
    }

    public interface QuerySubtitleDetailCallback{
        public void success(SubtitleDetailResult subtitleDetailResult);
        public void error();
    }

}
