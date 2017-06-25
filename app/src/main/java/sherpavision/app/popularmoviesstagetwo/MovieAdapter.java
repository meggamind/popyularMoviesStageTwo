package sherpavision.app.popularmoviesstagetwo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.Vector;

import sherpavision.app.popularmoviesstagetwo.data.MovieConstants;
import sherpavision.app.popularmoviesstagetwo.data.MovieContract;
import sherpavision.app.popularmoviesstagetwo.data.MovieItem;

/**
 * Created by aniket on 5/13/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private MovieItem[] mMovieItems;
    private Context mContext;
    private int mScreenHeight;
    private int mScreenWidth;
    private Cursor mCursor;

    private final MovieAdapterOnClickHandler mClickHandler;




    public interface MovieAdapterOnClickHandler {
        void onClick(MovieItem movieSelected);
    }

    // Add custom onClickListener as a parameter to the constructor
    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        Log.i("AniketAdaptor", "in MovieAdapterViewHolder");
        mClickHandler = clickHandler;
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mPosterImage;
        private final LinearLayout mMovieItemLayout;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mPosterImage = (ImageView) view.findViewById(R.id.movie_poster);
            mMovieItemLayout = (LinearLayout) view.findViewById(R.id.movie_item);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieItem movieItem = mMovieItems[adapterPosition];
            mClickHandler.onClick(movieItem);
        }
    }

    /*  This gets called when each new ViewHolder is created. This happens when the RecyclerView
     *  is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
    */

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.i("AniketAdaptor", "in onCreateViewHolder");
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);

        Log.i("AniketAdaptor", mCursor.getString(MovieGridActivity.COL_CURSOR_ID));
        Picasso.with(mContext)
                .load("http://image.tmdb.org/t/p/w185/" + mMovieItems[position].getMovie_poster())
                .error(R.drawable.ic_no_wifi)
                .placeholder(R.drawable.ic_loading)
                .into(movieAdapterViewHolder.mPosterImage);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) {
            Log.i("AniketAdaptor", "mCursor is null");
            return 0;
        }
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

//    public void setMovieData(Vector<ContentValues> movieItems){
//        Log.i("Aniket", "Setting movie data! ");
//        mMovieItems = new MovieItem[movieItems.size()];
//        for(int i=0; i < movieItems.size();i++){
//            mMovieItems[i] = new MovieItem();
//            mMovieItems[i].setOriginalTitle(movieItems.get(i).getAsString(MovieConstants.MOVIEDB_ORIGINAL_TITLE));
//            mMovieItems[i].setTitle(movieItems.get(i).getAsString(MovieConstants.MOVIEDB_TITLE));
//            mMovieItems[i].setMovie_poster(movieItems.get(i).getAsString(MovieConstants.MOVIEDB_POSTER_PATH));
//            mMovieItems[i].setMovie_backdrop(movieItems.get(i).getAsString(MovieConstants.MOVIEDB_BACKDROP_PATH));
//            mMovieItems[i].setMovie_overview(movieItems.get(i).getAsString(MovieConstants.MOVIEDB_OVERVIEW));
//            mMovieItems[i].setMovie_vote(movieItems.get(i).getAsFloat(MovieConstants.MOVIEDB_VOTE_AVERAGE));
//            mMovieItems[i].setMovie_releaseDate(movieItems.get(i).getAsString(MovieConstants.MOVIEDB_RELEASE_DATE));
//            mMovieItems[i].setMovieId(movieItems.get(i).getAsInteger(MovieConstants.MOVIEDB_ID));
//        }
//        notifyDataSetChanged();
//    }

}
