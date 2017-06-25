package sherpavision.app.popularmoviesstagetwo.utilities;

import java.net.URL;
import java.util.Vector;

import android.content.Context;
import android.view.View;
import android.os.AsyncTask;
import android.content.ContentValues;
import sherpavision.app.popularmoviesstagetwo.MovieGridActivity;
import sherpavision.app.popularmoviesstagetwo.data.MovieConstants;
import sherpavision.app.popularmoviesstagetwo.data.MovieContract;


/**
 * Created by aniket on 5/15/17.
 */

public class FetchMovieTask extends AsyncTask<String, Void, Vector<ContentValues>> {
    Vector<ContentValues> simpleJsonMovieData = new Vector<ContentValues>();
    MovieDataJsonUtils mMovieDataJsonUtils;

    public interface OnMovieDataFetched{
        void onMovieDataFetched(Vector<ContentValues> movieData);
    }

    private OnMovieDataFetched delegate = null;


    public FetchMovieTask(Context mContext, OnMovieDataFetched asyncReponse){
        mMovieDataJsonUtils = new MovieDataJsonUtils(mContext);
        delegate = asyncReponse;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        MovieGridActivity.mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Vector<ContentValues> doInBackground(String... params){
        if(params.length ==0){
            return null;
        }

        String toDo = params[0];
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

        try{
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
            simpleJsonMovieData = mMovieDataJsonUtils.getSimpleMovieDataStringsFromJson(jsonMovieResponse, toDo);
            return simpleJsonMovieData;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Vector<ContentValues> movieData) {
        delegate.onMovieDataFetched(movieData);
    }
}