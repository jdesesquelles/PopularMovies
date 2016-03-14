package co.fabrk.popmovies.ui.utils;

import co.fabrk.popmovies.tmdb.TMDBMovie;

/**
 * Created by ebal on 18/08/15.
 */
public interface iTMDB {

    void onArrayListFilled(TMDBMovie movies);

    void addToFavorite(TMDBMovie movie);

}
