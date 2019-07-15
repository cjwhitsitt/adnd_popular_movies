package com.jaywhitsitt.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jaywhitsitt.popularmovies.utilities.MovieJsonUtils;
import com.jaywhitsitt.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class TrailersActivity extends AppCompatActivity {

    private int mMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_UID)) {
            mMovieId = intent.getIntExtra(Intent.EXTRA_UID, 0);
            loadData(mMovieId);
        }
        if (mMovieId == 0) {
            // TODO: showError();
        }

        String title = "Videos";
        if (intent.hasExtra(Intent.EXTRA_TITLE)) {
            title += " of " + intent.getStringExtra(Intent.EXTRA_TITLE);
        }
        setTitle(title);
    }

    private void loadData(int id) {
        new FetchVideosTask().execute(id);
    }

    // TODO: return Videos
    public static class FetchVideosTask extends AsyncTask<Integer, Void, Void> {

        private static final String TAG = FetchVideosTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            if (integers.length < 1 || integers[0] == null) {
                return null;
            }
            int id = integers[0];
            URL url = NetworkUtils.urlForVideos(id);

            try {
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                // TODO: parse
                Log.d(TAG, json);
                return null;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
