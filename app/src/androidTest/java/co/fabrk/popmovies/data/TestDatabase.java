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

public class TestDatabase extends AndroidTestCase {

    private static SQLiteDatabase db;
    private Cursor cursor;

    public void test11DatabaseOpening() throws Throwable{
        mContext.deleteDatabase(TmdbDbHelper.DATABASE_NAME);
        db = new TmdbDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
    }

    public void test12DatabaseCreated() throws Throwable {
        cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: The database has not been created correctly",
                cursor.moveToFirst());
        cursor.close();
    }

    public void test13TablesCreated() throws Throwable{
        TestUtilities.testTablesCreation(TestUtilities.createTableListHashSet(),db);
    }

//    public void test14TablesCreation() throws Throwable {
//        HashSet<String> tableList = TestUtilities.createTableListHashSet();
//        for (String tableName : tableList) {
//            test3InsertIntoTable(tableName);
//        }
//    }

//    public void test3InsertIntoTable(String tableName) {
//        ContentValues testValues = TestUtilities.createValues(tableName);
//        TestUtilities.testTableInsertion(testValues, TmdbContract.FavoriteEntry.TABLE_NAME, db);
//    }

    public void test2FavoriteTableCreated() throws Throwable {
        TestUtilities.testTableCreation(TmdbContract.FavoriteEntry.TABLE_NAME, TestUtilities.createFavoriteColumnHashSet(), db);
    }

    public void test3InsertIntoFavoriteTable() throws Throwable {
        ContentValues testValues = TestUtilities.createMovieValues();
        TestUtilities.testTableInsertion(testValues, TmdbContract.FavoriteEntry.TABLE_NAME, TmdbContract.FavoriteEntry.COLUMN_TMDB_MOVIE_ID,db);
    }

    // INDIVIDUAL TABLES

    public void test2MovieTableCreated() throws Throwable {
        TestUtilities.testTableCreation( TmdbContract.MovieEntry.TABLE_NAME, TestUtilities.createMovieColumnHashSet(),db);
    }
    public void test3InsertIntoMovieTable() throws Throwable {
        ContentValues testValues = TestUtilities.createMovieValues();
        TestUtilities.testTableInsertion(testValues, TmdbContract.MovieEntry.TABLE_NAME, TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID,db);
    }

    public void test2ReviewTableCreated() throws Throwable {
        TestUtilities.testTableCreation( TmdbContract.ReviewEntry.TABLE_NAME, TestUtilities.createReviewsColumnHashSet(),db);
    }
    public void test3InsertIntoReviewTable() throws Throwable {
        ContentValues testValues = TestUtilities.createReviewValues();
        TestUtilities.testTableInsertion(testValues, TmdbContract.ReviewEntry.TABLE_NAME, TmdbContract.ReviewEntry.COLUMN_TMDB_MOVIE_ID,db);
    }

    public void test2TrailerTableCreated() throws Throwable {
        TestUtilities.testTableCreation(TmdbContract.TrailerEntry.TABLE_NAME, TestUtilities.createTrailersColumnHashSet(), db);
    }
    public void test3InsertIntoTrailerTable() throws Throwable {
        ContentValues testValues = TestUtilities.createTrailerValues();
        TestUtilities.testTableInsertion(testValues, TmdbContract.TrailerEntry.TABLE_NAME, TmdbContract.TrailerEntry.COLUMN_TMDB_MOVIE_ID,db);
    }

    public void test2PopularMovieListTableCreated() throws Throwable {
        TestUtilities.testTableCreation(TmdbContract.PopularEntry.TABLE_NAME, TestUtilities.createMovieListColumnHashSet(), db);
    }
    public void test3InsertIntoPopularTable() throws Throwable {
        ContentValues testValues = TestUtilities.createListValues();
        TestUtilities.testTableInsertion(testValues, TmdbContract.PopularEntry.TABLE_NAME, TmdbContract.PopularEntry.COLUMN_TMDB_MOVIE_ID,db);
    }

    public void test2HighestRatedMovieListTableCreated() throws Throwable {
        TestUtilities.testTableCreation(TmdbContract.HighestRatedEntry.TABLE_NAME, TestUtilities.createMovieListColumnHashSet(),  db);
    }
    public void test3InsertIntoHighestRatedTable() throws Throwable {
        ContentValues testValues = TestUtilities.createListValues();
        TestUtilities.testTableInsertion(testValues, TmdbContract.HighestRatedEntry.TABLE_NAME, TmdbContract.HighestRatedEntry.COLUMN_TMDB_MOVIE_ID,db);
//        // TestDatabase Join tables
//        cursor = db.query(
//                TmdbContract.HighestRatedEntry.TABLE_NAME + " JOIN " + TmdbContract.MovieEntry.TABLE_NAME + " USING ( " + TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID + " )" ,  // Table to Query
//                new String[] {TmdbContract.MovieEntry.COLUMN_ORIGINAL_TITLE}, // all columns
//                null, // Columns for the "where" clause
//                null, // Values for the "where" clause
//                null, // columns to group by
//                null, // columns to filter by row groups
//                null // sort order
//        );
//        assertTrue("Error: No Records returned from Highest Rated joined query", cursor.moveToFirst());
//        cursor.close();

//        System.out.println("Number of found entry :" + cursor.getCount());

    }

    public void testZDatabaseClosing() throws Throwable{
        db.close();
        assertEquals(false, db.isOpen());
    }
}

//    public void testBulkUpdate() throws Throwable{
//        mContext.deleteDatabase(TmdbDbHelper.DATABASE_NAME);
//        db = new TmdbDbHelper(
//                this.mContext).getWritableDatabase();
//
//        assertEquals(true, db.isOpen());
//    }
