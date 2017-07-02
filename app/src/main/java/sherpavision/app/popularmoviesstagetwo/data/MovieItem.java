package sherpavision.app.popularmoviesstagetwo.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.method.MovementMethod;
import android.util.Log;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by aniket on 5/14/17.
 */

public class MovieItem implements Parcelable {
    private String title;
    private String moviePoster;
    private String movieBackdrop;
    private String movieOverview;
    private float voteAverage;
    private String releaseDate;
    private String originalTitle;
    private int mMovieId;
    private Vector<String> mMovieTrailer;
    private Vector<String> mMovieReviewAuthors;
    private Vector<String> mMovieReviews;


    public MovieItem(Parcel in) {
        title = in.readString();
        moviePoster = in.readString();
        movieBackdrop = in.readString();
        movieOverview = in.readString();
        voteAverage = in.readFloat();
        releaseDate = in.readString();
        originalTitle = in.readString();
        mMovieId = in.readInt();
    }

    public MovieItem(){
        mMovieTrailer = new Vector<String>();
        mMovieReviewAuthors = new Vector<String>();
        mMovieReviews = new Vector<String>();
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(moviePoster);
        dest.writeString(movieBackdrop);
        dest.writeString(movieOverview);
        dest.writeFloat(voteAverage);
        dest.writeString(releaseDate);
        dest.writeString(originalTitle);
        dest.writeInt(mMovieId);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MovieItem> CREATOR
            = new Parcelable.Creator<MovieItem>() {

        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };


    public String getTitle(){return title;}
    public void setTitle(String title){
        this.title = title;
    }

    public String getMovie_poster(){
        return moviePoster;
    }
    public void setMovie_poster(String moviePoster){
        this.moviePoster = moviePoster;
    }

    public String getMovie_overview(){
        return movieOverview;
    }
    public void setMovie_overview(String movieOverview){
        this.movieOverview = movieOverview;
    }

    public float getMovie_vote(){
        return voteAverage;
    }
    public void setMovie_vote(float voteAverage){
        this.voteAverage = voteAverage;
    }

    public String getMovie_releaseDate(){
        return releaseDate;
    }
    public void setMovie_releaseDate(String releaseDate){
        this.releaseDate = releaseDate;
    }

    public String getMovie_backdrop(){
        return movieBackdrop;
    }
    public void setMovie_backdrop(String movieBackdrop){
        this.movieBackdrop = movieBackdrop;
    }

    public String getOriginalTitle(){return originalTitle;}
    public void setOriginalTitle(String originalTitle){
        this.originalTitle = originalTitle;
    }

    public int getMovieId(){return mMovieId;}
    public void setMovieId(int movieId){this.mMovieId = movieId;}

    public String getMovieTrailer(int trailerIndex){return this.mMovieTrailer.get(trailerIndex);}
    public Vector<String> getMovieTrailers(){return this.mMovieTrailer;}

    public String getMovieReviewAuthor(int trailerIndex){return this.mMovieReviewAuthors.get(trailerIndex);}
    public Vector<String> getMovieReviewAuthors(){return this.mMovieReviewAuthors;}

    public String getMovieReview(int trailerIndex){return this.mMovieReviews.get(trailerIndex);}
    public Vector<String> getMovieReviews(){return this.mMovieReviews;}
}
