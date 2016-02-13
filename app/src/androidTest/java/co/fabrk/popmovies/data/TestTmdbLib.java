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
package co.fabrk.popmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import java.util.Map;
import java.util.Set;
import co.fabrk.popmovies.tmdb.TmdbDatabaseOperations;
import co.fabrk.popmovies.tmdb.TmdbProviderUtils;


public class TestTmdbLib extends AndroidTestCase {

    private static SQLiteDatabase db;
    private Cursor cursor;
    private TestUtilities testUtilities = new TestUtilities();

    public void testMain() {
        TmdbOperation();
    }

    private void TmdbOperation() {
        deleteFromFavorite();
    }

    private void deleteFromFavorite() {
        // Create test data
        ContentValues testData = createTestValues();
        String movieId = testData.getAsString(TmdbContract.FavoriteEntry.COLUMN_TMDB_MOVIE_ID);

        // Check data doesn't exist
        assertFalse(TmdbProviderUtils.isFavoriteMovie(movieId,mContext));

        // Check Error is return
        assertEquals(0,TmdbDatabaseOperations.deleteFromFavorite(movieId, mContext.getContentResolver()));
        //Insert data into the Database
        if (!db.isOpen()){TestUtilities.OpenDbConnection(mContext);}
        insertTestValues(testData);
        TestUtilities.CloseDbConnection(db);

        // Check data has been inserted
        assertTrue(TmdbProviderUtils.isFavoriteMovie(movieId,mContext));

        // Call deleteFromFavorite
        // Check the data has been deleted
        assertEquals(1,TmdbDatabaseOperations.deleteFromFavorite(movieId, mContext.getContentResolver()));

        // Call once again
        // Check Error is return
        assertFalse(TmdbProviderUtils.isFavoriteMovie(movieId,mContext));

    }

    private ContentValues createTestValues() {
        return testUtilities.createMovieValues();
    }

    private long insertTestValues(ContentValues testData) {
        long rowId;
        rowId = db.insert(TmdbContract.FavoriteEntry.TABLE_NAME, null, testData);
        assertTrue(rowId != -1);
        return rowId;
    }


    private void TmdbProviderUtils() {
//        IsFavoriteFunctionTest();
    }

    public void IsFavorite() throws Throwable {
        // Create two entry in popular movie.
        // Create one in the favorite
        // Check is favorite is detected
        // Check if not favorite is detected

        // Open a connection to the database
//        mContext.deleteDatabase(TmdbDbHelper.DATABASE_NAME);
        db = new TmdbDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());


        // Create test values
        ContentValues testValues = testUtilities.createMovieValues();
        long rowId;
        rowId = db.insert(TmdbContract.FavoriteEntry.TABLE_NAME, null, testValues);
        assertTrue(rowId != -1);
        // Check the values has been inserted
        cursor = db.query(
                TmdbContract.FavoriteEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                TmdbContract.FavoriteEntry.COLUMN_TMDB_MOVIE_ID + "=?", // Columns for the "where" clause
                new String[] {testValues.getAsString(TmdbContract.FavoriteEntry.COLUMN_TMDB_MOVIE_ID)}, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        assertTrue("Error: No Records returned from Favorite query", cursor.moveToFirst());
        validateCurrentRecord("Error: Favorite Movies Query Validation Failed",
                cursor, testValues);
        assertFalse("Error: More than one movie returned for this Id",
                cursor.moveToNext());
        cursor.close();

        assertFalse(TmdbProviderUtils.isFavoriteMovie("Wrong value",mContext));
        assertTrue(TmdbProviderUtils.isFavoriteMovie(testValues.getAsString(TmdbContract.FavoriteEntry.COLUMN_TMDB_MOVIE_ID),mContext));
        db.close();
    }

    private void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }


}
