package com.jaywhitsitt.popularmovies.data;

public class MovieBase {

    // Always available
    public final int id;
    public final String title;
    public final String imageUrl;

    public MovieBase(int id, String title, String imageUrl) {
        super();
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

}
