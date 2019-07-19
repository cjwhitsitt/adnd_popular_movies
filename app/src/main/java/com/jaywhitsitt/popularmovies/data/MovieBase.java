package com.jaywhitsitt.popularmovies.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Favorite_Movies")
public class MovieBase {

    // Always available
    @PrimaryKey
    public final int id;
    public final String title;
    @ColumnInfo(name = "image_url")
    public final String imageUrl;

    public MovieBase(int id, String title, String imageUrl) {
        super();
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

}
