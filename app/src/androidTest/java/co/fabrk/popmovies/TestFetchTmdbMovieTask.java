/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.fabrk.popmovies;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.ArrayList;

import co.fabrk.popmovies.tmdb.TmdbDatabaseOperations;
import co.fabrk.popmovies.jobs.FetchTmdbMovies;
import co.fabrk.popmovies.data.TmdbContract;
import co.fabrk.popmovies.tmdb.TMDBMovie;

public class TestFetchTmdbMovieTask extends AndroidTestCase {

    static final String TEST_MOVIE_ID = "99705";
    static final String TEST_MOVIE_TITLE = "Movie title test";
    static final String TEST_POSTER_PATH = "/thumbnail.jpg";
    static final String TEST_MOVIE_RELEASE_DATE = "22-09-1978";
    static final String TEST_MOVIE_VOTE_AVERAGE = "10.02";
    static final String TEST_MOVIE_POPULARITY = "9.0909";
    static final String TEST_MOVIE_OVERVIEW = "Old mother f***";
    static final String TEST2_MOVIE_ID = "99706";
    static final String TEST3_MOVIE_ID = "99707";
    static final String TEST4_MOVIE_ID = "99708";
    static final String TEST2_MOVIE_TITLE = "Movie title test";
    static final String TEST_REVIEW_AUTHOR = "author test";
    static final String TEST_REVIEW_CONTENT = "test review content";
    static final String TEST_TRAILER_NAME = "test trailer name";
    static final String TEST_TRAILER_KEY = "test trailer key";
    static final String TEST_TRAILER_ID = "test trailer id";
    static final String TEST_TRAILER_SITE = "test trailer site";

    @TargetApi(11)
    public void test2AddBulkMovie() {
        ArrayList<TMDBMovie> movieArrayList = new ArrayList<TMDBMovie>();
        String[] movieInfo = new String[8];
        movieInfo[0] = TEST_MOVIE_ID;
        movieInfo[1] = TEST_MOVIE_TITLE;
        movieInfo[2] = TEST_POSTER_PATH;
        movieInfo[3] = TEST_MOVIE_RELEASE_DATE;
        movieInfo[4] = TEST_MOVIE_VOTE_AVERAGE;
        movieInfo[5] = TEST_MOVIE_OVERVIEW;
        movieInfo[6] = TEST_MOVIE_POPULARITY;
        TMDBMovie movie1 = new TMDBMovie(movieInfo);
        movieArrayList.add(movie1);
        // test duplicate
        movieArrayList.add(movie1);

        movieInfo[0] = TEST2_MOVIE_ID;
        TMDBMovie movie2 = new TMDBMovie(movieInfo);
        movieArrayList.add(movie2);
        movieInfo[0] = TEST3_MOVIE_ID;
        TMDBMovie movie3 = new TMDBMovie(movieInfo);
        movieArrayList.add(movie3);

        Cursor movieCursor = getContext().getContentResolver().query(
                TmdbContract.MovieEntry.CONTENT_URI,
                new String[]{TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID},
                null,
                null,
                null);
        movieCursor.close();
//        assertTrue(movieCursor.getCount() + "records in the DB at start", false);

        FetchTmdbMovies fmt = new FetchTmdbMovies(getContext().getContentResolver());
        TmdbDatabaseOperations.addBulkMovies(movieArrayList, mContext.getContentResolver());

        movieCursor = getContext().getContentResolver().query(
                TmdbContract.MovieEntry.CONTENT_URI,
                new String[]{TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID},
                null,
                null,
                null);
        movieCursor.close();

                movieCursor = getContext().getContentResolver().query(
                TmdbContract.MovieEntry.CONTENT_URI,
                new String[]{
                        TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID,
                        TmdbContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
                        TmdbContract.MovieEntry.COLUMN_POSTER_PATH,
                        TmdbContract.MovieEntry.COLUMN_RELEASE_DATE,
                        TmdbContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                        TmdbContract.MovieEntry.COLUMN_POPULARITY,
                        TmdbContract.MovieEntry.COLUMN_OVERVIEW
                },
                TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID + " = ?",
                new String[]{TEST_MOVIE_ID},
                null);
        fail("Cursor lenght :" + movieCursor.getCount());

        if (movieCursor.moveToFirst()) {
            assertEquals("Error: the queried value of movieId does not match the returned value" +
                    "from addLocation", movieCursor.getString(1), TEST_MOVIE_ID);
            assertEquals("Error: the queried value of Movie Title is incorrect",
                    movieCursor.getString(2), TEST_MOVIE_TITLE);
            assertEquals("Error: the queried value of backdrop path is incorrect",
                    movieCursor.getString(3), "http://image.tmdb.org/t/p/w185" + TEST_POSTER_PATH);
            assertEquals("Error: the queried value of release date is incorrect",
                    movieCursor.getString(4), TEST_MOVIE_RELEASE_DATE);
            assertEquals("Error: the queried value of vote average is incorrect",
                    Double.toString(movieCursor.getDouble(5)), TEST_MOVIE_VOTE_AVERAGE);
            assertEquals("Error: the queried value of popularity is incorrect",
                    Double.toString(movieCursor.getDouble(6)), TEST_MOVIE_POPULARITY);
            assertEquals("Error: the queried value of overview is incorrect",
                    movieCursor.getString(7), TEST_MOVIE_OVERVIEW);
            if (movieCursor.moveToNext()){
                fail(movieCursor.getString(1) + " : TEST_MOVIE_ID");
            }

        } else {
            fail("Error: the id you used to query returned an empty cursor");
        }
        movieCursor.close();

        movieCursor = getContext().getContentResolver().query(
                TmdbContract.MovieEntry.CONTENT_URI,
                new String[]{
                        TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID,
                        TmdbContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
                        TmdbContract.MovieEntry.COLUMN_POSTER_PATH,
                        TmdbContract.MovieEntry.COLUMN_RELEASE_DATE,
                        TmdbContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                        TmdbContract.MovieEntry.COLUMN_POPULARITY,
                        TmdbContract.MovieEntry.COLUMN_OVERVIEW
                },
                TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID + " = ?",
                new String[]{TEST2_MOVIE_ID},
                null);
        fail("Cursor lenght :" + movieCursor.getCount());

        if (movieCursor.moveToFirst()) {
            assertEquals("Error: the queried value of movieId does not match the returned value" +
                    "from addLocation", movieCursor.getString(1), TEST2_MOVIE_ID);
            assertEquals("Error: the queried value of Movie Title is incorrect",
                    movieCursor.getString(2), TEST_MOVIE_TITLE);
            assertEquals("Error: the queried value of backdrop path is incorrect",
                    movieCursor.getString(3), "http://image.tmdb.org/t/p/w185" + TEST_POSTER_PATH);
            assertEquals("Error: the queried value of release date is incorrect",
                    movieCursor.getString(4), TEST_MOVIE_RELEASE_DATE);
            assertEquals("Error: the queried value of vote average is incorrect",
                    Double.toString(movieCursor.getDouble(5)), TEST_MOVIE_VOTE_AVERAGE);
            assertEquals("Error: the queried value of popularity is incorrect",
                    Double.toString(movieCursor.getDouble(6)), TEST_MOVIE_POPULARITY);
            assertEquals("Error: the queried value of overview is incorrect",
                    movieCursor.getString(7), TEST_MOVIE_OVERVIEW);
        } else {
            fail("Error: the id you used to query returned an empty cursor");
        }
        movieCursor.close();



    }

    @TargetApi(11)
    public void test1AddMovie() {
        // Prepare ArrayList<TMDBMovie>
        ArrayList<TMDBMovie> movieArrayList = new ArrayList<TMDBMovie>();
        String[] movieInfo = new String[8];
        movieInfo[0] = TEST_MOVIE_ID;
        movieInfo[1] = TEST_MOVIE_TITLE;
        movieInfo[2] = TEST_POSTER_PATH;
        movieInfo[3] = TEST_MOVIE_RELEASE_DATE;
        movieInfo[4] = TEST_MOVIE_VOTE_AVERAGE;
        movieInfo[5] = TEST_MOVIE_OVERVIEW;
        movieInfo[6] = TEST_MOVIE_POPULARITY;
        TMDBMovie movie1 = new TMDBMovie(movieInfo);
        movieInfo[0] = TEST2_MOVIE_ID;
        TMDBMovie movie2 = new TMDBMovie(movieInfo);

        getContext().getContentResolver().delete(TmdbContract.MovieEntry.CONTENT_URI,
                null,
                null);
        FetchTmdbMovies fmt = new FetchTmdbMovies(getContext().getContentResolver());
        Long movieId = TmdbDatabaseOperations.InsertMovieInDb(movie1, mContext.getContentResolver());
        // does InsertMovieInDb return a valid record ID?
        assertFalse("Error: InsertMovieInDb returned an invalid ID on insert",
                movieId == -1);
        // test all this twice
        for ( int i = 0; i < 2; i++ ) {
            // does the ID point to our location?
            Cursor movieCursor = getContext().getContentResolver().query(
                    TmdbContract.MovieEntry.CONTENT_URI,
                    new String[]{
                            TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID,
                            TmdbContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
                            TmdbContract.MovieEntry.COLUMN_POSTER_PATH,
                            TmdbContract.MovieEntry.COLUMN_RELEASE_DATE,
                            TmdbContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                            TmdbContract.MovieEntry.COLUMN_POPULARITY,
                            TmdbContract.MovieEntry.COLUMN_OVERVIEW
                    },
                    TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID + " = ?",
                    new String[]{TEST_MOVIE_ID},
                    null);
            // these match the indices of the projection
            if (movieCursor.moveToFirst()) {
                assertEquals("Error: the queried value of movieId does not match the returned value" +
                        "from addLocation", movieCursor.getString(1), TEST_MOVIE_ID);
                assertEquals("Error: the queried value of Movie Title is incorrect",
                        movieCursor.getString(2), TEST_MOVIE_TITLE);
                assertEquals("Error: the queried value of backdrop path is incorrect",
                        movieCursor.getString(3), "http://image.tmdb.org/t/p/w185" + TEST_POSTER_PATH);
                assertEquals("Error: the queried value of release date is incorrect",
                        movieCursor.getString(4), TEST_MOVIE_RELEASE_DATE);
                assertEquals("Error: the queried value of vote average is incorrect",
                        Double.toString(movieCursor.getDouble(5)), TEST_MOVIE_VOTE_AVERAGE);
                assertEquals("Error: the queried value of popularity is incorrect",
                        Double.toString(movieCursor.getDouble(6)), TEST_MOVIE_POPULARITY);
                assertEquals("Error: the queried value of overview is incorrect",
                        movieCursor.getString(7), TEST_MOVIE_OVERVIEW);
            } else {
                fail("Error: the id you used to query returned an empty cursor");
            }
            // there should be no more records
            assertFalse("Error: there should be only one record returned from a Movie query",
                    movieCursor.moveToNext());
            // add the location again
            Long newMovieId = TmdbDatabaseOperations.InsertMovieInDb(movie1, mContext.getContentResolver());
            assertEquals("Error: inserting a movie again should return the same ID",
                    movieId, newMovieId);
        }
        Long movie2Id = TmdbDatabaseOperations.InsertMovieInDb(movie2, mContext.getContentResolver());
        // does InsertMovieInDb return a valid record ID?
        assertFalse("Error: InsertMovieInDb returned an invalid ID on insert",
                movie2Id == -1);
        getContext().getContentResolver().delete(TmdbContract.MovieEntry.CONTENT_URI,
                null,
                null);
        // clean up the test so that other tests can use the content provider
        getContext().getContentResolver().
                acquireContentProviderClient(TmdbContract.MovieEntry.CONTENT_URI).
                getLocalContentProvider().shutdown();
    }
}
