package com.udacity.yamini.popularmovies;

/**
 * Created by Yamini on 11/23/2015.
 */
public class Movies {

    String movieTitle;
    int movieId;
    String moviePosterPath; // drawable reference id
    String movieReleaseDate;
    String movieRating;
    String moviePlot;

    public Movies(String movieTitle, int movieId, String moviePosterPath,String movieReleaseDate,String movieRating,String moviePlot )
    {
        this.movieTitle = movieTitle;
        this.movieId = movieId;
        this.moviePosterPath = moviePosterPath;
        this.movieReleaseDate= movieReleaseDate;
        this.movieRating = movieRating;
        this.moviePlot = moviePlot;
    }
}
