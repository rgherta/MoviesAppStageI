package com.github.moviesappstagei.moviesappstagei;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
        fetchData();
        // TODO - 1. Replace fetchData by Async FetchApiData
          //  FetchApiData.main();   // for testing purposes try in Asyunc task

    }

    private void fetchData() {
        List<MovieObject> movies = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
           movies.add(new MovieObject());
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
        return true;
    }
    // TODO - Add onAction override for Spinner



}







