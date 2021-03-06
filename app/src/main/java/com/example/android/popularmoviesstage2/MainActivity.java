package com.example.android.popularmoviesstage2;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.database.AppDatabase;
import com.example.android.popularmoviesstage2.database.FavoriteMovieEntry;
import com.example.android.popularmoviesstage2.utilities.JsonUtils;
import com.example.android.popularmoviesstage2.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity
        implements MovieItemAdapter.ListItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener, FavoriteMoviesAdapter.ItemClickListener {

    private static final int NUM_LIST_ITEMS = 20;

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();
    static ArrayList<MovieItem> globalMovieData;
    private static MovieItemAdapter mAdapter;
    private static FavoriteMoviesAdapter favoritesAdapter;
    private static RecyclerView mRecyclerView;
    private static TextView mErrorMessageDisplay;
    private static TextView mEmptyMessageDisplay;
    private static ProgressBar mLoadingIndicator;
    private String sortSelection;
    private boolean isFavoritesSelected;
    private AppDatabase mDb;
    private Parcelable recyclerViewState = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_grid_activity);

        setupSharedPreferences();

        mDb = AppDatabase.getInstance(getApplicationContext());



        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mRecyclerView = findViewById(R.id.rv_grid);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = findViewById(R.id.no_connection_view);
        mEmptyMessageDisplay = findViewById(R.id.empty_view);

        final RecyclerView.LayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

      if (savedInstanceState!=null)  {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(mRecyclerView.getLayoutManager())
                    .onRestoreInstanceState
                            (Objects.requireNonNull(savedInstanceState)
                                    .getParcelable("RVState"));
          }
        }

        /*
         * Setting to improve performance  -  changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MovieItemAdapter(NUM_LIST_ITEMS, this);
        favoritesAdapter = new FavoriteMoviesAdapter
                (this, this);


        if (isFavoritesSelected) mRecyclerView.setAdapter(favoritesAdapter);
        else mRecyclerView.setAdapter(mAdapter);


        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         */
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);



        /*
         * Once all of our views are setup, we can load the movie data.
         * Passing sort parameter from the Settings
         *
         * */
        if (isFavoritesSelected) loadFavoritesData();
        else loadMovieData(sortSelection);


    }

    private void setupSharedPreferences() {

        /*
         * Getting parameters from the Settings Menu
         * */
        SharedPreferences sharedPrefs = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        sortSelection = sharedPrefs.getString(
                getString(R.string.settings_sort_parameter),
                getString(R.string.settings_default_sort_by_popularity));

        isFavoritesSelected = sharedPrefs.getBoolean("checkboxPrefFavorites", false);
        // Register the listener
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);

    }

    // Updates the screen if the shared preferences change.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.setting_sort_by_popularity_label))) {
            sharedPreferences.getString(
                    getString(R.string.settings_sort_parameter),
                    getString(R.string.settings_default_sort_by_popularity));
        }
        if (key.equals("checkboxPrefFavorites")) {
            isFavoritesSelected = sharedPreferences.getBoolean("checkboxPrefFavorites", false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);

    }

    /**
     * This method will get the user's preferred sort parameter, and then tell some
     * background method to get the movie data in the background.
     */
    private void loadMovieData(String sortSelection) {
        showMovieDataView();
        new FetchMovieTask().execute(sortSelection);

    }

    /**
     * This method will get the user's preferred sort parameter, and then tell some
     * background method to get the movie data in the background.
     */
    private void loadFavoritesData() {

        final LiveData<List<FavoriteMovieEntry>> entries = mDb.favoriteMovieDao().loadAllFavoriteMovies();
        entries.observe(this, new Observer<List<FavoriteMovieEntry>>() {

            @Override
            public void onChanged(@Nullable List<FavoriteMovieEntry> favoriteMovieEntries) {
                if (favoriteMovieEntries.size() == 0) {
                    mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    mEmptyMessageDisplay.setVisibility(View.VISIBLE);
                } else {
                    mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyMessageDisplay.setVisibility(View.INVISIBLE);
                    setupViewModel();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This callback is invoked when you click on an item in the list (not Favorites Movies).
     *
     * @param clickedItemIndex Index in the list of the item that was clicked.
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {

        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);


        String stringInfo = String.valueOf(clickedItemIndex);

        if (globalMovieData != null) {
            String[] stringArray = {stringInfo,
                    globalMovieData.get(clickedItemIndex).getThumbnailImage(),
                    globalMovieData.get(clickedItemIndex).getOriginalTitle(),
                    globalMovieData.get(clickedItemIndex).getPlotSynopsis(),
                    globalMovieData.get(clickedItemIndex).getUserRating().toString(),
                    globalMovieData.get(clickedItemIndex).getReleaseDate(),
                    String.valueOf(globalMovieData.get(clickedItemIndex).getId()),
                    globalMovieData.get(clickedItemIndex).getPoster()};
            intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, stringArray);
            startActivity(intentToStartDetailActivity);
        } else showErrorMessage();

    }


    //Listener on FavoriteMoviesAdapter

    @Override
    public void onItemClickListener(int itemId) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        final Intent intentToStartDetailActivity = new Intent(context, destinationClass);


        final String stringInfo = String.valueOf(itemId);
        final int position = itemId;

        final LiveData<List<FavoriteMovieEntry>> entries = mDb.favoriteMovieDao().loadAllFavoriteMovies();
        entries.observe(this, new Observer<List<FavoriteMovieEntry>>() {

            @Override
            public void onChanged(@Nullable List<FavoriteMovieEntry> favoriteMovieEntries) {


                if (favoriteMovieEntries != null) {
                    if (favoriteMovieEntries.size() != 0) {
                        String[] stringArray = {stringInfo,
                                favoriteMovieEntries.get(position).getThumbnail(),
                                favoriteMovieEntries.get(position).getTitle(),
                                favoriteMovieEntries.get(position).getSynopsis(),
                                favoriteMovieEntries.get(position).getRating(),
                                favoriteMovieEntries.get(position).getDate(),
                                favoriteMovieEntries.get(position).getExternalId(),
                                favoriteMovieEntries.get(position).getPoster()
                        };
                        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, stringArray);
                        startActivity(intentToStartDetailActivity);


                    }
                } else {
                    mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    mEmptyMessageDisplay.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getListOfMovies().observe(this, new Observer<List<FavoriteMovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovieEntry> favoriteMovieEntries) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                favoritesAdapter
                        .setFavoritesMovieData(favoriteMovieEntries);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            recyclerViewState = Objects.requireNonNull(mRecyclerView.getLayoutManager()).onSaveInstanceState();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            outState.putParcelable("RVState", recyclerViewState);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getParcelable("RVState");
        }
    }

    public static class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieItem>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieItem> doInBackground(String... params) {
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            String sortSelectionParameter;
            if (params.length != 0) {
                sortSelectionParameter = params[0];
            } else sortSelectionParameter = "top_rated";

            URL movieRequestUrl = NetworkUtils.buildUrl(sortSelectionParameter);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                ArrayList<MovieItem> jsonMovieData = JsonUtils
                        .getListOfMovieItemsFromJson(jsonMovieResponse);

                return jsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItem> movieData) {
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData == null) {
                showErrorMessage();

            } else {
                showMovieDataView();
                mAdapter.setMovieData(movieData);
                globalMovieData = movieData;
            }
        }

    }

    /**
     * This method will make the View for the movie data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private static void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movie
     * View.
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private static void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}