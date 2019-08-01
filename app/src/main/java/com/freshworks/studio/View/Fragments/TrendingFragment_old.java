/*
package com.freshworks.studio.View.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.freshworks.studio.View.Adapters.TrendingAdapter_OLD;
import com.freshworks.studio.Model.GifData;
import com.freshworks.studio.R;
import com.freshworks.studio.Utility.Common;
import com.freshworks.studio.Utility.RetrofitInterface;
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TrendingFragment_old extends Fragment
{

    Context context;
    Common objCommon = new Common();

    RecyclerView recyclerView;

    GridLayoutManager mLayoutManager;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int trendingOffset = 10, trendingLimitCount = 10;
    int searchingOffset = 10, searchLimitCount = 10;

    String query = "";
    boolean isTrending = false, isSearching = false;

    TrendingAdapter_OLD adapter ;

    ArrayList<GifData> gifDataList = new ArrayList<>();


    public TrendingFragment_old(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trending_layout, container, false);

        recyclerView = view.findViewById(R.id.rvTrending);

        mLayoutManager = new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false);
       // mLayoutManager.setReverseLayout(true);
       // mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        final EditText etSearch = (EditText) view.findViewById(R.id.etSearch);
        final ImageButton ivSearch = (ImageButton) view.findViewById(R.id.ivSearch);

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (objCommon.isOnline(context)) {
                    query = etSearch.getText().toString();
                    if (!query.isEmpty() && query != null)
                    {
                        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(ivSearch.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                        getSearchGIF(query, searchLimitCount, 0, true);
                    }else
                        Toast.makeText(context, Common.ENTER_SEARCH_QUERY, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, Common.NETWORK_ERROR_MSG, Toast.LENGTH_SHORT).show();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0)
                {
                    trendingOffset = 0;
                    searchingOffset = 0;
                    trendingLimitCount = 10;
                    searchLimitCount = 10;
                    if (objCommon.isOnline(context))
                        getTrendingGIF(trendingLimitCount, 0, true);
                    else
                        Toast.makeText(context, Common.NETWORK_ERROR_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.addOnScrollListener(createInfiniteScrollListener());

       */
/* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            if (isTrending)
                            {
                                trendingOffset = trendingLimitCount;
                                trendingLimitCount += 50;
                                if (objCommon.isOnline(context))
                                    getTrendingGIF(trendingLimitCount, trendingOffset);
                                else
                                    Toast.makeText(context, Common.NETWORK_ERROR_MSG, Toast.LENGTH_SHORT).show();
                            } else if (isSearching) {

                                searchingOffset = searchLimitCount;
                                searchLimitCount += 50;
                                if (!query.isEmpty() && query != null)
                                    getSearchGIF(query, searchLimitCount, searchingOffset);
                                else
                                    Toast.makeText(context, Common.ENTER_SEARCH_QUERY, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });*//*


        return view;
    }

    private InfiniteScrollListener createInfiniteScrollListener() {
        return new InfiniteScrollListener(trendingLimitCount, mLayoutManager) {
            @Override
            public void onScrolledToEnd(final int firstVisibleItemPosition) {
                // load your items here
                // logic of loading items will be different depending on your specific use case

                // when new items are loaded, combine old and new items, pass them to your adapter
                // and call refreshView(...) method from InfiniteScrollListener class to refresh RecyclerView

                if (isTrending)
                {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            trendingOffset = trendingLimitCount;
                            trendingLimitCount += 10;
                            if (objCommon.isOnline(context))
                                getTrendingGIF(trendingLimitCount, trendingOffset, false);
                            else
                                Toast.makeText(context, Common.NETWORK_ERROR_MSG, Toast.LENGTH_SHORT).show();
                        }
                    }, 100);


                }
                else if (isSearching) {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            searchingOffset = searchLimitCount;
                            searchLimitCount += 10;
                            if (!query.isEmpty() && query != null)
                                getSearchGIF(query, searchLimitCount, searchingOffset, false);
                            else
                                Toast.makeText(context, Common.ENTER_SEARCH_QUERY, Toast.LENGTH_SHORT).show();
                        }
                    }, 100);

                }

                refreshView(recyclerView, new TrendingAdapter_OLD(context, gifDataList), firstVisibleItemPosition);
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (objCommon.isOnline(context))
            getTrendingGIF(trendingLimitCount, 0, true);
        else
            Toast.makeText(context, Common.NETWORK_ERROR_MSG, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getTrendingGIF(int limitCount, int offset, boolean isFirstTime) {
        isTrending = true;
        isSearching = false;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.GIF_JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);
        Call<String> call = api.getTrendingResults(Common.Giphy_App_Key, limitCount, offset);

        updateView(call, isFirstTime);
        loading = true;
    }

    @Override
    public void getSearchGIF(String query, int limitCount, int offset, boolean isFirstTime) {
        isSearching = true;
        isTrending = false;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.GIF_JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);
        Call<String> call = api.getSearchingResults(Common.Giphy_App_Key, query, limitCount, offset);

        updateView(call, isFirstTime);
        loading = true;
    }

    */
/*public void update(int limitCount, int movePosition)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.GIF_JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);
        Call<String> call = api.getTrendingResults(Common.Giphy_App_Key, limitCount, movePosition);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonResponse = response.body().toString();

                        try {

                            JSONObject obj = new JSONObject(jsonResponse);
                            JSONArray dataArray = obj.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {

                                GifData objGifData = new GifData();
                                JSONObject dataObj = dataArray.getJSONObject(i);

                                JSONObject objMeta = dataObj.getJSONObject("images");
                                objMeta = objMeta.getJSONObject("downsized");

                                objGifData.setImgURL(objMeta.getString("url"));
                                objGifData.setTitle(dataObj.getString("title"));
                                objGifData.setId(dataObj.getString("id"));

                                gifDataList.add(objGifData);
                            }

                           // adapter = new TrendingAdapter_OLD(context, gifDataList);
                           // recyclerView.setAdapter(adapter);
                            //  adapter.notifyItemInserted(gifDataList.size());
                            //  recyclerView.smoothScrollToPosition(movePosition);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Log.i("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log.e("", t.getMessage());
            }
        });
    }*//*

    @Override
    public void updateView(Call<String> call, final boolean isFirstTime) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonResponse = response.body().toString();

                        try {

                            JSONObject obj = new JSONObject(jsonResponse);
                            JSONArray dataArray = obj.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {

                                GifData objGifData = new GifData();
                                JSONObject dataObj = dataArray.getJSONObject(i);

                                JSONObject objMeta = dataObj.getJSONObject("images");
                                objMeta = objMeta.getJSONObject("downsized");

                                objGifData.setImgURL(objMeta.getString("url"));
                                objGifData.setTitle(dataObj.getString("title"));
                                objGifData.setId(dataObj.getString("id"));

                                gifDataList.add(objGifData);
                            }

                            if(isFirstTime) {
                                adapter = new TrendingAdapter_OLD(context, gifDataList);
                                recyclerView.setAdapter(adapter);
                            }
                            //  adapter.notifyItemInserted(gifDataList.size());
                          //  recyclerView.smoothScrollToPosition(movePosition);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                       // Log.i("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
               // Log.e("", t.getMessage());
            }
        });
    }
}*/
