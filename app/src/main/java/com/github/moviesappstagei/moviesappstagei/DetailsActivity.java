package com.github.moviesappstagei.moviesappstagei;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.moviesappstagei.moviesappstagei.Adapters.MovieObject;
import com.github.moviesappstagei.moviesappstagei.Loaders.MovieLoader;
import com.github.moviesappstagei.moviesappstagei.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        TRAILERS_ASYNCTASKLOADER_ID = 34; //TODO: Hardcode
        REVIEWS_ASYNCTASKLOADER_ID = 35; //TODO: Hardcode

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
            movieID = myMovie.getMovieID();
            Log.v("ROMAN", movieID); //TODO: Remove at the end

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

        //Loader Callback for Trailers and Review
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
        if(id == TRAILERS_ASYNCTASKLOADER_ID) { result = new MovieLoader(this, args.getString(BUNDLE_TRAILERS));}
        if(id == REVIEWS_ASYNCTASKLOADER_ID) { result = new MovieLoader(this, args.getString(BUNDLE_REVIEWS));}
        return result;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        if(loader.getId() == TRAILERS_ASYNCTASKLOADER_ID ) {
            if (data != null && !data.equals("")) {
                try {
                    fetchJsonTrailers(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        if(loader.getId() == REVIEWS_ASYNCTASKLOADER_ID ) {
            if (data != null && !data.equals("")) {
                try {
                    fetchJsonReviews(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
    // --- End of LoaderCallbacks interface override methods ---





    //TODO: implement json handling for trailers
    private void fetchJsonTrailers(String data) throws JSONException {
        JSONObject myJson = new JSONObject(data);
        JSONArray myData = myJson.getJSONArray("results"); //TODO: HARDCODE
        String firstTrailerKey = myData.getJSONObject(0).getString("key"); //TODO: Hardcode
        String secondTrailerKey = myData.getJSONObject(1).getString("key"); // TODO: Hardcode
        String firstTrailerName = myData.getJSONObject(0).getString("name"); // TODO: Hardcode
        String secondTrailerName = myData.getJSONObject(1).getString("name"); // TODO: Hardcode
        final String firstTrailerUrl = NetworkUtils.buildYoutubeUrl(firstTrailerKey);
        final String secondTrailerUrl = NetworkUtils.buildYoutubeUrl(secondTrailerKey);


        TextView firstTrailerTextView = (TextView) findViewById(R.id.first_trailer_txt);
        firstTrailerTextView.setText(firstTrailerName);

        TextView secondTrailerTextView = (TextView) findViewById(R.id.second_trailer_txt);
        secondTrailerTextView.setText(secondTrailerName);

        ImageView trailerImageViewFirst = (ImageView) findViewById(R.id.trailer_icon1);
        trailerImageViewFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent firstTrailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(firstTrailerUrl));
                Intent chooser = Intent.createChooser(firstTrailerIntent, "Open With"); //TODO hardcode
                if (firstTrailerIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
            }

        });

        ImageView trailerImageViewSecond = (ImageView) findViewById(R.id.trailer_icon2);
        trailerImageViewSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent secondTrailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(secondTrailerUrl));
                Intent chooser = Intent.createChooser(secondTrailerIntent, "Open With"); //TODO hardcode
                if (secondTrailerIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
            }

        });

    }

    //TODO: implement json handling for reviews
    private void fetchJsonReviews(String data) throws JSONException {
        JSONObject myJson = new JSONObject(data);
        JSONArray myData = myJson.getJSONArray("results"); //TODO: HARDCODE
        if(myData.length() > 0){
            TextView reviewsTitle = (TextView) findViewById(R.id.reviews_title);
            reviewsTitle.setVisibility(View.VISIBLE);

            TextView firstReview = (TextView) findViewById(R.id.review_first);
            firstReview.setVisibility(View.VISIBLE);

            String firstReviewContent = myData.getJSONObject(0).getString("content").replaceAll("\r\n\r\n","").replaceAll("[^A-Za-z0-9 ]",""); //TODO: hardcode
            int maxCharsFirst;
            if(firstReviewContent.length()>300) {
                maxCharsFirst = 300;
            } else {
                maxCharsFirst = firstReviewContent.length();
            }
            firstReview.setText(firstReviewContent.substring(0,maxCharsFirst).concat("..."));

            TextView firstAuthor = (TextView) findViewById(R.id.review_first_author);
            firstAuthor.setVisibility(View.VISIBLE);
            firstAuthor.setText(myData.getJSONObject(0).getString("author"));

        }

        if(myData.length() > 1){
            TextView secondReview = (TextView) findViewById(R.id.review_second);
            secondReview.setVisibility(View.VISIBLE);
            String secondReviewContent = myData.getJSONObject(1).getString("content").replaceAll("\r\n\r\n","").replaceAll("[^A-Za-z0-9 ]",""); //TODO: hardcode
            int maxCharsSec;
            if(secondReviewContent.length()>300) {
                maxCharsSec = 300;
            } else {
                maxCharsSec = secondReviewContent.length();
            }
            secondReview.setText(secondReviewContent.substring(0,maxCharsSec).concat("..."));

            TextView secondAuthor = (TextView) findViewById(R.id.review_second_author);
            secondAuthor.setVisibility(View.VISIBLE);
            secondAuthor.setText(myData.getJSONObject(1).getString("author"));

            Button reviewsButton = (Button) findViewById(R.id.all_reviews);
            reviewsButton.setVisibility(View.VISIBLE);
        }


    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

}
