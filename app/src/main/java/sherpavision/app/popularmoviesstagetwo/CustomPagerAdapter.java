package sherpavision.app.popularmoviesstagetwo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import sherpavision.app.popularmoviesstagetwo.data.MovieItem;

/**
 * Created by aniket on 6/17/17.
 */



public class CustomPagerAdapter extends PagerAdapter{
    Context mContext;
    private MovieItem mMovieItem;
    LayoutInflater mLayoutInflater;


    public CustomPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(mMovieItem == null) return 0;
        return mMovieItem.getMovieTrailers().size();
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
//        @BindView(R.id.trialer_image_view) ImageView trailerImageView;

        Picasso.with(mContext)
                .load("http://img.youtube.com/vi/" + mMovieItem.getMovieTrailer(position) + "/mqdefault.jpg")
                .error(R.drawable.ic_no_wifi)
                .placeholder(R.drawable.ic_loading)
                .into(trailerImageView);
        container.addView(itemView);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Aniket", "here in new onClick: " + String.valueOf(position));
                String tarilerKey = mMovieItem.getMovieTrailer(position);
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" +tarilerKey ));
                mContext.startActivity(youtubeIntent);
            }
        });


        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }

    void setMovieDetails(Vector<ContentValues> movieDetail) {
        try {
            Log.i("Aniket", "Setting movie data! ");
            mMovieItem = new MovieItem();
            String[] trailerArray = convertByteToStringArray(movieDetail.get(0).getAsByteArray("key"));
            String[] authorsArray = convertByteToStringArray(movieDetail.get(1).getAsByteArray("author"));
            String[] reviewsArray = convertByteToStringArray(movieDetail.get(2).getAsByteArray("review"));


            for (String trailer : trailerArray) {
                Log.i("aniket12", "trailer: " + trailer);
                mMovieItem.addMovieTrailer(trailer);
            }

            for (String author : authorsArray) {
                Log.i("aniket12", "trailer: " + author);
                mMovieItem.addMovieReviewAuthor(author);
            }

            for (String review : reviewsArray) {
                Log.i("aniket12", "trailer: " + review);
                mMovieItem.addMovieReviews(review);
            }

            //        movieDetail.get(1).getAsString("key");
            //        for(int i=0; i < movieDetail.size();i++){
            //            mMovieItem.addMovieTrailer(movieDetail.get(i).getAsString("key"));
            //        }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onTrailerClicked(View view){
        Log.i("Aniket","Clicked");
//        view.getResources().
    }



    public String[] convertByteToStringArray(byte[] byteArrayToConvert) {
        try {
            final ByteArrayInputStream byteArrayInputStream =
//                    new ByteArrayInputStream(movieDetail.get(0).getAsByteArray("key"));
                    new ByteArrayInputStream(byteArrayToConvert);

            final ObjectInputStream objectInputStream =
                    new ObjectInputStream(byteArrayInputStream);

            String[] trailerArray = (String[]) objectInputStream.readObject();

            objectInputStream.close();
            return trailerArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
