package sherpavision.app.popularmoviesstagetwo.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.Vector;

import sherpavision.app.popularmoviesstagetwo.MovieGridActivity;
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

    synchronized public static void syncMovies(Context context, String... params) {
        try {
            if (params.length <= 0) {
                throw new Exception("Nothing to do ,empty params");
            }

            String toDo = params[0];


            Uri movieDatabase = null;
            String tableName = null;
            int inserted;
            Vector<ContentValues> jsonMovieData;
            URL popularMovieRequestUrl = MovieUrlBuilder.buildUrl(MovieConstants.FETCH_POPULAR_MOVIES);
            URL topRatedMovieRequestUrl = MovieUrlBuilder.buildUrl(MovieConstants.FETCH_TOPRATED_MOVIES);
            URL detailMovieRequestUrl;


            switch (toDo) {
                case MovieConstants.FETCH_POPULAR_MOVIES:
                    Log.i(TAG, "FETCHING POPULAR_MOVIES");

                    jsonMovieData = getResponseFrom(context, popularMovieRequestUrl, MovieConstants.FETCH_POPULAR_MOVIES);
                    movieDatabase = MovieContract.MoviePopular.CONTENT_URI;
                    if (jsonMovieData.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[jsonMovieData.size()];
                        jsonMovieData.toArray(cvArray);

                        for (ContentValues value : cvArray) {
                            int movieId = value.getAsInteger(MovieContract.MoviePopular.COLUMN_MOVIE_ID);
                            detailMovieRequestUrl = MovieUrlBuilder.buildUrlToFetchMovieSelected(String.valueOf(movieId));
                            jsonMovieData = getResponseFrom(context, detailMovieRequestUrl, MovieConstants.FETCH_MOVIE_SELECTED_DETAILS);
                            String trailer = jsonMovieData.get(0).getAsString(MovieConstants.MOVIEDB_MOVIE_KEY);
                            String reviewName = jsonMovieData.get(0).getAsString("author");
                            String reviews = jsonMovieData.get(0).getAsString("content");
                            value.put(MovieContract.MoviePopular.COLUMN_TRAILER, trailer);
                            value.put(MovieContract.MoviePopular.COLUMN_REVIEW_NAME, reviewName);
                            value.put(MovieContract.MoviePopular.COLUMN_REVIEW, reviews);
                        }
                        context.getContentResolver().delete(movieDatabase, null, null);
                        inserted = context.getContentResolver().bulkInsert(movieDatabase, cvArray);
                        Log.i("database", "download complete FETCH_POPULAR_MOVIES: " + inserted + " Inserted");
                    }
                    break;

                case MovieConstants.FETCH_TOPRATED_MOVIES:
                    Log.i(TAG, "FETCHING TOPRATED_MOVIES");
                    jsonMovieData = getResponseFrom(context, topRatedMovieRequestUrl, MovieConstants.FETCH_TOPRATED_MOVIES);
                    movieDatabase = MovieContract.MovieTopRated.CONTENT_URI;
                    if (jsonMovieData.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[jsonMovieData.size()];
                        jsonMovieData.toArray(cvArray);

                        for (ContentValues value : cvArray) {
                            int movieId = value.getAsInteger(MovieContract.MovieTopRated.COLUMN_MOVIE_ID);
                            detailMovieRequestUrl = MovieUrlBuilder.buildUrlToFetchMovieSelected(String.valueOf(movieId));
                            jsonMovieData = getResponseFrom(context, detailMovieRequestUrl, MovieConstants.FETCH_MOVIE_SELECTED_DETAILS);
                            String trailer = jsonMovieData.get(0).getAsString(MovieConstants.MOVIEDB_MOVIE_KEY);
                            String reviewName = jsonMovieData.get(0).getAsString("author");
                            String reviews = jsonMovieData.get(0).getAsString("content");
                            value.put(MovieContract.MovieTopRated.COLUMN_TRAILER, trailer);
                            value.put(MovieContract.MovieTopRated.COLUMN_REVIEW_NAME, reviewName);
                            value.put(MovieContract.MovieTopRated.COLUMN_REVIEW, reviews);
                        }
                        context.getContentResolver().delete(movieDatabase, null, null);
                        inserted = context.getContentResolver().bulkInsert(movieDatabase, cvArray);
                        Log.i("database", "download complete. FETCH_TOPRATED_MOVIES:  " + inserted + " Inserted");
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Action not supported: " + toDo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Vector<ContentValues> getResponseFrom(Context mContext, URL movieRequestUrl, String toDo) throws Exception {
        Vector<ContentValues> simpleJsonMovieData;
        MovieDataJsonUtils mMovieDataJsonUtils;
        mMovieDataJsonUtils = new MovieDataJsonUtils(mContext);

        String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
        simpleJsonMovieData = mMovieDataJsonUtils.getSimpleMovieDataStringsFromJson(jsonMovieResponse, toDo);
        return simpleJsonMovieData;
    }
}