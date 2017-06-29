package sherpavision.app.popularmoviesstagetwo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
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

import java.util.Collections;
import java.util.Vector;

import sherpavision.app.popularmoviesstagetwo.data.MovieConstants;
import sherpavision.app.popularmoviesstagetwo.data.MovieContract;
import sherpavision.app.popularmoviesstagetwo.data.MovieItem;

/**
 * Created by aniket on 5/13/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private final MovieAdapterOnClickHandler mClickHandler;
    private Vector<MovieItem> mMovieItems = new Vector<MovieItem>();

    public interface MovieAdapterOnClickHandler {
        void onClick(int movieId);
    }

    // Add custom onClickListener as a parameter to the constructor
    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
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
            MovieItem movieItem = mMovieItems.get(adapterPosition);
            mClickHandler.onClick(movieItem.getMovieId());
        }
    }

    /*  This gets called when each new ViewHolder is created. This happens when the RecyclerView
     *  is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
    */

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        Picasso.with(mContext)
                .load("http://image.tmdb.org/t/p/w185/" + mMovieItems.get(position).getMovie_poster())
                .error(R.drawable.ic_no_wifi)
                .placeholder(R.drawable.ic_loading)
                .into(movieAdapterViewHolder.mPosterImage);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieItems) {
            return 0;
        }

        return mMovieItems.size();
    }

    void swapCursor(Cursor newCursor, String tabSelected) {
        mCursor = newCursor;
        setMovieData(mCursor, tabSelected);
        notifyDataSetChanged();
    }

    public void setMovieData(Cursor mCursor, String tabSelected) {
        mMovieItems.clear();
        for (int i = 0; i < mCursor.getCount(); i++) {
            mCursor.moveToPosition(i);
            mMovieItems.add(new MovieItem());
            mMovieItems.get(i).setMovie_poster(mCursor.getString(MovieGridActivity.COL_POSTER_PATH));
            mMovieItems.get(i).setMovieId(mCursor.getInt(MovieGridActivity.COL_MOVIE_ID));
        }
        mCursor.close();
        notifyDataSetChanged();
    }
}
