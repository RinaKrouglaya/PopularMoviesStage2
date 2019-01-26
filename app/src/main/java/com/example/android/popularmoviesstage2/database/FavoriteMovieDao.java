package com.example.android.popularmoviesstage2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM favorites ORDER BY local_id")
    LiveData<List<FavoriteMovieEntry>> loadAllFavoriteMovies();

    @Query("SELECT * FROM favorites WHERE external_id = :userId")
    List<FavoriteMovieEntry> findById(String userId);

    @Query("DELETE FROM favorites WHERE external_id = :userId")
    void deleteByUserId(String userId);


    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertMovieToFavorites(FavoriteMovieEntry favoriteMovieEntry);

    @Delete
    void deleteMovieFromFavorites (FavoriteMovieEntry favoriteMovieEntry);
}
