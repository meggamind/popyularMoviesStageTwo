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
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import sherpavision.app.popularmoviesstagetwo.data.MovieConstants;

/**
 * Created by aniket on 5/13/17.
 */

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    /**
     * This method returns the entire result from the HTTP response.
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setConnectTimeout(MovieConstants.URL_CONNECTION_CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(MovieConstants.URL_CONNECTION_READ_TIMEOUT);
            Log.i("NetworkUtils","url: " + urlConnection.toString());
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch(Exception e){
            return null;
        }
        finally {
            urlConnection.disconnect();
        }
    }
}