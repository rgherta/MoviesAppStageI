package com.github.moviesappstagei.moviesappstagei;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.moviesappstagei.moviesappstagei.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RGherta on 2/19/2017.
 */

public class MovieGallery extends AppCompatActivity {

    //Hardcoded Strings
    public static String MOVIES_BASE_URL;
    public static String QUERY_PARAM;
    public static String MDB_KEY;

    public static String MENU_POP;
    public static String PARAM_POP;
    public static String MENU_RATED;
    public static String PARAM_RATED;

    public static String JSON_RESULTS;
    public static String JSON_ID;
    public static String JSON_POSTER_PATH;
    public static String JSON_OVERVIEW;
    public static String JSON_TITLE;
    public static String JSON_RELEASE_DATE;
    public static String JSON_VOTE_AVERAGE;
    public static String MOVIE_EXTRA;

    public static String POSTERS_BASE_URL;
    public static String ERROR_PARCELABLE;
    public static String ERROR_INTERNET;
    public static String VOTES_LABEL;

    //Main Objects
    MainAdapter movieAdapter;
    RecyclerView mainRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_gallery);

        MOVIES_BASE_URL = getString(R.string.movies_base_url);
        QUERY_PARAM = getString(R.string.query_param);
        MDB_KEY = getString(R.string.mdb_key);
        MENU_POP = getString(R.string.menu_pop);
        PARAM_POP = getString(R.string.param_pop);
        MENU_RATED = getString(R.string.menu_rated);
        PARAM_RATED = getString(R.string.param_rated);
        JSON_RESULTS = getString(R.string.json_results);
        JSON_ID = getString(R.string.json_id);
        JSON_POSTER_PATH = getString(R.string.json_poster_path);
        JSON_OVERVIEW = getString(R.string.json_overview);
        JSON_TITLE = getString(R.string.json_title);
        JSON_RELEASE_DATE = getString(R.string.json_release_date);
        JSON_VOTE_AVERAGE = getString(R.string.json_vote_average);
        MOVIE_EXTRA = getString(R.string.movie_extra);
        POSTERS_BASE_URL = getString(R.string.posters_base_url);
        ERROR_PARCELABLE = getString(R.string.error_parcelable);
        ERROR_INTERNET = getString(R.string.error_internet);
        VOTES_LABEL = getString(R.string.votes_label);

        //Adding the RW reference
        mainRecycler = (RecyclerView) findViewById(R.id.main_recycler);

        //Calculating number of items per row
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Integer widthDivider = 600; //TODO: Hardcode
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) nColumns = 2; //TODO: Hardcode

        //Adding LayoutManager
        GridLayoutManager gLayoutManager = new GridLayoutManager(this, nColumns);

        //Binding to the xml tag
        mainRecycler.setLayoutManager(gLayoutManager);

        //Binding the adapter
        movieAdapter = new MainAdapter(this);
        mainRecycler.setAdapter(movieAdapter);
    }

    //Some intuitive help on Parcelable/ArrayList behaviour found on Jose Mateo blog https://goo.gl/087425
    public void fetchMainScreenData(String mJsonString) throws JSONException {

        if(isOnline()) {
            JSONObject myJson = new JSONObject(mJsonString);
            JSONArray myData = myJson.getJSONArray(JSON_RESULTS);
            List<MovieObject> movies = new ArrayList<>();

            for (int i = 0; i < myData.length(); i++) {
                String movieId = myData.getJSONObject(i).getString(JSON_ID);
                String moviePoster = POSTERS_BASE_URL + myData.getJSONObject(i).getString(JSON_POSTER_PATH);
                String movieDescription = myData.getJSONObject(i).getString(JSON_OVERVIEW);
                String movieTitle = myData.getJSONObject(i).getString(JSON_TITLE);
                String movieReleaseDate = myData.getJSONObject(i).getString(JSON_RELEASE_DATE);
                String movieVoteAverage = myData.getJSONObject(i).getString(JSON_VOTE_AVERAGE);

                MovieObject newMovie = new MovieObject();
                newMovie.setMovieID(movieId);
                newMovie.setMoviePoster(moviePoster);
                newMovie.setMovieDescription(movieDescription);
                newMovie.setMovieTitle(movieTitle);
                newMovie.setMovieReleaseDate(movieReleaseDate);
                newMovie.setVoteAverage(movieVoteAverage);
                movies.add(newMovie);
            }
            movieAdapter.setList(movies);
        } else {
            Toast.makeText(this, ERROR_INTERNET, Toast.LENGTH_SHORT).show();
        }
    }

    //Adding Spinner for Pop/Rating sort
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedOption = adapterView.getSelectedItem().toString();
                if (selectedOption.equals(MENU_POP)) {
                    URL newUrl = NetworkUtils.buildUrl(PARAM_POP);
                    new MoviesQueryTask().execute(newUrl);
                    mainRecycler.scrollToPosition(0);
                } else if(selectedOption.equals(MENU_RATED)){
                    URL newUrl = NetworkUtils.buildUrl(PARAM_RATED);
                    new MoviesQueryTask().execute(newUrl);
                    mainRecycler.scrollToPosition(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        return true;
    }

    class MoviesQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String urlResults = null;
            try {
                urlResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return urlResults;
        }

        @Override
        protected void onPostExecute(String urlResults) {
            if (urlResults != null && !urlResults.equals("")) {
                try {fetchMainScreenData(urlResults);} catch (JSONException e) { e.printStackTrace();
                }
            }
        }

    }

    //From StackOverflow https://goo.gl/BAoDL2 as per Udacity instruction.
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


}


