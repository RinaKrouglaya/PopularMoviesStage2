package com.example.android.popularmoviesstage2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.database.AppDatabase;
import com.example.android.popularmoviesstage2.database.FavoriteMovieDao;
import com.example.android.popularmoviesstage2.database.FavoriteMovieItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    String[] receivedDataStringArray ;
   //Member variable for the Database
    private AppDatabase mDb;
    private boolean isFavorite;
    private FavoriteMovieItem favoriteMovieItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        ImageView mMovieThumbnail = findViewById(R.id.details_movie_thumbnail);
        final ImageView mFavoriteButton = findViewById(R.id.details_heart);

        TextView mMovieTitle = findViewById(R.id.details_movie_title);
        TextView mMovieSynopsis = findViewById(R.id.details_synopsis);
        TextView mMovieRating = findViewById(R.id.details_rating);
        TextView mMovieReleaseDate = findViewById(R.id.details_date);
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                receivedDataStringArray = intentThatStartedThisActivity.getStringArrayExtra(Intent.EXTRA_TEXT);

                Picasso.get()
                        .load(receivedDataStringArray[1])
                        .error(R.drawable.noposter)
                        .into( mMovieThumbnail);
                mMovieTitle.setText(receivedDataStringArray[2]);
                mMovieSynopsis.setText(receivedDataStringArray[3]);
                mMovieRating.setText(receivedDataStringArray[4]);
                mMovieReleaseDate.setText(receivedDataStringArray[5]);
            }
        }
        else mMovieTitle.setText(getString(R.string.error));

        mDb = AppDatabase.getInstance(getApplicationContext());
       List<FavoriteMovieItem> inDatabase = mDb.favoriteMovieDao().findById(receivedDataStringArray[6]);
        if (inDatabase.isEmpty())isFavorite =false;
        if (inDatabase.isEmpty()) Log.d("DetailedActivity","database is empty", null);

        // Set the image based on condition whether or not the movie is in the favorites Database
        if (!inDatabase.isEmpty()) mFavoriteButton.setImageResource (R.drawable.favoritered);
        else mFavoriteButton.setImageResource (R.drawable.favoritewhite);

        favoriteMovieItem = new FavoriteMovieItem(receivedDataStringArray[2],
                receivedDataStringArray[7], receivedDataStringArray[1],
                receivedDataStringArray[3], receivedDataStringArray[4],
               receivedDataStringArray[6],receivedDataStringArray[5] );

        // Set a click listener on the add to custom Playlist Button
        mFavoriteButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                List<FavoriteMovieItem> inDatabase = mDb.favoriteMovieDao().findById(receivedDataStringArray[6] );
                if (inDatabase.isEmpty()) Log.d("DetailedActivity","after click empty", null);
                else Log.d("DetailedActivity","after click not empty",null);

                if (inDatabase.isEmpty()) {
                    Log.d("DetailedActivity","EMPTY",null);
                    mFavoriteButton.setImageResource (R.drawable.favoritered);
                    mDb.favoriteMovieDao().insertMovieToFavorites( favoriteMovieItem);



                } else {
                    mFavoriteButton.setImageResource (R.drawable.favoritewhite);
                    mDb.favoriteMovieDao().deleteByUserId( favoriteMovieItem.getExternalId());
                    Log.d("DetailedActivity",inDatabase.get(0).getTitle(),null);
                }


            }

        });
    }




}


