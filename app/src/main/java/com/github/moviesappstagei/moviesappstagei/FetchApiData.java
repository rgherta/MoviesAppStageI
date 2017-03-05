package com.github.moviesappstagei.moviesappstagei;

import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by RGHERTA on 2/20/2017.
 */

public class FetchApiData {

    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
        private static String QUERY_PARAM = "api_key";
        private static final String MDB_KEY = "4d2d0514584191012c556bb5c1e78cef";

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

    public static void main() throws IOException {
        URL url = buildUrl("popular");
        Log.v("me", url.toString());
        String response = getResponseFromHttpUrl(url).toString();
        Log.v("UrlResponse", response);


    }




}
