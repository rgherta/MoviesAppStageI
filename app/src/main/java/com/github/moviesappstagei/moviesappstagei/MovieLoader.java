package com.github.moviesappstagei.moviesappstagei;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.github.moviesappstagei.moviesappstagei.utilities.NetworkUtils;

import java.io.IOException;


/**
 * Created by RGHERTA on 3/25/2017.
 */

//Useful intuitive step by step help on implementing Loaders found in this google android developer gitbook https://goo.gl/tksUOp

public class MovieLoader extends AsyncTaskLoader<String> {

    private  String queryString;


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public MovieLoader(Context context, String args) {
        super(context);
        queryString = args;
    }


    @Override
    public String loadInBackground() {
        String urlResults = null;
        if(queryString == null) {
            return null;
        }
        try {
            urlResults = NetworkUtils.getResponseFromHttpUrl(queryString);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return urlResults;
    }
}



