package co.fabrk.popmovies.tmdb;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import co.fabrk.popmovies.data.TmdbContract;

/**
 * Created by ebal on 02/12/15.
 */
public class TmdbProviderUtils {

    public static boolean isFavoriteMovie(TMDBMovie movie, Context context) {
        String id = movie.getId();
        Uri movieUri = TmdbContract.FavoriteEntry.buildFavoriteWithMovieId(id);
        Cursor cursor = context.getContentResolver().query(
                movieUri,
                null,
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public static boolean isFavoriteMovie(String movieId, Context context) {
        Uri uri = TmdbContract.FavoriteEntry.buildFavoriteWithMovieId(movieId);
        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
}
