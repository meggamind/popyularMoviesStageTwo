package sherpavision.app.popularmoviesstagetwo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import sherpavision.app.popularmoviesstagetwo.data.MovieContract.MoviePopular;

/**
 * Created by aniket on 6/21/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "popularmovies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("Aniket", "inOncreate");
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MoviePopular.TABLE_NAME + " (" +
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
                MoviePopular.COLUMN_FAVORITE + " INTEGER DEFAULT 0, " +
                MoviePopular.COLUMN_FAVORITE_TIMESTAMP + " INTEGER, " +
                MoviePopular.COLUMN_DETAILS_LOADED  + " INTEGER DEFAULT 0, " +
                MoviePopular.COLUMN_REVIEW + " TEXT, " +
                MoviePopular.COLUMN_REVIEW_NAME + " TEXT" +
                " );";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        Log.i("Aniket","Finished creating a table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Aniket","on upgrade table");

        db.execSQL("DROP TABLE IF EXISTS " + MoviePopular.TABLE_NAME);
        onCreate(db);

    }
}
