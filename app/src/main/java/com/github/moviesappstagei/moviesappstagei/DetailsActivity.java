package com.github.moviesappstagei.moviesappstagei;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.github.moviesappstagei.moviesappstagei.MovieGallery.ERROR_PARCELABLE;
import static com.github.moviesappstagei.moviesappstagei.MovieGallery.MOVIE_EXTRA;
import static com.github.moviesappstagei.moviesappstagei.MovieGallery.VOTES_LABEL;


public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

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
            averageVote.setText(myMovie.getVoteAverage() + VOTES_LABEL);
            detailDescription.setText(myMovie.getMovieDescription());

            Picasso.with(DetailsActivity.this)
                    .load(myMovie.getMoviePoster())
                    .into(poster);


        } else {
            throw new IllegalArgumentException(ERROR_PARCELABLE);
        }


    }
}
