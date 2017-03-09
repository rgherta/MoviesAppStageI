package com.github.moviesappstagei.moviesappstagei.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static com.github.moviesappstagei.moviesappstagei.MovieGallery.MDB_KEY;
import static com.github.moviesappstagei.moviesappstagei.MovieGallery.MOVIES_BASE_URL;
import static com.github.moviesappstagei.moviesappstagei.MovieGallery.QUERY_PARAM;

/**
 * Created by RGHERTA on 2/20/2017.
 */

public class NetworkUtils {


    public static URL buildUrl(String apiType) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(apiType)
                .appendQueryParameter(QUERY_PARAM, MDB_KEY)
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}
