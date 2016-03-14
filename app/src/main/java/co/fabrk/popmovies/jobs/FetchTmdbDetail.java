package co.fabrk.popmovies.jobs;

import android.content.ContentResolver;
import android.os.AsyncTask;

import co.fabrk.popmovies.tmdb.TmdbDatabaseOperations;
import co.fabrk.popmovies.tmdb.TmdbRequestApi;
import co.fabrk.popmovies.tmdb.TMDBMovie;
import co.fabrk.popmovies.tmdb.TmdbConstants;
import co.fabrk.popmovies.BuildConfig;

/**
 * Created by ebal on 18/08/15.
 */

public class FetchTmdbDetail extends AsyncTask<TMDBMovie, Void, TMDBMovie> {

    private final ContentResolver mContentResolver;

    public FetchTmdbDetail(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
        TmdbConstants.setApiKeyValue(BuildConfig.TMDB_API_KEY);

    }

    @Override
    protected TMDBMovie doInBackground(TMDBMovie... movies) {
        TMDBMovie movie = TmdbRequestApi.getExtraInfo(movies[0]);
        TmdbDatabaseOperations.updateMovieExtraInfo(movie, mContentResolver);
        return movie;
    }

    @Override
    protected void onPostExecute(TMDBMovie tmdbMovies) {
        super.onPostExecute(tmdbMovies);
    }

}
