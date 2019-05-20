package com.jaywhitsitt.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.jaywhitsitt.popularmovies.data.MovieDetail;
import com.jaywhitsitt.popularmovies.utilities.MovieJsonUtils;
import com.jaywhitsitt.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        String title = "Loading...";
        if (intent.hasExtra(Intent.EXTRA_TITLE)) {
            title = intent.getStringExtra(Intent.EXTRA_TITLE);
        }
        getSupportActionBar().setTitle(title);

        if (intent.hasExtra(Intent.EXTRA_UID)) {
            int id = intent.getIntExtra(Intent.EXTRA_UID, 0);
            loadData(id);
        }
    }

    private void loadData(int id) {
        if (id <= 0) {
            // TODO: error
        }

        new FetchMovieTask().execute(Integer.valueOf(id));
    }

    public class FetchMovieTask extends AsyncTask<Integer, Void, MovieDetail> {

        private final String TAG = this.getClass().getSimpleName();

        @Override
        protected MovieDetail doInBackground(Integer... integers) {
            if (integers == null || integers.length <= 0 || integers[0] == null) {
                throw new IllegalArgumentException("Integer array must have non-null elements");
            }
            int id = integers[0].intValue();
            URL url = NetworkUtils.urlForMovie(id);
            try {
                String jsonString = NetworkUtils.getResponseFromHttpUrl(url);
                Log.v(TAG, jsonString == null ? "null" : jsonString);
                MovieDetail movie = MovieJsonUtils.movieFromJson(jsonString);
                return movie;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

//        @Override
//        protected void onPostExecute(MovieDetail movies) {
//            setData(movies);
//        }

    }
}
