package sherpavision.app.popularmoviesstagetwo.data;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import sherpavision.app.popularmoviesstagetwo.BuildConfig;

/**
 * Created by aniket on 5/13/17.
 */

public class MovieConstants {


    public static final String FETCH_POPULAR_MOVIES = "popularity";
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



    public static String getBaseUrlFor(String sortBy){
       if(sortBy == FETCH_POPULAR_MOVIES){
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
}
