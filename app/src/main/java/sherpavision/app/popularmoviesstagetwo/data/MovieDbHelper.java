package sherpavision.app.popularmoviesstagetwo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MoviePopular.TABLE_NAME + " (" +
                MovieContract.MoviePopular._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +  // REQUIRED!!!
                MovieContract.MoviePopular.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE, " +
                MovieContract.MoviePopular.COLUMN_POPULAR_INDEX + " INTEGER, " +
                MovieContract.MoviePopular.COLUMN_TOP_RATED_INDEX + " INTEGER, " +
                MovieContract.MoviePopular.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MoviePopular.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.MoviePopular.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieContract.MoviePopular.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MoviePopular.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieContract.MoviePopular.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
//                MovieContract.MoviePopular.COLUMN_VOTE_COUNT + " TEXT NOT NULL, " +
//                MovieContract.MoviePopular.COLUMN_TAGLINE + " TEXT, " +
                MovieContract.MoviePopular.COLUMN_TRAILER + " TEXT, " +
//                MovieContract.MoviePopular.COLUMN_TRAILER2 + " TEXT, " +
//                MovieContract.MoviePopular.COLUMN_TRAILER3 + " TEXT, "  +
//                MovieContract.MoviePopular.COLUMN_TRAILER_NAME + " TEXT, " +
//                MovieContract.MoviePopular.COLUMN_TRAILER2_NAME + " TEXT, " +
//                MovieContract.MoviePopular.COLUMN_TRAILER3_NAME + " TEXT, "  +
                MovieContract.MoviePopular.COLUMN_FAVORITE + " INTEGER DEFAULT 0, " +
                MovieContract.MoviePopular.COLUMN_FAVORITE_TIMESTAMP + " INTEGER, " +
                MovieContract.MoviePopular.COLUMN_DETAILS_LOADED  + " INTEGER DEFAULT 0, " +
                MovieContract.MoviePopular.COLUMN_REVIEW + " TEXT, " +
//                MovieContract.MoviePopular.COLUMN_REVIEW2 + " TEXT, " +
//                MovieContract.MoviePopular.COLUMN_REVIEW3 + " TEXT, "  +
                MovieContract.MoviePopular.COLUMN_REVIEW_NAME + " TEXT" +
//                MovieContract.MoviePopular.COLUMN_REVIEW2_NAME + " TEXT, " +
//                MovieContract.MoviePopular.COLUMN_REVIEW3_NAME + " TEXT, "  +
//                MovieContract.MoviePopular.COLUMN_SPARE_INTEGER  + " INTEGER DEFAULT 0, " +
//                MovieContract.MoviePopular.COLUMN_SPARE_STRING1 + " TEXT, " +
//                MovieContract.MoviePopular.COLUMN_SPARE_STRING2 + " TEXT, " +
//                MovieContract.MoviePopular.COLUMN_SPARE_STRING3 + " TEXT, "  +
//                MovieContract.MoviePopular.COLUMN_MYNAME + " TEXT NOT NULL" +

                " );";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        Log.i("Aniket","Finished creating a table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Aniket","on upgrade table");

        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MoviePopular.TABLE_NAME);
        onCreate(db);

    }
}
