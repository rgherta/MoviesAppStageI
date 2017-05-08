package com.github.moviesappstagei.moviesappstagei.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Grossman on 5/1/2017.
 */

public class FavoritesDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites_database.db";
    private static final int DATABASE_VERSION = 1;


    public FavoritesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE = "CREATE TABLE " +
                MovieContract.FeedDatabase.TABLE_NAME + " (" +
                MovieContract.FeedDatabase._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.FeedDatabase.MOVIE_ID + " INTEGER PRIMARY KEY, " +
                MovieContract.FeedDatabase.MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.FeedDatabase.MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.FeedDatabase.MOVIE_PLOT + " TEXT NOT NULL, " +
                MovieContract.FeedDatabase.MOVIE_POSTER + " TEXT NOT NULL, " +
                MovieContract.FeedDatabase.MOVIE_RATING + " TEXT NOT NULL, " +
                MovieContract.FeedDatabase.MOVIE_TRAILER + " TEXT NOT NULL " + ");";

        sqLiteDatabase.execSQL(SQL_CREATE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        Log.w(FavoritesDatabase.class.getName(),
                "Upgrading database from version " + i + " to "
                        + i1 + ", which will destroy all old data");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.FeedDatabase.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
