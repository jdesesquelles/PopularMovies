package co.fabrk.popmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import co.fabrk.popmovies.data.TmdbContract.*;


/**
 * Manages a local database for the Movie database API data.
 */
public class TmdbDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "TMDB.db";

    public TmdbDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        CreateFavoriteMoviesTable(sqLiteDatabase);
        CreateTMDBMoviesTable(sqLiteDatabase);
        CreateTMDBReviewsTable(sqLiteDatabase);
        CreateTMDBTrailersTable(sqLiteDatabase);
        CreateTMDBPopularTable(sqLiteDatabase);
        CreateTMDBHighestRatedTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HighestRatedEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // Create table methods
    private void CreateFavoriteMoviesTable(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                FavoriteEntry._ID + " INTEGER PRIMARY KEY, " +
                FavoriteEntry.COLUMN_TMDB_MOVIE_ID + " INTEGER UNIQUE NOT NULL," +
                FavoriteEntry.COLUMN_ORIGINAL_TITLE + " NUMERIC NOT NULL, " +
                FavoriteEntry.COLUMN_POSTER_PATH + " NUMERIC NOT NULL, " +
                FavoriteEntry.COLUMN_RELEASE_DATE + " NUMERIC NOT NULL, " +
                FavoriteEntry.COLUMN_VOTE_AVERAGE + " DOUBLE, " +
                FavoriteEntry.COLUMN_POPULARITY + " DOUBLE, " +
                FavoriteEntry.COLUMN_OVERVIEW + " NUMERIC NOT NULL, " +
                FavoriteEntry.COLUMN_BACKDROP + " NUMERIC NOT NULL, " +
//                FavoriteEntry.COLUMN_GENRE + " TEXT, " +
//                FavoriteEntry.COLUMN_COUNTRY + " TEXT, " +
                " UNIQUE (" + FavoriteEntry.COLUMN_TMDB_MOVIE_ID + ") ON CONFLICT IGNORE);";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    private void CreateTMDBMoviesTable(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_TMDB_MOVIE_ID + " INTEGER UNIQUE NOT NULL," +
                MovieEntry.COLUMN_ORIGINAL_TITLE + " NUMERIC NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " NUMERIC NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " NUMERIC NOT NULL, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " DOUBLE, " +
                MovieEntry.COLUMN_POPULARITY + " DOUBLE, " +
                MovieEntry.COLUMN_OVERVIEW + " NUMERIC NOT NULL, " +
                MovieEntry.COLUMN_BACKDROP + " NUMERIC NOT NULL, " +
//                MovieEntry.COLUMN_GENRE + " TEXT, " +
//                MovieEntry.COLUMN_COUNTRY + " TEXT, " +
                " UNIQUE (" + MovieEntry.COLUMN_TMDB_MOVIE_ID + ") ON CONFLICT IGNORE);";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    private void CreateTMDBReviewsTable(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                ReviewEntry._ID + " INTEGER PRIMARY KEY," +
                ReviewEntry.COLUMN_TMDB_MOVIE_ID + " INTEGER NOT NULL, " +
                ReviewEntry.COLUMN_TMDB_REVIEW_AUTHOR + " NUMERIC, " +
                ReviewEntry.COLUMN_TMDB_REVIEW_CONTENT + " NUMERIC NOT NULL, " +
                " FOREIGN KEY (" + ReviewEntry.COLUMN_TMDB_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_TMDB_MOVIE_ID + ") " +
                " UNIQUE (" + ReviewEntry.COLUMN_TMDB_MOVIE_ID + "," + ReviewEntry.COLUMN_TMDB_REVIEW_AUTHOR +"," + ReviewEntry.COLUMN_TMDB_REVIEW_CONTENT +") ON CONFLICT IGNORE);";
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    private void CreateTMDBTrailersTable(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +
                TrailerEntry._ID + " INTEGER PRIMARY KEY," +
                TrailerEntry.COLUMN_TMDB_MOVIE_ID + " INTEGER NOT NULL, " +
                TrailerEntry.COLUMN_TMDB_TRAILER_NAME + " NUMERIC NOT NULL, " +
                TrailerEntry.COLUMN_TMDB_TRAILER_KEY + " NUMERIC NOT NULL, " +
                TrailerEntry.COLUMN_TMDB_TRAILER_ID + " NUMERIC NOT NULL, " +
                TrailerEntry.COLUMN_TMDB_TRAILER_SITE + " NUMERIC NOT NULL, " +
                " FOREIGN KEY (" + TrailerEntry.COLUMN_TMDB_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") " +
                " UNIQUE (" + TrailerEntry.COLUMN_TMDB_TRAILER_KEY +") ON CONFLICT IGNORE);";
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
    }

    private void CreateTMDBPopularTable(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_POPULAR_TABLE = "CREATE TABLE " + PopularEntry.TABLE_NAME + " (" +
                PopularEntry._ID + " INTEGER PRIMARY KEY," +
                PopularEntry.COLUMN_TMDB_MOVIE_ID + " INTEGER NOT NULL, " +
                " UNIQUE (" + PopularEntry.COLUMN_TMDB_MOVIE_ID + ") ON CONFLICT IGNORE " +
        " FOREIGN KEY (" + PopularEntry.COLUMN_TMDB_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_TMDB_MOVIE_ID + ") " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_TABLE);
    }

    private void CreateTMDBHighestRatedTable(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_HIGHEST_RATED_TABLE = "CREATE TABLE " + HighestRatedEntry.TABLE_NAME + " (" +
                HighestRatedEntry._ID + " INTEGER PRIMARY KEY," +
                HighestRatedEntry.COLUMN_TMDB_MOVIE_ID + " INTEGER NOT NULL, " +
                " UNIQUE (" + HighestRatedEntry.COLUMN_TMDB_MOVIE_ID + ") ON CONFLICT IGNORE " +
                " FOREIGN KEY (" + HighestRatedEntry.COLUMN_TMDB_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_TMDB_MOVIE_ID + ") " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_HIGHEST_RATED_TABLE);

    }
}
