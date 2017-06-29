package sherpavision.app.popularmoviesstagetwo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Vector;

import butterknife.BindView;
import sherpavision.app.popularmoviesstagetwo.data.MovieConstants;
import sherpavision.app.popularmoviesstagetwo.data.MovieContract;
import sherpavision.app.popularmoviesstagetwo.data.MovieItem;

/**
 * Created by aniket on 6/17/17.
 */



public class CustomPagerAdapter extends PagerAdapter{
    Context mContext;
    private MovieItem mMovieItem;
    LayoutInflater mLayoutInflater;
    String[] mTrailerArray;
    String mMovieId;



    public CustomPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(mTrailerArray == null) return 0;
        return mTrailerArray.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView trailerImageView = (ImageView) itemView.findViewById(R.id.trialer_image_view);
        Button playButton = (Button) itemView.findViewById(R.id.play_button);

        Picasso.with(mContext)
                .load("http://img.youtube.com/vi/" + mTrailerArray[position] + "/mqdefault.jpg")
                .error(R.drawable.ic_no_wifi)
                .placeholder(R.drawable.ic_loading)
                .into(trailerImageView);
        container.addView(itemView);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieConstants.watchYoutubeVideo(mTrailerArray[position],mContext);
            }
        });


        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }

    void setMovieDetails(String[] movieDetail) {
        try {
            mTrailerArray = movieDetail[0].split(MovieConstants.MOVIEDB_DELIMINATOR);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onTrailerClicked(View view){
    }

    public void swapCursor(Cursor mCursor){

    }
}
