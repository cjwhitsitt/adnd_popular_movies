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

import com.jaywhitsitt.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MovieAdapter mMovieAdapter;

    String mData;

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

    public class FetchMoviesTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url = NetworkUtils.urlForMostPopularMovies();
            try {
                String data = NetworkUtils.getResponseFromHttpUrl(url);
                return data;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            mData = s;
            Log.i("FetchMoviesTask", s == null ? "null" : s);
        }

    }

}
