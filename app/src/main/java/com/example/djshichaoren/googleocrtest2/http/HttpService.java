package com.example.djshichaoren.googleocrtest2.http;

import com.example.djshichaoren.googleocrtest2.http.bean.JinshanTranslation;
import com.example.djshichaoren.googleocrtest2.http.bean.SubtitleDetailResult;
import com.example.djshichaoren.googleocrtest2.http.bean.SubtitleSearchResult;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/8/2 18:27
 * 修改备注：
 */
public interface HttpService {
    @GET("api/dictionary.php")
    Call<JinshanTranslation> jinshanTranslate(@Query("w") String word, @Query("key") String key, @Query("type") String type);

    @POST("search")
    Call<SubtitleSearchResult> searchSubtitle(@Query("token") String token, @Query("q") String q, @Query("pos") int pos);

    @POST("detail")
    Call<SubtitleDetailResult> querySubtitleDetail(@Query("token") String token, @Query("id") int id);
}
