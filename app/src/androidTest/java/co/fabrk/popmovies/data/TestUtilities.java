package co.fabrk.popmovies.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import co.fabrk.popmovies.tmdb.TMDBMovie;
import co.fabrk.popmovies.utils.PollingCheck;


public class TestUtilities extends AndroidTestCase {
    static final String TEST_MOVIE_ID = "99709";
    static final String TEST_MOVIE_TITLE = "Movie title test";
    static final String TEST_POSTER_PATH = "http://url.test/thumbnail.jpg";
    static final String TEST_MOVIE_RELEASE_DATE = "22-09-1978";
    static final String TEST_MOVIE_VOTE_AVERAGE = "10.02";
    static final String TEST_MOVIE_POPULARITY = "9.0909";
    static final String TEST_MOVIE_OVERVIEW = "Old mother f***";
    static final String TEST_REVIEW_AUTHOR = "author test";
    static final String TEST_REVIEW_CONTENT = "test review content";
    static final String TEST_TRAILER_NAME = "test trailer name";
    static final String TEST_TRAILER_KEY = "test trailer key";
    static final String TEST_TRAILER_ID = "test trailer id";
    static final String TEST_TRAILER_SITE = "test trailer site";
    static final String TEST_BACKDROP = "test trailer site";
    static final String TEST_GENRE = "test trailer site";
    static final String TEST_COUNTRY = "test trailer site";

    public static TMDBMovie movie;
//    public static ContentValues createValues(String tableName) {
//        ContentValues testValues;
//
//        return testValues;
//    }
    /*
                    DB TABLES
    */

    public static HashSet<String> createTableListHashSet() {
        HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(TmdbContract.FavoriteEntry.TABLE_NAME);
        tableNameHashSet.add(TmdbContract.MovieEntry.TABLE_NAME);
        tableNameHashSet.add(TmdbContract.ReviewEntry.TABLE_NAME);
        tableNameHashSet.add(TmdbContract.TrailerEntry.TABLE_NAME);
        tableNameHashSet.add(TmdbContract.PopularEntry.TABLE_NAME);
        tableNameHashSet.add(TmdbContract.HighestRatedEntry.TABLE_NAME);
        return tableNameHashSet;
    }

    public static void testTablesCreation(HashSet<String> tableHashSet, SQLiteDatabase db ) {
        Cursor cursor;
        cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        cursor.moveToFirst();
        do {tableHashSet.remove(cursor.getString(0));} while( cursor.moveToNext() );
        assertTrue("Error: Tables are missing",
                tableHashSet.isEmpty());
        cursor.close();

    }

    /*
                    TABLE COLUMNS
    */

//    public interface ContentValuesProvider {
//        ContentValues provide();
//    };
//
//    public test() {
//        HashSet<String> tableNameHashSet = createTableListHashSet();
//
//     HashMap<String, ContentValuesProvider> contentValuesProvider = new HashMap<String, ContentValuesProvider>();
//        for (String tableName : tableNameHashSet) {
//            contentValuesProvider.put(tableName, new ContentValuesProvider() {public ContentValues provide() {return createMovieValues();}});
//
//        }
//    private ContentValuesProvider[] contentValuesProvider = new ContentValuesProvider[]{
//            new ContentValuesProvider() {public ContentValues provide() {return createMovieValues();}},
//            new ContentValuesProvider {},
//    };
//    }


    public static ContentValues createMovieValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_TMDB_MOVIE_ID, TEST_MOVIE_ID);
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE, TEST_MOVIE_TITLE);
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_POSTER_PATH, TEST_POSTER_PATH);
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_RELEASE_DATE, TEST_MOVIE_RELEASE_DATE);
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, TEST_MOVIE_VOTE_AVERAGE);
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_POPULARITY, TEST_MOVIE_POPULARITY);
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_OVERVIEW, TEST_MOVIE_OVERVIEW);
        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_BACKDROP, TEST_BACKDROP);
//        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_GENRE, TEST_GENRE);
//        contentValues.put(TmdbContract.FavoriteEntry.COLUMN_COUNTRY, TEST_COUNTRY);
        return contentValues;
    }

    public static HashSet<String> createMovieColumnHashSet() {
        HashSet<String> MovieColumnHashSet = new HashSet<String>();
        MovieColumnHashSet.add(TmdbContract.FavoriteEntry._ID);
        MovieColumnHashSet.add(TmdbContract.FavoriteEntry.COLUMN_TMDB_MOVIE_ID);
        MovieColumnHashSet.add(TmdbContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE);
        MovieColumnHashSet.add(TmdbContract.FavoriteEntry.COLUMN_POSTER_PATH);
        MovieColumnHashSet.add(TmdbContract.FavoriteEntry.COLUMN_RELEASE_DATE);
        MovieColumnHashSet.add(TmdbContract.FavoriteEntry.COLUMN_VOTE_AVERAGE);
        MovieColumnHashSet.add(TmdbContract.FavoriteEntry.COLUMN_POPULARITY);
        MovieColumnHashSet.add(TmdbContract.FavoriteEntry.COLUMN_OVERVIEW);
        return MovieColumnHashSet;
    }

    public static ContentValues createListValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TmdbContract.PopularEntry.COLUMN_TMDB_MOVIE_ID, TEST_MOVIE_ID);
        return contentValues;
    }

    public static HashSet<String> createMovieListColumnHashSet() {
        HashSet<String> MovieListColumnHashSet = new HashSet<String>();
        MovieListColumnHashSet.add(TmdbContract.FavoriteEntry._ID);
        MovieListColumnHashSet.add(TmdbContract.FavoriteEntry.COLUMN_TMDB_MOVIE_ID);
        return MovieListColumnHashSet;
    }

    public static HashSet<String> createFavoriteColumnHashSet() {
        return createMovieColumnHashSet();
    }


    /*
                REVIEWS
     */

    public static ContentValues createReviewValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TmdbContract.ReviewEntry.COLUMN_TMDB_MOVIE_ID, TEST_MOVIE_ID);
        contentValues.put(TmdbContract.ReviewEntry.COLUMN_TMDB_REVIEW_AUTHOR, TEST_REVIEW_AUTHOR);
        contentValues.put(TmdbContract.ReviewEntry.COLUMN_TMDB_REVIEW_CONTENT, TEST_REVIEW_CONTENT);
        return contentValues;
    }

    public static HashSet<String> createReviewsColumnHashSet() {
        final HashSet<String> TMDBReviewColumnHashSet = new HashSet<String>();
        TMDBReviewColumnHashSet.add(TmdbContract.ReviewEntry._ID);
        TMDBReviewColumnHashSet.add(TmdbContract.ReviewEntry.COLUMN_TMDB_MOVIE_ID);
        TMDBReviewColumnHashSet.add(TmdbContract.ReviewEntry.COLUMN_TMDB_REVIEW_AUTHOR);
        TMDBReviewColumnHashSet.add(TmdbContract.ReviewEntry.COLUMN_TMDB_REVIEW_CONTENT);
        return TMDBReviewColumnHashSet;
    }

    /*
                TRAILERS
     */

    public static ContentValues createTrailerValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TmdbContract.TrailerEntry.COLUMN_TMDB_MOVIE_ID, TEST_MOVIE_ID);
        contentValues.put(TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_NAME, TEST_TRAILER_NAME);
        contentValues.put(TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_KEY, TEST_TRAILER_KEY);
        contentValues.put(TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_ID, TEST_TRAILER_ID);
        contentValues.put(TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_SITE, TEST_TRAILER_SITE);
        return contentValues;
    }

    public static HashSet<String> createTrailersColumnHashSet() {
        HashSet<String> TMDBTrailerColumnHashSet = new HashSet<String>();
        TMDBTrailerColumnHashSet.add(TmdbContract.TrailerEntry._ID);
        TMDBTrailerColumnHashSet.add(TmdbContract.TrailerEntry.COLUMN_TMDB_MOVIE_ID);
        TMDBTrailerColumnHashSet.add(TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_ID);
        TMDBTrailerColumnHashSet.add(TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_KEY);
        TMDBTrailerColumnHashSet.add(TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_NAME);
        TMDBTrailerColumnHashSet.add(TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_SITE);
        return TMDBTrailerColumnHashSet;
    }


    /*
                UTILITY METHODS
     */
    public static void testTableCreation(String tableName, HashSet<String> ColumnHashSet, SQLiteDatabase db) {
        Cursor cursor;
        if (db == null) {
            fail("No Db to Test table creation");
        }

        if (!db.isOpen()) {
            fail("Db is not open to Test table creation");
        }

        cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")",
                null);

        assertTrue("Error: Unable to query the database for table information.",
                cursor.moveToFirst());

        int columnNameIndex = cursor.getColumnIndex("name");
        do {
            String columnName = cursor.getString(columnNameIndex);
            ColumnHashSet.remove(columnName);
        } while (cursor.moveToNext());

        assertTrue("Error: Table :" + tableName + " doesn't contain all of the required entry columns" + ColumnHashSet.toString(),
                ColumnHashSet.isEmpty());
        cursor.close();

    }

    /*
    This function insert and delete a record from a table.
    Query, Insert and delete are tested in this method.
     */

    public static void testTableInsertion(ContentValues testValues, String tableName, String selectionParam, SQLiteDatabase db) {
        Cursor cursor;
        if (db == null) {fail("No Db to Test table insertion");}
        if (!db.isOpen()) {fail("Db is not open to Test table insertion");}

        long rowId = db.insert(tableName, null, testValues);
        assertTrue(rowId != -1);
        cursor = db.query(
                tableName,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        assertTrue("Table - " + tableName + " Inserting into table failed.", cursor.moveToFirst());
        validateCursor("Error: " + tableName + " Query Validation Failed", cursor, testValues);
        assertFalse("Table - " + tableName + " validation of inserted data failed: Data found more than once", cursor.moveToNext());
        cursor.close();

        int rowDeleted = db.delete(tableName, selectionParam + "=?", new String[] {testValues.getAsString(selectionParam)});
        assertEquals("Could not delete the row from table :" + tableName, 1, rowDeleted);
    }

    /*
    This function insert and delete a record from a joined table with foreign key.
    Query, Insert and delete are tested in this method.
     */
    public static long insertRecords(Context context, String table) {
        TmdbDbHelper dbHelper = new TmdbDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = null;
        long RowId;
        switch (table) {
            case "favorite":
                table = TmdbContract.FavoriteEntry.TABLE_NAME;
                testValues = TestUtilities.createMovieValues();
                break;
            case "movie":
                table = TmdbContract.MovieEntry.TABLE_NAME;
                testValues = TestUtilities.createMovieValues();
                break;
            case "trailer":
                table = TmdbContract.TrailerEntry.TABLE_NAME;
                testValues = TestUtilities.createTrailerValues();
                break;
            case "review":
                table = TmdbContract.ReviewEntry.TABLE_NAME;
                testValues = TestUtilities.createReviewValues();
                break;
            case "popular":
                table = TmdbContract.PopularEntry.TABLE_NAME;
                testValues = TestUtilities.createListValues();
                break;
            case "highestrated":
                table = TmdbContract.HighestRatedEntry.TABLE_NAME;
                testValues = TestUtilities.createListValues();
                break;
        }
        RowId = db.insert(table, null, testValues);
//        RowId = db.insert(TmdbContract.FavoriteEntry.TABLE_NAME, null, testValues);
        assertTrue("Error: Failure to insert favorite records", RowId != -1);
        return RowId;
    }

    public static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    public static void validateCursor(String error, Cursor valueCursor, ArrayList<ContentValues> expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        for (ContentValues cv : expectedValues) {
            validateCurrentRecord(error, valueCursor, cv);
//            valueCursor.moveToNext();
        }
        valueCursor.close();
    }

    public static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + valueCursor.getString(idx) +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static SQLiteDatabase OpenDbConnection(Context context) {
        SQLiteDatabase db;
        db = new TmdbDbHelper(context).getWritableDatabase();
        assertEquals(true, db.isOpen());
        return db;
    }

    static void CloseDbConnection(SQLiteDatabase db) {
        if (db.isOpen()) {
            db.close();
        }
        assertEquals(false, db.isOpen());
    }

    public static void flushProvider(HashSet<Uri> uriHashSet, ContentResolver contentResolver) {
        for (Uri uri : uriHashSet) {
            flushUri(uri, contentResolver);
        }
    }

    public static void flushUri(Uri uri, ContentResolver contentResolver) {

        try {
            contentResolver.delete(
                    uri,
                    null,
                    null
            );

        } catch (NullPointerException e) { fail("Flush URI :" + uri.toString() + " failed,  Null pointer exception");}

        Cursor cursor = contentResolver.query(
                uri,
                null,
                null,
                null,
                null
        );
        int i = cursor.getCount();
        assertEquals("Error: Reccords not deleted from Uri :" + uri.toString(), 0, cursor.getCount());
        cursor.close();
    }

    public static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged = false;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility TestDatabase Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    public static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

}