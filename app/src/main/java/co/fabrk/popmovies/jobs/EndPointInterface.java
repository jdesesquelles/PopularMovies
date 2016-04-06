package co.fabrk.popmovies.jobs;

import java.util.List;

import co.fabrk.popmovies.tmdb.TMDBMovie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ebal on 05/04/16.
 */
public interface EndPointInterface {
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
