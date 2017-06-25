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
    public static final int MOVIE_POPULAR_WITH_ID = 101;
//    static final int MOVIE_POPULAR_DETAIL = 200;

//    public static final int TASK_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieDbHelper;


    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, MovieContract.PATH_POPULAR, MOVIE_POPLUAR_LIST);
        uriMatcher.addURI(authority, MovieContract.PATH_POPULAR + "/#", MOVIE_POPULAR_WITH_ID);
        return uriMatcher;
    }

//    private static final SQLiteQueryBuilder sMoviePopularListQueryBuilder;
//    static {
//        sMoviePopularListQueryBuilder = new SQLiteQueryBuilder();
//        sMoviePopularListQueryBuilder.setTables(
//                MovieContract.MoviePopular.TABLE_NAME);
//    }
//    // Member variable for a TaskDbHelper that's initialized in the onCreate() method
//    private MovieDbHelper mMovieDbHelper;
    Context mContext;
    @Override
    public boolean onCreate() {
        mContext = getContext();
        mMovieDbHelper = new MovieDbHelper(mContext);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
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

        switch(match){
            case MOVIE_POPLUAR_LIST:
                long id = db.insert(MovieContract.MoviePopular.TABLE_NAME, null, values);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(MovieContract.MoviePopular.CONTENT_URI, id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into "+ id);
                }
                break;

            default:
                throw new RuntimeException("We are not implementing insert in Sunshine. Use bulkInsert instead");
        }

        Log.i("Aniket","ANiket throwing new notification");
        ContentResolver resolver = getContext().getContentResolver();
        synchronized (resolver){
            resolver.notify();
        }

        return returnUri;
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @Nullable ContentValues[] values) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        Log.i("Aniket","URI: " + uri.toString());
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case MOVIE_POPLUAR_LIST:
                db.beginTransaction();
                int rowsInserted = 0;
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
                    Log.i("Aniket", "Notifying");

                    mContext.getContentResolver().notifyChange(uri, null);
                    Log.i("Aniket", "Done Notifying");

                }
                Log.i("Aniket", "Done with bulkInsert");
                return rowsInserted;

            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                return super.bulkInsert(uri, values);
        }

//
////                db.beginTransaction();
//                long id = 0;
//                try {
//                    id = db.insert(MovieContract.MoviePopular.TABLE_NAME, null, values);
//                } catch (Exception e) {
//                    Log.i("Aniket", "eroor:" + e);
//                }
//                // Insert new values into the database
//                // Inserting values into tasks table
//                if (id > 0) {
//                    returnUri = MovieContract.MoviePopular.buildPopularMoviesUri();
//                    getContext().getContentResolver().notifyChange(uri, null);



//                }

                // Notify the resolver if the uri has been changed, and return the newly inserted URI
//        if (id > 0) {
//            getContext().getContentResolver().notifyChange(uri, null);
//        }

                // Return constructed uri (this points to the newly inserted row of data)
//        return null;
        }

        @Override
        public int delete (@NonNull Uri uri, @Nullable String selection, @Nullable String[]
        selectionArgs){
            Log.i("Aniket", "deleting");

            int numRowsDeleted = 0;
            if (null == selection) selection = "1";

            switch (sUriMatcher.match(uri)) {

                case MOVIE_POPLUAR_LIST:
                    numRowsDeleted = mMovieDbHelper.getWritableDatabase().delete(
                            MovieContract.MoviePopular.TABLE_NAME,
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

        @Override
        public int update (@NonNull Uri uri, @Nullable ContentValues values, @Nullable String
        selection, @Nullable String[]selectionArgs){
            return 0;
        }
    }
