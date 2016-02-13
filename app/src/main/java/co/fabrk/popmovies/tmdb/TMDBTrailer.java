package co.fabrk.popmovies.tmdb;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ebal on 19/08/15.
 */
public class TMDBTrailer implements Parcelable {
    public String movieId;
    public String name;
    public String key;
    public String id;
    public String site;

    /* Constructors */
    public TMDBTrailer() {
        movieId = null;
        name = null;
        key = null;
        id = null;
        site = null;
    }
    public TMDBTrailer(String[] movieInfo) {
        this.movieId = movieInfo[0];
        this.name = movieInfo[1];
        this.key = movieInfo[2];
        this.id = movieInfo[3];
        this.site = movieInfo[4];
    }
    private TMDBTrailer(Parcel in) {
        this.movieId = in.readString();
        this.name = in.readString();
        this.key = in.readString();
        this.id = in.readString();
        this.site = in.readString();
    }

    // Parcelable interface
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TMDBTrailer createFromParcel(Parcel in) {
            return new TMDBTrailer(in);
        }

        public TMDBTrailer[] newArray(int size) {
            return new TMDBTrailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(name);
        dest.writeString(key);
        dest.writeString(id);
        dest.writeString(site);
    }

    /* Getters and setter */

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
