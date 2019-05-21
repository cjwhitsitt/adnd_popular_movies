package com.jaywhitsitt.popularmovies.data;

import java.util.Date;

public class MovieDetail extends MovieBase {

    public final int runtime;
    public final Date releaseDate;
    public final double rating;
    public final String synopsis;

    public MovieDetail(int id, String title, String imageUrl, int runtime, Date releaseDate, double rating, String synopsis) {
        super(id, title, imageUrl);
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.synopsis = synopsis;
    }

}
