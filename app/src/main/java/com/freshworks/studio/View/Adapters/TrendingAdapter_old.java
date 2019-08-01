/*
package com.freshworks.studio.View.Adapters;

import android.content.ContentValues;
import android.content.Context;
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
import com.freshworks.studio.Model.GifData;
import com.freshworks.studio.R;

import java.io.IOException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifDrawableBuilder;
import pl.droidsonroids.gif.GifImageView;

public class TrendingAdapter_OLD extends RecyclerView.Adapter<TrendingAdapter_OLD.ViewHolder>
{
    Context context;

    static final String TAG = TrendingAdapter_OLD.class.getSimpleName();
    private static final int GIF_IMAGE_HEIGHT_PIXELS = 128;
    private static final int GIF_IMAGE_WIDTH_PIXELS = GIF_IMAGE_HEIGHT_PIXELS;
    private ArrayList<GifData> gifDataList;

    public TrendingAdapter_OLD(Context context, ArrayList<GifData> gifDataList)
    {
        this.context = context;
        this.gifDataList = gifDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.ivLike.bringToFront();
        holder.ivLike.setTag(position);

        final GifData model = gifDataList.get(position);
        String url = model.getImgURL();
        String gif_id = model.getId();

        LikeDetails sql_LikeDetails = new LikeDetails(context);
        sql_LikeDetails.open();

        if(sql_LikeDetails.isLike(gif_id))
           holder.ivLike.setImageResource(R.drawable.ic_like);
        else
            holder.ivLike.setImageResource(R.drawable.ic_dislike);


        Glide.with(context)
                .load(url)
                .asGif()
                .toBytes()
                .thumbnail(0.1f)
                .override(GIF_IMAGE_WIDTH_PIXELS, GIF_IMAGE_HEIGHT_PIXELS)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .into(new SimpleTarget<byte[]>() {

                    @Override public void onResourceReady(final byte[] resource,
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

                    }
                });

        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LikeDetails sql_LikeDetails = new LikeDetails(context);
                sql_LikeDetails.open();

                int position = (Integer) v.getTag();

                final GifData model = gifDataList.get(position);
                String url = model.getImgURL();
                String title = model.getTitle();
                String gif_id = model.getId();

                if(sql_LikeDetails.isLike(gif_id))
                {
                    sql_LikeDetails.removeFavourite(gif_id);
                    holder.ivLike.setImageResource(R.drawable.ic_dislike);
                }
                else
                {
                    String like = "Y";
                    ContentValues cv = new ContentValues();
                    cv.put(LikeDetails.URL, url);
                    cv.put(LikeDetails.TITLE, title);
                    cv.put(LikeDetails.GIF_ID, gif_id);
                    cv.put(LikeDetails.LIKE, like);
                    sql_LikeDetails.insert(cv);
                    holder.ivLike.setImageResource(R.drawable.ic_like);
                }

                sql_LikeDetails.close();
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return gifDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public final ProgressBar progressBar;
        public final GifImageView gifImageView;
        public final ImageView ivLike;

        public ViewHolder(View view)
        {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.gif_progress);
            gifImageView = (GifImageView) view.findViewById(R.id.gif_image);
            ivLike = (ImageView) view.findViewById(R.id.ivLike);
        }


    }
}
*/
