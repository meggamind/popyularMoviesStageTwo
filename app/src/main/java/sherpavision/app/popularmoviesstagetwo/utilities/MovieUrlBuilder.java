package sherpavision.app.popularmoviesstagetwo.utilities;
import java.net.URL;
import android.net.Uri;
import android.util.Log;
import java.net.MalformedURLException;

import sherpavision.app.popularmoviesstagetwo.data.MovieConstants;

/**
 * Created by aniket on 5/13/17.
 */

public class MovieUrlBuilder {
    private static final String TAG = "MovieUrlBuilder";
    public static URL buildUrl(String sortBy) {
        String fetch_base_url = MovieConstants.getBaseUrlFor(sortBy);
        Uri builtUri = Uri.parse(fetch_base_url).buildUpon()
                .appendQueryParameter(MovieConstants.API_KEY, MovieConstants.KEY)
                .appendQueryParameter("append_to_response","videos")
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrlToFetchMovieSelected(String movieId) {
        String fetch_base_url = MovieConstants.MOVIE_BASE_URL;
        Uri builtUri = Uri.parse(fetch_base_url).buildUpon()
                .appendQueryParameter(MovieConstants.API_KEY, MovieConstants.KEY)
                .appendPath(movieId)
                .appendQueryParameter("append_to_response","reviews,videos")
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }




}
