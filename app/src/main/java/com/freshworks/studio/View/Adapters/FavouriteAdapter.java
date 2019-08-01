package com.freshworks.studio.View.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.freshworks.studio.Model.Database.LikeDetails;
import com.freshworks.studio.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifDrawableBuilder;
import pl.droidsonroids.gif.GifImageView;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {
    Context context;

    static final String TAG = FavouriteAdapter.class.getSimpleName();
    private static final int GIF_IMAGE_HEIGHT_PIXELS = 128;
    private static final int GIF_IMAGE_WIDTH_PIXELS = GIF_IMAGE_HEIGHT_PIXELS;
    private Cursor cursor;
    private RecyclerView rvFavourite;

    public FavouriteAdapter(Context context, Cursor cursor, RecyclerView rvFavourite) {
        this.context = context;
        this.cursor = cursor;
        this.rvFavourite = rvFavourite;
    }

    @NonNull
    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new FavouriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavouriteAdapter.ViewHolder holder, int position) {
        holder.ivLike.bringToFront();

        cursor.moveToPosition(position);
        String url = cursor.getString(cursor.getColumnIndex(LikeDetails.URL));
        String gif_id = cursor.getString(cursor.getColumnIndex(LikeDetails.GIF_ID));

        holder.ivLike.setTag(gif_id);

        LikeDetails sql_LikeDetails = new LikeDetails(context);
        sql_LikeDetails.open();

        holder.ivLike.setImageResource(R.drawable.ic_like);

        Glide.with(context)
                .load(url)
                .asGif()
                .toBytes()
                .thumbnail(0.1f)
                .override(GIF_IMAGE_WIDTH_PIXELS, GIF_IMAGE_HEIGHT_PIXELS)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .into(new SimpleTarget<byte[]>() {

                    @Override
                    public void onResourceReady(final byte[] resource,
                                                final GlideAnimation<? super byte[]> glideAnimation) {

                        final GifDrawable gifDrawable;
                        try {
                            gifDrawable = new GifDrawableBuilder().from(resource).build();
                            holder.gifImageView.setImageDrawable(gifDrawable);
                        } catch (final IOException e) {
                            holder.gifImageView.setImageResource(R.mipmap.ic_launcher);
                        }
                        holder.gifImageView.setVisibility(View.VISIBLE);
                        holder.ivLike.setVisibility(View.VISIBLE);
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        if (Log.isLoggable(TAG, Log.INFO)) {
                        }
                    }
                });

        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LikeDetails sql_LikeDetails = new LikeDetails(context);
                sql_LikeDetails.open();

                String gif_id = v.getTag().toString();
                sql_LikeDetails.removeFavourite(gif_id);

                Cursor cursor = sql_LikeDetails.getData();
                cursor.moveToFirst();

                FavouriteAdapter adapter = new FavouriteAdapter(context, cursor, rvFavourite);
                rvFavourite.setAdapter(adapter);
                sql_LikeDetails.close();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ProgressBar progressBar;
        public final GifImageView gifImageView;
        public final ImageView ivLike;

        public ViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.gif_progress);
            gifImageView = view.findViewById(R.id.gif_image);
            ivLike = view.findViewById(R.id.ivLike);
        }
    }
}
