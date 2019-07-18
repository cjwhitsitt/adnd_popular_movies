package com.jaywhitsitt.popularmovies.utilities;

import com.jaywhitsitt.popularmovies.data.MovieBase;
import com.jaywhitsitt.popularmovies.data.MovieDetail;
import com.jaywhitsitt.popularmovies.data.Review;
import com.jaywhitsitt.popularmovies.data.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

public class MovieJsonUtils {

    private static final String RESULTS_KEY = "results";
    private static final String ID_KEY = "id";
    private static final String TITLE_KEY = "title";
    private static final String NAME_KEY = "name";
    private static final String POSTER_PATH_KEY = "poster_path";
    private static final String RUNTIME_KEY = "runtime";
    private static final String RELEASE_DATE_KEY = "release_date";
    private static final String VOTE_AVERAGE_KEY = "vote_average";
    private static final String OVERVIEW_KEY = "overview";
    private static final String KEY_KEY = "key";
    private static final String SITE_KEY = "site";
    private static final String AUTHOR_KEY = "author";
    private static final String CONTENT_KEY = "content";

    public static MovieBase[] moviesFromJson(String string) {
        MovieBase[] movies = null;

        try {
            JSONObject json = new JSONObject(string);
            JSONArray results = json.getJSONArray(RESULTS_KEY);
            movies = new MovieBase[results.length()];

            for (int i = 0; i < results.length(); i++) {
                int id = results.getJSONObject(i).getInt(ID_KEY);
                String title = results.getJSONObject(i).getString(TITLE_KEY);
                String posterPath = results.getJSONObject(i).getString(POSTER_PATH_KEY);
                movies[i] = new MovieBase(id, title, posterPath);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public static MovieDetail movieFromJson(String jsonString) {
        MovieDetail movie = null;

        try {
            JSONObject obj = new JSONObject(jsonString);
            int id, runtime;
            String title, posterPath, synopsis;
            Date releaseDate;
            double rating;

            try { id = obj.getInt(ID_KEY); }
            catch (JSONException e) { id = MovieDetail.INVALID_NUMERIC_VALUE; }


            try { title = obj.getString(TITLE_KEY); }
            catch (JSONException e) { title = null; }

            try { posterPath = obj.getString(POSTER_PATH_KEY); }
            catch (JSONException e) { posterPath = null; }

            try { runtime = obj.getInt(RUNTIME_KEY); }
            catch (JSONException e) { runtime = MovieDetail.INVALID_NUMERIC_VALUE; }

            try {
                String releaseDateString = obj.getString(RELEASE_DATE_KEY);
                releaseDate = Date.valueOf(releaseDateString);
            } catch (JSONException e) { releaseDate = null; }

            try { rating = obj.getDouble(VOTE_AVERAGE_KEY); }
            catch (JSONException e) { rating = MovieDetail.INVALID_NUMERIC_VALUE; }

            try { synopsis = obj.getString(OVERVIEW_KEY); }
            catch (JSONException e) { synopsis = null; }

            movie = new MovieDetail(id, title, posterPath, runtime, releaseDate, rating, synopsis);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movie;
    }

    public static Video[] videosFromJson(String jsonString) {
        Video[] videos = null;

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray results = json.getJSONArray(RESULTS_KEY);
            videos = new Video[results.length()];

            for (int i = 0; i < results.length(); i++) {
                JSONObject obj = results.getJSONObject(i);
                String id = obj.getString(ID_KEY);
                String title = obj.getString(NAME_KEY);
                String key = obj.getString(KEY_KEY);

                String siteString = obj.getString(SITE_KEY);
                Video.Site site = Video.Site.get(siteString);

                videos[i] = new Video(id, title, site, key);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return videos;
    }

    public static Review[] reviewsFromJson(String jsonString) {
        Review[] reviews = null;

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray results = json.getJSONArray(RESULTS_KEY);
            reviews = new Review[results.length()];

            for (int i = 0; i < results.length(); i++) {
                JSONObject obj = results.getJSONObject(i);
                String author = obj.getString(AUTHOR_KEY);
                String content = obj.getString(CONTENT_KEY);
                reviews[i] = new Review(author, content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }
}
