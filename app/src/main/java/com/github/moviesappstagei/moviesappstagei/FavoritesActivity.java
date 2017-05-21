package com.github.moviesappstagei.moviesappstagei;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.moviesappstagei.moviesappstagei.Adapters.FavoritesAdapter;
import com.github.moviesappstagei.moviesappstagei.Database.MovieContract;

/**
 * Created by RGHERTA on 5/21/2017.
 */

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static int ID_FAV_LOADER;

    private FavoritesAdapter mFavAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ID_FAV_LOADER = getResources().getInteger(R.integer.fav_loader_id);

        mRecyclerView = (RecyclerView) findViewById(R.id.fav_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mFavAdapter = new FavoritesAdapter(this);
        mRecyclerView.setAdapter(mFavAdapter);

        getSupportLoaderManager().initLoader(ID_FAV_LOADER, null, this);
        Uri uri = MovieContract.FeedDatabase.CONTENT_URI;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        if (loaderId==ID_FAV_LOADER) {
                Uri uri = MovieContract.FeedDatabase.CONTENT_URI;
                return new CursorLoader(this,
                        uri,
                        null,
                        null,
                        null,
                        null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFavAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavAdapter.swapCursor(null);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }

}
