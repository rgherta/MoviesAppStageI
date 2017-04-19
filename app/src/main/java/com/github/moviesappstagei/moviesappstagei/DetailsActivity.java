package com.github.moviesappstagei.moviesappstagei;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.moviesappstagei.moviesappstagei.Adapters.MovieObject;
import com.github.moviesappstagei.moviesappstagei.Loaders.MovieLoader;
import com.github.moviesappstagei.moviesappstagei.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import static com.github.moviesappstagei.moviesappstagei.MovieGallery.ERROR_PARCELABLE;
import static com.github.moviesappstagei.moviesappstagei.MovieGallery.MOVIE_EXTRA;

/**
 * Created by RGHERTA on 2/19/2017.
 */

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    //Hardcoded Strings
    public static String PARAM_TRAILERS;
    public static String PARAM_REVIEWS;
    public static String BUNDLE_TRAILERS;
    public static String BUNDLE_REVIEWS;
    public static int TRAILERS_ASYNCTASKLOADER_ID;
    public static int REVIEWS_ASYNCTASKLOADER_ID;

    //Loader variables
    private String videosUrlString;
    private String reviewsUrlString;
    private LoaderManager.LoaderCallbacks<String> callback;
    private LoaderManager loaderManager;
    private Loader<String> asyncTaskLoader;
    private Bundle queryBundle;
    private String movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        PARAM_REVIEWS = getString(R.string.reviews_param);
        PARAM_TRAILERS = getString(R.string.trailers_param);
        BUNDLE_REVIEWS = getString(R.string.reviews_bundle);
        BUNDLE_TRAILERS = getString(R.string.trailers_bundle);


        ImageView poster = (ImageView) findViewById(R.id.detail_image);
        TextView title = (TextView) findViewById(R.id.detail_title);
        TextView releaseDate = (TextView) findViewById(R.id.detail_release_date);
        TextView averageVote = (TextView) findViewById(R.id.detail_average_vote);
        TextView detailDescription = (TextView) findViewById(R.id.detail_description);

        Intent startIntent = getIntent();
        if(startIntent.hasExtra(MOVIE_EXTRA)){
            MovieObject myMovie = startIntent.getParcelableExtra(MOVIE_EXTRA);
            title.setText(myMovie.getMovieTitle());
            releaseDate.setText(myMovie.getMovieReleaseDate());
            averageVote.setText(myMovie.getVoteAverage());
            detailDescription.setText(myMovie.getMovieDescription());
            movieID = myMovie.getMovieID(); //TODO: important movieID

            Picasso.with(DetailsActivity.this)
                    .load(myMovie.getMoviePoster())
                    .into(poster);

            //Adding RatingBar
            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            float barRating = (float) ratingBar.getNumStars();
            float myRating = Float.parseFloat(myMovie.getVoteAverage()) * (barRating / 10.0f); //TODO Hardcode float value
            ratingBar.setRating(myRating);

        } else {
            throw new IllegalArgumentException(ERROR_PARCELABLE);
        }

        //Adding UP action button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Loader Callback for Trailers and Reviews TODO: Add Movie ID to API path
        callback = DetailsActivity.this;
        videosUrlString = NetworkUtils.getMovieInfo(PARAM_TRAILERS, movieID);
        reviewsUrlString = NetworkUtils.getMovieInfo(PARAM_REVIEWS, movieID);

        //Using LoaderManager
        queryBundle = new Bundle();
        queryBundle.putString(BUNDLE_TRAILERS, videosUrlString);
        queryBundle.putString(BUNDLE_REVIEWS, reviewsUrlString);

        loaderManager = getSupportLoaderManager();
        asyncTaskLoader = loaderManager.getLoader(TRAILERS_ASYNCTASKLOADER_ID);
        if(asyncTaskLoader == null) {
            loaderManager.initLoader(TRAILERS_ASYNCTASKLOADER_ID, queryBundle, callback);
            loaderManager.initLoader(REVIEWS_ASYNCTASKLOADER_ID, queryBundle, callback);
        } else {
            loaderManager.restartLoader(TRAILERS_ASYNCTASKLOADER_ID, queryBundle, callback);
            loaderManager.restartLoader(REVIEWS_ASYNCTASKLOADER_ID, queryBundle, callback);
        }

    }


    // --- LoaderCallbacks interface override methods ---
    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        MovieLoader result = null;
        if(id == TRAILERS_ASYNCTASKLOADER_ID) { result = new MovieLoader(this, (String) args.get(BUNDLE_TRAILERS)); }
        if(id == REVIEWS_ASYNCTASKLOADER_ID) { result = new MovieLoader(this, (String) args.get(BUNDLE_REVIEWS));}
        return result;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        if(loader.getId() == TRAILERS_ASYNCTASKLOADER_ID ) {
            if (data != null && !data.equals("")) {
                fetchJsonTrailers(data);
            }

        };

        if(loader.getId() == REVIEWS_ASYNCTASKLOADER_ID ) {
            if (data != null && !data.equals("")) {
                fetchJsonReviews(data);
            }

        };

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
    // --- End of LoaderCallbacks interface override methods ---





    //TODO: implement json handling for trailers
    private void fetchJsonTrailers(String data){

    }

    //TODO: implement json handling for reviews
    private void fetchJsonReviews(String data){

    }
}
