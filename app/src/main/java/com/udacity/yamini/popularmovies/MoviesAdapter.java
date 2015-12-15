package com.udacity.yamini.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yamini on 11/23/2015.
 */
public class MoviesAdapter extends ArrayAdapter<Movies> {

    private static final String LOG_TAG = MoviesAdapter.class.getSimpleName();
    private List<Movies> movieListResults = new ArrayList<Movies>();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param androidFlavors A List of AndroidFlavor objects to display in a list
     */
    public MoviesAdapter(Activity context,int layoutResourceId, List<Movies> moviesList) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, layoutResourceId, moviesList);

        movieListResults.addAll(moviesList);

    }

    @Override
    public int getCount() {

        return (movieListResults != null) ? movieListResults.size() : 0;
    }

    @Override
    public Movies getItem(int position) {

        return movieListResults.get(position);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Movies movie = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.

        Context context = getContext();
        final String IMAGE_BASE_URL =
                "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w185";
        final String POSTER_PATH = movie.moviePosterPath;

        String imageUrl = IMAGE_BASE_URL+IMAGE_SIZE+POSTER_PATH;

       if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.movie_item, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso.with(context).load(imageUrl).into(iconView);

        return convertView;
    }

}
