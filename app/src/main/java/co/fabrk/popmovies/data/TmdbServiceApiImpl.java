package co.fabrk.popmovies.data;

import android.database.Cursor;

/**
 * Created by ebal on 16/12/15.
 */
public class TmdbServiceApiImpl implements TmdbServiceApi {

    @Override
    public Cursor getUri(String uri) {
    return null;

    }

//    @Override
//    public void getPopularMovies(TmdbServiceCallback<Cursor> callback) {
////        Uri popularMovieUri = TmdbContract.PopularEntry.CONTENT_URI;
////    Cursor cursor =  new CursorLoader(getActivity(),
////    popularMovieUri,
////            null,
////            null,
////            null,
////            null);
//
//    }

//    case POPULAR_MOVIE_LOADER:
//    Uri popularMovieUri = TmdbContract.PopularEntry.CONTENT_URI;
//    return new CursorLoader(getActivity(),
//    popularMovieUri,
//            null,
//            null,
//            null,
//            null);
//    case HIGHEST_RATED_MOVIE_LOADER:
//    Uri highestRatedMovieUri = TmdbContract.HighestRatedEntry.CONTENT_URI;
//    return new CursorLoader(getActivity(),
//    highestRatedMovieUri,
//            null,
//            null,
//            null,
//            null);
//    case FAVORITE_MOVIE_LOADER:
//    Uri favoriteMovieUri = TmdbContract.FavoriteEntry.CONTENT_URI;
//    return new CursorLoader(getActivity(),
//    favoriteMovieUri,
//            null,
//            null,
//            null,
//            null);
}
