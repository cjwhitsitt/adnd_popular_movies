package com.jaywhitsitt.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jaywhitsitt.popularmovies.data.MovieDetail;
import com.jaywhitsitt.popularmovies.utilities.MovieJsonUtils;
import com.jaywhitsitt.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DetailActivity extends AppCompatActivity {

    ImageView mImageView;
    TextView mYearTextView;
    TextView mLengthTextView;
    TextView mRatingTextView;
    TextView mSynopsisTextView;
    ProgressBar mLoadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mImageView = findViewById(R.id.iv_detail_poster);
        mYearTextView = findViewById(R.id.tv_detail_year);
        mLengthTextView = findViewById(R.id.tv_detail_length);
        mRatingTextView = findViewById(R.id.tv_detail_rating);
        mSynopsisTextView = findViewById(R.id.tv_detail_synopsis);
        mLoadingSpinner = findViewById(R.id.pb_detail_loading_spinner);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TITLE)) {
            getSupportActionBar().setTitle(intent.getStringExtra(Intent.EXTRA_TITLE));
        }

        if (intent.hasExtra(Intent.EXTRA_UID)) {
            int id = intent.getIntExtra(Intent.EXTRA_UID, 0);
            loadData(id);
        } else {
            showError();
        }
    }

    private void loadData(int id) {
        if (id <= 0) {
            showError();
            return;
        }
        new FetchMovieTask().execute(id);
    }

    private void showError() {
        findViewById(R.id.sv_details).setVisibility(View.GONE);
        findViewById(R.id.iv_detail_error).setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n") // Rating shouldn't be internationalized
    private void updateUI(MovieDetail movie) {
        if (movie == null) {
            showError();
            return;
        }

        int width = mImageView.getMeasuredWidth();
        Picasso.get()
                .load(NetworkUtils.urlStringForPosterImage(movie.imageUrl, width))
                .error(R.drawable.ic_error_cloud)
                .into(mImageView);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(movie.releaseDate);
        mYearTextView.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        String length = getResources().getQuantityString(
                R.plurals.minutes_short,
                movie.runtime,
                movie.runtime);
        mLengthTextView.setText(length);
        mRatingTextView.setText(movie.rating + "/10");
        mSynopsisTextView.setText(movie.synopsis);
    }

    private class FetchMovieTask extends AsyncTask<Integer, Void, MovieDetail> {

        private final String TAG = this.getClass().getSimpleName();

        @Override
        protected void onPreExecute() {
            mLoadingSpinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieDetail doInBackground(Integer... integers) {
            if (integers == null || integers.length <= 0 || integers[0] == null) {
                throw new IllegalArgumentException("Integer array must have non-null elements");
            }
            int id = integers[0];
            URL url = NetworkUtils.urlForMovie(id);
            try {
                String jsonString = NetworkUtils.getResponseFromHttpUrl(url);
                Log.v(TAG, jsonString == null ? "null" : jsonString);
                return MovieJsonUtils.movieFromJson(jsonString);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieDetail movie) {
            updateUI(movie);
            mLoadingSpinner.setVisibility(View.GONE);
        }

    }
}
