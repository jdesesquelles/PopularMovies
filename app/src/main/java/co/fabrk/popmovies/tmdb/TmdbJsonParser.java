package co.fabrk.popmovies.tmdb;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by ebal on 02/12/15.
 */
class TmdbJsonParser {

    // JSON OBJECTS
    // JSON ARRAY
    // Key
    // Values

    private static final String TMDB_RESULTS = "results";

    // Main URL Discover movies
    private static final String TMDB_ID = "id";
    private static final String TMDB_POSTER_PATH = "poster_path";
    private static final String TMDB_TITLE = "title";
    private static final String TMDB_RELEASE_DATE = "release_date";
    private static final String TMDB_VOTE_AVERAGE = "vote_average";
    private static final String TMDB_PLOT_OVERVIEW = "overview";
    private static final String TMDB_POPULARITY = "popularity";
    private static final String TMDB_BACKDROP = "backdrop_path";
    // Reviews field
    private static final String TMDB_REVIEW_AUTHOR = "author";
    private static final String TMDB_RVIEW_CONTENT = "content";
    // Trailer
    public static final String TMDB_TRAILER = "youtube";
    private static final String TMDB_TRAILER_NAME = "name";
    private static final String TMDB_TRAILER_KEY = "key";
    private static final String TMDB_TRAILER_ID = "id";
    private static final String TMDB_TRAILER_SITE = "site";

    public static ArrayList<TMDBMovie> getMovieListFromTmdbJson(String movieJsonStr) throws JSONException {
        // Field names in JSON

        if (movieJsonStr == null) {return null;}

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieList = movieJson.getJSONArray(TMDB_RESULTS);

        TMDBMovie movie;
        ArrayList<TMDBMovie> movieArrayList = new ArrayList<>();
        String[] movieInfo = new String[8];

        for (int i = 0; i < movieList.length(); i++) {
            JSONObject resultItem = movieList.getJSONObject(i);
            movieInfo[0] = resultItem.getString(TMDB_ID);
            movieInfo[1] = resultItem.getString(TMDB_TITLE);
            movieInfo[2] = resultItem.getString(TMDB_POSTER_PATH);
            movieInfo[3] = resultItem.getString(TMDB_RELEASE_DATE);
            movieInfo[4] = resultItem.getString(TMDB_VOTE_AVERAGE);
            movieInfo[5] = resultItem.getString(TMDB_PLOT_OVERVIEW);
            movieInfo[6] = resultItem.getString(TMDB_POPULARITY);
            movieInfo[7] = resultItem.getString(TMDB_BACKDROP);
            movie = new TMDBMovie(movieInfo);
            movieArrayList.add(movie);
        }
        return movieArrayList;
    }


    public static ArrayList<TMDBReview> getReviewsFromTmdbJson(String reviewsJson) throws JSONException {


        JSONObject reviewsJsonObj = new JSONObject(reviewsJson);
        JSONArray reviewList = reviewsJsonObj.getJSONArray(TMDB_RESULTS);

        ArrayList<TMDBReview> TMDBReviewArrayList = new ArrayList<>();

        for (int i = 0; i < reviewList.length(); i++) {
            TMDBReview TMDBReview = new TMDBReview();
            JSONObject resultItem = reviewList.getJSONObject(i);
            TMDBReview.setAuthor(resultItem.getString(TMDB_REVIEW_AUTHOR));
            TMDBReview.setContent(resultItem.getString(TMDB_RVIEW_CONTENT));
            TMDBReviewArrayList.add(TMDBReview);

        }
        return TMDBReviewArrayList;
    }

    // Method getting the List of trailers from a json string
    // @return an ArrayList of TMDBTrailers that can be add to an TMDBMovie object directly using the setTmdbReviews member method

    public static ArrayList<TMDBTrailer> getTrailersFromTmdbJson(String trailersJson) throws JSONException {

        ArrayList<TMDBTrailer> trailerArrayList = new ArrayList<>();

        JSONObject trailersJsonObj = new JSONObject(trailersJson);
        JSONArray trailerList = trailersJsonObj.getJSONArray(TMDB_RESULTS);

        for (int i = 0; i < trailerList.length(); i++) {
            JSONObject trailer = trailerList.getJSONObject(i);
            String trailerSite = trailer.getString(TMDB_TRAILER_SITE);
            Log.e("trailer site", trailer.getString(TMDB_TRAILER_SITE));

            if (trailerSite.equals("YouTube")) {
                TMDBTrailer tmdbTrailer = new TMDBTrailer();
                tmdbTrailer.setName(trailer.getString(TMDB_TRAILER_NAME));
                tmdbTrailer.setKey(trailer.getString(TMDB_TRAILER_KEY));
                tmdbTrailer.setId(trailer.getString(TMDB_TRAILER_ID));
                tmdbTrailer.setSite(trailerSite);
                trailerArrayList.add(tmdbTrailer);
            }
        }
        return trailerArrayList;
    }

}
