package com.jaywhitsitt.popularmovies.data;

public class MovieBase {

    // Always available
    public final String title;
    public final String imageUrl;

    public MovieBase(String title, String imageUrl) {
        super();
        this.title = title;
        this.imageUrl = imageUrl;
    }

}
