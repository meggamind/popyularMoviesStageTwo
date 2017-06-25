package sherpavision.app.popularmoviesstagetwo.sync;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.net.URL;
import java.util.Vector;

import sherpavision.app.popularmoviesstagetwo.data.MovieConstants;
import sherpavision.app.popularmoviesstagetwo.data.MovieContract;
import sherpavision.app.popularmoviesstagetwo.utilities.MovieDataJsonUtils;
import sherpavision.app.popularmoviesstagetwo.utilities.MovieUrlBuilder;
import sherpavision.app.popularmoviesstagetwo.utilities.NetworkUtils;

/**
 * Created by aniket on 6/21/17.
 */

public class MovieSyncTask {

    private static String TAG = "MovieSycnTask";

    synchronized public static void syncMovies(Context context, String...params){
        try{
            if(params.length ==0){
                Log.e(TAG, "no parmas for syncing");
            }

            String toDo = params[0];
            Log.i("Aniket","toDo : " + toDo);
            URL movieRequestUrl = null;
            switch(toDo){
                case MovieConstants.FETCH_POPULAR_MOVIES:
                    movieRequestUrl = MovieUrlBuilder.buildUrl(toDo);
                    break;
                case MovieConstants.FETCH_TOPRATED_MOVIES:
                    movieRequestUrl = MovieUrlBuilder.buildUrl(toDo);
                    break;
                case MovieConstants.FETCH_MOVIE_SELECTED_DETAILS:
                    String movieSlectedId = params[1];
                    movieRequestUrl = MovieUrlBuilder.buildUrlToFetchMovieSelected(movieSlectedId);
                    break;
                default:
                    movieRequestUrl = MovieUrlBuilder.buildUrl(MovieConstants.MOVIEDB_POPULAR_URL);
                    break;
            }

            Vector<ContentValues> simpleJsonMovieData; //= new Vector<ContentValues>();
            MovieDataJsonUtils mMovieDataJsonUtils;
            mMovieDataJsonUtils = new MovieDataJsonUtils(context);

            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
            simpleJsonMovieData = mMovieDataJsonUtils.getSimpleMovieDataStringsFromJson(jsonMovieResponse, toDo);
            int inserted = 0;
            // add to database
            if ( simpleJsonMovieData.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[simpleJsonMovieData.size()];
                simpleJsonMovieData.toArray(cvArray);
                context.getContentResolver().delete(MovieContract.MoviePopular.CONTENT_URI,null,null);

                for (ContentValues values:cvArray){
                    context.getContentResolver().insert(MovieContract.MoviePopular.CONTENT_URI, values);
                }

//                inserted = context.getContentResolver().bulkInsert(MovieContract.MoviePopular.CONTENT_URI, cvArray);
            }



            Log.i("database","download complete. " + inserted + " Inserted");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}