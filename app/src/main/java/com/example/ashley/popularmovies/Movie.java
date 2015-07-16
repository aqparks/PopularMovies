package com.example.ashley.popularmovies;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * The <code>Movie</code> describes a Movie object that is maintained by the movie database
 * found at themoviedb.org.
 *
 * @author aqparks@gmail.com
 * @version 1.0
 *
 * Parcelable ref: http://www.perfectapk.com/android-parcelable.html
 */
public class Movie implements Parcelable {

    String backdrop = null;
    ArrayList<Integer> genres;
    int id;
    String overview = null;
    String releaseDate;
    String poster = null;
    double popularity;
    String title = null;
    double voteAverage;
    int voteCount;

    private static final String KEY_BACKDROP = "backdrop";
    private static final String KEY_GENRES = "genres";
    private static final String KEY_ID = "id";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_POSTER = "poster";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_VOTE_COUNT = "vote_count";

    // default constructor
    public Movie() {

    }

    public Movie(String backdrop, ArrayList<Integer> genres, int id, String overview,
                 String releaseDate, String poster, double popularity, String title,
                 double voteAverage, int voteCount) {
        this.backdrop = backdrop;
        this.genres = genres;
        this.id = id;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.popularity = popularity;
        this.title = title;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    // getters and setters
    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public ArrayList<Integer> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Integer> genres) {
        this.genres = genres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return ("\nID: " + getId()
                +"\nTitle: " + getTitle()
                +"\nBackdrop: " + getBackdrop()
                +"\nPoster: " + getPoster()
                +"\nReleaseDate: " + getReleaseDate()
                +"\nPopularity: " + getPopularity()
                + "\nVoteCount: " + getVoteCount()
                + "\nVoteAverage: " + getVoteAverage()
                + "\nGenres: " + Arrays.toString(getGenres().toArray()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();

        bundle.putString(KEY_BACKDROP, backdrop);
        bundle.putIntegerArrayList(KEY_GENRES, genres);
        bundle.putInt(KEY_ID, id);
        bundle.putString(KEY_OVERVIEW, overview);
        bundle.putString(KEY_RELEASE_DATE, getReleaseDate());
        bundle.putString(KEY_POSTER, poster);
        bundle.putDouble(KEY_POPULARITY, popularity);
        bundle.putString(KEY_TITLE, title);
        bundle.putDouble(KEY_VOTE_AVERAGE, voteAverage);
        bundle.putInt(KEY_VOTE_COUNT, voteCount);

        dest.writeBundle(bundle);

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            Bundle bundle = source.readBundle();

            return new Movie(bundle.getString(KEY_BACKDROP),
                    bundle.getIntegerArrayList(KEY_GENRES),
                    bundle.getInt(KEY_ID),
                    bundle.getString(KEY_OVERVIEW),
                    bundle.getString(KEY_RELEASE_DATE),
                    bundle.getString(KEY_POSTER),
                    bundle.getDouble(KEY_POPULARITY),
                    bundle.getString(KEY_TITLE),
                    bundle.getDouble(KEY_VOTE_AVERAGE),
                    bundle.getInt(KEY_VOTE_COUNT));
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
