package com.example.android.popularmoviesstage2.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM favorites ORDER BY local_id")
    List<FavoriteMovieItem> loadAllFavoriteMovies();

    @Query("SELECT * FROM favorites WHERE external_id = :userId")
    List<FavoriteMovieItem> findById(String userId);

    @Query("DELETE FROM favorites WHERE external_id = :userId")
    void deleteByUserId(String userId);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertMovieToFavorites(FavoriteMovieItem favoriteMovieItem);

    @Delete
    void deleteMovieFromFavorites (FavoriteMovieItem favoriteMovieItem);
}
