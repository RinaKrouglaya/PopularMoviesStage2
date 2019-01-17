package com.example.android.popularmoviesstage2;


/**
 * A {@link  MovieItem} object contains information related to a single  MovieItem.
 */
public class MovieItem {

    /**
     * Constant value that represents no Plot Synopsis was provided for this MovieItem
     */
    private static final String NO_PLOT_SYNOPSIS_PROVIDED = "Null";

    /**
     * Constant value that represents no User Rating was provided for this MovieItem
     */
    private static final Double NO_USER_RATING_PROVIDED = 0.0;

    /**
     * Constant value that represents no release date was provided for this movie
     */
    private static final String NO_DATE_PROVIDED = "Null";

    /**
     * Original title of the movie
     */
    private String mOriginalTitle;

    /**
     * Poster of the movie
     */
    private String mPoster;

    /**
     * Thumbnail image of the movie
     */
    private String mThumbnailImage;


    /**
     * Plot synopsis of the movie
     */
    private String mPlotSynopsis = NO_PLOT_SYNOPSIS_PROVIDED;

    /**
     * User rating of the movie
     */
    private Double mUserRating = NO_USER_RATING_PROVIDED;

    /**
     * The ID of the movie
     */
    private int mId;

    /**
     * Release Date of the movie
     */
    private String mReleaseDate = NO_DATE_PROVIDED;



    /**
     * Constructs a new {@link  MovieItem} object with all the parameters.
     *
     * @param originalTitle  original title of the movie
     * @param poster         poster of the movie
     * @param thumbnailImage thumbnail image of the movie
     * @param plotSynopsis   plot synopsis of the movie
     * @param userRating     user rating of the movie
     * @param id             movie ID
     * @param releaseDate    the date when the movie was released
     *
     */
    public MovieItem(String originalTitle, String poster , String thumbnailImage,
                     String plotSynopsis, Double userRating, int id, String releaseDate) {
        mOriginalTitle = originalTitle;
        mPoster = poster;
        mThumbnailImage = thumbnailImage;
        mPlotSynopsis = plotSynopsis;
        mUserRating = userRating;
        mId = id;
        mReleaseDate = releaseDate;
    }



    /**
     * Returns the original title of the  movie
     */
    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    /**
     * Returns part of the web address  - link to the poster of the movie
     */
    public String getPoster() {
        return mPoster;
    }

    /**
     * Returns part of the web address  - link to the thumbnail image of the movie
     */
    public String getThumbnailImage() {
        return mThumbnailImage;
    }


    /**
     * Returns the plot synopsis of the movie
     */
    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }


    /**
     * Returns the user rating of the movie
     */
    public Double getUserRating() {
        return mUserRating;
    }

    /**
     * Returns the id the movie
     */
    public int getId() {
        return mId;
    }


    /**
     * Returns the release date  of the movie
     */
    public String getReleaseDate() {
        return mReleaseDate;
    }

}

