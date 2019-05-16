package com.jaywhitsitt.popularmovies.utilities;

import android.net.Uri;

import com.jaywhitsitt.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static private String apiBase = "https://api.themoviedb.org/3/";
    final static private String imageBase = "https://image.tmdb.org/t/p/";

    public static URL urlForMostPopularMovies() {
        return urlWithPath("popular");
    }

    private static URL urlWithPath(String path) {
        Uri uri = Uri.parse(apiBase).buildUpon()
                .appendPath("movie")
                .appendEncodedPath(path) // TODO: look into using path instead
                .appendQueryParameter("api_key", BuildConfig.TMDAPIKey)
                .build();

        try {
            return new URL(uri.toString());
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
