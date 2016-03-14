package co.fabrk.popmovies.jobs;

import android.content.ContentResolver;
import android.os.AsyncTask;

import java.util.ArrayList;

import co.fabrk.popmovies.BuildConfig;
import co.fabrk.popmovies.tmdb.TmdbRequestApi;
import co.fabrk.popmovies.ui.utils.Utility;
import co.fabrk.popmovies.tmdb.TMDBMovie;
import co.fabrk.popmovies.tmdb.TmdbDatabaseOperations;
import co.fabrk.popmovies.tmdb.TmdbConstants;

/**
 * Created by ebal on 10/09/15.
 */
public class FetchTmdbMovies extends AsyncTask<String, Void, ArrayList<TMDBMovie>>  {

    private final ContentResolver mContentResolver;

    public FetchTmdbMovies(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
        TmdbConstants.setApiKeyValue(BuildConfig.TMDB_API_KEY);
    }

    @Override
    protected ArrayList<TMDBMovie> doInBackground(String... params) {
        ArrayList<TMDBMovie> movieArrayList;
//        TmdbDatabaseOperations.deleteOldData(mContentResolver);
        switch (params[0]) {
            case TmdbConstants.POPULAR:
                // TODO: uncomment Netowrk connection set for actual devices
//            if (Utility.isWifiConnectionAvailable(mContentResolver)) {
                movieArrayList = TmdbRequestApi.getPopularMovies();
                if (movieArrayList != null) {TmdbDatabaseOperations.addBulkMovies(movieArrayList, mContentResolver);
                    TmdbDatabaseOperations.addPopularList(movieArrayList, mContentResolver);
                }
//            }
//            else movieArrayList=null;
            case TmdbConstants.HIGHEST_RATED:
                // TODO: uncomment Netowrk connection set for actual devices
//            if (Utility.isWifiConnectionAvailable(mContentResolver)) {
                movieArrayList = TmdbRequestApi.getHighestRatedMovies();
                if (movieArrayList != null) {TmdbDatabaseOperations.addBulkMovies(movieArrayList, mContentResolver);
                    TmdbDatabaseOperations.addHighestRatedList(movieArrayList, mContentResolver);
                }
//            }
//            else movieArrayList=null;
            default:
                movieArrayList=null;
                TMDBMovie movie = Utility.createHardCodedData();
                movieArrayList = new ArrayList<>();
                movieArrayList.add(movie);
        }
        return movieArrayList;
    }
}
