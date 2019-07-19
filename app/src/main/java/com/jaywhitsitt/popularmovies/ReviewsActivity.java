package com.jaywhitsitt.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.jaywhitsitt.popularmovies.data.Review;
import com.jaywhitsitt.popularmovies.utilities.MovieJsonUtils;
import com.jaywhitsitt.popularmovies.utilities.NetworkUtils;

import java.io.IOException;

public class ReviewsActivity extends AppCompatActivity {

    private int mMovieId;

    private ProgressBar mLoadingSpinner;
    private RecyclerView mRecyclerView;
    private ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        mLoadingSpinner = findViewById(R.id.pb_review_loading_spinner);

        mRecyclerView = findViewById(R.id.rv_reviews);
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
            loadData(mMovieId);
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

    private void loadData(int id) {
        new FetchReviewsTask().execute();
    }

    private void showError() {
        // TODO:
    }

    public class FetchReviewsTask extends AsyncTask<Void, Void, Review[]> {

        @Override
        protected void onPreExecute() {
            mLoadingSpinner.setVisibility(View.VISIBLE);
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
                // TODO: Show no reviews available
            }

            mReviewAdapter.setData(reviews);
            mLoadingSpinner.setVisibility(View.GONE);
            mRecyclerView.scrollToPosition(0);
        }
    }

}
