package com.jaywhitsitt.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.jaywhitsitt.popularmovies.data.Video;
import com.jaywhitsitt.popularmovies.utilities.MovieJsonUtils;
import com.jaywhitsitt.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class TrailersActivity extends AppCompatActivity implements VideoOnClickHandler {

    private static final String TAG = TrailersActivity.class.getSimpleName();

    private int mMovieId;

    private ProgressBar mLoadingSpinner;
    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingSpinner = findViewById(R.id.pb_main_loading_spinner);
        mRecyclerView = findViewById(R.id.rv_movies);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new VideoAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_UID)) {
            mMovieId = intent.getIntExtra(Intent.EXTRA_UID, 0);
            loadData(mMovieId);
        }
        if (mMovieId == 0) {
            showError();
        }

        // TODO: localize
        String title = "Videos";
        if (intent.hasExtra(Intent.EXTRA_TITLE)) {
            title += " of " + intent.getStringExtra(Intent.EXTRA_TITLE);
        }
        setTitle(title);
    }

    private void loadData(int id) {
        new FetchVideosTask().execute(id);
    }

    private void showError() {
        // TODO:
    }

    private void showToastError() {
        // TODO:
    }

    @Override
    public void onClick(Video video) {
        if (video.site == Video.Site.YOUTUBE) {
            Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + video.key);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else {
            Log.w(TAG, "Unhandled video site: " + video.site.getId());
            showToastError();
        }
    }

    public class FetchVideosTask extends AsyncTask<Integer, Void, Video[]> {

        private final String TAG = FetchVideosTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            mLoadingSpinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Video[] doInBackground(Integer... integers) {
            if (integers.length < 1 || integers[0] == null) {
                return null;
            }
            int id = integers[0];
            URL url = NetworkUtils.urlForVideos(id);

            try {
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                Log.d(TAG, json);
                return MovieJsonUtils.videosFromJson(json);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Video[] videos) {
            if (videos == null) {
                showError();
            } else if (videos.length == 0) {
                // TODO: Show no videos available
            }

            mAdapter.setData(videos);
            mLoadingSpinner.setVisibility(View.GONE);
            mRecyclerView.scrollToPosition(0);
        }
    }
}
