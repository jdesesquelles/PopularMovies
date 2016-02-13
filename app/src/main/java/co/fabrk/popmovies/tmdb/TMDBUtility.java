package co.fabrk.popmovies.tmdb;

import android.util.Log;

/**
 * Created by ebal on 02/12/15.
 */
class TMDBUtility {

    private static final String LOG_TAG = TMDBUtility.class.getSimpleName();
    public static void printTMDBMovie(TMDBMovie movie) {
        Log.e(LOG_TAG,"id" + " - " + movie.id);
        Log.e(LOG_TAG,"title" + "-" + movie.title);
        Log.e(LOG_TAG,"thumbnailPath" + " - " + movie.thumbnailPath);
        Log.e(LOG_TAG,"releaseDate" + " - " + movie.releaseDate);
        Log.e(LOG_TAG,"voteAverage" + " - " + movie.voteAverage);
        Log.e(LOG_TAG,"popularity" + " - " + movie.popularity);
        Log.e(LOG_TAG,"overview" + " - " + movie.overview);
        Log.e(LOG_TAG,"backdrop" + " - " + movie.backdrop);
        printTMDBTrailers(movie);
        printTMDBReviews(movie);
        Log.e(LOG_TAG,"");
    }

    private static void printTMDBReviews(TMDBMovie movie) {
        Integer counter = 1;
        for (TMDBReview review : movie.getTmdbReviews()) {
            Log.e(LOG_TAG, "Review n° " + counter.toString());
            counter++;
            printReview(review);
        }
    }

    private static void printReview(TMDBReview review){
        Log.e(LOG_TAG,"Review - movieId" + " - " + review.movieId);
        Log.e(LOG_TAG,"Review - author" + " - " + review.author);
        Log.e(LOG_TAG,"Review - content" + " - " + review.content);
    }

    private static void printTMDBTrailers(TMDBMovie movie) {
        Integer counter = 1;
        for (TMDBTrailer trailer : movie.getTmdbTrailers()) {
            Log.e(LOG_TAG, "Trailer n° " + counter.toString());
            printTrailer(trailer);
        }
    }

    private static void printTrailer(TMDBTrailer trailer) {
        Log.e(LOG_TAG,"Trailer - movieId" + " - " + trailer.movieId);
        Log.e(LOG_TAG,"Trailer - name" + " - " + trailer.name);
        Log.e(LOG_TAG,"Trailer - key" + " - " + trailer.key);
        Log.e(LOG_TAG,"Trailer - id" + " - " + trailer.id);
        Log.e(LOG_TAG,"Trailer - site" + " - " + trailer.site);

    }

}
