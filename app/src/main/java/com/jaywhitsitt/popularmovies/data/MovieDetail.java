package com.jaywhitsitt.popularmovies.data;

import java.util.Date;

public class MovieDetail extends MovieBase {

    public final int runtime;
    public final Date releaseDate;

    public MovieDetail(int id, String title, String imageUrl, int runtime, Date releaseDate) {
        super(id, title, imageUrl);
        this.runtime = runtime;
        this.releaseDate = releaseDate;
    }

}
