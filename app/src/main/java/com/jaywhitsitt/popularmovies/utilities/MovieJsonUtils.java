package com.jaywhitsitt.popularmovies.utilities;

import com.jaywhitsitt.popularmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieJsonUtils {

    public static Movie[] moviesFromJson(String string) {
        Movie[] movies = null;

        try {
            JSONObject json = new JSONObject(string);
            JSONArray results = json.getJSONArray("results");
            movies = new Movie[results.length()];

            for (int i = 0; i < results.length(); i++) {
                String posterPath = results.getJSONObject(i).getString("poster_path");
                movies[i] = new Movie(posterPath);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

}
