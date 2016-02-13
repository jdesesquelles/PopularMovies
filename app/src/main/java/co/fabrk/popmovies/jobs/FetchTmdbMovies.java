package co.fabrk.popmovies.jobs;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import co.fabrk.popmovies.tmdb.TmdbRequestApi;
import co.fabrk.popmovies.utils.Utility;
import co.fabrk.popmovies.tmdb.TMDBMovie;
import co.fabrk.popmovies.tmdb.TmdbDatabaseOperations;
import co.fabrk.popmovies.tmdb.TmdbConstants;

/**
 * Created by ebal on 10/09/15.
 */
public class FetchTmdbMovies extends AsyncTask<String, Void, ArrayList<TMDBMovie>>  {

    private final Context mContext;

    public FetchTmdbMovies(Context context, String apiKey) {
        mContext = context;
        TmdbConstants.setApiKeyValue(apiKey);
    }

    @Override
    protected ArrayList<TMDBMovie> doInBackground(String... params) {
        ArrayList<TMDBMovie> movieArrayList;
//        TmdbDatabaseOperations.deleteOldData(mContext);
        switch (params[0]) {
            case TmdbConstants.POPULAR:
                // TODO: uncomment Netowrk connection set for actual devices
//            if (Utility.isWifiConnectionAvailable(mContext)) {
                movieArrayList = TmdbRequestApi.getPopularMovies();
                if (movieArrayList != null) {TmdbDatabaseOperations.addBulkMovies(movieArrayList, mContext);
                    TmdbDatabaseOperations.addPopularList(movieArrayList, mContext);
                }
//            }
//            else movieArrayList=null;
            case TmdbConstants.HIGHEST_RATED:
                // TODO: uncomment Netowrk connection set for actual devices
//            if (Utility.isWifiConnectionAvailable(mContext)) {
                movieArrayList = TmdbRequestApi.getHighestRatedMovies();
                if (movieArrayList != null) {TmdbDatabaseOperations.addBulkMovies(movieArrayList, mContext);
                    TmdbDatabaseOperations.addHighestRatedList(movieArrayList, mContext);
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
