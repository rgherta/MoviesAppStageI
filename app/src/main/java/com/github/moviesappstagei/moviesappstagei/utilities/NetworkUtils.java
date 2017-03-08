package com.github.moviesappstagei.moviesappstagei.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by RGHERTA on 2/20/2017.
 */

public class NetworkUtils {

    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static String QUERY_PARAM = "api_key";
    private static final String MDB_KEY = "4d2d0514584191012c556bb5c1e78cef";

    public static final String PARAM_POP = "popular";
    public static final String PARAM_RATED = "top_rated";

    public static final String JSON_RESULTS = "results";
    public static final String JSON_ID = "id";
    public static final String JSON_POSTER_PATH = "poster_path";
    public static final String JSON_OVERVIEW = "overview";
    public static final String JSON_TITLE = "title";
    public static final String JSON_RELEASE_DATE = "release_date";
    public static final String POSTERS_BASE_URL = "http://image.tmdb.org/t/p/w185/";

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
