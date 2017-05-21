package com.github.moviesappstagei.moviesappstagei;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.moviesappstagei.moviesappstagei.Adapters.MovieObject;
import com.github.moviesappstagei.moviesappstagei.Database.FavoritesDatabase;
import com.github.moviesappstagei.moviesappstagei.Database.MovieContract;
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
    public static String BUNDLE_MOVIE;
    public static int TRAILERS_ASYNCTASKLOADER_ID;
    public static int REVIEWS_ASYNCTASKLOADER_ID;
    public static int TASK_LOADER_INSERT_MOVIE_ID;
    public static int TASK_LOADER_DELETE_MOVIE_ID;
    public static int TASK_LOADER_QUERY_MOVIE_ID;

    //Loader variables
    private String videosUrlString;
    private String reviewsUrlString;
    private LoaderManager.LoaderCallbacks<String> callback;
    private LoaderManager loaderManager;
    private Loader<String> asyncTaskLoader;
    private Bundle queryBundle;
    private String movieID;
    private String movieTitle;
    private String movieReleaseDate;
    private String moviePlot;
    private String movieRating;
    private String moviePoster;

    //Movie isFavourite variable
    private boolean isFav = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        PARAM_REVIEWS = getString(R.string.reviews_param);
        PARAM_TRAILERS = getString(R.string.trailers_param);
        BUNDLE_REVIEWS = getString(R.string.reviews_bundle);
        BUNDLE_TRAILERS = getString(R.string.trailers_bundle);
        BUNDLE_MOVIE = getString(R.string.movie_bundle);

        TRAILERS_ASYNCTASKLOADER_ID = getResources().getInteger(R.integer.trailer_loader_id);
        REVIEWS_ASYNCTASKLOADER_ID = getResources().getInteger(R.integer.review_loader_id);
        TASK_LOADER_INSERT_MOVIE_ID = getResources().getInteger(R.integer.task_insert_loader_id);
        TASK_LOADER_DELETE_MOVIE_ID = getResources().getInteger(R.integer.task_delete_loader_id);
        TASK_LOADER_QUERY_MOVIE_ID = getResources().getInteger(R.integer.task_query_loader_id);


        ImageView poster = (ImageView) findViewById(R.id.detail_image);
        TextView title = (TextView) findViewById(R.id.detail_title);
        TextView releaseDate = (TextView) findViewById(R.id.detail_release_date);
        TextView averageVote = (TextView) findViewById(R.id.detail_average_vote);
        TextView detailDescription = (TextView) findViewById(R.id.detail_description);

        Intent startIntent = getIntent();
        if(startIntent.hasExtra(MOVIE_EXTRA)){
            MovieObject myMovie = startIntent.getParcelableExtra(MOVIE_EXTRA);
            //passing object fields to string variables
            movieID = myMovie.getMovieID();
            movieTitle = myMovie.getMovieTitle();
            movieReleaseDate = myMovie.getMovieReleaseDate();
            moviePlot = myMovie.getMovieDescription();
            movieRating = myMovie.getVoteAverage();
            moviePoster = myMovie.getMoviePoster();

            title.setText(movieTitle);
            releaseDate.setText(movieReleaseDate);
            averageVote.setText(movieRating);
            detailDescription.setText(moviePlot);
            Log.v("ROMAN", movieID); //TODO: Remove at the end

            Picasso.with(DetailsActivity.this)
                    .load(moviePoster)
                    .into(poster);

            //Adding RatingBar
            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            float barRating = (float) ratingBar.getNumStars();
            float myRating = Float.parseFloat(movieRating) * (barRating / 10.0f);
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
        queryBundle.putString(BUNDLE_MOVIE, movieID);

        loaderManager = getSupportLoaderManager();
        asyncTaskLoader = loaderManager.getLoader(TRAILERS_ASYNCTASKLOADER_ID);
        if(asyncTaskLoader == null) {
            loaderManager.initLoader(TRAILERS_ASYNCTASKLOADER_ID, queryBundle, callback);
            loaderManager.initLoader(REVIEWS_ASYNCTASKLOADER_ID, queryBundle, callback);
            loaderManager.initLoader(TASK_LOADER_QUERY_MOVIE_ID, queryBundle, callback);
        } else {
            loaderManager.restartLoader(TRAILERS_ASYNCTASKLOADER_ID, queryBundle, callback);
            loaderManager.restartLoader(REVIEWS_ASYNCTASKLOADER_ID, queryBundle, callback);
            loaderManager.restartLoader(TASK_LOADER_QUERY_MOVIE_ID, queryBundle, callback);
        };

        //Creates Database Favourites
        FavoritesDatabase favDb = new FavoritesDatabase(this);

        //Gets writable DB
        favDb.getWritableDatabase();

    }



    // --- LoaderCallbacks interface override methods ---
    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        MovieLoader result = null;
        if(id == TRAILERS_ASYNCTASKLOADER_ID) { result = new MovieLoader(this, args.getString(BUNDLE_TRAILERS));}
        if(id == REVIEWS_ASYNCTASKLOADER_ID) { result = new MovieLoader(this, args.getString(BUNDLE_REVIEWS));}
        if(id == TASK_LOADER_QUERY_MOVIE_ID) { result = new MovieLoader(this, args.getString(BUNDLE_MOVIE));}
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

        if(loader.getId() == TASK_LOADER_QUERY_MOVIE_ID ) {
            if (data != null && !data.equals("")) {
                if(data.equals("ok")) {
                    isFav = true;
                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_favourites);
                    Drawable drb = fab.getDrawable();
                    drb.setTint(ContextCompat.getColor(getBaseContext(), R.color.colorRating));
                }
            }

        };

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
    // --- End of LoaderCallbacks interface override methods ---



    private void fetchJsonTrailers(String data) throws JSONException {
        String jsonMovieResults = getResources().getString(R.string.json_movie_results);
        String jsonMovieKey = getResources().getString(R.string.json_movie_key);
        String jsonMovieName = getResources().getString(R.string.json_movie_name);

        JSONObject myJson = new JSONObject(data);
        JSONArray myData = myJson.getJSONArray(jsonMovieResults);

        if(myData.length()>0) {
            String firstTrailerKey = myData.getJSONObject(0).getString(jsonMovieKey);
            String firstTrailerName = myData.getJSONObject(0).getString(jsonMovieName);
            final String firstTrailerUrl = NetworkUtils.buildYoutubeUrl(firstTrailerKey);

            TextView firstTrailerTextView = (TextView) findViewById(R.id.first_trailer_txt);
            firstTrailerTextView.setVisibility(View.VISIBLE);
            firstTrailerTextView.setText(firstTrailerName);

            ImageView trailerImageViewFirst = (ImageView) findViewById(R.id.trailer_icon1);
            trailerImageViewFirst.setVisibility(View.VISIBLE);

            trailerImageViewFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String intentMovieDescr = getResources().getString(R.string.intent_movie_descr);
                    String intentMovieLd = getResources().getString(R.string.intent_movie_ld);
                    String intentMovieError = getResources().getString(R.string.intent_movie_error);

                    Intent firstTrailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(firstTrailerUrl));
                    Intent chooser = Intent.createChooser(firstTrailerIntent, intentMovieDescr);
                    if (firstTrailerIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(chooser);
                    } else {
                        Log.d(intentMovieLd, intentMovieError);
                    }
                }

            });
        }

        if(myData.length()>1) {
            String secondTrailerKey = myData.getJSONObject(1).getString(jsonMovieKey);
            String secondTrailerName = myData.getJSONObject(1).getString(jsonMovieName);
            final String secondTrailerUrl = NetworkUtils.buildYoutubeUrl(secondTrailerKey);

            TextView secondTrailerTextView = (TextView) findViewById(R.id.second_trailer_txt);
            secondTrailerTextView.setVisibility(View.VISIBLE);
            secondTrailerTextView.setText(secondTrailerName);

            ImageView trailerImageViewSecond = (ImageView) findViewById(R.id.trailer_icon2);
            trailerImageViewSecond.setVisibility(View.VISIBLE);

            trailerImageViewSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String intentMovieDescr = getResources().getString(R.string.intent_movie_descr);
                    String intentMovieLd = getResources().getString(R.string.intent_movie_ld);
                    String intentMovieError = getResources().getString(R.string.intent_movie_error);

                    Intent secondTrailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(secondTrailerUrl));
                    Intent chooser = Intent.createChooser(secondTrailerIntent, intentMovieDescr);
                    if (secondTrailerIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(chooser);
                    } else {
                        Log.d(intentMovieLd, intentMovieError);
                    }
                }

            });
        }

    }

    private void fetchJsonReviews(String data) throws JSONException {
        String jsonMovieResults = getResources().getString(R.string.json_movie_results);
        String jsonMovieContent = getResources().getString(R.string.intent_movie_content);
        String jsonMovieAuthor = getResources().getString(R.string.intent_movie_author);
        int maxCharactersReview = getResources().getInteger(R.integer.review_max_char);


        JSONObject myJson = new JSONObject(data);
        JSONArray myData = myJson.getJSONArray(jsonMovieResults);
        if(myData.length() > 0){
            TextView reviewsTitle = (TextView) findViewById(R.id.reviews_title);
            reviewsTitle.setVisibility(View.VISIBLE);

            TextView firstReview = (TextView) findViewById(R.id.review_first);
            firstReview.setVisibility(View.VISIBLE);

            String firstReviewContent = myData.getJSONObject(0).getString(jsonMovieContent).replaceAll("\r\n\r\n","").replaceAll("[^A-Za-z0-9- -.-']",""); //TODO: hardcode
            int maxCharsFirst;
            if(firstReviewContent.length()>maxCharactersReview) {
                maxCharsFirst = maxCharactersReview;
            } else {
                maxCharsFirst = firstReviewContent.length();
            }
            firstReview.setText(firstReviewContent.substring(0,maxCharsFirst).concat("..."));

            TextView firstAuthor = (TextView) findViewById(R.id.review_first_author);
            firstAuthor.setVisibility(View.VISIBLE);
            firstAuthor.setText(myData.getJSONObject(0).getString(jsonMovieAuthor));

        }

        if(myData.length() > 1){
            TextView secondReview = (TextView) findViewById(R.id.review_second);
            secondReview.setVisibility(View.VISIBLE);
            String secondReviewContent = myData.getJSONObject(1).getString(jsonMovieContent).replaceAll("\r\n\r\n","").replaceAll("[^A-Za-z0-9- -.-']",""); //TODO: hardcode
            int maxCharsSec;
            if(secondReviewContent.length()>maxCharactersReview) {
                maxCharsSec = maxCharactersReview;
            } else {
                maxCharsSec = secondReviewContent.length();
            }
            secondReview.setText(secondReviewContent.substring(0,maxCharsSec).concat("..."));

            TextView secondAuthor = (TextView) findViewById(R.id.review_second_author);
            secondAuthor.setVisibility(View.VISIBLE);
            secondAuthor.setText(myData.getJSONObject(1).getString(jsonMovieAuthor));

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                int id = item.getItemId();
                if (id == R.id.action_share) {
                    Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                            .setType("text/plain")
                            //TODO: Hardcode and add youtube URL
                            .setText("Hey checkout this cool movie")
                            .getIntent();
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                    startActivity(shareIntent);
                    return true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    };


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public void fabEvent(View view)  {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_favourites);
        Drawable drb = fab.getDrawable();
        Uri uri;

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.FeedDatabase.MOVIE_ID, movieID);
        contentValues.put(MovieContract.FeedDatabase.MOVIE_TITLE, movieTitle);
        contentValues.put(MovieContract.FeedDatabase.MOVIE_RELEASE_DATE, movieReleaseDate);
        contentValues.put(MovieContract.FeedDatabase.MOVIE_PLOT, moviePlot);
        contentValues.put(MovieContract.FeedDatabase.MOVIE_RATING, movieRating);
        contentValues.put(MovieContract.FeedDatabase.MOVIE_POSTER, moviePoster);

        //If not favourite, add to database
        if(!isFav) {
            uri = getContentResolver().insert(MovieContract.FeedDatabase.CONTENT_URI, contentValues);
            if (uri != null) {
                isFav = true;
                drb.setTint(ContextCompat.getColor(getBaseContext(), R.color.colorRating));
            }
        //If favourite, remove from database
        } else if(isFav) {
            uri = MovieContract.FeedDatabase.CONTENT_URI.buildUpon().appendPath(movieID).build();
            int nbLines = getContentResolver().delete(uri, null, null);
            if (nbLines > 0) {
                isFav = false;
                drb.setTint(ContextCompat.getColor(getBaseContext(), R.color.colorFont));
            }
            }
        }



    }
