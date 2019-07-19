package com.jaywhitsitt.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jaywhitsitt.popularmovies.database.Database;
import com.jaywhitsitt.popularmovies.data.MovieBase;
import com.jaywhitsitt.popularmovies.utilities.MovieJsonUtils;
import com.jaywhitsitt.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieOnClickHandler {

    RecyclerView mRecyclerView;
    ProgressBar mLoadingSpinner;
    ImageView mErrorImageView;
    MovieAdapter mMovieAdapter;

    LiveData<List<MovieBase>> mFavoriteMovies;

    private static final String SORT_BY_POPULAR = "POPULAR";
    private static final String SORT_BY_TOP_RATED = "TOP_RATED";
    private static final String SORT_BY_FAVORITES = "FAVORITES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.main_title);

        mRecyclerView = findViewById(R.id.recycler_view);
        mLoadingSpinner = findViewById(R.id.pb_loading_spinner);
        mErrorImageView = findViewById(R.id.iv_error);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        mMovieAdapter = new MovieAdapter(this, size.x/2);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadData(SORT_BY_POPULAR);
    }

    private void loadData(String sortSelection) {
        if (sortSelection.equals(SORT_BY_FAVORITES)) {
            if (mFavoriteMovies == null) {
                mFavoriteMovies = Database.databaseInstance(this).movieBaseDao().getFavoriteMovies();
            }
            mFavoriteMovies.observe(this, new Observer<List<MovieBase>>() {
                @Override
                public void onChanged(List<MovieBase> movieBases) {
                    // TODO: use List<>
                    mMovieAdapter.setData(movieBases);
                    mRecyclerView.scrollToPosition(0);
                }
            });
            return;
        }

        if (mFavoriteMovies != null) {
            mFavoriteMovies.removeObservers(this);
        }
        new FetchMoviesTask().execute(sortSelection);
    }

    private void showError() {
        mErrorImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(MovieBase movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TITLE, movie.title);
        intent.putExtra(Intent.EXTRA_UID, movie.id);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    public class FetchMoviesTask extends AsyncTask<String, Void, List<MovieBase>> {

        private final String TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            mLoadingSpinner.setVisibility(View.VISIBLE);
            mErrorImageView.setVisibility(View.GONE);
        }

        @Override
        protected List<MovieBase> doInBackground(String... strings) {
            String selection = strings.length > 0 && strings[0] != null ? strings[0] : SORT_BY_POPULAR;

            URL url;
            if (selection.equals(SORT_BY_POPULAR)) {
                url = NetworkUtils.urlForMostPopularMovies();
            } else if (selection.equals(SORT_BY_TOP_RATED)) {
                url = NetworkUtils.urlForTopRatedMovies();
            } else {
                Log.e(TAG, "Invalid sorting selection " + selection);
                return null;
            }

            try {
                String jsonString = NetworkUtils.getResponseFromHttpUrl(url);
                Log.i(TAG, jsonString == null ? "null" : jsonString);
                return MovieJsonUtils.moviesFromJson(jsonString);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MovieBase> movies) {
            if (movies == null) {
                showError();
            } // TODO: if 0, show no content
            mMovieAdapter.setData(movies);
            mLoadingSpinner.setVisibility(View.GONE);
            mRecyclerView.scrollToPosition(0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort_popular) {
            loadData(SORT_BY_POPULAR);
            return true;
        }
        if (item.getItemId() == R.id.action_sort_top_rated) {
            loadData(SORT_BY_TOP_RATED);
            return true;
        }
        if (item.getItemId() == R.id.action_sort_favorites) {
            loadData(SORT_BY_FAVORITES);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
