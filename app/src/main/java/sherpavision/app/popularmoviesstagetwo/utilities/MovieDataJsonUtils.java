/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sherpavision.app.popularmoviesstagetwo.utilities;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Context;
import android.content.ContentValues;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;

import sherpavision.app.popularmoviesstagetwo.data.MovieConstants;
import sherpavision.app.popularmoviesstagetwo.data.MovieContract;

/**
 * Created by aniket on 5/13/17.
 */

public final class MovieDataJsonUtils {
    public static Context mContext = null;

    public MovieDataJsonUtils(Context context) {
        mContext = context;
    }
    public static Vector<ContentValues> getSimpleMovieDataStringsFromJson(String movieListJsonStr, String whatToDo)
            throws JSONException {
        switch(whatToDo){
            case MovieConstants.FETCH_POPULAR_MOVIES:
                return fetchMovieListDetails(movieListJsonStr);
            case MovieConstants.FETCH_TOPRATED_MOVIES:
                return fetchMovieListDetails(movieListJsonStr);
            case MovieConstants.FETCH_MOVIE_SELECTED_DETAILS:
                return fetchMovieSelectedDetails(movieListJsonStr);
        }
        return null;
    }

    private static Vector<ContentValues> fetchMovieListDetails(String movieListJsonStr){
        Vector<ContentValues> cVVector  = new Vector<ContentValues>();
        try {
            JSONObject movieDataJson = new JSONObject(movieListJsonStr);
            JSONArray movieListArray = movieDataJson.getJSONArray(MovieConstants.MOVIEDB_RESULTS);

            cVVector = new Vector<ContentValues>(movieListArray.length());

            for (int i = 0; i < movieListArray.length(); i++) {
                int id;
                String overview;
                String posterPath;
                String backdropPath;
                String releaseDate;
                String popularity;
                String title;
                String originalTitle;
                String voteAverage;
                String voteCount;
                String reviewAuthor;
                String reviewContent;

                JSONObject movieInfo = movieListArray.getJSONObject(i);

                overview = movieInfo.getString(MovieConstants.MOVIEDB_OVERVIEW);
                posterPath = movieInfo.getString(MovieConstants.MOVIEDB_POSTER_PATH);
                backdropPath = movieInfo.getString(MovieConstants.MOVIEDB_BACKDROP_PATH);
                releaseDate = movieInfo.getString(MovieConstants.MOVIEDB_RELEASE_DATE);
                popularity = movieInfo.getString(MovieConstants.MOVIEDB_POPULARITY);
                title = movieInfo.getString(MovieConstants.MOVIEDB_TITLE);
                voteAverage = movieInfo.getString(MovieConstants.MOVIEDB_VOTE_AVERAGE);
                originalTitle = movieInfo.getString(MovieConstants.MOVIEDB_ORIGINAL_TITLE);
                id = movieInfo.getInt(MovieConstants.MOVIEDB_ID);
                ContentValues movieValues = new ContentValues();
                movieValues.put(MovieContract.MoviePopular.COLUMN_MOVIE_ID, id);
                movieValues.put(MovieContract.MoviePopular.COLUMN_OVERVIEW, overview);
                movieValues.put(MovieContract.MoviePopular.COLUMN_POSTER_PATH, posterPath);
                movieValues.put(MovieContract.MoviePopular.COLUMN_RELEASE_DATE, releaseDate);
                movieValues.put(MovieContract.MoviePopular.COLUMN_TITLE, title);
                movieValues.put(MovieContract.MoviePopular.COLUMN_ORIGINAL_TITLE, originalTitle);
                movieValues.put(MovieContract.MoviePopular.COLUMN_VOTE_AVERAGE, voteAverage);
                movieValues.put(MovieContract.MoviePopular.COLUMN_POPULAR_INDEX, popularity);

                cVVector.add(movieValues);
            }

            } catch (JSONException e) {
            e.printStackTrace();
        }
        return cVVector;
    }




    private static Vector<ContentValues> fetchMovieSelectedDetails(String movieListJsonStr){
        Vector<ContentValues> cVVector  = new Vector<ContentValues>();
        try {
            JSONObject movieDataJson = new JSONObject(movieListJsonStr);
            JSONObject videoObject = movieDataJson.getJSONObject("videos");
            JSONArray movieTrailers = videoObject.getJSONArray(MovieConstants.MOVIEDB_RESULTS);
            JSONObject reviewObject = movieDataJson.getJSONObject(MovieConstants.MOVIEDB_REVIEWS);

            JSONArray movieReviewArray = reviewObject.getJSONArray("results");
            cVVector = new Vector<ContentValues>(movieTrailers.length()+ movieReviewArray.length());

            ContentValues movieValues = new ContentValues();
            Vector<String> trailer = new Vector<String>();
            Vector<String> authors = new Vector<String>();
            Vector<String> reviews = new Vector<String>();
            int k =0;
            StringBuilder trailerBuilder = new StringBuilder();
            StringBuilder movieReviewBuilder = new StringBuilder();
            StringBuilder movieReviewAuthorBuilder = new StringBuilder();
            StringBuilder movieReviewContentBuilder = new StringBuilder();

            for (int i = 0; i < movieTrailers.length(); i++) {
                String type;
                String videoKey;

                JSONObject movieVideo = movieTrailers.getJSONObject(i);

                type = movieVideo.getString(MovieConstants.MOVIEDB_VIDEO_TYPE);
                if(MovieConstants.MOVIEDB_TRAILER_TYPE.equals(type)){
                    videoKey = movieVideo.getString(MovieConstants.MOVIEDB_MOVIE_KEY);
                    trailer.add(videoKey);
                    trailerBuilder.append(videoKey);
                    trailerBuilder.append(MovieConstants.MOVIEDB_DELIMINATOR);
                }
            }
            movieValues.put(MovieConstants.MOVIEDB_MOVIE_KEY, trailerBuilder.toString());
            for (int j = 0; j < movieReviewArray.length(); j++) {
                JSONObject movieReview = movieReviewArray.getJSONObject(j);
                authors.add(movieReview.getString("author"));
                movieReviewAuthorBuilder.append(movieReview.getString("author"));
                movieReviewAuthorBuilder.append(MovieConstants.MOVIEDB_DELIMINATOR);
                reviews.add(movieReview.getString("content"));
                movieReviewContentBuilder.append(movieReview.getString("content"));
                movieReviewContentBuilder.append(MovieConstants.MOVIEDB_DELIMINATOR);
            }
            movieValues.put("author", movieReviewAuthorBuilder.toString());
            movieValues.put("content", movieReviewContentBuilder.toString());

            cVVector.add(movieValues);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cVVector;
    }


    private static byte[] convertVectorToByteAsrray(Vector<String> vector){
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            final ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(vector.toArray(new String[vector.size()]));
            objectOutputStream.flush();
            objectOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        final byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }
}