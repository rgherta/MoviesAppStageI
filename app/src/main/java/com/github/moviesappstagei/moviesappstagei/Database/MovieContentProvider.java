package com.github.moviesappstagei.moviesappstagei.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by RGHERTA on 5/1/2017.
 */

public class MovieContentProvider extends ContentProvider {

    private FavoritesDatabase favoritesDatabase;
    private static final String TAG = MovieContentProvider.class.getSimpleName();
    public static final int MOVIES = 100;
    public static final int MOVIE = 101;
    public static final UriMatcher myUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH); //construct an empty matcher
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE);
        return uriMatcher;
    };


    @Override
    public boolean onCreate() {
        Context context = getContext();
        favoritesDatabase = new FavoritesDatabase(context);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = favoritesDatabase.getWritableDatabase();
        int match = myUriMatcher.match(uri);
        Cursor myCursor = null;
        switch (match) {
            // Query for the tasks directory
            case MOVIES:
                //Query all movies
                myCursor =  db.query(MovieContract.FeedDatabase.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE:
                //using Selection and selection Args
                //URI: content://authority/movies/#
                String id = uri.getPathSegments().get(1);
                //Selection is the _ID column and the SelectionArgs = row ID from the URI
                String mSelection = MovieContract.FeedDatabase.MOVIE_ID + "=?"; //? indicates it will take value from Selection Args
                String[] mSelectionArgs = new String[]{id};
                myCursor = db.query(MovieContract.FeedDatabase.TABLE_NAME, projection, mSelection, mSelectionArgs, null, null, sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        myCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return myCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = favoritesDatabase.getWritableDatabase();
        int match = myUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES:
                long id = db.insert(MovieContract.FeedDatabase.TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MovieContract.FeedDatabase.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //notify the resolver if uri has changed
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = favoritesDatabase.getWritableDatabase();
        int match = myUriMatcher.match(uri);
        int movieDeleted = 0;

        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case MOVIE:
                String id = uri.getPathSegments().get(1);
                movieDeleted = db.delete(MovieContract.FeedDatabase.TABLE_NAME, MovieContract.FeedDatabase.MOVIE_ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (movieDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return movieDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
