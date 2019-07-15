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
        return urlForMovieCategory("popular");
    }

    public static URL urlForTopRatedMovies() {
        return urlForMovieCategory("top_rated");
    }

    public static URL urlForMovie(int id) {
        return urlForMovieCategory(String.valueOf(id));
    }

    public static String urlStringForPosterImage(String path, int minWidth) {
        String size;
        if (minWidth > 780) {
            size = "original";
        } else if (minWidth > 500) {
            size = "w780";
        } else if (minWidth > 342) {
            size = "w500";
        } else if (minWidth > 185) {
            size = "w342";
        } else if (minWidth > 154) {
            size = "w185";
        } else if (minWidth > 92) {
            size = "w154";
        } else {
            size = "w92";
        }
        String url = imageBase + size + path;
        Log.d(TAG, "poster url = " + url);
        return url;
    }

    public static URL urlForVideos(int id) {
        return urlFromUriBuilder(
                uriBuilder()
                        .appendPath("movie")
                        .appendPath(String.valueOf(id))
                        .appendPath("videos")
        );
    }

    private static URL urlForMovieCategory(String path) {
        return urlFromUriBuilder(
                uriBuilder()
                        .appendPath("movie")
                        .appendPath(path)
        );
    }

    private static Uri.Builder uriBuilder() {
        return Uri.parse(apiBase).buildUpon();
    }

    private static URL urlFromUriBuilder(Uri.Builder builder) {
        Uri uri = builder
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
