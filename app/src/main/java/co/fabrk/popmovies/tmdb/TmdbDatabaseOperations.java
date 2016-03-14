package co.fabrk.popmovies.tmdb;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;

import co.fabrk.popmovies.data.TmdbContract;
import co.fabrk.popmovies.data.TmdbDbHelper;

import android.content.Context;

import java.util.ArrayList;
import java.util.Vector;


/**
 * Created by ebal on 02/12/15.
 */
public class TmdbDatabaseOperations {

    //TODO Disable Debug mode
    private static boolean DEBUG = true;
    private static final String LOG_TAG = TmdbDatabaseOperations.class.getSimpleName();

    public static Long InsertMovieInDb(TMDBMovie movie, ContentResolver contentResolver) {
        // First, check if the movie with this tmdb_id already exists in the db
        Cursor cursor = contentResolver.query(
                TmdbContract.MovieEntry.CONTENT_URI,
                new String[]{TmdbContract.MovieEntry._ID},
                TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID + " = ?",
                new String[]{movie.getId()},
                null);
        if (cursor.moveToFirst()) {
//            Log.v(LOG_TAG, "Found it in the database!");
            int movieIdIndex = cursor.getColumnIndex(TmdbContract.MovieEntry._ID);
            Long movieId = cursor.getLong(movieIdIndex);
            cursor.close();
            return movieId;
        } else {
//            Log.v(LOG_TAG, "Didn't find it in the database, inserting now!");
            ContentValues movieValues = new ContentValues();
            movieValues.put(TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID, movie.getId());
            movieValues.put(TmdbContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getTitle());
            movieValues.put(TmdbContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            movieValues.put(TmdbContract.MovieEntry.COLUMN_POSTER_PATH, movie.getThumbnailPath());
            movieValues.put(TmdbContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            movieValues.put(TmdbContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            movieValues.put(TmdbContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
            movieValues.put(TmdbContract.MovieEntry.COLUMN_BACKDROP, movie.getBackdrop());
            Uri movieInsertUri = contentResolver
                    .insert(TmdbContract.MovieEntry.CONTENT_URI, movieValues);
            cursor.close();
            return ContentUris.parseId(movieInsertUri);
        }
    }

    public static void addBulkMovies(ArrayList<TMDBMovie> movieArrayList, ContentResolver contentResolver) {
        Vector<ContentValues> cVVector = new Vector<>(movieArrayList.size());
        for (int i = 0; i  < movieArrayList.size(); i++) {
            ContentValues movieValues = new ContentValues();
            movieValues.put(TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID, movieArrayList.get(i).getId());
            movieValues.put(TmdbContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movieArrayList.get(i).getTitle());
            movieValues.put(TmdbContract.MovieEntry.COLUMN_RELEASE_DATE, movieArrayList.get(i).getReleaseDate());
            movieValues.put(TmdbContract.MovieEntry.COLUMN_VOTE_AVERAGE, movieArrayList.get(i).getVoteAverage());
            movieValues.put(TmdbContract.MovieEntry.COLUMN_POSTER_PATH, movieArrayList.get(i).getThumbnailPath());
            movieValues.put(TmdbContract.MovieEntry.COLUMN_OVERVIEW, movieArrayList.get(i).getOverview());
            movieValues.put(TmdbContract.MovieEntry.COLUMN_POPULARITY, movieArrayList.get(i).getPopularity());
            movieValues.put(TmdbContract.MovieEntry.COLUMN_BACKDROP, movieArrayList.get(i).getBackdrop());
            cVVector.add(movieValues);
        }
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            int rowsInserted = contentResolver
                    .bulkInsert(TmdbContract.MovieEntry.CONTENT_URI, cvArray);
        }
    }

    public static void addHighestRatedList(ArrayList<TMDBMovie> movieArrayList, ContentResolver contentResolver) {
        Vector<ContentValues> cVVector = new Vector<>(movieArrayList.size());

        for (int i = 0; i  < movieArrayList.size(); i++) {
            ContentValues movieValues = new ContentValues();
            movieValues.put(TmdbContract.HighestRatedEntry.COLUMN_TMDB_MOVIE_ID, movieArrayList.get(i).getId());
            cVVector.add(movieValues);
        }

        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            int rowsInserted = contentResolver
                    .bulkInsert(TmdbContract.HighestRatedEntry.CONTENT_URI, cvArray);
        }
    }

    public static void addPopularList(ArrayList<TMDBMovie> movieArrayList, ContentResolver contentResolver) {
        Vector<ContentValues> cVVector = new Vector<>(movieArrayList.size());

        for (int i = 0; i  < movieArrayList.size(); i++) {
            ContentValues movieValues = new ContentValues();
            movieValues.put(TmdbContract.PopularEntry.COLUMN_TMDB_MOVIE_ID, movieArrayList.get(i).getId());
            cVVector.add(movieValues);
        }

        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            int rowsInserted = contentResolver
                    .bulkInsert(TmdbContract.PopularEntry.CONTENT_URI, cvArray);
        }
    }

    public static void deleteOldData(ContentResolver contentResolver) {
        contentResolver.delete(TmdbContract.PopularEntry.CONTENT_URI, null, null);
        contentResolver.delete(TmdbContract.HighestRatedEntry.CONTENT_URI, null, null);
        contentResolver.delete(TmdbContract.MovieEntry.CONTENT_URI, null, null);
    }

    public static void updateMovieExtraInfo(TMDBMovie movie, ContentResolver contentResolver) {
        addBulkTrailers(movie.getId(), movie.getTmdbTrailers(), contentResolver);
        addBulkReviews(movie.getId(), movie.getTmdbReviews(), contentResolver);
    }

    public static void addBulkTrailers(String movieId, ArrayList<TMDBTrailer> trailerArrayList, ContentResolver contentResolver) {
        Vector<ContentValues> cVVector = new Vector<>(trailerArrayList.size());
        for (int i = 0; i  < trailerArrayList.size(); i++) {
            ContentValues movieValues = new ContentValues();
            movieValues.put(TmdbContract.TrailerEntry.COLUMN_TMDB_MOVIE_ID, movieId);
            movieValues.put(TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_NAME, trailerArrayList.get(i).getName());
            movieValues.put(TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_KEY, trailerArrayList.get(i).getKey());
            movieValues.put(TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_ID, trailerArrayList.get(i).getId());
            movieValues.put(TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_SITE, trailerArrayList.get(i).getSite());
            cVVector.add(movieValues);
        }

        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            int rowsInserted = contentResolver
                    .bulkInsert(TmdbContract.TrailerEntry.CONTENT_URI, cvArray);
            Log.v(LOG_TAG, "inserted " + rowsInserted + " rows of movie");
            Log.v(LOG_TAG, "cVVector.size() :" + cVVector.size());

            //TODO remove DEBUG Mode

            if (DEBUG) {
                Cursor cursor = contentResolver.query(
                        TmdbContract.TrailerEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );

                if (cursor.moveToFirst()) {
                    ContentValues resultValues = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(cursor, resultValues);
                    Log.v(LOG_TAG, "Query succeeded! **********");
                    for (String key : resultValues.keySet()) {
                        Log.v(LOG_TAG, key + ": " + resultValues.getAsString(key));
                    }
                } else {
                    Log.v(LOG_TAG, "Query failed! :( **********");
                }
                cursor.close();
            }
        }
    }

    public static void addBulkReviews(String movieId, ArrayList<TMDBReview> reviewArrayList, ContentResolver contentResolver) {
        Vector<ContentValues> cVVector = new Vector<>(reviewArrayList.size());
        for (int i = 0; i  < reviewArrayList.size(); i++) {
            ContentValues movieValues = new ContentValues();
            movieValues.put(TmdbContract.ReviewEntry.COLUMN_TMDB_MOVIE_ID, movieId);
            movieValues.put(TmdbContract.ReviewEntry.COLUMN_TMDB_REVIEW_AUTHOR, reviewArrayList.get(i).getAuthor());
            movieValues.put(TmdbContract.ReviewEntry.COLUMN_TMDB_REVIEW_CONTENT, reviewArrayList.get(i).getContent());
            cVVector.add(movieValues);
        }

        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            int rowsInserted = contentResolver
                    .bulkInsert(TmdbContract.ReviewEntry.CONTENT_URI, cvArray);
            Log.v(LOG_TAG, "inserted " + rowsInserted + " rows of movie");
            Log.v(LOG_TAG, "cVVector.size() :" + cVVector.size());

            //TODO remove DEBUG Mode

            if (DEBUG) {
                Cursor cursor = contentResolver.query(
                        TmdbContract.ReviewEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );

                if (cursor.moveToFirst()) {
                    ContentValues resultValues = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(cursor, resultValues);
                    Log.v(LOG_TAG, "Query succeeded! **********");
                    for (String key : resultValues.keySet()) {
                        Log.v(LOG_TAG, key + ": " + resultValues.getAsString(key));
                    }
                } else {
                    Log.v(LOG_TAG, "Query failed! :( **********");
                }
                cursor.close();
            }
        }
    }

    private static final String TAG = "TmdbDatabaseOperations";

    public static Uri addToFavorite(TMDBMovie movie, ContentResolver contentResolver) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_TMDB_MOVIE_ID, movie.getId());
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE, movie.getTitle());
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_POSTER_PATH, movie.getThumbnailPath());
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_POPULARITY, movie.getPopularity());
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_BACKDROP, movie.getBackdrop());
        Log.e(TAG, "addToFavorite: " +  movie.getTitle());
        return contentResolver.insert(TmdbContract.FavoriteEntry.CONTENT_URI, contentValues);

    }

    public static int deleteFromFavorite(String movieId, ContentResolver contentResolver) {
        Log.e(TAG, "deleteFromFavorite: " +  movieId);
        return contentResolver.delete(TmdbContract.FavoriteEntry.CONTENT_URI, TmdbContract.FavoriteEntry.COLUMN_TMDB_MOVIE_ID + " = ?", new String[]{ movieId});
    }

}
