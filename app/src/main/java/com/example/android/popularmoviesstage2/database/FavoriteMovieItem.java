package com.example.android.popularmoviesstage2.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * A {@link  FavoriteMovieItem} object contains information related to a single  FavoriteMovieItem.
 */

@Entity (tableName = "favorites")
public class FavoriteMovieItem {

    private String title;
    private String poster;
    private String thumbnail;
    private String synopsis;
    private String rating;
    @ColumnInfo(name = "external_id")
    private String externalId;
    private String date;

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "local_id")
    private int localId ;




    public FavoriteMovieItem(String title, String poster , String thumbnail,
                             String synopsis, String rating,
                             String externalId, String date, int localId ) {
        this.title = title;
        this.poster = poster;
        this.thumbnail = thumbnail;
        this.synopsis = synopsis;
        this.rating = rating;
        this.externalId = externalId;
        this.date = date;
        this.localId = localId;
    }




    @Ignore
    public FavoriteMovieItem(String title, String poster , String thumbnail,
                             String synopsis, String rating,
                             String externalId, String date ) {
        this.title = title;
        this.poster = poster;
        this.thumbnail = thumbnail;
        this.synopsis = synopsis;
        this.rating = rating;
        this.externalId = externalId;
        this.date = date;


    }



    /**
     * Original title of the  movie
     */
    public String getTitle() {
        return title;
    }


    public void setTitle(String originalTitle) {
        this.title = originalTitle;
    }

    /**
     * Part of the web address  - link to the poster of the movie
     */
    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    /**
     * Part of the web address  - link to the thumbnail image of the movie
     */
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnailImage) {
        this.thumbnail = thumbnailImage;
    }

    /**
     * Plot synopsis of the movie
     */
    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String plotSynopsis) {
            this.synopsis = plotSynopsis;
    }

    /**
     * User rating of the movie
     */
    public String getRating() {
        return rating;
    }

    public void setRating(String userRating) {
        this.rating = userRating;
    }

    /**
     * Id of the movie for external Movies' Database
     */
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String idFromMoviesDatabase) {
        this.externalId = idFromMoviesDatabase;
    }


    /**
     * Release date  of the movie
     */
    public String getDate() {
        return date;
    }
    public void setDate(String releaseDate) {
        this.date = releaseDate;
    }




    /**
     * Id of the movie for internal Movies' Database
     */


    public Integer getLocalId() {
        return localId;
    }

    public void setLocalId(int idFromLocalFavoritesDatabase) {
        this.localId = idFromLocalFavoritesDatabase;
    }


}

