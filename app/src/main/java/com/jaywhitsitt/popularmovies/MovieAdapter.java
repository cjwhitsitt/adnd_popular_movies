package com.jaywhitsitt.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jaywhitsitt.popularmovies.data.MovieBase;
import com.jaywhitsitt.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

interface MovieOnClickHandler {
    void onClick(MovieBase movie);
}

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    final static private String TAG = MovieAdapter.class.getSimpleName();

    private MovieBase[] mMovies;
    final private MovieOnClickHandler mOnClickHandler;
    final private int mExpectedItemWidth;

    public MovieAdapter(MovieOnClickHandler onClickHandler, int expectedItemWidth) {
        mOnClickHandler = onClickHandler;
        mExpectedItemWidth = expectedItemWidth;
    }

    public void setData(MovieBase[] movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_poster, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder movieAdapterViewHolder, int i) {
        ImageView imageView = movieAdapterViewHolder.mImageView;
        final ProgressBar loadingSpinner = movieAdapterViewHolder.mLoadingSpinner;
        MovieBase movie = mMovies[i];

        imageView.setImageDrawable(null);
        loadingSpinner.setVisibility(View.VISIBLE);

        Picasso.get()
                .load(NetworkUtils.urlStringForPosterImage(movie.imageUrl, mExpectedItemWidth))
                .error(R.drawable.ic_error_cloud)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        loadingSpinner.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Exception e) { }
                });
    }

    @Override
    public int getItemCount() {
        return mMovies == null ? 0 : mMovies.length;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;
        ProgressBar mLoadingSpinner;

        public MovieAdapterViewHolder(@NonNull final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_poster);
            mLoadingSpinner = itemView.findViewById(R.id.pb_poster_loading_spinner);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            MovieBase movie = mMovies[i];
            if (mOnClickHandler != null) {
                mOnClickHandler.onClick(movie);
            }
        }
    }

}
