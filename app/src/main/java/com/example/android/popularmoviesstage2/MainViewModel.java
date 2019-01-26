package com.example.android.popularmoviesstage2;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.popularmoviesstage2.database.AppDatabase;
import com.example.android.popularmoviesstage2.database.FavoriteMovieEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<FavoriteMovieEntry>> listOfMovies;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        listOfMovies = database.favoriteMovieDao().loadAllFavoriteMovies();
    }

    public LiveData<List<FavoriteMovieEntry>> getListOfMovies() {
        return listOfMovies;
    }
}
