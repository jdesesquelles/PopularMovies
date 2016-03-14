package co.fabrk.popmovies.tmdb;

import android.net.Uri;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Created by ebal on 02/12/15.
 */
public class TmdbConstants {

    public static String REQUEST_METHOD = "GET";
    public static final String MOVIE_BASE_URL =
            "http://api.themoviedb.org/3/";
    public static final String DISCOVER_MOVIES_URL =
            "discover/movie?";
    public static final String EXTRA_URL =
            "movie/";
    public static final String TRAILER_URL =
            "/videos?";
    public static final String REVIEWS_URL =
            "/reviews?";

    // Discover
    public static final String SORT_VALUE_POPULAR = "Popular";
    public static final String SORT_VALUE_HIGHEST_RATED = "Highest Rated";
    public static final String SORT_VALUE_FAVORITE = "Favorite";


    // API Query parameters
    public static final String SORT_PARAM = "sort_by";
    public static final String API_KEY_PARAM = "api_key";

    // API Parameters Values
    public static final String SORT_VALUE_POPULARITY_DESC = "popularity.desc";
    public static final String SORT_VALUE_VOTE_AVERAGE_DESC = "vote_average.desc";
    public static String API_KEY_VALUE;


    public static final String POPULAR = "popular";
    public static final String HIGHEST_RATED = "highest_rated";
    public static final String TRAILERS = "trailers";
    public static final String REVIEWS = "reviews";
    public static final String TMDB_MOVIE_PARCELABLE_NAME = "movie";
    public static final String TMDB_TRAILER_PARCELABLE_NAME = "trailer";
    public static final String TMDB_REVIEW_PARCELABLE_NAME = "review";


    public static class ContentProvider {
        // Content provider
        public static final String CONTENT_AUTHORITY = "co.fabrk.popmovies";
        private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_MOVIES = "movies";
        public static final String PATH_REVIEWS = "reviews";
        public static final String PATH_TRAILERS = "trailers";
        public static final String PATH_POPULAR_MOVIES = "popular";
        public static final String PATH_HIGHEST_RATED = "rated";
        public static final String PATH_FAVORITES = "favorites";
    }

    public static void setApiKeyValue(String apiKey) {
        if (!Objects.equals(API_KEY_VALUE, apiKey)) {
            API_KEY_VALUE = apiKey;
        }
    }

}
