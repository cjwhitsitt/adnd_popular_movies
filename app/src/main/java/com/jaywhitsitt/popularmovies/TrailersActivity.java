package com.jaywhitsitt.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jaywhitsitt.popularmovies.data.Video;
import com.jaywhitsitt.popularmovies.utilities.MovieJsonUtils;
import com.jaywhitsitt.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class TrailersActivity extends AppCompatActivity implements VideoOnClickHandler {

    private static final String TAG = TrailersActivity.class.getSimpleName();

    private int mMovieId;

    private ProgressBar mLoadingSpinner;
    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;
    private TextView mNoContentTextView;
    private ImageView mErrorImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingSpinner = findViewById(R.id.pb_loading_spinner);
        mRecyclerView = findViewById(R.id.recycler_view);
        mNoContentTextView = findViewById(R.id.tv_no_content);
        mNoContentTextView.setText(R.string.no_trailers_available);
        mErrorImageView = findViewById(R.id.iv_error);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new VideoAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

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
            title = getString(R.string.trailers_title_format, intent.getStringExtra(Intent.EXTRA_TITLE));
        } else {
            title = getString(R.string.detail_label_trailers);
        }
        setTitle(title);
    }

    private void loadData(int id) {
        new FetchVideosTask().execute(id);
    }

    private void showError() {
        mErrorImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Video video) {
        if (video.site == Video.Site.YOUTUBE) {
            Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + video.key);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else {
            Log.w(TAG, "Unhandled video site: " + video.site.getId());
            Toast.makeText(this, R.string.trailer_open_error, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public class FetchVideosTask extends AsyncTask<Integer, Void, List<Video>> {

        private final String TAG = FetchVideosTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            mLoadingSpinner.setVisibility(View.VISIBLE);
            mNoContentTextView.setVisibility(View.GONE);
            mErrorImageView.setVisibility(View.GONE);
        }

        @Override
        protected List<Video> doInBackground(Integer... integers) {
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
        protected void onPostExecute(List<Video> videos) {
            if (videos == null) {
                showError();
            } else if (videos.size() == 0) {
                mNoContentTextView.setVisibility(View.VISIBLE);
            }

            mAdapter.setData(videos);
            mLoadingSpinner.setVisibility(View.GONE);
            mRecyclerView.scrollToPosition(0);
        }
    }
}
