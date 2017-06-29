package sherpavision.app.popularmoviesstagetwo.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by aniket on 6/21/17.
 */

public class MovieContentProvider extends ContentProvider {

    public static final int MOVIE_POPLUAR_LIST = 100;
    public static final int POPULAR_MOVIE_WITH_ID = 101;
    public static final int MOVIE_TOP_RATED_LIST = 110;
    public static final int MOVIE_TOP_RATED_WITH_ID = 111;
    public static final int MOVIE_FAVORITE_LIST = 120;
    public static final int MOVIE_FAVORITE_WITH_ID = 121;


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieDbHelper;

    private static final String sMoviePopularIdSelection =
            MovieContract.MoviePopular.TABLE_NAME +
                    "." + MovieContract.MoviePopular.COLUMN_MOVIE_ID + " = ? ";

    private static final String sMovieTopRatedIdSelection =
            MovieContract.MovieTopRated.TABLE_NAME +
                    "." + MovieContract.MovieTopRated.COLUMN_MOVIE_ID + " = ? ";

    private static final String sMovieFavoriteIdSelection =
            MovieContract.MovieFavorite.TABLE_NAME +
                    "." + MovieContract.CommonMovieCol.COLUMN_MOVIE_ID + " = ? ";

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, MovieContract.PATH_POPULAR, MOVIE_POPLUAR_LIST);
        uriMatcher.addURI(authority, MovieContract.PATH_POPULAR + "/#", POPULAR_MOVIE_WITH_ID);
        uriMatcher.addURI(authority, MovieContract.PATH_TOP_RATED, MOVIE_TOP_RATED_LIST);
        uriMatcher.addURI(authority, MovieContract.PATH_TOP_RATED + "/#", MOVIE_TOP_RATED_WITH_ID);
        uriMatcher.addURI(authority, MovieContract.PATH_FAVORITE, MOVIE_FAVORITE_LIST);
        uriMatcher.addURI(authority, MovieContract.PATH_FAVORITE + "/#", MOVIE_FAVORITE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {

            case MOVIE_POPLUAR_LIST: {
                cursor = mMovieDbHelper.getReadableDatabase().query(
                        MovieContract.MoviePopular.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case MOVIE_TOP_RATED_LIST: {
                cursor = mMovieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieTopRated.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case MOVIE_TOP_RATED_WITH_ID: {
                cursor = getMovieById(uri, projection, sortOrder, MOVIE_TOP_RATED_WITH_ID);
                break;
            }
            case MOVIE_FAVORITE_LIST: {
                cursor = mMovieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieFavorite.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case MOVIE_FAVORITE_WITH_ID: {
                cursor = getMovieById(uri, projection, sortOrder, MOVIE_FAVORITE_WITH_ID);
                break;
            }
            case POPULAR_MOVIE_WITH_ID: {
                cursor = getMovieById(uri, projection, sortOrder, POPULAR_MOVIE_WITH_ID);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;
        switch (match) {
            case MOVIE_POPLUAR_LIST:
                id = db.insert(MovieContract.MoviePopular.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MoviePopular.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + id);
                }
                break;
            case MOVIE_TOP_RATED_LIST:
                id = db.insert(MovieContract.MovieTopRated.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieTopRated.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + id);
                }
                break;
            case MOVIE_FAVORITE_LIST:
                id = db.insert(MovieContract.MovieFavorite.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieFavorite.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + id);
                }
                break;

            default:
                throw new RuntimeException("We are not implementing insert in Sunshine. Use bulkInsert instead");
        }
        ContentResolver resolver = getContext().getContentResolver();
        synchronized (resolver) {
            resolver.notify();
        }
        return returnUri;
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned
        int rowsInserted;
        switch (match) {
            case MOVIE_POPLUAR_LIST:
                rowsInserted = 0;
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MoviePopular.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case MOVIE_TOP_RATED_LIST:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MovieTopRated.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[]
            selectionArgs) {
        int numRowsDeleted = 0;
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case MOVIE_POPLUAR_LIST:
                numRowsDeleted = mMovieDbHelper.getWritableDatabase().delete(
                        MovieContract.MoviePopular.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case MOVIE_TOP_RATED_LIST:
                numRowsDeleted = mMovieDbHelper.getWritableDatabase().delete(
                        MovieContract.MovieTopRated.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case MOVIE_FAVORITE_WITH_ID:
                String favMovie_id = MovieContract.MovieFavorite.getMovieIDFromUri(uri);
                String deleteOneRowFavoriteSelectionArgs[] = {favMovie_id};
                selection = MovieContract.MovieFavorite.TABLE_NAME +
                        "." + MovieContract.CommonMovieCol.COLUMN_MOVIE_ID + " = ? ";
                numRowsDeleted = mMovieDbHelper.getWritableDatabase().delete(
                        MovieContract.MovieFavorite.TABLE_NAME,
                        selection,
                        deleteOneRowFavoriteSelectionArgs);
                break;
            case POPULAR_MOVIE_WITH_ID:
                String movie_id = MovieContract.MoviePopular.getMovieIDFromUri(uri);
                String deleteOneRowSelectionArgs[] = {movie_id};
                numRowsDeleted = mMovieDbHelper.getWritableDatabase().delete(
                        MovieContract.MoviePopular.TABLE_NAME,
                        sMoviePopularIdSelection,
                        deleteOneRowSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String
            selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted = 0;
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case MOVIE_POPLUAR_LIST:
                numRowsDeleted = mMovieDbHelper.getWritableDatabase().update(
                        MovieContract.MoviePopular.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            case POPULAR_MOVIE_WITH_ID:
                String movie_id = MovieContract.MoviePopular.getMovieIDFromUri(uri);
                String deletOneRowSelectionArgs[] = {movie_id};
                numRowsDeleted = mMovieDbHelper.getWritableDatabase().update(
                        MovieContract.MovieTopRated.TABLE_NAME,
                        values,
                        sMoviePopularIdSelection,
                        deletOneRowSelectionArgs);
                break;
            case MOVIE_FAVORITE_LIST:
                numRowsDeleted = mMovieDbHelper.getWritableDatabase().update(
                        MovieContract.MovieFavorite.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }


    private Cursor getMovieById(Uri uri, String[] projection, String sortOrder, int movieType) {
        String movie_id;
        String selection = null;
        String selectionArgs[] = new String[1];
        String table = null;
        switch (movieType) {
            case POPULAR_MOVIE_WITH_ID:
                movie_id = MovieContract.MoviePopular.getMovieIDFromUri(uri);
                selection = sMoviePopularIdSelection;
                selectionArgs[0] = movie_id;
                table = MovieContract.MoviePopular.TABLE_NAME;
                break;

            case MOVIE_TOP_RATED_WITH_ID:
                movie_id = MovieContract.MovieTopRated.getMovieIDFromUri(uri);
                selection = sMovieTopRatedIdSelection;
                selectionArgs[0] = movie_id;
                table = MovieContract.MovieTopRated.TABLE_NAME;
                break;

            case MOVIE_FAVORITE_WITH_ID:
                movie_id = MovieContract.MovieFavorite.getMovieIDFromUri(uri);
                selection = sMovieFavoriteIdSelection;
                selectionArgs[0] = movie_id;
                table = MovieContract.MovieFavorite.TABLE_NAME;
                break;

        }
        return mMovieDbHelper.getReadableDatabase().query(
                table,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

}
