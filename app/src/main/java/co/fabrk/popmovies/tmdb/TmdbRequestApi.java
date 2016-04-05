package co.fabrk.popmovies.tmdb;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.Call;
import java.util.List;

/**
 * Created by ebal on 02/12/15.
 */
public class TmdbRequestApi {

    public static void get() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TmdbConstants.MOVIE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public interface TmdbApiEndpointInterface {
        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter

        @GET("discover/movie")
        Call<List<TMDBMovie>> getPopularMovies(@Path("id") int groupId, @Query("key") String key);

        @GET("discover/movie")
        Call<List<TMDBMovie>> getHighestRatedMovies(@Path("id") int groupId, @Query("key") String key);

        @GET("movie/{id}")
        Call<TMDBMovie> getExtraInfo(@Path("id") String id, @Query("key") String key);

        @GET("videos")
        Call<List<TMDBMovie>> getTrailers(@Path("id") int groupId, @Query("key") String key);

        @GET("reviews")
        Call<List<TMDBMovie>> getReviews(@Path("id") int groupId, @Query("key") String key);

    }

    private static final String TAG = "TmdbRequestApi";

    private static  String BuildUrl(TMDBMovie movie, String type) {

        Uri builtUri;
        switch (type) {
            case TmdbConstants.TRAILERS:
                builtUri = Uri.parse(TmdbConstants.MOVIE_BASE_URL + TmdbConstants.EXTRA_URL + movie.getId() + TmdbConstants.TRAILER_URL).buildUpon()
                        .appendQueryParameter(TmdbConstants.API_KEY_PARAM, TmdbConstants.API_KEY_VALUE)
                        .build();
                return builtUri.toString();
            // Case review is made default, so we always return a value.
            case TmdbConstants.REVIEWS:
            default:
                builtUri = Uri.parse(TmdbConstants.MOVIE_BASE_URL + TmdbConstants.EXTRA_URL + movie.getId() + TmdbConstants.REVIEWS_URL).buildUpon()
                        .appendQueryParameter(TmdbConstants.API_KEY_PARAM, TmdbConstants.API_KEY_VALUE)
                        .build();
                return builtUri.toString();

        }
    }

    private static String BuildUrl(String Options) {
        Uri builtUri;
        builtUri = Uri.parse(TmdbConstants.MOVIE_BASE_URL + TmdbConstants.DISCOVER_MOVIES_URL).buildUpon()
                .appendQueryParameter(TmdbConstants.SORT_PARAM, Options)
                .appendQueryParameter(TmdbConstants.API_KEY_PARAM, TmdbConstants.API_KEY_VALUE)
                .build();
        return builtUri.toString();
    }

    private static String RequestAPI(String stringUrl) {
        // String Url as an input, JSON String as the output
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;
        try {
            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(TmdbConstants.REQUEST_METHOD);
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            movieJsonStr = buffer.toString();
        } catch (IOException e) {
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
            return movieJsonStr;
        }
    }

    // Movie object
    // Method that return a List of movies object from a Url
    private static ArrayList<TMDBMovie> getMovieList(String url){
        String json = RequestAPI(url);
        try {
            return TmdbJsonParser.getMovieListFromTmdbJson(json);
        } catch (JSONException e) {
        }
        return null;
    }

    // Method that return the list of most popular movies
    public static ArrayList<TMDBMovie> getPopularMovies() {
        String url = BuildUrl(TmdbConstants.SORT_VALUE_POPULARITY_DESC);
        return getMovieList(url);
    }

    // Method that return the list of highest rated movies
    public static ArrayList<TMDBMovie> getHighestRatedMovies() {
        String url = BuildUrl(TmdbConstants.SORT_VALUE_VOTE_AVERAGE_DESC);
        return getMovieList(url);
    }

    // Decorator for Trailers and Reviews, takes a movie object as an argument
    // Method that return a movie object decorated with its trailers and reviews
    public static  TMDBMovie getExtraInfo(TMDBMovie movie) {
        if (movie.getTmdbTrailers() == null) {movie = getTrailers(movie);}
        if (movie.getTmdbReviews() == null) {movie = getReviews(movie);}
        return movie;
    }

    // TODO implement decorator pattern for trailers and reviews

    // Method that return a movie object decorated with its trailers list
    private static  TMDBMovie getTrailers(TMDBMovie movie) {
        String url = BuildUrl(movie, TmdbConstants.TRAILERS);
        Log.e("Trailers Url", url);
        String json = TmdbRequestApi.RequestAPI(url);
        try {
            movie.setTmdbTrailers(TmdbJsonParser.getTrailersFromTmdbJson(json));
        } catch (JSONException e) {
        }
        return movie;
    }

    // Method that return a movie object decorated with its reviews list
    private static  TMDBMovie getReviews(TMDBMovie movie) {
        String url = BuildUrl(movie, TmdbConstants.REVIEWS);
        Log.e("Reviews Url", url);
        String json = TmdbRequestApi.RequestAPI(url);
        try {
            movie.setTmdbReviews(TmdbJsonParser.getReviewsFromTmdbJson(json));
        } catch (JSONException e) {
        }
        return movie;
    }



}
