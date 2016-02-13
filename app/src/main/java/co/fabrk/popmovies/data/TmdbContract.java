package co.fabrk.popmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class TmdbContract {

    public static final String CONTENT_AUTHORITY = "co.fabrk.popmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_TRAILERS = "trailers";
    public static final String PATH_POPULAR_MOVIES = "popular";
    public static final String PATH_HIGHEST_RATED = "rated";
    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoriteEntry implements BaseColumns {
        // TMDBMovies: String BaseUrl , String id, String title, String thumbnailPath, String releaseDate, String voteAverage;
        // String overview, ArrayList<TMDBTrailer> tmdbTrailers, ArrayList<TMDBReview> tmdbReviews;
        // Reviews and trailers are not saved for the favorite list.
        public static final String TABLE_NAME = "tmdb_favorites";
        public static final String COLUMN_TMDB_MOVIE_ID = "tmdb_movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_POSTER_PATH = "thumbnail_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_GENRE = "genre";
        public static final String COLUMN_COUNTRY = "countries";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        public static Uri buildFavoriteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildFavoriteWithMovieId(String TmdbMovieId) {
            Uri uri = CONTENT_URI.buildUpon().appendPath(TmdbContract.PATH_MOVIES).appendQueryParameter(TmdbContract.FavoriteEntry.COLUMN_TMDB_MOVIE_ID, TmdbMovieId).build();
            return uri;
        }
        public static String getMovieIdFromUri(Uri uri) {
            return uri.getQueryParameter(TmdbContract.FavoriteEntry.COLUMN_TMDB_MOVIE_ID);
        }

    }

    public static final class MovieEntry implements BaseColumns{
        // TMDBMovies: String BaseUrl , String id, String title, String thumbnailPath, String releaseDate, String voteAverage;
        // String overview, ArrayList<TMDBTrailer> tmdbTrailers, ArrayList<TMDBReview> tmdbReviews;
        public static final String TABLE_NAME = "tmdb_movies";
        public static final String COLUMN_TMDB_MOVIE_ID = "tmdb_movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_POSTER_PATH = "thumbnail_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_GENRE = "genre";
        public static final String COLUMN_COUNTRY = "countries";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieWithMovieId(String TmdbMovieId) {
            return CONTENT_URI.buildUpon().appendPath(TmdbContract.PATH_MOVIES).appendQueryParameter(TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID, TmdbMovieId).build();
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getQueryParameter(TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID);
        }
    }

    public static final class ReviewEntry implements BaseColumns {
        public static final String TABLE_NAME = "tmdb_reviews";
        public static final String COLUMN_TMDB_MOVIE_ID = "movie_id";
        public static final String COLUMN_TMDB_REVIEW_AUTHOR = "author";
        public static final String COLUMN_TMDB_REVIEW_CONTENT = "content";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public static Uri buildReviewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildReviewsWithMovieId(String TmdbMovieId) {
            return CONTENT_URI.buildUpon().appendPath(TmdbContract.PATH_MOVIES).appendQueryParameter(MovieEntry.COLUMN_TMDB_MOVIE_ID, TmdbMovieId).build();
        }
        public static String getMovieIdFromUri(Uri uri) {
            return uri.getQueryParameter(MovieEntry.COLUMN_TMDB_MOVIE_ID);
        }
    }

    public static final class TrailerEntry implements BaseColumns {
        public static final String TABLE_NAME = "tmdb_trailers";
        public static final String COLUMN_TMDB_MOVIE_ID = "tmdb_movie_id";
        public static final String COLUMN_TMDB_TRAILER_NAME = "name";
        public static final String COLUMN_TMDB_TRAILER_KEY = "key";
        public static final String COLUMN_TMDB_TRAILER_ID = "id";
        public static final String COLUMN_TMDB_TRAILER_SITE = "site";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        public static Uri buildTrailersUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
        // content://co.fabrk.popmovies/trailers/movie/12023
        public static Uri buildTrailersWithMovieId(String TmdbMovieId) {
            return CONTENT_URI.buildUpon().appendPath(TmdbContract.PATH_MOVIES).appendQueryParameter(MovieEntry.COLUMN_TMDB_MOVIE_ID,TmdbMovieId).build();

        }
        public static String getMovieIdFromUri(Uri uri) {
            return uri.getQueryParameter(MovieEntry.COLUMN_TMDB_MOVIE_ID);
        }
    }

    public static final class PopularEntry implements BaseColumns {
        public static final String TABLE_NAME = "tmdb_popular";
        public static final String COLUMN_TMDB_MOVIE_ID = "tmdb_movie_id";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULAR_MOVIES).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR_MOVIES;

        public static Uri buildPopularMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
        public static Uri buildPopularWithMovieId(String TmdbMovieId) {
            return CONTENT_URI.buildUpon().appendPath(TmdbContract.PATH_MOVIES).appendQueryParameter(PopularEntry.COLUMN_TMDB_MOVIE_ID,TmdbMovieId).build();

        }
        public static String getMovieIdFromUri(Uri uri) {
            return uri.getQueryParameter(PopularEntry.COLUMN_TMDB_MOVIE_ID);
        }
    }

    public static final class HighestRatedEntry implements BaseColumns {
        public static final String TABLE_NAME = "tmdb_highest_rated";
        public static final String COLUMN_TMDB_MOVIE_ID = "tmdb_movie_id";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HIGHEST_RATED).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HIGHEST_RATED;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HIGHEST_RATED;

        public static Uri buildHighestRatedUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
        public static Uri buildPopularWithMovieId(String TmdbMovieId) {
            return CONTENT_URI.buildUpon().appendPath(TmdbContract.PATH_MOVIES).appendQueryParameter(HighestRatedEntry.COLUMN_TMDB_MOVIE_ID,TmdbMovieId).build();

        }
        public static String getMovieIdFromUri(Uri uri) {
            return uri.getQueryParameter(HighestRatedEntry.COLUMN_TMDB_MOVIE_ID);
        }
    }

}
