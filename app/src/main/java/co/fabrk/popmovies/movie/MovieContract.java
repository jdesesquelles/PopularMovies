package co.fabrk.popmovies.movie;

import java.util.ArrayList;

import co.fabrk.popmovies.tmdb.TMDBMovie;
import co.fabrk.popmovies.tmdb.TMDBReview;
import co.fabrk.popmovies.tmdb.TMDBTrailer;

/**
 * Created by ebal on 15/12/15.
 */
public interface MovieContract {
    interface View {
        void showMovieDetail(TMDBMovie movie);
        void showTrailers(ArrayList<TMDBTrailer> trailers);
        void showReviews(ArrayList<TMDBReview> reviews);
        void toggleFavorite();
    }

    interface UserActionsListener {
        void playVideo(TMDBTrailer trailer);
        void addToFavorite(TMDBMovie movie);
    }
}
