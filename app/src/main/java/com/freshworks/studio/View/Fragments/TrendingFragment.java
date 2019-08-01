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

import com.freshworks.studio.Model.GifData;
import com.freshworks.studio.R;
import com.freshworks.studio.Utility.Common;
import com.freshworks.studio.Utility.PaginationScrollListener;
import com.freshworks.studio.Utility.RetrofitAPI;
import com.freshworks.studio.Utility.RetrofitInterface;
import com.freshworks.studio.View.Adapters.TrendingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingFragment extends Fragment implements TrendingInterface {
    Context context;
    Common objCommon = new Common();

    private TrendingAdapter adapter;
    private RetrofitInterface retrofitInterface;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;

    private boolean isLoading = false;

    private int trendingOffset = 10, trendingLimitCount = 10;
    private int searchingOffset = 10, searchLimitCount = 10;

    private String query = "";
    private boolean isTrending = false, isSearching = false;

    public TrendingFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trending_layout, container, false);

        recyclerView = view.findViewById(R.id.rvTrending);

        adapter = new TrendingAdapter(context);

        gridLayoutManager = new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;

                if (isTrending) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            trendingOffset = trendingLimitCount;
                            trendingOffset += 10;
                            if (objCommon.isOnline(context))
                                loadTrendingGIF(true);
                            else
                                Toast.makeText(context, Common.NETWORK_ERROR_MSG, Toast.LENGTH_SHORT).show();
                        }
                    }, 100);
                } else if (isSearching) {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            searchingOffset = searchLimitCount;
                            searchingOffset += 10;
                            if (!query.isEmpty() && query != null)
                                loadSearchingGIF(true);
                            else
                                Toast.makeText(context, Common.ENTER_SEARCH_QUERY, Toast.LENGTH_SHORT).show();
                        }
                    }, 100);

                }
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        retrofitInterface = RetrofitAPI.getClient().create(RetrofitInterface.class);

        final EditText etSearch = view.findViewById(R.id.etSearch);
        final ImageButton ivSearch = view.findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (objCommon.isOnline(context)) {
                    query = etSearch.getText().toString();
                    if (!query.isEmpty() && query != null) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(ivSearch.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                        loadSearchingGIF(false);

                    } else
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

                if (s.length() == 0) {
                    trendingOffset = 0;
                    searchingOffset = 0;
                    if (objCommon.isOnline(context))
                        loadTrendingGIF(false);
                    else
                        Toast.makeText(context, Common.NETWORK_ERROR_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadTrendingGIF(false);
        return view;
    }

    @Override
    public void loadTrendingGIF(final boolean isNextTime) {

        isTrending = true;
        isSearching = false;

        callTrendingGifApi().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Got data. Send it to adapter

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (isNextTime) {
                            adapter.removeLoadingFooter();
                            isLoading = false;
                        } else {
                            adapter = new TrendingAdapter(context);
                            recyclerView.setAdapter(adapter);
                        }

                        List<GifData> results = fetchResults(response);
                        adapter.addAll(results);
                        adapter.addLoadingFooter();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @Override
    public Call<String> callTrendingGifApi() {
        Call<String> call = retrofitInterface.getTrendingResults(Common.Giphy_App_Key, trendingLimitCount, trendingOffset);
        return call;
    }

    @Override
    public void loadSearchingGIF(final boolean isNextTime) {
        isSearching = true;
        isTrending = false;

        callSearchngGifApi().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Got data. Send it to adapter

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (isNextTime) {
                            adapter.removeLoadingFooter();
                            isLoading = false;
                        } else {
                            adapter = new TrendingAdapter(context);
                            recyclerView.setAdapter(adapter);
                        }

                        List<GifData> results = fetchResults(response);
                        adapter.addAll(results);
                        adapter.addLoadingFooter();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @Override
    public Call<String> callSearchngGifApi() {
        Call<String> call = retrofitInterface.getSearchingResults(Common.Giphy_App_Key, query, searchLimitCount, searchingOffset);
        return call;
    }

    @Override
    public List<GifData> fetchResults(Response<String> response) {
        ArrayList<GifData> gifDataList = new ArrayList<>();
        String jsonResponse = response.body();

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

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gifDataList;
    }
}