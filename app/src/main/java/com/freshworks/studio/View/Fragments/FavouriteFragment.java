package com.freshworks.studio.View.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.freshworks.studio.Model.Database.LikeDetails;
import com.freshworks.studio.R;
import com.freshworks.studio.Utility.Common;
import com.freshworks.studio.View.Adapters.FavouriteAdapter;

public class FavouriteFragment extends Fragment implements FavouriteInterface {
    Context context;
    Common objCommon = new Common();

    RecyclerView recyclerView;

    public FavouriteFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourite_layout, container, false);

        recyclerView = view.findViewById(R.id.rvFavourite);

        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(mLayoutManager);

        loadData();

        return view;
    }

    @Override
    public void loadData() {
        LikeDetails sql_LikeDetails = new LikeDetails(context);
        sql_LikeDetails.open();

        Cursor cursor = sql_LikeDetails.getData();
        cursor.moveToFirst();

        FavouriteAdapter adapter = new FavouriteAdapter(context, cursor, recyclerView);
        recyclerView.setAdapter(adapter);
    }

}
