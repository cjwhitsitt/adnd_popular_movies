package com.jaywhitsitt.popularmovies.utilities;

import com.jaywhitsitt.popularmovies.data.MovieBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieJsonUtils {

    public static MovieBase[] moviesFromJson(String string) {
        MovieBase[] movies = null;

        try {
            JSONObject json = new JSONObject(string);
            JSONArray results = json.getJSONArray("results");
            movies = new MovieBase[results.length()];

            for (int i = 0; i < results.length(); i++) {
                String title = results.getJSONObject(i).getString("title");
                String posterPath = results.getJSONObject(i).getString("poster_path");
                movies[i] = new MovieBase(title, posterPath);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

}
