package co.fabrk.popmovies.ui.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import java.util.ArrayList;
import co.fabrk.popmovies.data.TmdbContract;
import co.fabrk.popmovies.tmdb.TMDBMovie;
import co.fabrk.popmovies.tmdb.TMDBReview;
import co.fabrk.popmovies.tmdb.TMDBTrailer;

/**
 * Created by ebal on 23/09/15.
 */
public class Utility {

    // Data resources status functions

    public static boolean isDatabaseEmpty(Context context) {
        // Checking if there is any data already in the database.
        // The goal is to determine whether or not we need to inject hardcoded data
        // to avoid crash.
        Uri movieUri = TmdbContract.MovieEntry.CONTENT_URI;
        Uri popularUri = TmdbContract.PopularEntry.CONTENT_URI;

        Cursor movieCursor = context.getContentResolver().query(movieUri,
                null, null, null, null);

        Cursor popularCursor = context.getContentResolver().query(popularUri,
                null, null, null, null);

        if (movieCursor.moveToFirst() && popularCursor.moveToFirst()) {
            movieCursor.close();
            popularCursor.close();
            return true;
        } else {
            movieCursor.close();
            popularCursor.close();
            return false;

        }
    }

    public static boolean isContentResourceAvailable(Context context, Uri uris) {
        Cursor cursor = context.getContentResolver().query(uris,
                    null, null, null, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;

        }
    }

    // Connectivity Status functions
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }

    public static boolean isWifiConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isMobileConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }


    // Function for test data injection
    public static TMDBMovie createHardCodedData() {
            // movie info
            String[] movieInfo = new String[8];
            movieInfo[0] = "116741";
            movieInfo[1] = "The Internship";
            movieInfo[2] = "http://image.tmdb.org/t/p/w185/xxOKDTQbQo7h1h7TyrQIW7u8KcJ.jpg";
            movieInfo[3] = "2013-06-07";
            movieInfo[4] = "6.1";
            movieInfo[5] = "Two recently laid-off men in their 40s try to make it as interns at a successful Internet company where their managers are in their 20s.";
            movieInfo[6] = "13.23181";
            movieInfo[7] = "http://image.tmdb.org/t/p/w185/dkMD5qlogeRMiEixC4YNPUvax2T.jpg";

        TMDBMovie movie = new TMDBMovie(movieInfo);

            // reviews and trailers
            String reviewAuthor = new String("kaitlin_a_6");
            String reviewContent = new String("**Very Well done!** A Great film to show to your super old boss who doesn't understand or refuses to comprehend the impact the internet has on trying to run a successful business these days. **Hilarious** movie! Awesomely **Positive** movie, and a **great influence** for people to remember that anything is possible.");
            TMDBReview reviews = new TMDBReview(movieInfo[0], reviewAuthor, reviewContent);
            ArrayList<TMDBReview> reviewArrayList = new ArrayList<>();
            reviewArrayList.add(reviews);
            movie.setTmdbReviews(reviewArrayList);

            // Trailers
            String[] movieTrailer = new String[5];
            movieTrailer[0] = movieInfo[0];
            movieTrailer[1] = "Trailer";
            movieTrailer[2] = "cdnoqCViqUo";
            movieTrailer[3] = "533ec6e6c3a3685448008b19";
            movieTrailer[4] = "YouTube";
            TMDBTrailer trailer = new TMDBTrailer(movieTrailer);
            ArrayList<TMDBTrailer> trailerArrayList = new ArrayList<>();
            trailerArrayList.add(trailer);
            movie.setTmdbTrailers(trailerArrayList);
            return movie;
    }
}
