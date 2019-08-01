package com.freshworks.studio.View.Fragments;

import com.freshworks.studio.Model.GifData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public interface TrendingInterface {
    void loadTrendingGIF(final boolean isNextTime);

    Call<String> callTrendingGifApi();

    void loadSearchingGIF(final boolean isNextTime);

    Call<String> callSearchngGifApi();

    List<GifData> fetchResults(Response<String> response);
}
