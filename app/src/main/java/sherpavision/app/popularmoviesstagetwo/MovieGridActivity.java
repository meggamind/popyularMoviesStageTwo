package sherpavision.app.popularmoviesstagetwo;

import java.util.Vector;

//import android.support.v4.app.LoaderManager.LoaderCallbacks;
//import android.support.v4.app.LoaderManager;
import android.content.ContentResolver;
//import android.support.v4.content.CursorLoader;
//import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.view.MenuItem;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.content.ContentValues;
import android.support.v7.app.ActionBar;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import sherpavision.app.popularmoviesstagetwo.data.MovieContract;
import sherpavision.app.popularmoviesstagetwo.data.MovieItem;
import sherpavision.app.popularmoviesstagetwo.data.MovieConstants;
import sherpavision.app.popularmoviesstagetwo.sync.MovieSyncUtils;
import sherpavision.app.popularmoviesstagetwo.utilities.FetchMovieTask;

/**
 * Created by aniket on 5/13/17.
 */

public class MovieGridActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>,
        ActionBar.TabListener {
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private MovieAdapter mMovieAdapter;
    public static ProgressBar mProgressBar;
    private int mPosition = RecyclerView.NO_POSITION;
    private static final int ID_MOVIEDB_LOADER = 44;


    static final String[] MOVIE_COLUMNS = {
            MovieContract.MoviePopular._ID,
            MovieContract.MoviePopular.COLUMN_MOVIE_ID,
            MovieContract.MoviePopular.COLUMN_POPULAR_INDEX,
            MovieContract.MoviePopular.COLUMN_TOP_RATED_INDEX,
            MovieContract.MoviePopular.COLUMN_OVERVIEW,
            MovieContract.MoviePopular.COLUMN_POSTER_PATH,
            MovieContract.MoviePopular.COLUMN_RELEASE_DATE,
            MovieContract.MoviePopular.COLUMN_TITLE,
            MovieContract.MoviePopular.COLUMN_ORIGINAL_TITLE,
            MovieContract.MoviePopular.COLUMN_VOTE_AVERAGE,
            MovieContract.MoviePopular.COLUMN_TRAILER,
            MovieContract.MoviePopular.COLUMN_FAVORITE,
            MovieContract.MoviePopular.COLUMN_FAVORITE_TIMESTAMP,
            MovieContract.MoviePopular.COLUMN_DETAILS_LOADED,
            MovieContract.MoviePopular.COLUMN_REVIEW,
            MovieContract.MoviePopular.COLUMN_REVIEW_NAME
    };


    static final int COL_CURSOR_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_POPULAR_INDEX = 2;
    static final int COL_TOP_RATED_INDEX = 3;
    static final int COL_OVERVIEW = 4;
    static final int COL_POSTER_PATH = 5;
    static final int COL_RELEASE_DATE = 6;
    static final int COL_TITLE = 7;
    static final int COL_ORIGINAL_TITLE = 8;
    static final int COL_VOTE_AVERAGE = 9;
    static final int COL_TRAILER = 10;
    static final int COL_FAVORITE = 11;
    static final int COL_FAVORITE_TIMESTAMP = 12;
    static final int COL_DETAILS_LOADED = 13;
    static final int COL_REVIEW = 14;
    static final int COL_REVIEW_NAME = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);

        // Now we can setup the adapter of the RecyclerView and toggle the visibility.
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie_list);

        // Used to display errors amd will be hidden id there are no errors
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        mRecyclerView.setHasFixedSize(true);

        /*  Adapter is responsible for linking our weather data with the views.
            Will display our movie data
        */

        mMovieAdapter = new MovieAdapter(getApplicationContext(), this);
        mRecyclerView.setAdapter(mMovieAdapter);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        showLoading();
        setupTabs();


        getSupportLoaderManager().initLoader(ID_MOVIEDB_LOADER, null, this);
//        {
//            @Override
//            public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
//
////                switch (loaderId){
////                    case ID_MOVIEDB_LOADER:
//                        Log.i("Ainket","ID_MOVIEDB_LOADER");
//                        Uri movieDbQueryUri = MovieContract.MoviePopular.CONTENT_URI;
//                        Log.i("Aniket", "URI2: " + movieDbQueryUri.toString());
//
//                        String sortOrder = MovieContract.MoviePopular.COLUMN_POPULAR_INDEX + " DESC";
////                String Selection = MovieContract.MoviePopular.g
//
////                long normalizedUtcNow = SunshineDateUtils.normalizeDate(System.currentTimeMillis());
////                String selection = MovieContract.MoviePopular.COLUMN_POPULAR_INDEX + " >= " + 0;
//
//                        return new CursorLoader(getApplicationContext(),
//                                movieDbQueryUri,
//                                MOVIE_COLUMNS,
//                                null,
//                                null,
//                                sortOrder);
//
////                        if (a==null){
////                            Log.i("Ainket","loader is null");
////                        }
////                        Log.i("Ainket","done wiyh onCreateLoader");
////                        return a;
////                    default:
////                        throw new RuntimeException("Loader not implemented: " + loaderId);
////                }
//            }
//
//            @Override
//            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//
//                Log.i("Aniket","onLoadFinished");
//                mMovieAdapter.swapCursor(data);
//                if(mPosition == RecyclerView.NO_POSITION) mPosition = 0;
//                mRecyclerView.smoothScrollToPosition(mPosition);
//            }
//
//            @Override
//            public void onLoaderReset(Loader<Cursor> loader) {
//                Log.i("Aniket","onLoaderReset");
//                mMovieAdapter.swapCursor(null);
//            }
//        });
        MovieSyncUtils.startImmediateSync(this, MovieConstants.FETCH_POPULAR_MOVIES);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //Called when a tab is selected
        Log.i("Aniket", "onTabselected");

        int nTabSelected = tab.getPosition();
        switch (nTabSelected) {
            case 0:
                loadMovieData(MovieConstants.FETCH_POPULAR_MOVIES);
                break;
            case 1:
                loadMovieData(MovieConstants.FETCH_TOPRATED_MOVIES);
                break;
        }
        Log.i("Aniket", "DOMNEEeeeeee");
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // Called when a tab unselected.
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // Called when a tab is selected again.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }


    private void loadMovieData(String sortBy) {
        Log.i("Aniket", "Aniket1");
        MovieItem mMovieItem = new MovieItem();

//        showMovieDataView();
//        FetchMovieTask asyncTask = new FetchMovieTask(this.getApplicationContext(), new FetchMovieTask.OnMovieDataFetched() {
//            @Override
//            public void onMovieDataFetched(Vector<ContentValues> movieData) {
//                Log.i("Aniket","movieData : " + movieData) ;
//
////              mMovieItem
//                mMovieAdapter.setMovieData(movieData);
//            }
//        });
//        asyncTask.execute(sortBy);
//        mProgressBar.setVisibility(View.INVISIBLE);
//        MovieSyncUtils.startImmediateSync(this, sortBy);


    }


    @Override
    public void onClick(MovieItem movieItem) {
        Context context = this;
        Intent movieSelectedIntent = new Intent(context, MovieSelectedActivity.class);
        movieSelectedIntent.putExtra("movieItemSelected", movieItem);
        startActivity(movieSelectedIntent);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
    }

    private void setupTabs() {
        final ActionBar actionBar = getSupportActionBar();
        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.addTab(actionBar.newTab().setText("Popular").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Top Rated").setTabListener(this));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, final Bundle args) {

        switch (loaderId) {
            case ID_MOVIEDB_LOADER:
                Log.i("Ainket", "ID_MOVIEDB_LOADER");
                Uri movieDbQueryUri = MovieContract.MoviePopular.CONTENT_URI;
                Log.i("Aniket", "URI2: " + movieDbQueryUri.toString());

                String sortOrder = MovieContract.MoviePopular.COLUMN_POPULAR_INDEX + " DESC";
//                String Selection = MovieContract.MoviePopular.g

//                long normalizedUtcNow = SunshineDateUtils.normalizeDate(System.currentTimeMillis());
//                String selection = MovieContract.MoviePopular.COLUMN_POPULAR_INDEX + " >= " + 0;

//                return new AsyncTaskLoader<Cursor>(this) {
//
//                    // Initialize a Cursor, this will hold all the task data
//                    Cursor mTaskData = null;
//
//                    // onStartLoading() is called when a loader first starts loading data
//                    @Override
//                    protected void onStartLoading() {
//                        if (mTaskData != null) {
//                            // Delivers any previously loaded data immediately
//                            deliverResult(mTaskData);
//                        } else {
//                            // Force a new load
//                            forceLoad();
//                        }
//                    }
//
//                    // deliverResult sends the result of the load, a Cursor, to the registered listener
//                    public void deliverResult(Cursor data) {
//                        mTaskData = data;
//                        super.deliverResult(data);
//                    }
//
//                    @Override
//                    public Cursor loadInBackground() {
//                        return null;
//                    }
//                };
//                }
                Log.i("Ainket", "done wiyh onCreateLoader");
                return new CursorLoader(this,
                        movieDbQueryUri,
                        MOVIE_COLUMNS,
                        null,
                        null,
                        sortOrder);
            default:
                throw new RuntimeException("Loader not implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.i("Aniket", "onLoadFinished");
        mMovieAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(ID_MOVIEDB_LOADER, null, this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i("Aniket", "onLoaderReset");
        mMovieAdapter.swapCursor(null);
    }
}
