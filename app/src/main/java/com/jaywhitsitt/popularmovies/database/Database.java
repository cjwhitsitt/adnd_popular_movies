package com.jaywhitsitt.popularmovies.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jaywhitsitt.popularmovies.data.MovieBase;

@androidx.room.Database(
        entities = {MovieBase.class},
        version = 1,
        exportSchema = false
)
public abstract class Database extends RoomDatabase {
    private static Database db;
    public static Database databaseInstance(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(), Database.class, "Movies-Database").build();
        }
        return db;
    }
    public abstract MovieBaseDao movieBaseDao();
}
