package com.example.android.popularmoviesstage2.utilities;


import android.net.Uri;
import android.util.Log;

import com.example.android.popularmoviesstage2.APIKey;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the movie servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private NetworkUtils() { };

    private static final String THE_MOVIE_DB_REQUEST_URL =
            "https://api.themoviedb.org/3/movie/";
    final static String API_KEY_LABEL_NAME = "api_key";


    /**
     * Builds the URL used to talk to the movie server.
     *
     * @return The URL to use to query the movie server.
     */
    public static URL buildUrl(String sortOrderParam) {
        Log.v(TAG, sortOrderParam);
        Uri builtUri = Uri.parse(THE_MOVIE_DB_REQUEST_URL).buildUpon()
                .appendPath(sortOrderParam)
                .appendQueryParameter(API_KEY_LABEL_NAME, APIKey.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

      /**
     * Builds the URL used to talk to the movie server.
     *
     * @return The URL to use to query reviews from the movie server.
     */
    public static URL buildReviewsUrl(String movieId) {
        Log.v(TAG, movieId);
        Uri builtUri = Uri.parse(THE_MOVIE_DB_REQUEST_URL).buildUpon()
                .appendPath(movieId)
                .appendPath("reviews")
                .appendQueryParameter(API_KEY_LABEL_NAME, APIKey.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI for reviews query" + url);

        return url;
    }



    /**
     * Builds the URL used to talk to the movie server.
     *
     * @return The URL to use to query reviews from the movie server.
     */
    public static URL buildTrailersUrl(String movieId) {
        Log.v(TAG, movieId);
        Uri builtUri = Uri.parse(THE_MOVIE_DB_REQUEST_URL).buildUpon()
                .appendPath(movieId)
                .appendPath("videos")
                .appendQueryParameter(API_KEY_LABEL_NAME, APIKey.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI for trailers query " + url);

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}