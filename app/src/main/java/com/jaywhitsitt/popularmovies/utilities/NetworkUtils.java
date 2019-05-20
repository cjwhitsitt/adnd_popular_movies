package com.jaywhitsitt.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.jaywhitsitt.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static private String TAG = NetworkUtils.class.getSimpleName();

    final static private String apiBase = "https://api.themoviedb.org/3/";
    final static private String imageBase = "https://image.tmdb.org/t/p/";

    public static URL urlForMostPopularMovies() {
        return urlWithPath("popular");
    }

    public static URL urlForTopRatedMovies() {
        return urlWithPath("top_rated");
    }

    public static String urlStringForPosterImage(String path) {
        String url = imageBase + "/w185" + path;
        Log.d(TAG, "poster url = " + url);
        return url;
    }

    private static URL urlWithPath(String path) {
        Uri uri = Uri.parse(apiBase).buildUpon()
                .appendPath("movie")
                .appendPath(path)
                .appendQueryParameter("api_key", BuildConfig.TMDAPIKey)
                .build();

        try {
            String url = uri.toString();
            Log.d(TAG, "url = " + url);
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
