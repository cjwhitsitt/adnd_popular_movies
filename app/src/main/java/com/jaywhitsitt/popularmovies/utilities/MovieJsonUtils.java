package com.jaywhitsitt.popularmovies.utilities;

import com.jaywhitsitt.popularmovies.data.MovieBase;
import com.jaywhitsitt.popularmovies.data.MovieDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;

public class MovieJsonUtils {

    // TODO: common variables for json keys

    public static MovieBase[] moviesFromJson(String string) {
        MovieBase[] movies = null;

        try {
            JSONObject json = new JSONObject(string);
            JSONArray results = json.getJSONArray("results");
            movies = new MovieBase[results.length()];

            for (int i = 0; i < results.length(); i++) {
                int id = results.getJSONObject(i).getInt("id");
                String title = results.getJSONObject(i).getString("title");
                String posterPath = results.getJSONObject(i).getString("poster_path");
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
            int id = obj.getInt("id");
            String title = obj.getString("title");
            String posterPath = obj.getString("poster_path");
            int runtime = obj.getInt("runtime");
            String releaseDateString = obj.getString("release_date");
            Date releaseDate = Date.valueOf(releaseDateString);
            double rating = obj.getDouble("vote_average");
            String synopsis = obj.getString("overview");
            movie = new MovieDetail(id, title, posterPath, runtime, releaseDate, rating, synopsis);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movie;
    }
}
