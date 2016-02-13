package co.fabrk.popmovies.tmdb;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import co.fabrk.popmovies.data.TmdbContract;

/**
 * Created by ebal on 26/07/15.
 */
public class TMDBMovie implements Parcelable {

    //Todo: adjust backrop and poster size
    //Then you will need a ‘size’, which will be one of the following:
    // "w92", "w154", "w185", "w342", "w500", "w780", or "original".
    // For most phones we recommend using “w185”.
    // getSystemService.getWitdh(), switch case

    //Member variables
    private String imageBaseUrl = "http://image.tmdb.org/t/p/";
    private String imageWidth = "w342";
    private String BaseUrl = imageBaseUrl + imageWidth;
//    "http://image.tmdb.org/t/p/w185";
    public String id;
    public String title;
    public String thumbnailPath;
    public String releaseDate;
    public String voteAverage;
    public String popularity;
    public String overview;
    public String backdrop;
    private ArrayList<TMDBTrailer> tmdbTrailers;
    private ArrayList<TMDBReview> tmdbReviews;

    // Parcelable interface
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TMDBMovie createFromParcel(Parcel in) {
            return new TMDBMovie(in);
        }

        public TMDBMovie[] newArray(int size) {
            return new TMDBMovie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(thumbnailPath);
        dest.writeString(releaseDate);
        dest.writeString(voteAverage);
        dest.writeString(popularity);
        dest.writeString(overview);
        dest.writeString(backdrop);


        if (tmdbReviews != null) {dest.writeTypedList(tmdbReviews);}
        if (tmdbTrailers != null) {dest.writeTypedList(tmdbTrailers);}
    }

    public TMDBMovie(String[] movieInfo) {
        this.id = movieInfo[0];
        this.title = movieInfo[1];
        this.thumbnailPath = movieInfo[2];
        this.releaseDate = movieInfo[3];
        this.voteAverage = movieInfo[4];
        this.overview = movieInfo[5];
        this.popularity = movieInfo[6];
        this.backdrop = movieInfo[7];

    }

    public TMDBMovie(Cursor cursor) {
        this.id = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID));
        this.title = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_ORIGINAL_TITLE));
        this.thumbnailPath = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_POSTER_PATH));
        this.releaseDate = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_RELEASE_DATE));
        this.voteAverage = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_VOTE_AVERAGE));
        this.overview = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_OVERVIEW));
        this.popularity = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_POPULARITY));
        this.backdrop = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_BACKDROP));

    }

    private TMDBMovie(Parcel in) {

        this.id = in.readString();
        this.title = in.readString();
        this.thumbnailPath = BaseUrl + in.readString();
        this.releaseDate = in.readString();
        this.voteAverage = in.readString();
        this.popularity = in.readString();
        this.overview = in.readString();
        this.backdrop = in.readString();

        try {
            in.readTypedList(tmdbReviews, TMDBReview.CREATOR);
        } catch (NullPointerException e){}

        try {
            in.readTypedList(tmdbTrailers, TMDBTrailer.CREATOR);
        } catch (NullPointerException e){}
    }

    // Getters and Setter

    public String getPopularity() {
        return popularity;
    }
    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }
    public ArrayList<TMDBTrailer> getTmdbTrailers() {
        return tmdbTrailers;
    }
    public void setTmdbTrailers(ArrayList<TMDBTrailer> tmdbTrailers) {
        this.tmdbTrailers = tmdbTrailers;
    }

    public ArrayList<TMDBReview> getTmdbReviews() {
        return tmdbReviews;
    }
    public void setTmdbReviews(ArrayList<TMDBReview> tmdbReviews) {
        this.tmdbReviews = tmdbReviews;
    }


    public String getBackdrop() {
        return BaseUrl + backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailPath() {
        return BaseUrl + thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
