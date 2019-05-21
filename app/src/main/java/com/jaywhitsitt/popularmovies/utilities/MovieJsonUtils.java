package com.jaywhitsitt.popularmovies.utilities;

import com.jaywhitsitt.popularmovies.data.MovieBase;
import com.jaywhitsitt.popularmovies.data.MovieDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;

public class MovieJsonUtils {

    private static final String RESULTS_KEY = "results";
    private static final String ID_KEY = "id";
    private static final String TITLE_KEY = "title";
    private static final String POSTER_PATH_KEY = "poster_path";
    private static final String RUNTIME_KEY = "runtime";
    private static final String RELEASE_DATE_KEY = "release_date";
    private static final String VOTE_AVERAGE_KEY = "vote_average";
    private static final String OVERVIEW_KEY = "overview";

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
            int id = obj.getInt(ID_KEY);
            String title = obj.getString(TITLE_KEY);
            String posterPath = obj.getString(POSTER_PATH_KEY);
            int runtime = obj.getInt(RUNTIME_KEY);
            String releaseDateString = obj.getString(RELEASE_DATE_KEY);
            Date releaseDate = Date.valueOf(releaseDateString);
            double rating = obj.getDouble(VOTE_AVERAGE_KEY);
            String synopsis = obj.getString(OVERVIEW_KEY);
            movie = new MovieDetail(id, title, posterPath, runtime, releaseDate, rating, synopsis);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movie;
    }
}
