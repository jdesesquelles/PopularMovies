package co.fabrk.popmovies.discover;

import co.fabrk.popmovies.tmdb.TMDBMovie;

/**
 * Created by ebal on 15/12/15.
 */
public interface MoviePresenterTest {
    interface View {
        void showMovieGrid();
        void showSplash();
    }

    interface UserActionsListener {
        void openMovieDetail(TMDBMovie movie);
    }
}
