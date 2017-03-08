package com.github.moviesappstagei.moviesappstagei;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.moviesappstagei.moviesappstagei.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MovieGallery extends AppCompatActivity {

    MainAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_gallery);

        //Adding the RW reference
        RecyclerView mainRecycler = (RecyclerView) findViewById(R.id.main_recycler);

        //Adding layout manager
        GridLayoutManager gLayoutManager = new GridLayoutManager(this,2);

        //Binding to the xml tag
        mainRecycler.setLayoutManager(gLayoutManager);

        //Binding the adapter
        movieAdapter = new MainAdapter(this);
        mainRecycler.setAdapter(movieAdapter);

        //populates the main gallery
        try {fetchMainScreenData(NetworkUtils.PARAM_POP);} catch (JSONException exc) { exc.printStackTrace(); Log.v("me","shitface"); }

    }

    private String getApiData(String param) {
        String JsonResult = null;
        URL url = NetworkUtils.buildUrl(param);
        try {
            JsonResult = new MoviesQueryTask().execute(url).get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }
        return JsonResult;
    }


    public class MoviesQueryTask extends AsyncTask<URL, Void, String> {

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

    }

    private void fetchMainScreenData(String param) throws JSONException {
        String mJsonString = getApiData(param);  //HTTP call
        JSONObject myJson = new JSONObject(mJsonString);
        JSONArray myData = myJson.getJSONArray(NetworkUtils.JSON_RESULTS);
        List<MovieObject> movies = new ArrayList<>();

        for (int i=0; i<myData.length();i++) {
            String movieId = myData.getJSONObject(i).getString(NetworkUtils.JSON_ID);
            String moviePoster = NetworkUtils.POSTERS_BASE_URL + myData.getJSONObject(i).getString(NetworkUtils.JSON_POSTER_PATH);
            String movieDescription = myData.getJSONObject(i).getString(NetworkUtils.JSON_OVERVIEW);
            String movieTitle = myData.getJSONObject(i).getString(NetworkUtils.JSON_TITLE);
            String movieReleaseDate = myData.getJSONObject(i).getString(NetworkUtils.JSON_RELEASE_DATE);
            String movieVoteAverage = myData.getJSONObject(i).getString(NetworkUtils.JSON_VOTE_AVERAGE);

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
                String ok = adapterView.getSelectedItem().toString();
                Log.v("Roman", ok);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });


        return true;
    }
    // TODO - Add onAction override for Spinner


}







