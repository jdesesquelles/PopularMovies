package co.fabrk.popmovies.data;

import android.database.Cursor;

/**
 * Created by ebal on 16/12/15.
 */
public interface TmdbServiceApi {
    interface TmdbServiceCallback<T> {
        void onLoaded(T movies);
    }

    Cursor getUri(String uri);
//    void getPopularMovies(TmdbServiceCallback<Cursor> callback);
//    void getHighestRatedMovies(TmdbServiceCallback<ArrayList<TMDBMovie>> callback);
//    void getFavoriteMovies(TmdbServiceCallback<ArrayList<TMDBMovie>> callback);
//
//    void addMovieToFavorite(TmdbServiceCallback<TMDBMovie> callback);
//    void removeMovieFromFavorite(TmdbServiceCallback<TMDBMovie> callback);
//
//    void getMovieDetailInformation(TmdbServiceCallback<TMDBMovie> callback);
//    void getMovieThumbnail(TmdbServiceCallback<Drawable> callback);
//    void getMovieBackDrop(TmdbServiceCallback<Drawable> callback);
//
//
//    void getMovieTrailers(TmdbServiceCallback<ArrayList<TMDBTrailer>> callback);
//    void getMovieReviews(TmdbServiceCallback<ArrayList<TMDBReview>> callback);

}
