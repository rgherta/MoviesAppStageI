package com.github.moviesappstagei.moviesappstagei;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RGherta on 2/19/2017.
 */

public class MovieObject implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieTitle);
        parcel.writeString(movieDescription);
        parcel.writeString(moviePoster);
        parcel.writeString(movieID);
        parcel.writeString(movieReleaseDate);
        parcel.writeString(voteAverage);
    }

    public MovieObject(Parcel parcel) {
        movieTitle = parcel.readString();
        movieDescription = parcel.readString();
        moviePoster = parcel.readString();
        movieID = parcel.readString();
        movieReleaseDate = parcel.readString();
        voteAverage = parcel.readString();
    }

    public MovieObject() { }

    public static final Parcelable.Creator<MovieObject> CREATOR = new Parcelable.Creator<MovieObject>(){
        @Override
        public MovieObject createFromParcel(Parcel parcel) {
            return new MovieObject(parcel);
        }

        @Override
        public MovieObject[] newArray(int size) {
            return new MovieObject[size];
        }

    };


}
