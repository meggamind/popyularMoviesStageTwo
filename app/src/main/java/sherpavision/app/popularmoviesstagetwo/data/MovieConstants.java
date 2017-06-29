package sherpavision.app.popularmoviesstagetwo.data;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;

import sherpavision.app.popularmoviesstagetwo.BuildConfig;

/**
 * Created by aniket on 5/13/17.
 */

public class MovieConstants {


    public static final String FETCH_POPULAR_MOVIES = "popular";
    public static final String FETCH_TOPRATED_MOVIES = "vote_average";
    public static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "api_key";
    public static final String KEY = BuildConfig.THE_MOVIE_DATABASE_API_KEY;

    // Json fields required
    public static final String MOVIEDB_RESULTS = "results";
    public static final String MOVIEDB_ID = "id";
    public static final String MOVIEDB_OVERVIEW = "overview";
    public static final String MOVIEDB_POSTER_PATH = "poster_path";
    public static final String MOVIEDB_BACKDROP_PATH = "backdrop_path";
    public static final String MOVIEDB_RELEASE_DATE = "release_date";
    public static final String MOVIEDB_POPULARITY = "popularity";
    public static final String MOVIEDB_TITLE = "title";
    public static final String MOVIEDB_ORIGINAL_TITLE = "original_title";
    public static final String MOVIEDB_VOTE_AVERAGE = "vote_average";
    public static final String MOVIEDB_POPULAR_URL = "popular";
    public static final String MOVIEDB_TOPRATED_URL = "top_rated";
    public static final String FETCH_MOVIE_SELECTED_DETAILS = "selectedMovieDetails";

    public static final String MOVIEDB_MOVIE_KEY = "key";
    public static final String MOVIEDB_VIDEO_TYPE = "type";
    public static final String MOVIEDB_TRAILER_TYPE = "Trailer";
    public static final String MOVIEDB_REVIEWS = "reviews";
    public static final String MOVIEDB_DELIMINATOR = ":::";



    public static String getBaseUrlFor(String sortBy){
       if(sortBy.equals(FETCH_POPULAR_MOVIES)){
           return MOVIE_BASE_URL + MOVIEDB_POPULAR_URL;
       }else{
           return MOVIE_BASE_URL + MOVIEDB_TOPRATED_URL;
       }
    }

    public static SpannableStringBuilder makeStringBold(String stringToConvert){
        final SpannableStringBuilder spReleaseDate = new SpannableStringBuilder(stringToConvert);
        spReleaseDate.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, stringToConvert.length(), 0);
        return spReleaseDate;
    }


    public static void watchYoutubeVideo(String id, Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            if (MovieConstants.isAppInstalled("com.google.android.youtube", context)) {
                intent.setClassName("com.google.android.youtube", "com.google.android.youtube.WatchActivity");
            }
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            context.startActivity(intent);
        }
    }


    public static boolean isAppInstalled(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }
}
