package com.jaywhitsitt.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jaywhitsitt.popularmovies.data.Review;
import com.jaywhitsitt.popularmovies.utilities.MovieJsonUtils;
import com.jaywhitsitt.popularmovies.utilities.NetworkUtils;

import java.io.IOException;

public class ReviewsActivity extends AppCompatActivity {

    private int mMovieId;

    private ProgressBar mLoadingSpinner;
    private RecyclerView mRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private TextView mNoReviewsTextView;
    private ImageView mErrorImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingSpinner = findViewById(R.id.pb_loading_spinner);
        mNoReviewsTextView = findViewById(R.id.tv_no_content);
        mNoReviewsTextView.setText(R.string.no_reviews_available);
        mErrorImageView = findViewById(R.id.iv_error);

        mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(layoutManager);
        mReviewAdapter = new ReviewAdapter();
        mRecyclerView.setAdapter(mReviewAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                this,
                layoutManager.getOrientation()));

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_UID)) {
            mMovieId = intent.getIntExtra(Intent.EXTRA_UID, 0);
            loadData();
        }
        if (mMovieId == 0) {
            showError();
        }

        String title;
        if (intent.hasExtra(Intent.EXTRA_TITLE)) {
            title = getString(R.string.reviews_title_format, intent.getStringExtra(Intent.EXTRA_TITLE));
        } else {
            title = getString(R.string.detail_label_reviews);
        }
        setTitle(title);
    }

    private void loadData() {
        new FetchReviewsTask().execute();
    }

    private void showError() {
        mErrorImageView.setVisibility(View.VISIBLE);
    }

    public class FetchReviewsTask extends AsyncTask<Void, Void, Review[]> {

        @Override
        protected void onPreExecute() {
            mLoadingSpinner.setVisibility(View.VISIBLE);
            mErrorImageView.setVisibility(View.GONE);
        }

        @Override
        protected Review[] doInBackground(Void... voids) {
            try {
                String response = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.urlForReview(mMovieId));
                return MovieJsonUtils.reviewsFromJson(response);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            if (reviews == null) {
                showError();
            } else if (reviews.length == 0) {
                mNoReviewsTextView.setVisibility(View.VISIBLE);
            }

            mReviewAdapter.setData(reviews);
            mLoadingSpinner.setVisibility(View.GONE);
            mRecyclerView.scrollToPosition(0);
        }
    }

}
