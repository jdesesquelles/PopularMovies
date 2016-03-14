package co.fabrk.popmovies.jobs;

import android.content.Context;
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

    private final Context mContext;

    public FetchTmdbDetail(Context context, String apiKey) {
        mContext = context;
//        TmdbConstants.setApiKeyValue(apiKey);
        TmdbConstants.setApiKeyValue(BuildConfig.TMDB_API_KEY);

    }

    @Override
    protected TMDBMovie doInBackground(TMDBMovie... movies) {
        TMDBMovie movie = TmdbRequestApi.getExtraInfo(movies[0]);
        TmdbDatabaseOperations.updateMovieExtraInfo(movie, mContext);
        return movie;
    }

    @Override
    protected void onPostExecute(TMDBMovie tmdbMovies) {
        super.onPostExecute(tmdbMovies);
    }

}
