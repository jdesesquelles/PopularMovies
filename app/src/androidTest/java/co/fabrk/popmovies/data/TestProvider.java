package co.fabrk.popmovies.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;

import co.fabrk.popmovies.data.TmdbContract.*;

import java.util.HashMap;

public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();
    HashSet<Uri> mUriHashSet = new HashSet<>();
    HashSet<Uri> mUriBaseHashSet = new HashSet<>();
    HashMap<Uri, String> mUriTypeHashSet = new HashMap<>();
    String tmdbMovieId = new String("666a");
    long id = 1;
    ContentValues testValues;
    TmdbDbHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    Uri uri;

    public HashSet<Uri> createUriHashSet() {
        HashSet<Uri> uriHashSet = new HashSet<>();
        uriHashSet.add(FavoriteEntry.CONTENT_URI);
        uriHashSet.add(FavoriteEntry.buildFavoriteUri(id));
        uriHashSet.add(FavoriteEntry.buildFavoriteWithMovieId(tmdbMovieId));
        uriHashSet.add(MovieEntry.CONTENT_URI);
        uriHashSet.add(MovieEntry.buildMovieUri(id));
        uriHashSet.add(MovieEntry.buildMovieWithMovieId(tmdbMovieId));
        uriHashSet.add(TrailerEntry.CONTENT_URI);
        uriHashSet.add(TrailerEntry.buildTrailersUri(id));
        uriHashSet.add(TrailerEntry.buildTrailersWithMovieId(tmdbMovieId));
        uriHashSet.add(ReviewEntry.CONTENT_URI);
        uriHashSet.add(ReviewEntry.buildReviewsUri(id));
        uriHashSet.add(ReviewEntry.buildReviewsWithMovieId(tmdbMovieId));
        uriHashSet.add(PopularEntry.CONTENT_URI);
        uriHashSet.add(PopularEntry.buildPopularMoviesUri(id));
        uriHashSet.add(HighestRatedEntry.CONTENT_URI);
        uriHashSet.add(HighestRatedEntry.buildHighestRatedUri(id));
        return uriHashSet;
    }

    public HashSet<Uri> createBaseUriHashSet() {
        HashSet<Uri> uriHashSet = new HashSet<>();
        uriHashSet.add(FavoriteEntry.CONTENT_URI);
        uriHashSet.add(MovieEntry.CONTENT_URI);
        uriHashSet.add(TrailerEntry.CONTENT_URI);
        uriHashSet.add(ReviewEntry.CONTENT_URI);
        uriHashSet.add(PopularEntry.CONTENT_URI);
        uriHashSet.add(HighestRatedEntry.CONTENT_URI);
        return uriHashSet;
    }

    public HashMap<Uri, String> createUriTypeHashMap() {
        HashMap<Uri, String> uriTypeHashMap = new HashMap<>();
        uriTypeHashMap.put(FavoriteEntry.CONTENT_URI, FavoriteEntry.CONTENT_TYPE);
        uriTypeHashMap.put(FavoriteEntry.buildFavoriteUri(id), FavoriteEntry.CONTENT_ITEM_TYPE);
        uriTypeHashMap.put(FavoriteEntry.buildFavoriteWithMovieId(tmdbMovieId), FavoriteEntry.CONTENT_ITEM_TYPE);
        uriTypeHashMap.put(MovieEntry.CONTENT_URI, MovieEntry.CONTENT_TYPE);
        uriTypeHashMap.put(MovieEntry.buildMovieUri(id), MovieEntry.CONTENT_ITEM_TYPE);
        uriTypeHashMap.put(MovieEntry.buildMovieWithMovieId(tmdbMovieId), MovieEntry.CONTENT_ITEM_TYPE);
        uriTypeHashMap.put(TrailerEntry.CONTENT_URI, TrailerEntry.CONTENT_TYPE);
        uriTypeHashMap.put(TrailerEntry.buildTrailersUri(id), TrailerEntry.CONTENT_ITEM_TYPE);
        uriTypeHashMap.put(TrailerEntry.buildTrailersWithMovieId(tmdbMovieId), TrailerEntry.CONTENT_TYPE);
        uriTypeHashMap.put(ReviewEntry.CONTENT_URI, ReviewEntry.CONTENT_TYPE);
        uriTypeHashMap.put(ReviewEntry.buildReviewsUri(id), ReviewEntry.CONTENT_ITEM_TYPE);
        uriTypeHashMap.put(ReviewEntry.buildReviewsWithMovieId(tmdbMovieId), ReviewEntry.CONTENT_TYPE);
        uriTypeHashMap.put(PopularEntry.buildPopularMoviesUri(id), PopularEntry.CONTENT_ITEM_TYPE);
        uriTypeHashMap.put(HighestRatedEntry.buildHighestRatedUri(id), HighestRatedEntry.CONTENT_ITEM_TYPE);
        uriTypeHashMap.put(PopularEntry.CONTENT_URI, PopularEntry.CONTENT_TYPE);
        uriTypeHashMap.put(HighestRatedEntry.CONTENT_URI, HighestRatedEntry.CONTENT_TYPE);
        return uriTypeHashMap;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mUriHashSet = createUriHashSet();
        mUriBaseHashSet = createBaseUriHashSet();
        testValues = TestUtilities.createMovieValues();
        dbHelper = new TmdbDbHelper(mContext);
        db = dbHelper.getWritableDatabase();
        TestUtilities.flushProvider(mUriBaseHashSet, mContext.getContentResolver());
    }

    public void test100ProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                TmdbProvider.class.getName());

        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: Provider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + TmdbContract.CONTENT_AUTHORITY,
                    providerInfo.authority, TmdbContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // Provider isn't registered correctly.
            assertTrue("Error: Provider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void test200UriType() {
        mUriTypeHashSet = createUriTypeHashMap();
        String type;
        // content://co.fabrk.popularmovies/favorite/
        for (Uri uri : createUriTypeHashMap().keySet()) {
            type = mContext.getContentResolver().getType(uri);
            assertEquals("Error: " + uri.toString() + " should return " + createUriTypeHashMap().get(uri) + ".",
                    createUriTypeHashMap().get(uri), type);
        }
    }

    // FAVORITE - QUERY
    public void test300FavoriteQueryBasic() {
        ContentValues testValues = TestUtilities.createMovieValues();
        Uri uri = mContext.getContentResolver().insert(FavoriteEntry.CONTENT_URI, testValues);
        Cursor favoriteCursor = mContext.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBasicFavoriteQuery", favoriteCursor, testValues);
    }

    public void test300FavoriteQueryWithId() {
        TmdbDbHelper dbHelper = new TmdbDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMovieValues();
        uri = mContext.getContentResolver().insert(FavoriteEntry.CONTENT_URI, testValues);
        db.close();
        Cursor favoriteCursor = mContext.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBasicFavoriteQuery", favoriteCursor, testValues);
    }

    public void test300FavoriteQueryWithMovieId() {
        uri = mContext.getContentResolver().insert(FavoriteEntry.CONTENT_URI, testValues);
        Cursor favoriteCursor = mContext.getContentResolver().query(
                FavoriteEntry.buildFavoriteWithMovieId(testValues.getAsString(FavoriteEntry.COLUMN_TMDB_MOVIE_ID)),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBasicFavoriteQuery", favoriteCursor, testValues);
    }


    public void test301FavoriteUpdate() {
        ContentValues values = TestUtilities.createMovieValues();
        Uri favoriteUri = mContext.getContentResolver().
                insert(FavoriteEntry.CONTENT_URI, values);
        Long favoriteRowId = ContentUris.parseId(favoriteUri);
        assertTrue(favoriteRowId != -1);
        Log.d(LOG_TAG, "New row id: " + favoriteRowId);
        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(FavoriteEntry._ID, favoriteRowId);
        updatedValues.put(FavoriteEntry.COLUMN_ORIGINAL_TITLE, "Santa's Village");

        Cursor favoriteCursor = mContext.getContentResolver().query(FavoriteEntry.CONTENT_URI, null, null, null, null);
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        favoriteCursor.registerContentObserver(tco);
        int count = mContext.getContentResolver().update(
                FavoriteEntry.CONTENT_URI, updatedValues, FavoriteEntry._ID + "= ?",
                new String[]{Long.toString(favoriteRowId)});
        assertEquals(count, 1);

        tco.waitForNotificationOrFail();
        favoriteCursor.unregisterContentObserver(tco);
        favoriteCursor.close();
        Cursor cursor = mContext.getContentResolver().query(
                FavoriteEntry.CONTENT_URI,
                null,
                FavoriteEntry._ID + " =?",
                new String[]{favoriteRowId.toString()},
                null
        );
        TestUtilities.validateCursor("testUpdateFavorite.  Error validating location entry update.",
                cursor, updatedValues);
        cursor.close();
    }

    public void test302FavoriteInsert() {
        ContentValues testValues = TestUtilities.createMovieValues();
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavoriteEntry.CONTENT_URI, true, tco);
        Uri locationUri = mContext.getContentResolver().insert(FavoriteEntry.CONTENT_URI, testValues);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);
        long locationRowId = ContentUris.parseId(locationUri);
        assertTrue(locationRowId != -1);
        Cursor cursor = mContext.getContentResolver().query(
                FavoriteEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestUtilities.validateCursor("testInsertReadProvider. Error validating Favorite Entry.",
                cursor, testValues);
    }

    public void test303DeleteFavorite() {
        test302FavoriteInsert();
        TestUtilities.TestContentObserver favoriteObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavoriteEntry.CONTENT_URI, true, favoriteObserver);
        TestUtilities.flushProvider(mUriBaseHashSet, mContext.getContentResolver());
        favoriteObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(favoriteObserver);
    }
    //TODO BulkInsert

    // Movie
    public void test400MovieQueryBasic() {
        uri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, testValues);
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                MovieEntry._ID + " =?",
                new String[]{MovieEntry.getIdFromUri(uri)},
                null
        );
        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieQuery", movieCursor, testValues);
    }

    public void test400MovieQueryWithId() {
        ContentValues testValues = TestUtilities.createMovieValues();
        uri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, testValues);
        Cursor movieCursor = mContext.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBasicMovieQuery", movieCursor, testValues);
    }

    public void test400MovieQueryWithMovieId() {
        ContentValues testValues = TestUtilities.createMovieValues();
        uri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, testValues);
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieEntry.buildMovieWithMovieId(testValues.getAsString(MovieEntry.COLUMN_TMDB_MOVIE_ID)),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBasicMovieQuery", movieCursor, testValues);
    }

    public void test401UpdateMovie() {
        ContentValues values = TestUtilities.createMovieValues();
        Uri MovieUri = mContext.getContentResolver().
                insert(MovieEntry.CONTENT_URI, values);
        Long MovieRowId = ContentUris.parseId(MovieUri);
        assertTrue(MovieRowId != -1);
        Log.d(LOG_TAG, "New row id: " + MovieRowId);
        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(MovieEntry._ID, MovieRowId);
        updatedValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE, "Santa's Village");
        Cursor MovieCursor = mContext.getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null);
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        MovieCursor.registerContentObserver(tco);
        int count = mContext.getContentResolver().update(
                MovieEntry.CONTENT_URI, updatedValues, MovieEntry._ID + "= ?",
                new String[]{Long.toString(MovieRowId)});
        assertEquals(count, 1);

        tco.waitForNotificationOrFail();
        MovieCursor.unregisterContentObserver(tco);
        MovieCursor.close();

        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,   // select
                MovieEntry._ID + " =?",
                new String[]{MovieRowId.toString()},   // where values
                null    // sort order
        );
        TestUtilities.validateCursor("testUpdateMovie.  Error validating Movie entry update.",
                cursor, updatedValues);
        cursor.close();
    }

    public void test402MovieInsert() {
        ContentValues testValues = TestUtilities.createMovieValues();
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, tco);

        Uri movieUri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, testValues);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);
        Long movieRowId = ContentUris.parseId(movieUri);
        assertTrue(movieRowId != -1);
        Cursor cursor = mContext.getContentResolver().query(
                movieUri,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestUtilities.validateCursor("testInsertReadProvider. Error validating Movie Entry.",
                cursor, testValues);
    }

    public void test403DeleteMovie() {

        test402MovieInsert();
        TestUtilities.TestContentObserver MovieObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, MovieObserver);
        TestUtilities.flushProvider(mUriBaseHashSet, mContext.getContentResolver());
        MovieObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(MovieObserver);
    }


    // TRAILERS - QUERIES
    public void test500TrailerQueryBasic() {
        TmdbDbHelper dbHelper = new TmdbDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createTrailerValues();
        Long RowId = TestUtilities.insertRecords(mContext, "trailer");
        assertTrue("Unable to Insert TrailerEntry into the Database", RowId != -1);
        db.close();
        Cursor cursor = mContext.getContentResolver().query(
                TrailerEntry.CONTENT_URI,
                null,
                TrailerEntry._ID + "=?",
                new String[]{RowId.toString()},
                null
        );
        TestUtilities.validateCursor("testBasicTrailerQuery", cursor, testValues);
    }

    public void test500TrailerQueryWithID() {
        TmdbDbHelper dbHelper = new TmdbDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createTrailerValues();
        Long RowId = TestUtilities.insertRecords(mContext, "trailer");
        assertTrue("Unable to Insert TrailerEntry into the Database", RowId != -1);
        db.close();
        Cursor cursor = mContext.getContentResolver().query(
                TrailerEntry.buildTrailersUri(RowId),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBasicReviewQuery", cursor, testValues);
    }

    public void test500TrailerQueryWithMovieId() {
        TmdbDbHelper dbHelper = new TmdbDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createTrailerValues();
        Long RowId = TestUtilities.insertRecords(mContext, "trailer");
        assertTrue("Unable to Insert TrailerEntry into the Database", RowId != -1);
        db.close();
        Cursor cursor = mContext.getContentResolver().query(
                TrailerEntry.buildTrailersWithMovieId(testValues.getAsString(TrailerEntry.COLUMN_TMDB_MOVIE_ID)),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBasicReviewQuery", cursor, testValues);
    }

    public void test501TrailerUpdate() {
        ContentValues values = TestUtilities.createTrailerValues();
        Uri Uri = mContext.getContentResolver().
                insert(TrailerEntry.CONTENT_URI, values);
        Long RowId = ContentUris.parseId(Uri);
        assertTrue(RowId != -1);
        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(TrailerEntry._ID, RowId);
        updatedValues.put(TrailerEntry.COLUMN_TMDB_TRAILER_NAME, "Santa's Village");
        Cursor cursor = mContext.getContentResolver().query(TrailerEntry.buildTrailersWithMovieId(values.getAsString(TrailerEntry.COLUMN_TMDB_MOVIE_ID)), null, null, null, null);
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        cursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                TrailerEntry.CONTENT_URI, updatedValues, TrailerEntry._ID + "= ?",
                new String[]{Long.toString(RowId)});
        assertEquals(count, 1);

        tco.waitForNotificationOrFail();
        cursor.unregisterContentObserver(tco);
        cursor.close();
        cursor = mContext.getContentResolver().query(
                TrailerEntry.CONTENT_URI,
                null,   // select
                TrailerEntry._ID + " =?",
                new String[]{RowId.toString()},   // where values
                null    // sort order
        );
        TestUtilities.validateCursor("testUpdateTrailer.  Error validating Trailer entry update.",
                cursor, updatedValues);
        cursor.close();
    }

    public void test502TrailerInsert() {
        ContentValues testValues = TestUtilities.createTrailerValues();
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(TrailerEntry.CONTENT_URI, true, tco);

        Uri uri = mContext.getContentResolver().insert(TrailerEntry.CONTENT_URI, testValues);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);
        Long rowId = ContentUris.parseId(uri);
        assertTrue(rowId != -1);
        Cursor cursor = mContext.getContentResolver().query(
                uri,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestUtilities.validateCursor("testInsertReadProvider. Error validating Trailer Entry.",
                cursor, testValues);
    }

    public void test503TrailerDelete() {
        test502TrailerInsert();
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(TrailerEntry.CONTENT_URI, true, observer);
        TestUtilities.flushProvider(mUriBaseHashSet, mContext.getContentResolver());
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);
    }


    // REVIEWS - QUERIES
    public void test600ReviewQueryBasic() {
        TmdbDbHelper dbHelper = new TmdbDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createReviewValues();
        Long RowId = TestUtilities.insertRecords(mContext, "review");
        assertTrue("Unable to Insert ReviewEntry into the Database", RowId != -1);
        db.close();
        Cursor cursor = mContext.getContentResolver().query(
                ReviewEntry.CONTENT_URI,
                null,
                ReviewEntry._ID + "=?",
                new String[]{RowId.toString()},
                null
        );
        TestUtilities.validateCursor("testBasicReviewQuery", cursor, testValues);
    }

    public void test600ReviewQueryWithID() {
        TmdbDbHelper dbHelper = new TmdbDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createReviewValues();
        Long RowId = TestUtilities.insertRecords(mContext, "review");
        assertTrue("Unable to Insert ReviewEntry into the Database", RowId != -1);
        db.close();
        Cursor cursor = mContext.getContentResolver().query(
                ReviewEntry.buildReviewsUri(RowId),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBasicReviewQuery", cursor, testValues);
    }

    public void test600ReviewQueryWithMovieId() {
        TmdbDbHelper dbHelper = new TmdbDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createReviewValues();
        Long RowId = TestUtilities.insertRecords(mContext, "review");
        assertTrue("Unable to Insert ReviewEntry into the Database", RowId != -1);
        db.close();
        Cursor cursor = mContext.getContentResolver().query(
                ReviewEntry.buildReviewsWithMovieId(testValues.getAsString(ReviewEntry.COLUMN_TMDB_MOVIE_ID)),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBasicReviewQuery", cursor, testValues);
    }

    public void test601ReviewUpdate() {
        ContentValues values = TestUtilities.createReviewValues();
        Uri uri = mContext.getContentResolver().
                insert(ReviewEntry.CONTENT_URI, values);
        Long rowId = ContentUris.parseId(uri);
        assertTrue(rowId != -1);
        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(ReviewEntry._ID, rowId);
        updatedValues.put(ReviewEntry.COLUMN_TMDB_REVIEW_AUTHOR, "Santa's Village");
        Cursor cursor = mContext.getContentResolver().query(ReviewEntry.buildReviewsWithMovieId(values.getAsString(ReviewEntry.COLUMN_TMDB_MOVIE_ID)), null, null, null, null);
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        cursor.registerContentObserver(tco);
        int count = mContext.getContentResolver().update(
                ReviewEntry.CONTENT_URI, updatedValues, ReviewEntry._ID + "= ?",
                new String[]{Long.toString(rowId)});
        assertEquals(count, 1);
        tco.waitForNotificationOrFail();
        cursor.unregisterContentObserver(tco);
        cursor.close();
        cursor = mContext.getContentResolver().query(
                ReviewEntry.CONTENT_URI,
                null,   // select
                ReviewEntry._ID + " =?",
                new String[]{rowId.toString()},   // where values
                null    // sort order
        );
        TestUtilities.validateCursor("testUpdateReview.  Error validating review entry update.",
                cursor, updatedValues);
        cursor.close();
    }

    public void test602ReviewInsert() {
        ContentValues testValues = TestUtilities.createReviewValues();
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(ReviewEntry.CONTENT_URI, true, tco);
        Uri uri = mContext.getContentResolver().insert(ReviewEntry.CONTENT_URI, testValues);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);
        long rowId = ContentUris.parseId(uri);
        assertTrue(rowId != -1);
        Cursor cursor = mContext.getContentResolver().query(
                ReviewEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestUtilities.validateCursor("testInsertReadProvider. Error validating Review Entry.",
                cursor, testValues);
    }

    public void test603ReviewDelete() {
        test602ReviewInsert();
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(ReviewEntry.CONTENT_URI, true, observer);
        TestUtilities.flushProvider(mUriBaseHashSet, mContext.getContentResolver());
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);
    }


    // POPULAR
    public void test700PopularQueryBasic() {
        ContentValues testValues = TestUtilities.createListValues();
        Uri uri = mContext.getContentResolver().insert(PopularEntry.CONTENT_URI, testValues);
        mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, TestUtilities.createMovieValues());
        Cursor cursor = mContext.getContentResolver().query(
                PopularEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBasicPopularQuery", cursor, TestUtilities.createMovieValues());

        cursor = mContext.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBasicPopularQuery", cursor, testValues);
    }

    public void test702PopularInsert() {
        ContentValues testValues = TestUtilities.createListValues();
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(PopularEntry.CONTENT_URI, true, tco);
        Uri uri = mContext.getContentResolver().insert(PopularEntry.CONTENT_URI, testValues);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);
        long rowId = ContentUris.parseId(uri);
        assertTrue(rowId != -1);
        Cursor cursor = mContext.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testInsertReadPopularProvider. Error validating Popular Entry.",
                cursor, testValues);
    }

    public void test703PopularDelete() {
        ContentValues testValues = TestUtilities.createListValues();
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(PopularEntry.CONTENT_URI, true, tco);
        Uri uri = mContext.getContentResolver().insert(PopularEntry.CONTENT_URI, testValues);

        Cursor cursor = mContext.getContentResolver().query(
                PopularEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertTrue(cursor.getCount() == 1);
        TestUtilities.flushProvider(mUriBaseHashSet, mContext.getContentResolver());
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);
        long rowId = ContentUris.parseId(uri);
        assertTrue(rowId != -1);
        cursor = mContext.getContentResolver().query(
                PopularEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertTrue(cursor.getCount() == 0);
    }

    public void test701UpdatePopular() {
        ContentValues values = TestUtilities.createListValues();
        Uri uri = mContext.getContentResolver().
                insert(PopularEntry.CONTENT_URI, values);
        long rowId = ContentUris.parseId(uri);
        assertTrue(rowId != -1);
        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(PopularEntry._ID, rowId);
        updatedValues.put(PopularEntry.COLUMN_TMDB_MOVIE_ID, "Santa's Village");
        Cursor cursor = mContext.getContentResolver().query(PopularEntry.CONTENT_URI, null, null, null, null);
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        cursor.registerContentObserver(tco);
        int count = mContext.getContentResolver().update(
                PopularEntry.CONTENT_URI, updatedValues, PopularEntry._ID + "= ?",
                new String[]{Long.toString(rowId)});
        assertEquals(count, 1);
        tco.waitForNotificationOrFail();
        cursor.unregisterContentObserver(tco);
        cursor.close();
        cursor = mContext.getContentResolver().query(
                PopularEntry.CONTENT_URI,
                null,   // select
                PopularEntry._ID + " = " + rowId,
                null,   // where values
                null    // sort order
        );
        TestUtilities.validateCursor("testUpdatePopular.  Error validating popular entry update.",
                cursor, updatedValues);
        cursor.close();
    }


    // HIGHEST RATED
    public void test800HighestRatedQueryBasic() {
        ContentValues testValues = TestUtilities.createListValues();
        mContext.getContentResolver().insert(HighestRatedEntry.CONTENT_URI, testValues);
        Uri uri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, TestUtilities.createMovieValues());
        Cursor cursor = mContext.getContentResolver().query(
                HighestRatedEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBasicPopularQuery", cursor, TestUtilities.createMovieValues());
        cursor = mContext.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBasicPopularQuery", cursor, testValues);
    }

    public void test802HighestRatedInsert() {
        ContentValues testValues = TestUtilities.createListValues();
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(HighestRatedEntry.CONTENT_URI, true, tco);
        Uri uri = mContext.getContentResolver().insert(HighestRatedEntry.CONTENT_URI, testValues);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);
        long rowId = ContentUris.parseId(uri);
        assertTrue(rowId != -1);
        Cursor cursor = mContext.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testInsertReadHighestRatedProvider. Error validating HighestRated Entry.",
                cursor, testValues);
    }

    public void test803HighestRatedDelete() {
        test802HighestRatedInsert();
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(HighestRatedEntry.CONTENT_URI, true, observer);
        TestUtilities.flushProvider(mUriBaseHashSet, mContext.getContentResolver());
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);
    }

    public void test801UpdateHighestRated() {
        ContentValues values = TestUtilities.createListValues();
        Uri uri = mContext.getContentResolver().
                insert(HighestRatedEntry.CONTENT_URI, values);
        long rowId = ContentUris.parseId(uri);
        assertTrue(rowId != -1);
        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(HighestRatedEntry._ID, rowId);
        updatedValues.put(HighestRatedEntry.COLUMN_TMDB_MOVIE_ID, "Santa's Village");
        Cursor cursor = mContext.getContentResolver().query(HighestRatedEntry.CONTENT_URI, null, null, null, null);
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        cursor.registerContentObserver(tco);
        int count = mContext.getContentResolver().update(
                HighestRatedEntry.CONTENT_URI, updatedValues, HighestRatedEntry._ID + "= ?",
                new String[]{Long.toString(rowId)});
        assertEquals(count, 1);
        tco.waitForNotificationOrFail();
        cursor.unregisterContentObserver(tco);
        cursor.close();
        cursor = mContext.getContentResolver().query(
                HighestRatedEntry.CONTENT_URI,
                null,   // select
                HighestRatedEntry._ID + " = " + rowId,
                null,   // where values
                null    // sort order
        );
        TestUtilities.validateCursor("testUpdateHighestRated.  Error validating Highest Rated entry update.",
                cursor, updatedValues);
        cursor.close();
    }

    // Insert

    /************/
    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;

    static ContentValues[] createBulkInsertMovieValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieEntry.COLUMN_TMDB_MOVIE_ID, Integer.toString(i + 66));
            movieValues.put(MovieEntry.COLUMN_POSTER_PATH, TestUtilities.TEST_POSTER_PATH);
            movieValues.put(MovieEntry.COLUMN_RELEASE_DATE, TestUtilities.TEST_MOVIE_RELEASE_DATE);
            movieValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE, TestUtilities.TEST_MOVIE_TITLE);
            movieValues.put(MovieEntry.COLUMN_OVERVIEW, TestUtilities.TEST_MOVIE_OVERVIEW);
            movieValues.put(MovieEntry.COLUMN_POPULARITY, TestUtilities.TEST_MOVIE_POPULARITY);
            movieValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, TestUtilities.TEST_MOVIE_VOTE_AVERAGE);

            returnContentValues[i] = movieValues;
        }
        return returnContentValues;
    }

    static ContentValues[] createBulkInsertFavoriteValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues movieValues = new ContentValues();
            movieValues.put(FavoriteEntry.COLUMN_TMDB_MOVIE_ID, Integer.toString(i + 66));
            movieValues.put(FavoriteEntry.COLUMN_POSTER_PATH, TestUtilities.TEST_POSTER_PATH);
            movieValues.put(FavoriteEntry.COLUMN_RELEASE_DATE, TestUtilities.TEST_MOVIE_RELEASE_DATE);
            movieValues.put(FavoriteEntry.COLUMN_ORIGINAL_TITLE, TestUtilities.TEST_MOVIE_TITLE);
            movieValues.put(FavoriteEntry.COLUMN_OVERVIEW, TestUtilities.TEST_MOVIE_OVERVIEW);
            movieValues.put(FavoriteEntry.COLUMN_POPULARITY, TestUtilities.TEST_MOVIE_POPULARITY);
            movieValues.put(FavoriteEntry.COLUMN_VOTE_AVERAGE, TestUtilities.TEST_MOVIE_VOTE_AVERAGE);
            returnContentValues[i] = movieValues;
        }
        return returnContentValues;
    }

    static ContentValues[] createBulkInsertTrailerValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues trailerValues = new ContentValues();
            trailerValues.put(TrailerEntry.COLUMN_TMDB_TRAILER_ID, Integer.toString(i + 66));
            trailerValues.put(TrailerEntry.COLUMN_TMDB_MOVIE_ID, Integer.toString(i + 66));
            trailerValues.put(TrailerEntry.COLUMN_TMDB_TRAILER_NAME, TestUtilities.TEST_TRAILER_NAME);
            trailerValues.put(TrailerEntry.COLUMN_TMDB_TRAILER_SITE, TestUtilities.TEST_TRAILER_SITE);
            trailerValues.put(TrailerEntry.COLUMN_TMDB_TRAILER_KEY, TestUtilities.TEST_TRAILER_KEY);
            returnContentValues[i] = trailerValues;
        }
        return returnContentValues;
    }

    static ContentValues[] createBulkInsertReviewValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues reviewValues = new ContentValues();
            reviewValues.put(ReviewEntry.COLUMN_TMDB_MOVIE_ID, Integer.toString(i + 66));
            reviewValues.put(ReviewEntry.COLUMN_TMDB_REVIEW_AUTHOR, TestUtilities.TEST_REVIEW_AUTHOR);
            reviewValues.put(ReviewEntry.COLUMN_TMDB_REVIEW_CONTENT, TestUtilities.TEST_REVIEW_CONTENT);
            returnContentValues[i] = reviewValues;
        }
        return returnContentValues;
    }

    static ContentValues[] createBulkInsertPopularValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues popularValues = new ContentValues();
            popularValues.put(PopularEntry.COLUMN_TMDB_MOVIE_ID, Integer.toString(i + 66));
            returnContentValues[i] = popularValues;
        }
        return returnContentValues;
    }

    static ContentValues[] createBulkInsertHighestRatedValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues highestRatedValues = new ContentValues();
            highestRatedValues.put(HighestRatedEntry.COLUMN_TMDB_MOVIE_ID, Integer.toString(i + 66));
            returnContentValues[i] = highestRatedValues;
        }
        return returnContentValues;
    }

    public void test900MovieBulkInsert() {
        TestUtilities.flushProvider(mUriBaseHashSet, mContext.getContentResolver());
        Cursor cursor;
        ContentValues[] bulkInsertContentValues = createBulkInsertMovieValues();
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, observer);
        int insertCount = mContext.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, bulkInsertContentValues);
        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);

        cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        cursor.moveToFirst();
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()) {
            TestUtilities.validateCursor("testBulkInsert.  Error validating MovieEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }

    public void test900FavoriteBulkInsert() {
        TestUtilities.flushProvider(mUriBaseHashSet, mContext.getContentResolver());
        Cursor cursor;
        ContentValues[] bulkInsertContentValues = createBulkInsertMovieValues();
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        int insertCount;

        // Favorite
        mContext.getContentResolver().registerContentObserver(FavoriteEntry.CONTENT_URI, true, observer);
        insertCount = mContext.getContentResolver().bulkInsert(FavoriteEntry.CONTENT_URI, bulkInsertContentValues);
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);
        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);
        cursor = mContext.getContentResolver().query(
                FavoriteEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);
        cursor.moveToFirst();
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()) {
            TestUtilities.validateCursor("testBulkInsert.  Error validating FavoriteEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }

    public void test900TrailerBulkInsert() {
        TestUtilities.flushProvider(mUriBaseHashSet, mContext.getContentResolver());
        Cursor cursor;
        ContentValues[] bulkInsertContentValues = createBulkInsertMovieValues();
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        int insertCount;


        // Trailer
        bulkInsertContentValues = createBulkInsertTrailerValues();
        mContext.getContentResolver().registerContentObserver(TrailerEntry.CONTENT_URI, true, observer);
        insertCount = mContext.getContentResolver().bulkInsert(TrailerEntry.CONTENT_URI, bulkInsertContentValues);
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);
        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);
        cursor = mContext.getContentResolver().query(
                TrailerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);
        cursor.moveToFirst();
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()) {
            TestUtilities.validateCursor("testBulkInsert.  Error validating TrailerEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();

    }
    public void test900ReviewBulkInsert() {
        TestUtilities.flushProvider(mUriBaseHashSet, mContext.getContentResolver());
        Cursor cursor;
        ContentValues[] bulkInsertContentValues = createBulkInsertMovieValues();
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        int insertCount;
        // Review
        bulkInsertContentValues = createBulkInsertReviewValues();
        mContext.getContentResolver().registerContentObserver(ReviewEntry.CONTENT_URI, true, observer);
        insertCount = mContext.getContentResolver().bulkInsert(ReviewEntry.CONTENT_URI, bulkInsertContentValues);
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);
        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);
        cursor = mContext.getContentResolver().query(
                ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);
        cursor.moveToFirst();
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()) {
            TestUtilities.validateCursor("testBulkInsert.  Error validating ReviewEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();

    }
    public void test900PopularBulkInsert() {
        TestUtilities.flushProvider(mUriBaseHashSet, mContext.getContentResolver());
        Cursor cursor;
        ContentValues[] bulkInsertContentValues = createBulkInsertMovieValues();
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        int insertCount;
        // Popular
        bulkInsertContentValues = createBulkInsertPopularValues();
        mContext.getContentResolver().registerContentObserver(PopularEntry.CONTENT_URI, true, observer);
        insertCount = mContext.getContentResolver().bulkInsert(PopularEntry.CONTENT_URI, bulkInsertContentValues);
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);
        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);
        cursor = mContext.getContentResolver().query(
                PopularEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);
        cursor.moveToFirst();
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()) {
            TestUtilities.validateCursor("testBulkInsert.  Error validating PopularEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
    public void test900HighestRatedBulkInsert() {
        TestUtilities.flushProvider(mUriBaseHashSet, mContext.getContentResolver());
        Cursor cursor;
        ContentValues[] bulkInsertContentValues = createBulkInsertMovieValues();
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        int insertCount;

        // Highest Rated
        bulkInsertContentValues = createBulkInsertHighestRatedValues();
        mContext.getContentResolver().registerContentObserver(HighestRatedEntry.CONTENT_URI, true, observer);
        insertCount = mContext.getContentResolver().bulkInsert(HighestRatedEntry.CONTENT_URI, bulkInsertContentValues);
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);
        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);
        cursor = mContext.getContentResolver().query(
                HighestRatedEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);
        cursor.moveToFirst();
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()) {
            TestUtilities.validateCursor("testBulkInsert.  Error validating HighestRatedEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }

}
