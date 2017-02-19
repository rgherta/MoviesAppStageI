package com.github.moviesappstagei.moviesappstagei;

/**
 * Created by RGherta on 2/19/2017.
 */

public class MovieObject {

    private String movieTitle;
    private String movieDescription;
    private String moviePoster;

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getMoviePoster() {
       // return moviePoster;
        return "http://graphicdesignjunction.com/wp-content/uploads/2011/12/grey-movie-poster.jpg";
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }
}
