package com.example.android.popularmoviesstage2.utilities;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmoviesstage2.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Utility functions to handle movie database JSON data.
 */

public final class JsonUtils {


    private static final String DATABASE_IMAGE_LINK = "https://image.tmdb.org/t/p/w500/";
    /**
     * This method parses JSON from a web response and returns the ArrayList of MovieItems

     * @param moviesJsonStr JSON response from server
     *
     * @return ArrayList of MovieItems
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static ArrayList<MovieItem> getListOfMovieItemsFromJson(String moviesJsonStr)
            throws JSONException {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(moviesJsonStr)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding movieItems to
        ArrayList<MovieItem> movieItems = new ArrayList<>();


        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the root of the JSON response string
            JSONObject rootObject = new JSONObject(moviesJsonStr);

            // gets the JSONArray with the results
            JSONArray resultsArray = rootObject.getJSONArray("results");


            // Creates a for loop that goes through the arrayList of results
            for (int i = 0; i < resultsArray.length(); i++) {

                // Get a single movieItem at position index within the list of movies
                JSONObject movieObject = resultsArray.getJSONObject(i);

                // Extract the value for the key called "original_title"
                String originalTitle = movieObject.getString("original_title");

                // Extract the value for the key called "vote_average"
                Double voteAverage = movieObject.getDouble("vote_average");

                // Extract the value for the key called "id"
                int movieId = movieObject.getInt("id");

                // Extract the value for the key called "poster_path"
                String posterPath = movieObject.getString("poster_path");

                // Extract the value for the key called "backdrop_path"
                String backdropPath = movieObject.getString("backdrop_path");

                // Extract the value for the key called "overview"
                String overview = movieObject.getString("overview");

                // Extract the value for the key called "release_date"
                String releasedDate = movieObject.getString("release_date");

                String linkToImage = DATABASE_IMAGE_LINK+ posterPath;

                String linkToThumbnail = DATABASE_IMAGE_LINK + backdropPath;

                // Create a new {@link MovieItem} object
                MovieItem movieItemObject = new MovieItem(originalTitle,linkToImage ,linkToThumbnail,
                        overview, voteAverage, movieId, releasedDate);
                movieItems.add(movieItemObject);


            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the movieItem JSON results", e);
        }

        // Return the ArrayList of movieItems
        return movieItems;
    }

}
