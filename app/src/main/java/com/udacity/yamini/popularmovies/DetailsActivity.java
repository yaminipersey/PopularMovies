package com.udacity.yamini.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String title = getIntent().getStringExtra("title");
        String imagePath = getIntent().getStringExtra("image");
        String releaseDate = getIntent().getStringExtra("releaseDate");
        String userRating = getIntent().getStringExtra("rating");
        String plot = getIntent().getStringExtra("plot");

        final String IMAGE_BASE_URL =
                "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w185";

        String imageUrl = IMAGE_BASE_URL+IMAGE_SIZE+imagePath;


        TextView titleTextView = (TextView) findViewById(R.id.grid_movie_title);
        ImageView imageView = (ImageView) findViewById(R.id.grid_movie_image);
        TextView  releaseDateTextView = (TextView) findViewById(R.id.gridmovie_release_date);
        TextView ratingTextView = (TextView) findViewById(R.id.grid_movie_rating);
        TextView plotTextView = (TextView) findViewById(R.id.grid_movie_plot);

        titleTextView.setText(title);
        Picasso.with(this).load(imageUrl).into(imageView);
        releaseDateTextView.setText(releaseDate);
        ratingTextView.setText(userRating);
        plotTextView.setText(plot);
    }

}
