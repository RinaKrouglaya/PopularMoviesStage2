package com.example.android.popularmoviesstage2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.database.AppDatabase;
import com.example.android.popularmoviesstage2.database.FavoriteMovieEntry;
import com.example.android.popularmoviesstage2.utilities.JsonUtils;
import com.example.android.popularmoviesstage2.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static ProgressBar mLoadingIndicator;
    private static TextView mMovieReviews;
    private static TextView mMovieTrailerLinkFirst;
    private static TextView mMovieTrailerLinkSecond;
    private static String urlFirstTrailer;
    private static String urlSecondTrailer;
    private static String allTheReviews = "";
    String[] receivedDataStringArray;
    //Member variable for the Database
    private AppDatabase mDb;
    private boolean isFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        urlFirstTrailer = null;
        urlSecondTrailer = null;
        ImageView mMovieThumbnail = findViewById(R.id.details_movie_thumbnail);
        final ImageView mFavoriteButton = findViewById(R.id.details_heart);

        TextView mMovieTitle = findViewById(R.id.details_movie_title);
        TextView mMovieSynopsis = findViewById(R.id.details_synopsis);
        TextView mMovieRating = findViewById(R.id.details_rating);
        TextView mMovieReleaseDate = findViewById(R.id.details_date);
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                receivedDataStringArray =
                        intentThatStartedThisActivity.getStringArrayExtra(Intent.EXTRA_TEXT);

                Picasso.get()
                        .load(receivedDataStringArray[1])
                        .error(R.drawable.noposter)
                        .into(mMovieThumbnail);
                mMovieTitle.setText(receivedDataStringArray[2]);
                mMovieSynopsis.setText(receivedDataStringArray[3]);
                mMovieRating.setText(receivedDataStringArray[4]);
                mMovieReleaseDate.setText(receivedDataStringArray[5]);
            }
        } else {
            mMovieTitle.setText(getString(R.string.error));

        }

        mDb = AppDatabase.getInstance(getApplicationContext());


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                final List<FavoriteMovieEntry> inDatabase =
                        mDb.favoriteMovieDao().findById(receivedDataStringArray[6]);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (inDatabase.size() == 0) isFavorite = false;
                        else isFavorite = true;

                        if (isFavorite) {
                            mFavoriteButton
                                    .setImageResource(R.drawable.favoritered);

                        } else {
                            mFavoriteButton.setImageResource(R.drawable.favoritewhite);

                        }
                    }
                });


            }
        });

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         */
        mLoadingIndicator = findViewById(R.id.details_loading_indicator);
        mMovieReviews = findViewById(R.id.details_reviews);
        mMovieTrailerLinkFirst = findViewById(R.id.details_trailer_link_first);
        mMovieTrailerLinkSecond = findViewById(R.id.details_trailer_link_second);

        new FetchReviewsTask().execute(receivedDataStringArray[6]);
        new FetchTrailersTask().execute(receivedDataStringArray[6]);


        mMovieTrailerLinkFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (urlFirstTrailer == null) {
                    Toast.makeText(getApplicationContext(), R.string.toast_trailer_link_1, Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(urlFirstTrailer));
                    startActivity(i);
                }
            }
        });

        mMovieTrailerLinkSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (urlSecondTrailer == null) {
                    Toast.makeText(getApplicationContext(), R.string.toast_trailer_link_1, Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(urlSecondTrailer));
                    startActivity(i);
                }
            }
        });


        final FavoriteMovieEntry favoriteMovieEntry = new FavoriteMovieEntry(receivedDataStringArray[2],
                receivedDataStringArray[7], receivedDataStringArray[1],
                receivedDataStringArray[3], receivedDataStringArray[4],
                receivedDataStringArray[6], receivedDataStringArray[5]);


        // Set a click listener on the add to favorites movies Button (heart)
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DetailActivity", "OnClick" + isFavorite);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final List<FavoriteMovieEntry> inDatabase =
                                mDb.favoriteMovieDao().findById(receivedDataStringArray[6]);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (inDatabase.size() == 0) isFavorite = false;
                                else isFavorite = true;


                            }
                        });

                    }
                });

                if (!isFavorite) {

                    mFavoriteButton.setImageResource(R.drawable.favoritered);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            mDb.favoriteMovieDao().insertMovieToFavorites(favoriteMovieEntry);

                        }
                    });

                } else {
                    mFavoriteButton.setImageResource(R.drawable.favoritewhite);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            mDb.favoriteMovieDao()
                                    .deleteByUserId(favoriteMovieEntry.getExternalId());

                        }
                    });
                }
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if ((id == android.R.id.home)&&(!isFavorite)) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class FetchReviewsTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            String movieExternalId = "";
            if (params.length != 0) {
                movieExternalId = params[0];
            } else allTheReviews = String.valueOf(R.string.no_reviews);

            URL reviewsRequestUrl = NetworkUtils.buildReviewsUrl(movieExternalId);

            try {
                String jsonReviewsResponse = NetworkUtils
                        .getResponseFromHttpUrl(reviewsRequestUrl);

                String[] jsonReviewsData = JsonUtils
                        .getReviewsFromJson(jsonReviewsResponse);


                return jsonReviewsData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] reviewsArray) {
            Log.d("DetailActivity", "onPost", null);
            if (Debug.isDebuggerConnected())
                Debug.waitForDebugger();
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (reviewsArray == null) {
                allTheReviews = "No reviews are currently available.";
                Log.d("DetailActivity", allTheReviews+"null");
            } else {
                allTheReviews = reviewsArray[0] + "\n";
                StringBuilder allTheReviewsBuilder = new StringBuilder();
                for (int i = 1; i < reviewsArray.length; i++) {
                    allTheReviewsBuilder.append(reviewsArray[i] + "\n");
                }
                allTheReviews = allTheReviews + allTheReviewsBuilder.toString();
            }

            Log.d("DetailActivity", allTheReviews);
            mMovieReviews.setText(allTheReviews);

        }

    }

    public static class FetchTrailersTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            String movieExternalId = "";
            if (params.length != 0) {
                movieExternalId = params[0];
            } else {
                return null;
            }

            URL trailersRequestUrl = NetworkUtils.buildTrailersUrl(movieExternalId);

            try {
                String jsonTrailersResponse = NetworkUtils
                        .getResponseFromHttpUrl(trailersRequestUrl);

                String[] jsonTrailersData = JsonUtils
                        .getTrailersFromJson(jsonTrailersResponse);


                return jsonTrailersData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] trailersArray) {


            if (Debug.isDebuggerConnected())
                Debug.waitForDebugger();
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (trailersArray != null) {

                urlFirstTrailer = trailersArray[0];

                if (trailersArray.length > 1) {
                    urlSecondTrailer = trailersArray[1];
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (isFavorite) {
            Intent i = new Intent(this, MainActivity.class);
            DetailActivity.this.finish();
            startActivity(i);
        }
    }
}


