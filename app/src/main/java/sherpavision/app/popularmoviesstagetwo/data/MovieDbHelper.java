package sherpavision.app.popularmoviesstagetwo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import sherpavision.app.popularmoviesstagetwo.data.MovieContract.CommonMovieCol;
import sherpavision.app.popularmoviesstagetwo.data.MovieContract.MoviePopular;
import sherpavision.app.popularmoviesstagetwo.data.MovieContract.MovieTopRated;
import sherpavision.app.popularmoviesstagetwo.data.MovieContract.MovieFavorite;

/**
 * Created by aniket on 6/21/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    static final String DATABASE_NAME = "popularmovies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_POPULAR_MOVIE_TABLE = "CREATE TABLE " + MoviePopular.TABLE_NAME + " (" +
                MoviePopular._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +  // REQUIRED!!!
                MoviePopular.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE, " +
                MoviePopular.COLUMN_POPULAR_INDEX + " INTEGER, " +
                MoviePopular.COLUMN_TOP_RATED_INDEX + " INTEGER, " +
                MoviePopular.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MoviePopular.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MoviePopular.COLUMN_RELEASE_DATE + " TEXT, " +
                MoviePopular.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviePopular.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MoviePopular.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MoviePopular.COLUMN_TRAILER + " TEXT, " +
                MoviePopular.COLUMN_FAVORITE_TIMESTAMP + " INTEGER, " +
                MoviePopular.COLUMN_REVIEW + " TEXT, " +
                MoviePopular.COLUMN_REVIEW_NAME + " TEXT" +
                " );";

        final String SQL_CREATE_TOP_RATED_MOVIE_TABLE = "CREATE TABLE " + MovieTopRated.TABLE_NAME + " (" +
                MovieTopRated._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +  // REQUIRED!!!
                MovieTopRated.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE, " +
                MovieTopRated.COLUMN_POPULAR_INDEX + " INTEGER, " +
                MovieTopRated.COLUMN_TOP_RATED_INDEX + " INTEGER, " +
                MovieTopRated.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieTopRated.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieTopRated.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieTopRated.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieTopRated.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieTopRated.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieTopRated.COLUMN_TRAILER + " TEXT, " +
                MovieTopRated.COLUMN_FAVORITE_TIMESTAMP + " INTEGER, " +
                MovieTopRated.COLUMN_REVIEW + " TEXT, " +
                MovieTopRated.COLUMN_REVIEW_NAME + " TEXT" +
                " );";


        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " + MovieFavorite.TABLE_NAME + " (" +
                MovieFavorite._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +  // REQUIRED!!!
                CommonMovieCol.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE, " +
                CommonMovieCol.COLUMN_POPULAR_INDEX + " INTEGER, " +
                CommonMovieCol.COLUMN_TOP_RATED_INDEX + " INTEGER, " +
                CommonMovieCol.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                CommonMovieCol.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                CommonMovieCol.COLUMN_RELEASE_DATE + " TEXT, " +
                CommonMovieCol.COLUMN_TITLE + " TEXT NOT NULL, " +
                CommonMovieCol.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                CommonMovieCol.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                CommonMovieCol.COLUMN_TRAILER + " TEXT, " +
                CommonMovieCol.COLUMN_FAVORITE_TIMESTAMP + " TEXT, " +
                CommonMovieCol.COLUMN_REVIEW + " TEXT, " +
                CommonMovieCol.COLUMN_REVIEW_NAME + " TEXT" +
                " );";


        db.execSQL(SQL_CREATE_POPULAR_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_TOP_RATED_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MoviePopular.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieTopRated.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieFavorite.TABLE_NAME);
        onCreate(db);

    }
}
