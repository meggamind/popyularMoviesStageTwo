package sherpavision.app.popularmoviesstagetwo;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import butterknife.BindView;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import android.support.v7.app.AppCompatActivity;

import java.util.Vector;

import sherpavision.app.popularmoviesstagetwo.data.MovieItem;
import sherpavision.app.popularmoviesstagetwo.data.MovieConstants;
import sherpavision.app.popularmoviesstagetwo.utilities.FetchMovieTask;

/**
 * Created by aniket on 5/13/17.
 */

public class MovieSelectedActivity extends AppCompatActivity {
    @BindView(R2.id.movie_thumbnail) ImageView mMovieThumbnail;
    @BindView(R2.id.trailer_image) ViewPager mBackdrop;
//    @BindView(R.id.favorite) ImageView mFavorite;

    @BindView(R2.id.overview) TextView mOverView;
    @BindView(R.id.vote_average) TextView mVote_average;
    @BindView(R2.id.original_title) TextView mOriginal_title;

    private static final String TAG = "popularmoviesstagetwo";
    private CustomPagerAdapter mCustomPagerAdapter;
    private String summaryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_selected);
        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();
        MovieItem mMovieItem = (MovieItem) intentThatStartedThisActivity.getParcelableExtra("movieItemSelected");

        Log.i(TAG, "Aniket here id: " + mMovieItem.getMovieId());
        mCustomPagerAdapter = new CustomPagerAdapter(this);

        mBackdrop.setAdapter(mCustomPagerAdapter);

        Picasso.with(getApplicationContext())
                .load("http://image.tmdb.org/t/p/w185/" + mMovieItem.getMovie_poster())
                .error(R.drawable.ic_no_wifi)
                .placeholder(R.drawable.ic_loading)
                .into(mMovieThumbnail);

        summaryText = mMovieItem.getMovie_overview();
        Log.i(TAG, "Aniket here getMovie_overview: " + summaryText);

        if (summaryText != null) {
            mOverView.setText(summaryText);
        }

        String releaseDateText = " (" + mMovieItem.getMovie_releaseDate() + ")";
        String originalTitleText = mMovieItem.getOriginalTitle();

        SpannableString ss1=  new SpannableString(originalTitleText);
        ss1.setSpan(new RelativeSizeSpan(1f), 0, originalTitleText.length(), 0); // set size
        SpannableString ss2=  new SpannableString(releaseDateText);
        ss2.setSpan(new RelativeSizeSpan(0.6f), 0, releaseDateText.length(), 0); // set size
        ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorReleaseDate)), 0, releaseDateText.length(), 0);// set color


        if (originalTitleText != null && releaseDateText!=null) {
            mOriginal_title.setText(ss1);
            mOriginal_title.append(ss2);
        }

        Log.i("Aniket", "mMovieItem.getMovie_vote():" + mMovieItem.getMovie_vote());
        mVote_average.setText(String.valueOf(mMovieItem.getMovie_vote()));

        String outOfVoteString = " /10";
        SpannableString outOfVote =  new SpannableString(outOfVoteString);
        outOfVote.setSpan(new RelativeSizeSpan(0.6f), 0, outOfVoteString.length(), 0); // set size
        outOfVote.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorReleaseDate)), 0, outOfVoteString.length(), 0);// set color
        mVote_average.append(outOfVote);

        Log.i(TAG,"Aniket, movieId: " + mMovieItem.getMovieId());
        loadMovieData(mMovieItem.getMovieId());
    }

    public void onFavorited(View view){
        view.setSelected(!view.isSelected());
        Log.i("Aniket","clicked: " + view.isSelected());
    }

    public void onSummaryClicked(View view){
        Intent summaryIntent = new Intent(this, SummaryFullActivity.class);
        summaryIntent.putExtra("movieSummary",summaryText);
        startActivity(summaryIntent);
    }

    private void loadMovieData(int movieId){
        FetchMovieTask asyncTask = new FetchMovieTask(this.getApplicationContext(), new FetchMovieTask.OnMovieDataFetched() {
            @Override
            public void onMovieDataFetched(Vector<ContentValues> movieData) {
                Log.i(TAG, "Aniket onMovieDataFetched");
                mCustomPagerAdapter.setMovieDetails(movieData);
//                setReviews();
            }
        });
        asyncTask.execute(MovieConstants.FETCH_MOVIE_SELECTED_DETAILS, String.valueOf(movieId));
    }

//    private void setReviews(){
//        for(int i =0; i<)
//
//
//        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View reviewItem = vi.inflate(R.layout.review_item, null);
//
//        // fill in any details dynamically here
//        TextView textView = (TextView) reviewItem.findViewById(R.id.review_author);
//        textView.setText("your text");
//
//        TextView textView1 = (TextView) reviewItem.findViewById(R.id.review_content);
//        textView1.setText("your text21212");
//
//        // insert into main view
//        ViewGroup insertReview = (ViewGroup) findViewById(R.id.reviews);
////        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) reviewItem.getLayoutParams();
////        params.addRule(RelativeLayout.ALIGN_TOP,R.id.review_title);
//
//        insertReview.addView(reviewItem, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//
//    }


}
