package com.freshworks.studio.Utility;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

    String GIF_JSONURL = "https://api.giphy.com";

    @GET("/v1/gifs/trending")
    Call<String> getTrendingResults(@Query("api_key") final String apiKey, @Query("limit") final int limit, @Query("offset") int offset);

    @GET("/v1/gifs/search")
    Call<String> getSearchingResults(@Query("api_key") final String apiKey, @Query("q") final String query, @Query("limit") final int limit, @Query("offset") int offset);

}
