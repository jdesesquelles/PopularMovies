package co.fabrk.popmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class TmdbProviderMock extends ContentProvider implements TmdbServiceApi {

    @Override
    public Cursor getUri(String uri) {
    return null;
    }



    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TmdbDbHelper mOpenHelper;
    static final int FAVORITE = 100;
    static final int FAVORITE_WITH_ID = 101;
    static final int MOVIE = 200;
    static final int MOVIE_WITH_ID = 201;
    static final int TRAILER = 300;
    static final int TRAILER_WITH_ID = 301;
    static final int REVIEW = 400;
    static final int REVIEW_WITH_ID = 401;
    static final int POPULAR = 500;
    static final int HIGHEST_RATED = 600;

    // Projections
    String[] MOVIE_COLUMNS = {TmdbContract.MovieEntry.TABLE_NAME + "." + TmdbContract.MovieEntry._ID, TmdbContract.MovieEntry.TABLE_NAME + "." + TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID, TmdbContract.MovieEntry.COLUMN_ORIGINAL_TITLE, TmdbContract.MovieEntry.COLUMN_POSTER_PATH, TmdbContract.MovieEntry.COLUMN_RELEASE_DATE, TmdbContract.MovieEntry.COLUMN_VOTE_AVERAGE, TmdbContract.MovieEntry.COLUMN_POPULARITY, TmdbContract.MovieEntry.COLUMN_OVERVIEW, TmdbContract.MovieEntry.COLUMN_BACKDROP};
    String[] TRAILER_COLUMNS = {TmdbContract.TrailerEntry.TABLE_NAME + "." + TmdbContract.TrailerEntry._ID, TmdbContract.TrailerEntry.TABLE_NAME + "." + TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID, TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_SITE, TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_ID, TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_KEY, TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_NAME};
    String[] REVIEW_COLUMNS = {TmdbContract.ReviewEntry.TABLE_NAME + "." + TmdbContract.ReviewEntry._ID, TmdbContract.ReviewEntry.TABLE_NAME + "." + TmdbContract.ReviewEntry.COLUMN_TMDB_MOVIE_ID, TmdbContract.ReviewEntry.COLUMN_TMDB_REVIEW_CONTENT, TmdbContract.ReviewEntry.COLUMN_TMDB_REVIEW_AUTHOR};


    // From Favorite
    private static final SQLiteQueryBuilder sFavoriteQueryBuilder;
    static{
        sFavoriteQueryBuilder = new SQLiteQueryBuilder();
        sFavoriteQueryBuilder.setTables(TmdbContract.FavoriteEntry.TABLE_NAME);
    }
    //tmdb_id  = ?
    private static final String sFavoriteWithIdSelection =
            TmdbContract.FavoriteEntry.COLUMN_TMDB_MOVIE_ID+ " = ?";

    private static final SQLiteQueryBuilder sMovieQueryBuilder;
    static{
        sMovieQueryBuilder = new SQLiteQueryBuilder();
        sMovieQueryBuilder.setTables(TmdbContract.MovieEntry.TABLE_NAME);
    }
    //tmdb_id  = ?
    private static final String sMovieWithIdSelection =
            TmdbContract.MovieEntry.TABLE_NAME + "." + TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID+ " = ?";

    private static final String[] sPopularIdSelectionArg =
            new String[]{TmdbContract.PopularEntry.COLUMN_TMDB_MOVIE_ID};

    private static final String[] sHighestRatedIdSelectionArg =
            new String[]{TmdbContract.HighestRatedEntry.COLUMN_TMDB_MOVIE_ID};

    private static final SQLiteQueryBuilder sTrailerQueryBuilder;
    static{
        sTrailerQueryBuilder = new SQLiteQueryBuilder();
//        sTrailerQueryBuilder.setTables(TmdbContract.TrailerEntry.TABLE_NAME);
        sTrailerQueryBuilder.setTables(TmdbContract.TrailerEntry.TABLE_NAME + " INNER JOIN " + TmdbContract.MovieEntry.TABLE_NAME + " ON (" + TmdbContract.TrailerEntry.TABLE_NAME + "." + TmdbContract.TrailerEntry.COLUMN_TMDB_MOVIE_ID + " = " + TmdbContract.MovieEntry.TABLE_NAME + "." + TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID + ")" );

    }
    //tmdb_id  = ?
    private static final String sTrailerWithMovieIdSelection =
            TmdbContract.TrailerEntry.COLUMN_TMDB_MOVIE_ID+ " = ?";

    private static final SQLiteQueryBuilder sReviewQueryBuilder;
    static{
        sReviewQueryBuilder = new SQLiteQueryBuilder();
//        sReviewQueryBuilder.setTables(TmdbContract.ReviewEntry.TABLE_NAME);
        sReviewQueryBuilder.setTables(TmdbContract.ReviewEntry.TABLE_NAME + " INNER JOIN " + TmdbContract.MovieEntry.TABLE_NAME + " ON (" + TmdbContract.ReviewEntry.TABLE_NAME + "." + TmdbContract.ReviewEntry.COLUMN_TMDB_MOVIE_ID + " = " + TmdbContract.MovieEntry.TABLE_NAME + "." + TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID + ")" );

    }
    //tmdb_id  = ?
    private static final String sReviewWithMovieIdSelection =
            TmdbContract.ReviewEntry.COLUMN_TMDB_MOVIE_ID+ " = ?";

    private static final SQLiteQueryBuilder sPopularQueryBuilder;
    static{
        sPopularQueryBuilder = new SQLiteQueryBuilder();
        sPopularQueryBuilder.setTables(TmdbContract.MovieEntry.TABLE_NAME + " INNER JOIN " + TmdbContract.PopularEntry.TABLE_NAME + " ON (" + TmdbContract.MovieEntry.TABLE_NAME + "." + TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID + " = " + TmdbContract.PopularEntry.TABLE_NAME + "." + TmdbContract.PopularEntry.COLUMN_TMDB_MOVIE_ID + ")" );
    }

    private static final SQLiteQueryBuilder sHighestRatedQueryBuilder;
    static{
        sHighestRatedQueryBuilder = new SQLiteQueryBuilder();
//        sHighestRatedQueryBuilder.setTables(TmdbContract.MovieEntry.TABLE_NAME);
        sHighestRatedQueryBuilder.setTables(TmdbContract.MovieEntry.TABLE_NAME + " INNER JOIN " + TmdbContract.HighestRatedEntry.TABLE_NAME + " ON (" + TmdbContract.MovieEntry.TABLE_NAME + "." + TmdbContract.MovieEntry.COLUMN_TMDB_MOVIE_ID + " = " + TmdbContract.HighestRatedEntry.TABLE_NAME + "." + TmdbContract.HighestRatedEntry.COLUMN_TMDB_MOVIE_ID + ")" );

    }


    // Get Cursors

    private Cursor getFavorite(Uri uri, String[] projection, String selection, String[] selectionArgs,
                               String sortOrder) {
        return sFavoriteQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getFavoriteById(Uri uri, String[] projection, String selection, String[] selectionArgs,
                                   String sortOrder) {

        return sFavoriteQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sFavoriteWithIdSelection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovie(Uri uri, String[] projection, String selection, String[] selectionArgs,
                            String sortOrder) {

        return sMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovieById(Uri uri, String[] projection, String selection, String[] selectionArgs,
                                String sortOrder) {

        return sMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieWithIdSelection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getReview(Uri uri, String[] projection, String selection, String[] selectionArgs,
                             String sortOrder) {

        return sReviewQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getReviewByMovieId(Uri uri, String[] projection, String selection, String[] selectionArgs,
                                      String sortOrder) {

        return sReviewQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTrailer(Uri uri, String[] projection, String selection, String[] selectionArgs,
                              String sortOrder) {

        return sTrailerQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTrailerByMovieId(Uri uri, String[] projection, String selection, String[] selectionArgs,
                                       String sortOrder) {
        return sTrailerQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getPopularMovie(Uri uri, String[] projection, String selection, String[] selectionArgs,
                                   String sortOrder) {
//  select
//  tmdb_movies.tmdb_movie_id, tmdb_movies.original_title, tmdb_movies.thumbnail_path, tmdb_movies.release_date,
// tmdb_movies.vote_average, tmdb_movies.popularity, tmdb_movies.overview
//        from tmdb_movies, tmdb_popular where tmdb_movies.tmdb_movie_id = tmdb_popular.tmdb_movie_id;
        return sPopularQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                MOVIE_COLUMNS,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getHighestRated(Uri uri, String[] projection, String selection, String[] selectionArgs,
                                   String sortOrder) {

        return sHighestRatedQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                MOVIE_COLUMNS,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TmdbContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, TmdbContract.PATH_FAVORITES, FAVORITE);
        matcher.addURI(authority, TmdbContract.PATH_FAVORITES + "/*", FAVORITE_WITH_ID);

        matcher.addURI(authority, TmdbContract.PATH_MOVIES, MOVIE);
        matcher.addURI(authority, TmdbContract.PATH_MOVIES + "/*", MOVIE_WITH_ID);

        matcher.addURI(authority, TmdbContract.PATH_TRAILERS, TRAILER);
        matcher.addURI(authority, TmdbContract.PATH_TRAILERS + "/*", TRAILER_WITH_ID);

        matcher.addURI(authority, TmdbContract.PATH_REVIEWS, REVIEW);
        matcher.addURI(authority, TmdbContract.PATH_REVIEWS + "/*", REVIEW_WITH_ID);

        matcher.addURI(authority, TmdbContract.PATH_POPULAR_MOVIES, POPULAR);
        matcher.addURI(authority, TmdbContract.PATH_HIGHEST_RATED, HIGHEST_RATED);
        return matcher;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVORITE:
                return TmdbContract.FavoriteEntry.CONTENT_TYPE;
            case FAVORITE_WITH_ID:
                return TmdbContract.FavoriteEntry.CONTENT_ITEM_TYPE;
            case MOVIE:
                return TmdbContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return TmdbContract.MovieEntry.CONTENT_ITEM_TYPE;
            case TRAILER:
                return TmdbContract.TrailerEntry.CONTENT_TYPE;
            case TRAILER_WITH_ID:
                return TmdbContract.TrailerEntry.CONTENT_TYPE;
            case REVIEW:
                return TmdbContract.ReviewEntry.CONTENT_TYPE;
            case REVIEW_WITH_ID:
                return TmdbContract.ReviewEntry.CONTENT_TYPE;
            case POPULAR:
                return TmdbContract.PopularEntry.CONTENT_TYPE;
            case HIGHEST_RATED:
                return TmdbContract.HighestRatedEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "favorite/*"
            case FAVORITE:
            {
                retCursor = getFavorite(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }
            case FAVORITE_WITH_ID: {
                retCursor = getFavoriteById(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }
            case MOVIE: {
                retCursor = getMovie(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }
            case MOVIE_WITH_ID: {
                retCursor = getMovieById(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }
            case TRAILER: {
                retCursor = getTrailer(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }
            case TRAILER_WITH_ID: {
                retCursor = getTrailerByMovieId(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }
            case REVIEW: {
                retCursor = getReview(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }
            case REVIEW_WITH_ID: {
                retCursor = getReviewByMovieId(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }
            case POPULAR: {
                retCursor = getPopularMovie(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }
            case HIGHEST_RATED: {
                retCursor = getHighestRated(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITE: {
                long _id = db.insert(TmdbContract.FavoriteEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TmdbContract.FavoriteEntry.buildFavoriteUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE: {
                long _id = db.insert(TmdbContract.MovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TmdbContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILER: {
                long _id = db.insert(TmdbContract.TrailerEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TmdbContract.TrailerEntry.buildTrailersUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILER_WITH_ID: {
                long _id = db.insert(TmdbContract.TrailerEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TmdbContract.TrailerEntry.buildTrailersUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW: {
                long _id = db.insert(TmdbContract.ReviewEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TmdbContract.ReviewEntry.buildReviewsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW_WITH_ID: {
                long _id = db.insert(TmdbContract.ReviewEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TmdbContract.ReviewEntry.buildReviewsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case POPULAR: {
                long _id = db.insert(TmdbContract.PopularEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TmdbContract.PopularEntry.buildPopularMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case HIGHEST_RATED: {
                long _id = db.insert(TmdbContract.HighestRatedEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TmdbContract.HighestRatedEntry.buildHighestRatedUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
//        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case FAVORITE:
                rowsDeleted = db.delete(
                        TmdbContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE: {
                rowsDeleted = db.delete(
                        TmdbContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TRAILER: {
                rowsDeleted = db.delete(
                        TmdbContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case REVIEW: {
                rowsDeleted = db.delete(
                        TmdbContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case POPULAR: {
                rowsDeleted = db.delete(
                        TmdbContract.PopularEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case HIGHEST_RATED: {
                rowsDeleted = db.delete(
                        TmdbContract.HighestRatedEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
//        db.close();
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case FAVORITE:
                rowsUpdated = db.update(TmdbContract.FavoriteEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MOVIE: {
                rowsUpdated = db.update(TmdbContract.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            case TRAILER: {
                rowsUpdated = db.update(TmdbContract.TrailerEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            case REVIEW: {
                rowsUpdated = db.update(TmdbContract.ReviewEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            case POPULAR: {
                rowsUpdated = db.update(TmdbContract.PopularEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            case HIGHEST_RATED: {
                rowsUpdated = db.update(TmdbContract.HighestRatedEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        db.beginTransaction();
        int returnCount = 0;
        switch (match) {
            case FAVORITE:
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TmdbContract.FavoriteEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
//                db.close();
                return returnCount;
            case MOVIE:
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TmdbContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
//                db.close();
                return returnCount;
            case TRAILER:
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TmdbContract.TrailerEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
//                db.close();
                return returnCount;
            case TRAILER_WITH_ID: {
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TmdbContract.TrailerEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
//                db.close();
                return returnCount;
            }
            case REVIEW:
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TmdbContract.ReviewEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
//                db.close();
                return returnCount;
            case REVIEW_WITH_ID: {
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TmdbContract.ReviewEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
//                db.close();
                return returnCount;
            }
            case POPULAR:
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TmdbContract.PopularEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
//                db.close();
                return returnCount;
            case HIGHEST_RATED:
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TmdbContract.HighestRatedEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
//                db.close();
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new TmdbDbHelper(getContext());
        return true;
    }
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(TmdbContract.FavoriteEntry.COLUMN_RELEASE_DATE)) {
            Long dateValue = values.getAsLong(TmdbContract.FavoriteEntry.COLUMN_RELEASE_DATE);
            values.put(TmdbContract.FavoriteEntry.COLUMN_RELEASE_DATE, TmdbContract.normalizeDate(dateValue));
        }
    }
}