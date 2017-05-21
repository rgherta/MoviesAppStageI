package com.github.moviesappstagei.moviesappstagei.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by RGHERTA on 5/1/2017.
 */

public final class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.github.moviesappstagei.moviesappstagei";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "Favorites";




    public static class FeedDatabase implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "Favorites";
        public static final String MOVIE_ID = "ID";
        public static final String MOVIE_TITLE = "Title";
        public static final String MOVIE_RELEASE_DATE = "ReleaseDate";
        public static final String MOVIE_PLOT = "Plot";
        public static final String MOVIE_RATING = "Rating";
        public static final String MOVIE_POSTER = "Poster";



    }


}
