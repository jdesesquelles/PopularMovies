package co.fabrk.popmovies.discover;

import android.app.Activity;
import android.database.Cursor;

import co.fabrk.popmovies.data.TmdbServiceApi;
/**
 * Created by ebal on 15/12/15.
 */
public class DiscoverPresenter implements DiscoverContract.UserActionsListener{
    Activity activity;
    private final TmdbServiceApi mTmdbServiceApi;
    private final DiscoverContract.View mDiscoverView;


    public DiscoverPresenter(TmdbServiceApi tmdbServiceApi, DiscoverContract.View discoverView) {
        this.mTmdbServiceApi = tmdbServiceApi;
        this.mDiscoverView = discoverView;
    }


    @Override
    public Cursor getPopularMovieList() {
//        mTmdbServiceApi.getUri(TmdbContract.PopularEntry.CONTENT_URI);
        return null;
    }

//    @Override
//    public void getHighestRatedMovieList() {
//
//    }
//
//    @Override
//    public void getFavoriteMovieList() {
//
//    }
//
//    @Override
//    public void addMovieToFavorite() {
//
//    }
//
//    @Override
//    public void isFavoriteMovie() {
//
//    }
//
//    @Override
//    public void openMovieDetail(TMDBMovie movie) {
//        if (movie != null) {
////                    ((Callback) activity)
////                            .onItemSelected(movie);
//        }
//    }

}
