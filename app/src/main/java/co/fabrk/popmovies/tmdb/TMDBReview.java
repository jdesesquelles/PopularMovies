package co.fabrk.popmovies.tmdb;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ebal on 19/08/15.
 */
public class TMDBReview implements Parcelable {
    public String movieId;
    public String author;
    public String content;

    public TMDBReview(String movieId, String author, String content) {
        this.movieId = movieId;
        this.author = author;
        this.content = content;
    }

    public TMDBReview() {
        movieId = null;
        author = null;
        content = null;
    }

    // Parcelable interface
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TMDBReview createFromParcel(Parcel in) {
            return new TMDBReview(in);
        }

        public TMDBReview[] newArray(int size) {
            return new TMDBReview[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(author);
        dest.writeString(content);
    }

    public TMDBReview(String[] reviewDetail) {
        this.movieId = reviewDetail[0];
        this.author = reviewDetail[1];
        this.content = reviewDetail[2];
    }

    private TMDBReview(Parcel in) {
        this.movieId = in.readString();
        this.author = in.readString();
        this.content = in.readString();
    }

    // Getters and Setters

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
