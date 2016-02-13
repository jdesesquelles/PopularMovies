package co.fabrk.popmovies.discover;

import android.database.Cursor;

/**
 * Created by ebal on 15/12/15.
 */
public interface DiscoverContract {
    interface View {
        void showMovieGrid();
        void showSplash();
//        void startAnimation();
    }

    interface UserActionsListener {
        Cursor getPopularMovieList();
//        void getHighestRatedMovieList();
//        void getFavoriteMovieList();
//        void openMovieDetail(TMDBMovie movie);
//        void isFavoriteMovie();
//        void refreshData();
    }
}
