package com.jaywhitsitt.popularmovies;

import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jaywhitsitt.popularmovies.data.Movie;
import com.jaywhitsitt.popularmovies.utilities.MovieJsonUtils;
import com.jaywhitsitt.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter();
        mRecyclerView.setAdapter(mMovieAdapter);

        loadData();
    }

    private void loadData() {
        new FetchMoviesTask().execute();
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, Movie[]> {

        private final String TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected Movie[] doInBackground(Void... voids) {
            URL url = NetworkUtils.urlForMostPopularMovies();
            try {
                String jsonString = NetworkUtils.getResponseFromHttpUrl(url);
                Log.i(TAG, jsonString == null ? "null" : jsonString);
                Movie[] movies = MovieJsonUtils.moviesFromJson(jsonString);
                return movies;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mMovieAdapter.setData(movies);
        }

    }

}
