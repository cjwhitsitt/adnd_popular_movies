package com.jaywhitsitt.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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
    TextView mDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mImageView = findViewById(R.id.iv_detail_poster);
        mYearTextView = findViewById(R.id.tv_detail_year);
        mLengthTextView = findViewById(R.id.tv_detail_length);
        mDateTextView = findViewById(R.id.tv_detail_date);

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

        new FetchMovieTask().execute(id);
    }

    private void updateUI(MovieDetail movie) {
        Picasso.get()
                .load(NetworkUtils.urlStringForPosterImage(movie.imageUrl))
                .error(R.drawable.ic_error_cloud)
                .into(mImageView);
        mDateTextView.setText(movie.releaseDate.toString());

        String length = getResources().getQuantityString(
                R.plurals.minutes_short,
                movie.runtime,
                movie.runtime);
        mLengthTextView.setText(length);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(movie.releaseDate);
        mYearTextView.setText(String.valueOf(calendar.get(Calendar.YEAR)));
    }

    private class FetchMovieTask extends AsyncTask<Integer, Void, MovieDetail> {

        private final String TAG = this.getClass().getSimpleName();

        // TODO: loading spinner

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
        }

    }
}
