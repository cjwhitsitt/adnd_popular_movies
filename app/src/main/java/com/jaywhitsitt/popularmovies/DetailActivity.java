package com.jaywhitsitt.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jaywhitsitt.popularmovies.data.Database;
import com.jaywhitsitt.popularmovies.data.MovieBase;
import com.jaywhitsitt.popularmovies.data.MovieBaseDao;
import com.jaywhitsitt.popularmovies.data.MovieDetail;
import com.jaywhitsitt.popularmovies.utilities.MovieJsonUtils;
import com.jaywhitsitt.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private int mMovieId;
    private String mMovieTitle;
    private MovieDetail mDisplayedMovie;

    ImageView mImageView;
    ImageButton mFavoriteButton;
    TextView mYearTextView;
    TextView mMonthDayTextView;
    TextView mLengthTextView;
    TextView mRatingTextView;
    TextView mSynopsisTextView;
    ProgressBar mLoadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mImageView = findViewById(R.id.iv_detail_poster);
        mFavoriteButton = findViewById(R.id.iv_favorite);
        mYearTextView = findViewById(R.id.tv_detail_year);
        mMonthDayTextView = findViewById(R.id.tv_detail_month_day);
        mLengthTextView = findViewById(R.id.tv_detail_length);
        mRatingTextView = findViewById(R.id.tv_detail_rating);
        mSynopsisTextView = findViewById(R.id.tv_detail_synopsis);
        mLoadingSpinner = findViewById(R.id.pb_detail_loading_spinner);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TITLE)) {
            String title = intent.getStringExtra(Intent.EXTRA_TITLE);
            getSupportActionBar().setTitle(title);
            mMovieTitle = title;
        }

        if (intent.hasExtra(Intent.EXTRA_UID)) {
            int id = intent.getIntExtra(Intent.EXTRA_UID, 0);
            loadData(id);
        } else {
            showError();
        }

        MovieBaseDao dao = Database.databaseInstance(this).movieBaseDao();
        final LiveData<List<MovieBase>> favorites = dao.getFavoriteMovies();
        favorites.observe(this, new Observer<List<MovieBase>>() {
            @Override
            public void onChanged(List<MovieBase> movieBases) {
                for (MovieBase movie: movieBases) {
                    if (movie.id == mMovieId) {
                        mFavoriteButton.setSelected(true);
                        return;
                    }
                }
                mFavoriteButton.setSelected(false);
            }
        });
    }

    private void loadData(int id) {
        mMovieId = id;
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

    public void showReviews(View view) {
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra(Intent.EXTRA_UID, mMovieId);
        intent.putExtra(Intent.EXTRA_TITLE, mMovieTitle);
        startActivity(intent);
    }

    public void showVideos(View view) {
        Intent intent = new Intent(this, TrailersActivity.class);
        intent.putExtra(Intent.EXTRA_UID, mMovieId);
        intent.putExtra(Intent.EXTRA_TITLE, mMovieTitle);
        startActivity(intent);
    }

    public void onFavorite(View view) {
        ImageButton button = (ImageButton) view;
        button.setSelected(!button.isSelected());

        final boolean isFavorite = button.isSelected();
        final MovieBaseDao dao = Database.databaseInstance(this).movieBaseDao();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isFavorite) {
                    dao.addFavoriteMovies(mDisplayedMovie);
                } else {
                    dao.removeFavoriteMovies(mDisplayedMovie);
                }
            }
        });
        thread.start();
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

        String monthDayString = android.text.format.DateFormat
                .format("MMMM dd", movie.releaseDate)
                .toString();
        mMonthDayTextView.setText(monthDayString);
        String length = getResources().getQuantityString(
                R.plurals.minutes_short,
                movie.runtime,
                movie.runtime);
        mLengthTextView.setText(length);
        mLengthTextView.setVisibility(
                movie.runtime == MovieDetail.INVALID_NUMERIC_VALUE ? View.GONE : View.VISIBLE);
        mRatingTextView.setText(movie.rating + "/10");
        mRatingTextView.setVisibility(
                movie.rating == MovieDetail.INVALID_NUMERIC_VALUE ? View.GONE : View.VISIBLE);
        mSynopsisTextView.setText(movie.synopsis);

        mDisplayedMovie = movie;
    }

    @SuppressLint("StaticFieldLeak")
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
