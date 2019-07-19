package com.jaywhitsitt.popularmovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieBaseDao {
    @Query("SELECT * FROM Favorite_Movies")
    public LiveData<List<MovieBase>> getFavoriteMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addFavoriteMovies(MovieBase... movies);

    @Delete
    public void removeFavoriteMovies(MovieBase... movies);
}
