package sherpavision.app.popularmoviesstagetwo.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by aniket on 6/21/17.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "sherpavision.app.popularmoviesstagetwo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_POPULAR = "popular";
    public static final String PATH_TOP_RATED = "top_rated";
    public static final String PATH_FAVORITES = "favorites";
    public static final String PATH_MOVIE = "movie";


    public static final class MoviePopular implements BaseColumns {

        /* The base CONTENT_URI used to query the Weather table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_POPULAR)
                .build();

        public static final String TABLE_NAME = "popular_movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POPULAR_INDEX = "popular_index";
        public static final String COLUMN_TOP_RATED_INDEX = "top_rated_index";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
//        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
//        public static final String COLUMN_VOTE_COUNT = "vote_count";
//        public static final String COLUMN_TAGLINE = "tagline";
        public static final String COLUMN_TRAILER = "trailers";
//        public static final String COLUMN_TRAILER2 = "trailer2";
//        public static final String COLUMN_TRAILER3 = "trailer3";
//        public static final String COLUMN_TRAILER_NAME = "trailer_name";
//        public static final String COLUMN_TRAILER2_NAME = "trailer2_name";
//        public static final String COLUMN_TRAILER3_NAME = "trailer3_name";
        public static final String COLUMN_FAVORITE = "favorite_flag";
        public static final String COLUMN_FAVORITE_TIMESTAMP = "favorite_timestamp";
        public static final String COLUMN_DETAILS_LOADED = "details_loaded";
        public static final String COLUMN_REVIEW = "reviews";
//        public static final String COLUMN_REVIEW2 = "review2";
//        public static final String COLUMN_REVIEW3 = "review3";
        public static final String COLUMN_REVIEW_NAME = "review_name";
//        public static final String COLUMN_REVIEW2_NAME = "review2_name";
//        public static final String COLUMN_REVIEW3_NAME = "review3_name";
//        public static final String COLUMN_SPARE_INTEGER = "spare_integer";
//        public static final String COLUMN_SPARE_STRING1 = "spare_string1";
//        public static final String COLUMN_SPARE_STRING2 = "spare_string2";
//        public static final String COLUMN_SPARE_STRING3 = "spare_string3";
//        public static final String COLUMN_MYNAME = "myname";




        public static final String CONTENT_POPULAR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;

        public static final String CONTENT_POPULAR_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY
                        + "/" + PATH_POPULAR + "/" + PATH_MOVIE;

        public static Uri buildMovieDetailsUri(long id) {
            return ContentUris.withAppendedId(buildMovieDetails(), id);
        }

        public static Uri buildPopularMoviesUri() {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_POPULAR)
                    .build();
        }

        public static Uri buildMovieDetails() {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_POPULAR)
                    .appendPath(PATH_MOVIE)
                    .build();
        }

        // these are pretty dependent on the structure of the URI
        //    ... / <mode> / movie / idnumber   in general here
        //   so:  item 0 is mode, item 2 is movie idnumber
        //  yes it is a bit of a hack but that is what we have here for lego pieces
        //
        public static String getMovieModeFromUri(Uri uri) {
            return uri.getPathSegments().get(0);
        }
        public static String getMovieIDFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }




    }
}
