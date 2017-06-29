package sherpavision.app.popularmoviesstagetwo;

import android.net.Uri;
import android.view.View;
import android.os.Bundle;

import butterknife.BindView;

import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.widget.TextView;

import butterknife.ButterKnife;

import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.GregorianCalendar;

import android.widget.ToggleButton;
import android.text.SpannableString;

import com.squareup.picasso.Picasso;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.support.v4.view.ViewPager;
import android.text.style.RelativeSizeSpan;
import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import sherpavision.app.popularmoviesstagetwo.data.MovieContract;
import sherpavision.app.popularmoviesstagetwo.data.MovieConstants;

/**
 * Created by aniket on 5/13/17.
 */

public class MovieSelectedActivity extends AppCompatActivity implements MovieSelectedReviewAdapter.MovieSelectedReviewOnClickHandler {
    @BindView(R2.id.movie_thumbnail)
    ImageView mMovieThumbnail;
    @BindView(R2.id.main_trailer_pager_view)
    ViewPager mBackdrop;
    @BindView(R.id.favorite)
    ToggleButton mFavorite;
    @BindView(R2.id.overview)
    TextView mOverView;
    @BindView(R.id.vote_average)
    TextView mVote_average;
    @BindView(R2.id.original_title)
    TextView mOriginal_title;
    @BindView(R.id.recyclerview_reviews)
    RecyclerView mReviews;


    private int movieId;

    private static final String TAG = "popularmoviesstagetwo";

    private String summaryText;
    private RecyclerView mRecyclerView;
    private Uri mSelectedMovieQueryUri;
    private Cursor mMovieSelectedCursor;
    public static ProgressBar mProgressBar;
    private CustomPagerAdapter mCustomPagerAdapter;
    private MovieSelectedReviewAdapter mMovieSelectedAdapter;
    private GregorianCalendar mCalendar = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_selected);
        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();

        movieId = intentThatStartedThisActivity.getIntExtra("movieId", 0);
        String tabMovieBelongTo = intentThatStartedThisActivity.getStringExtra("currentTab");

        if (tabMovieBelongTo.equals(MovieConstants.FETCH_POPULAR_MOVIES)) {
            mSelectedMovieQueryUri = MovieContract.MoviePopular.CONTENT_URI;
        } else if (tabMovieBelongTo.equals(MovieConstants.FETCH_TOPRATED_MOVIES)) {
            mSelectedMovieQueryUri = MovieContract.MovieTopRated.CONTENT_URI;
        } else {
            mSelectedMovieQueryUri = MovieContract.MovieFavorite.CONTENT_URI;
        }

        mSelectedMovieQueryUri = mSelectedMovieQueryUri.buildUpon().appendPath(String.valueOf(movieId)).build();

        mMovieSelectedCursor = getContentResolver().query(
                mSelectedMovieQueryUri,
                null,
                null,
                null,
                null
        );
        mMovieSelectedCursor.moveToFirst();
        String poster = mMovieSelectedCursor.getString(MovieGridActivity.COL_POSTER_PATH);
        summaryText = mMovieSelectedCursor.getString(MovieGridActivity.COL_OVERVIEW);

        mCustomPagerAdapter = new CustomPagerAdapter(this);
        mBackdrop.setAdapter(mCustomPagerAdapter);

        Picasso.with(getApplicationContext())
                .load("http://image.tmdb.org/t/p/w185/" + poster)
                .error(R.drawable.ic_no_wifi)
                .placeholder(R.drawable.ic_loading)
                .into(mMovieThumbnail);

        if (summaryText != null) {
            mOverView.setText(summaryText);
        }

        String releaseDateText = " (" + mMovieSelectedCursor.getString(MovieGridActivity.COL_RELEASE_DATE) + ")";
        String originalTitleText = mMovieSelectedCursor.getString(MovieGridActivity.COL_ORIGINAL_TITLE);

        SpannableString ss1 = new SpannableString(originalTitleText);
        ss1.setSpan(new RelativeSizeSpan(1f), 0, originalTitleText.length(), 0); // set size
        SpannableString ss2 = new SpannableString(releaseDateText);
        ss2.setSpan(new RelativeSizeSpan(0.6f), 0, releaseDateText.length(), 0); // set size
        ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorReleaseDate)), 0, releaseDateText.length(), 0);// set color


        if (originalTitleText != null && releaseDateText != null) {
            mOriginal_title.setText(ss1);
            mOriginal_title.append(ss2);
        }

        mVote_average.setText(mMovieSelectedCursor.getString(MovieGridActivity.COL_VOTE_AVERAGE));

        String outOfVoteString = " /10";
        SpannableString outOfVote = new SpannableString(outOfVoteString);
        outOfVote.setSpan(new RelativeSizeSpan(0.6f), 0, outOfVoteString.length(), 0); // set size
        outOfVote.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorReleaseDate)), 0, outOfVoteString.length(), 0);// set color
        mVote_average.append(outOfVote);

        mProgressBar = (ProgressBar) findViewById(R.id.movie_selected_pb_loading_indicator);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_reviews);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMovieSelectedAdapter = new MovieSelectedReviewAdapter(getApplicationContext(), this);
        mRecyclerView.setAdapter(mMovieSelectedAdapter);
        loadMovieData(mMovieSelectedCursor);
        setFavImage();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onFavorited(View view) {
        view.setSelected(!view.isSelected());
        ContentValues favoriteValue = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(mMovieSelectedCursor, favoriteValue);
        Uri favMovieWithId = MovieContract.MovieFavorite.buildSelectedFavoriteMovieUri(movieId);

        Cursor mCursor = getContentResolver()
                .query(favMovieWithId,
                        null,
                        null,
                        null,
                        null,
                        null
                );
        if (mCursor.getCount() <= 0) {
            favoriteValue.remove(MovieContract.CommonMovieCol.COLUMN_FAVORITE_TIMESTAMP);
            favoriteValue.put(MovieContract.CommonMovieCol.COLUMN_FAVORITE_TIMESTAMP, mCalendar.getTimeInMillis());
            getApplicationContext().getContentResolver().
                    insert(MovieContract.MovieFavorite.CONTENT_URI,
                            favoriteValue);
        } else {
            getApplicationContext().getContentResolver()
                    .delete(favMovieWithId, null, null);
        }
        setFavImage();
    }

    public void onSummaryClicked(View view) {
        Intent summaryIntent = new Intent(this, SummaryFullActivity.class);
        summaryIntent.putExtra("movieSummary", summaryText);
        startActivity(summaryIntent);
    }

    private void loadMovieData(Cursor mCursor) {
        String[] author = mCursor.getString(MovieGridActivity.COL_REVIEW_NAME).split(MovieConstants.MOVIEDB_DELIMINATOR);
        for (int i = 0; i < author.length; i++) {
            String[] movieData = {
                    mCursor.getString(MovieGridActivity.COL_TRAILER),
                    mCursor.getString(MovieGridActivity.COL_REVIEW_NAME),
                    mCursor.getString(MovieGridActivity.COL_REVIEW)
            };
            mCustomPagerAdapter.setMovieDetails(movieData);
            mMovieSelectedAdapter.setMovieData(
                    mCursor.getString(MovieGridActivity.COL_REVIEW_NAME).split(MovieConstants.MOVIEDB_DELIMINATOR),
                    mCursor.getString(MovieGridActivity.COL_REVIEW).split(MovieConstants.MOVIEDB_DELIMINATOR));

        }
    }


    @Override
    public void onClick(String reviewAuthor, String reviewContent) {
        Context context = this;
        Intent movieSelectedIntent = new Intent(context, MovieReview.class);
        movieSelectedIntent.putExtra("reviewAuthor", reviewAuthor);
        movieSelectedIntent.putExtra("reviewContent", reviewContent);
        startActivity(movieSelectedIntent);
    }

    private void setFavImage() {
        Uri movieSelectedFavUri = MovieContract.MovieFavorite.buildSelectedFavoriteMovieUri(movieId);
        Cursor mCursor = getApplicationContext().getContentResolver().
                query(movieSelectedFavUri,
                        null,
                        null,
                        null,
                        null,
                        null
                );
        if (mCursor.getCount() <= 0) {
            mFavorite.setBackgroundResource(R.drawable.ic_favorite);
        } else {
            mFavorite.setBackgroundResource(R.drawable.ic_favorited);
        }
    }
}
