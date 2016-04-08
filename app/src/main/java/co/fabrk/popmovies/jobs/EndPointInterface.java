package co.fabrk.popmovies.jobs;

import java.util.List;

import co.fabrk.popmovies.tmdb.TMDBMovie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import co.fabrk.popmovies.model.TmdbResponse;

/**
 * Created by ebal on 05/04/16.
 */
public interface EndPointInterface {
    @GET("3/discover/movie")
    Call<TmdbResponse> getPopularMovies(@Query("sort_by") String sortBy, @Query("api_key") String apiKey);

    @GET("3/discover/movie")
    Call<List<TmdbResponse>> getHighestRatedMovies(@Path("sort_by") int sortBy, @Query("api_key") String apiKey);

    @GET("3/movie/{id}")
    Call<TmdbResponse> getExtraInfo(@Path("id") String id, @Query("key") String key);

    @GET("3/videos")
    Call<List<TmdbResponse>> getTrailers(@Path("id") int groupId, @Query("key") String key);

    @GET("3/reviews")
    Call<List<TmdbResponse>> getReviews(@Path("id") int groupId, @Query("key") String key);
}
