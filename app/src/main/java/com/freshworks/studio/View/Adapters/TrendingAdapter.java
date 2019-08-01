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
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifDrawableBuilder;
import pl.droidsonroids.gif.GifImageView;

public class TrendingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<GifData> gifDataList;
    private Context context;

    private boolean isLoadingAdded = false;

    public TrendingAdapter(Context context) {
        this.context = context;
        gifDataList = new ArrayList<>();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_photo, parent, false);
        viewHolder = new GIFViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        GifData model = gifDataList.get(position);

        switch (getItemViewType(position)) {

            case ITEM:

                final GIFViewHolder gifViewHolder = (GIFViewHolder) holder;

                gifViewHolder.ivLike.bringToFront();
                gifViewHolder.ivLike.setTag(position);

                String url = model.getImgURL();
                String gif_id = model.getId();

                LikeDetails sql_LikeDetails = new LikeDetails(context);
                sql_LikeDetails.open();

                if (sql_LikeDetails.isLike(gif_id))
                    gifViewHolder.ivLike.setImageResource(R.drawable.ic_like);
                else
                    gifViewHolder.ivLike.setImageResource(R.drawable.ic_dislike);


                Glide.with(context)
                        .load(url)
                        .asGif()
                        .toBytes()
                        .thumbnail(0.1f)
                        // .override(GIF_IMAGE_WIDTH_PIXELS, GIF_IMAGE_HEIGHT_PIXELS)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(new SimpleTarget<byte[]>() {

                            @Override
                            public void onResourceReady(final byte[] resource,
                                                        final GlideAnimation<? super byte[]> glideAnimation) {
                                final GifDrawable gifDrawable;
                                try {
                                    gifDrawable = new GifDrawableBuilder().from(resource).build();
                                    gifViewHolder.gifImageView.setImageDrawable(gifDrawable);
                                } catch (final IOException e) {
                                    gifViewHolder.gifImageView.setImageResource(R.mipmap.ic_launcher);
                                }
                                gifViewHolder.gifImageView.setVisibility(View.VISIBLE);
                                gifViewHolder.ivLike.setVisibility(View.VISIBLE);
                                gifViewHolder.progressBar.setVisibility(View.INVISIBLE);

                            }
                        });

                gifViewHolder.ivLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        LikeDetails sql_LikeDetails = new LikeDetails(context);
                        sql_LikeDetails.open();

                        int position = (Integer) v.getTag();

                        final GifData model = gifDataList.get(position);
                        String url = model.getImgURL();
                        String title = model.getTitle();
                        String gif_id = model.getId();

                        if (sql_LikeDetails.isLike(gif_id)) {
                            sql_LikeDetails.removeFavourite(gif_id);
                            gifViewHolder.ivLike.setImageResource(R.drawable.ic_dislike);
                        } else {
                            String like = "Y";
                            ContentValues cv = new ContentValues();
                            cv.put(LikeDetails.URL, url);
                            cv.put(LikeDetails.TITLE, title);
                            cv.put(LikeDetails.GIF_ID, gif_id);
                            cv.put(LikeDetails.LIKE, like);
                            sql_LikeDetails.insert(cv);
                            gifViewHolder.ivLike.setImageResource(R.drawable.ic_like);
                        }

                        sql_LikeDetails.close();
                    }
                });

            case LOADING:
//                Do nothing
                break;

        }

    }

    @Override
    public int getItemCount() {

        return gifDataList == null ? 0 : gifDataList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return (position == gifDataList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }



     /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(GifData r) {
        gifDataList.add(r);
        notifyItemInserted(gifDataList.size() - 1);
    }

    public void addAll(List<GifData> moveResults) {
        for (GifData result : moveResults) {
            add(result);
        }
    }

    public void remove(GifData r) {
        int position = gifDataList.indexOf(r);
        if (position > -1) {
            gifDataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new GifData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = gifDataList.size() - 1;
        GifData result = getItem(position);

        if (result != null) {
            gifDataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public GifData getItem(int position) {
        return gifDataList.get(position);
    }


    /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class GIFViewHolder extends RecyclerView.ViewHolder {

        public final ProgressBar progressBar;
        public final GifImageView gifImageView;
        public final ImageView ivLike;

        public GIFViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.gif_progress);
            gifImageView = view.findViewById(R.id.gif_image);
            ivLike = view.findViewById(R.id.ivLike);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
