package com.jaywhitsitt.popularmovies;

import android.content.Intent;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jaywhitsitt.popularmovies.data.MovieBase;
import com.jaywhitsitt.popularmovies.utilities.MovieJsonUtils;
import com.jaywhitsitt.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieOnClickHandler {

    RecyclerView mRecyclerView;
    MovieAdapter mMovieAdapter;

    private static final String SORT_BY_POPULAR = "POPULAR";
    private static final String SORT_BY_TOP_RATED = "TOP_RATED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.main_title);

        mRecyclerView = findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadData(SORT_BY_POPULAR);
    }

    private void loadData(String sortSelection) {
        new FetchMoviesTask().execute(sortSelection);
    }

    @Override
    public void onClick(MovieBase movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TITLE, movie.title);
        intent.putExtra(Intent.EXTRA_UID, movie.id);
        startActivity(intent);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, MovieBase[]> {

        private final String TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected MovieBase[] doInBackground(String... strings) {
            String selection = strings.length > 0 && strings[0] != null ? strings[0] : SORT_BY_POPULAR;

            URL url;
            if (selection == SORT_BY_POPULAR) {
                url = NetworkUtils.urlForMostPopularMovies();
            } else if (selection == SORT_BY_TOP_RATED) {
                url = NetworkUtils.urlForTopRatedMovies();
            } else {
                Log.e(TAG, "Invalid sorting selection " + String.valueOf(selection));
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
        protected void onPostExecute(MovieBase[] movies) {
            mMovieAdapter.setData(movies);
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
        return super.onOptionsItemSelected(item);
    }
}
