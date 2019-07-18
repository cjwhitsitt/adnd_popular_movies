package com.jaywhitsitt.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ReviewsActivity extends AppCompatActivity {

    private int mMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_UID)) {
            mMovieId = intent.getIntExtra(Intent.EXTRA_UID, 0);
            loadData(mMovieId);
        }
        if (mMovieId == 0) {
            showError();
        }

        // TODO: localize
        String title = "Reviews";
        if (intent.hasExtra(Intent.EXTRA_TITLE)) {
            title += " of " + intent.getStringExtra(Intent.EXTRA_TITLE);
        }
        setTitle(title);
    }

    private void loadData(int id) {
        // TODO:
    }

    private void showError() {
        // TODO:
    }
}
