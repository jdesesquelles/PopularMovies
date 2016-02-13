package co.fabrk.popmovies.tmdb;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ebal on 04/11/15.
 */
public class TmdbTvSerie implements Parcelable {
    // http://api.themoviedb.org/3/discover/tv?api_key=
//    "backdrop_path":"/tPwpc9Uo1Fly50urDxfGWWk7H77.jpg",
//            "first_air_date":"2005-03-26",
//            "genre_ids":[
//            28,
//            12,
//            18,
//            878
//            ],
//            "id":57243,
//            "original_language":"en",
//            "original_name":"Doctor Who",
//            "overview":"The Doctor looks and seems human. He's handsome, witty, and could be mistaken for just another man in the street. But he is a Time Lord: a 900 year old alien with 2 hearts, part of a gifted civilization who mastered time travel. The Doctor saves planets for a living – more of a hobby actually, and he's very, very good at it. He's saved us from alien menaces and evil from before time began – but just who is he?",
//            "origin_country":[
//            "GB"
//            ],
//            "poster_path":"/igDhbYQTvact1SbNDbzoeiFBGda.jpg",
//            "popularity":76.156754,
//            "name":"Doctor Who",
//            "vote_average":8.0,
//            "vote_count":132

    private String backdrop_path;
    private String first_air_date;
    private String[] genre_ids;
    private String id;
    private String original_language;
    private String original_name;
    private String overview;
    private String[] origin_country;
    private String poster_path;
    private String popularity;
    private String name;
    private String vote_average;
    private String vote_count;

    //Constructors
    public TmdbTvSerie(String[] tvSerieInfo, String[] tvSerieGenreIds, String[] tvSerieOriginCountry) {
        this.backdrop_path = tvSerieInfo[0];
        this.first_air_date = tvSerieInfo[1];
        this.genre_ids = tvSerieGenreIds;
        this.id = tvSerieInfo[2];
        this.original_language = tvSerieInfo[3];
        this.original_name = tvSerieInfo[4];
        this.overview = tvSerieInfo[5];
        this.origin_country = tvSerieOriginCountry;
        this.poster_path = tvSerieInfo[6];
        this.popularity = tvSerieInfo[7];
        this.name = tvSerieInfo[8];
        this.vote_average = tvSerieInfo[9];
        this.vote_count = tvSerieInfo[10];
    }

    public TmdbTvSerie(Cursor cursor) {
//        this.id = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID));
//        this.title = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_ORIGINAL_TITLE));
//        this.thumbnailPath = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_POSTER_PATH));
//        this.releaseDate = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_RELEASE_DATE));
//        this.voteAverage = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_VOTE_AVERAGE));
//        this.overview = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_OVERVIEW));
//        this.popularity = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_POPULARITY));
//        this.backdrop = cursor.getString(cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_BACKDROP));

    }

    private TmdbTvSerie(Parcel in) {

        this.backdrop_path = in.readString();
        this.first_air_date = in.readString();
        this.genre_ids = in.createStringArray();
        this.id = in.readString();
        this.original_language = in.readString();
        this.original_name = in.readString();
        this.overview = in.readString();
        this.origin_country = in.createStringArray();
        this.poster_path = in.readString();
        this.popularity = in.readString();
        this.name = in.readString();
        this.vote_average = in.readString();
        this.vote_count = in.readString();
    }

    //Parcelable
    // Parcelable interface
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TmdbTvSerie createFromParcel(Parcel in) {
            return new TmdbTvSerie(in);
        }

        public TmdbTvSerie[] newArray(int size) {
            return new TmdbTvSerie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(backdrop_path);
        dest.writeString(first_air_date);
        dest.writeStringArray(genre_ids);
        dest.writeString(id);
        dest.writeString(original_language);
        dest.writeString(original_name);
        dest.writeString(overview);
        dest.writeStringArray(origin_country);
        dest.writeString(poster_path);
        dest.writeString(popularity);
        dest.writeString(name);
        dest.writeString(vote_average);
        dest.writeString(vote_count);
    }


    // Getters and Setters
    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String[] getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(String[] origin_country) {
        this.origin_country = origin_country;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(String[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
}
