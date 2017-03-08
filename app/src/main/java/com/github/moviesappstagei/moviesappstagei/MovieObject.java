package com.github.moviesappstagei.moviesappstagei;

/**
 * Created by RGherta on 2/19/2017.
 */

public class MovieObject {

    private String movieTitle;
    private String movieDescription;
    private String moviePoster;
    private String movieID;
    private String movieReleaseDate;
    private String voteAverage;

    public String getMovieID() {
        return movieID;
    }
    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getMovieTitle() {
        return movieTitle;
    }
    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieDescription() {
        return movieDescription;
    }
    public void setMovieDescription(String movieDescription) { this.movieDescription = movieDescription;}

    public String getMoviePoster() {return moviePoster;  }
    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieReleaseDate() {return movieReleaseDate;  }
    public void setMovieReleaseDate(String movieReleaseDate) {this.movieReleaseDate = movieReleaseDate;  }

    public String getVoteAverage() {return voteAverage; }
    public void setVoteAverage(String voteAverage) { this.voteAverage = voteAverage;}

}
