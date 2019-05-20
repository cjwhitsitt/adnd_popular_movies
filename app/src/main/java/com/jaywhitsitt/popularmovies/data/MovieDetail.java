package com.jaywhitsitt.popularmovies.data;

import java.util.Date;

public class MovieDetail extends MovieBase {

    public final int runtime;
    public final Date releaseDate;

    public MovieDetail(MovieDetail movieDetail, int runtime, Date releaseDate) {
        super(movieDetail.title, movieDetail.imageUrl);
        this.runtime = runtime;
        this.releaseDate = releaseDate;
    }

}
