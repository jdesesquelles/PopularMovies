package co.fabrk.popmovies.tmdb;

import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by ebal on 05/04/16.
 */
public class TmdbMovieResponse {
    List<TMDBMovie> movies;

    // public constructor is necessary for collections
    public TmdbMovieResponse() {
        movies = new ArrayList<TMDBMovie>();
    }

    public static TmdbMovieResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        TmdbMovieResponse tmdbMovieResponse = gson.fromJson(response, TmdbMovieResponse.class);
        return tmdbMovieResponse;
    }
}
