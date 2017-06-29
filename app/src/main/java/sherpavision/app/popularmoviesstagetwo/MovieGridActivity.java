package sherpavision.app.popularmoviesstagetwo;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.view.MenuItem;
import android.content.Intent;
import android.content.Context;
import android.widget.ImageView;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import sherpavision.app.popularmoviesstagetwo.data.MovieContract;
import sherpavision.app.popularmoviesstagetwo.data.MovieItem;
import sherpavision.app.popularmoviesstagetwo.data.MovieConstants;
import sherpavision.app.popularmoviesstagetwo.sync.MovieSyncUtils;

/**
 * Created by aniket on 5/13/17.
 */

public class MovieGridActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>,
        ActionBar.TabListener {

    @BindView(R2.id.recyclerview_movie_list)
    RecyclerView mRecyclerView;
    @BindView(R2.id.tv_error_message_display)
    TextView mErrorMessageDisplay;
    @BindView(R2.id.pb_loading_indicator)
    ProgressBar mProgressBar;


    private MovieAdapter mMovieAdapter;
    private int mPosition = RecyclerView.NO_POSITION;
    private static final int TAB_POPULAR_MOVIES = 0;
    private static final int TAB_TOPRATED_MOVIES = 1;
    private static final int TAB_FAVORITE_MOVIES = 2;

    private static final int ID_MOVIEDB_POPULAR = 44;
    private static final int ID_MOVIEDB_TOP_RATED = 45;
    private static final int ID_MOVIEDB_FAVORITE = 46;
    private String mTab = null;


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
            MovieContract.MoviePopular.COLUMN_FAVORITE_TIMESTAMP,
            MovieContract.MoviePopular.COLUMN_REVIEW,
            MovieContract.MoviePopular.COLUMN_REVIEW_NAME
    };


    private int mTabSelected =0;
    public static final int COL_CURSOR_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_POPULAR_INDEX = 2;
    public static final int COL_TOP_RATED_INDEX = 3;
    public static final int COL_OVERVIEW = 4;
    public static final int COL_POSTER_PATH = 5;
    public static final int COL_RELEASE_DATE = 6;
    public static final int COL_TITLE = 7;
    public static final int COL_ORIGINAL_TITLE = 8;
    public static final int COL_VOTE_AVERAGE = 9;
    public static final int COL_TRAILER = 10;
    public static final int COL_FAVORITE_TIMESTAMP = 11;
    public static final int COL_REVIEW = 12;
    public static final int COL_REVIEW_NAME = 13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);
        ButterKnife.bind(this);

        // Now we can setup the adapter of the RecyclerView and toggle the visibility.

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
        showLoading();
        setupTabs();
        MovieSyncUtils.startImmediateSync(this, MovieConstants.FETCH_POPULAR_MOVIES);
        MovieSyncUtils.startImmediateSync(this, MovieConstants.FETCH_TOPRATED_MOVIES);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //Called when a tab is selected
        mTabSelected = tab.getPosition();
        restartLoader(mTabSelected);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(int movieId) {
        Context context = this;
        Intent movieSelectedIntent = new Intent(context, MovieSelectedActivity.class);
        movieSelectedIntent.putExtra("movieId", movieId);
        movieSelectedIntent.putExtra("currentTab", mTab);
        startActivity(movieSelectedIntent);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
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
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.addTab(actionBar.newTab().setText("Popular").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Top Rated").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(MovieContract.MovieFavorite.TABLE_NAME).setTabListener(this));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, final Bundle args) {
        Uri movieDbQueryUri;
        String sortOrder;
        showLoading();
        switch (loaderId) {
            case ID_MOVIEDB_POPULAR:
                movieDbQueryUri = MovieContract.MoviePopular.CONTENT_URI;
                sortOrder = MovieContract.MoviePopular.COLUMN_POPULAR_INDEX + " DESC";
                return new CursorLoader(this,
                        movieDbQueryUri,
                        MOVIE_COLUMNS,
                        null,
                        null,
                        sortOrder);
            case ID_MOVIEDB_TOP_RATED:
                movieDbQueryUri = MovieContract.MovieTopRated.CONTENT_URI;
                sortOrder = MovieContract.MoviePopular.COLUMN_TOP_RATED_INDEX + " DESC";
                return new CursorLoader(this,
                        movieDbQueryUri,
                        MOVIE_COLUMNS,
                        null,
                        null,
                        sortOrder);
            case ID_MOVIEDB_FAVORITE:
                movieDbQueryUri = MovieContract.MovieFavorite.CONTENT_URI;
                sortOrder = MovieContract.CommonMovieCol.COLUMN_FAVORITE_TIMESTAMP + " DESC";
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
            if (data != null && data.getCount() != 0) {
                showMovieDataView();
                mMovieAdapter.swapCursor(data, mTab);
            }
            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            mRecyclerView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    protected void onResume(){
        super.onResume();
        showMovieDataView();
        restartLoader(mTabSelected);
    }

    void  restartLoader(int nTabSelected){
        switch (nTabSelected) {
            case TAB_POPULAR_MOVIES:
                mTab = MovieConstants.FETCH_POPULAR_MOVIES;
                getSupportLoaderManager().restartLoader(ID_MOVIEDB_POPULAR, null, this);
                break;
            case TAB_TOPRATED_MOVIES:
                mTab = MovieConstants.FETCH_TOPRATED_MOVIES;
                getSupportLoaderManager().restartLoader(ID_MOVIEDB_TOP_RATED, null, this);
                break;
            case TAB_FAVORITE_MOVIES:
                mTab = MovieContract.MovieFavorite.TABLE_NAME;
                getSupportLoaderManager().restartLoader(ID_MOVIEDB_FAVORITE, null, this);
                break;
        }
    }
}
