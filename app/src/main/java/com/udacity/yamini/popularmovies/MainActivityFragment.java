package com.udacity.yamini.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private MoviesAdapter moviesAdapter;
    List<Movies> moviesResults = new ArrayList<>();
    private GridView mMoviesGridView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        updateMovies();
        mMoviesGridView = (GridView) rootView.findViewById(R.id.movies_grid);

        mMoviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                //Get item at position
                Movies movieFromGrid = (Movies) parent.getItemAtPosition(position);

                //Pass the image title and url to DetailsActivity
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("title", movieFromGrid.movieTitle);
                intent.putExtra("image", movieFromGrid.moviePosterPath);
                intent.putExtra("releaseDate", movieFromGrid.movieReleaseDate);
                intent.putExtra("plot", movieFromGrid.moviePlot);
                intent.putExtra("rating", movieFromGrid.movieRating);


                //Start details activity
                startActivity(intent);
            }
        });

        return rootView;
    }

   /* @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }
*/
    private void updateMovies() {

        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute();
    }

    private List<Movies> getMovieDataFromJson(String forecastJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String MOVIE_RESULTS = "results";
        final String MOVIE_ID = "id";
        final String MOVIE_POSTERPATH = "poster_path";
        final String MOVIE_TITLE = "title";
        final String MOVIE_RELEASEDATE="release_date";
        final String MOVIE_RATING = "vote_average";
        final String MOVIE_PLOT = "overview";


        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray movieArray = forecastJson.getJSONArray(MOVIE_RESULTS);

        int arraySize = movieArray.length();
        if(moviesResults.size()>0)
        {
            moviesResults.clear();
        }

        for (int i = 0; i < arraySize; ++i) {
            // For now, using the format "Day, description, hi/low"

            // Get the JSON object representing the day
            JSONObject movieObject = movieArray.getJSONObject(i);

            int movieId = Integer.parseInt(movieObject.getString(MOVIE_ID));
            String movieTitle=movieObject.getString(MOVIE_TITLE);
            String moviePosterPath = movieObject.getString(MOVIE_POSTERPATH);
            String movieReleaseDate = movieObject.getString(MOVIE_RELEASEDATE);
            String movieRating = movieObject.getString(MOVIE_RATING);
            String moviePlot = movieObject.getString(MOVIE_PLOT);

            moviesResults.add(new Movies(movieTitle,movieId,moviePosterPath,movieReleaseDate,movieRating,moviePlot));
        }
        return moviesResults;

    }

    public class FetchMoviesTask extends AsyncTask<Movies, Void, List<Movies>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected List<Movies> doInBackground(Movies... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            SharedPreferences moviePrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortByPreference = moviePrefs.getString(
                    getString(R.string.pref_movie_prefs_key),
                    getString(R.string.pref_movies_popularity));

            try {

                Uri builtUri = builtURIBYPreference(sortByPreference);
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        private Uri builtURIBYPreference(String sortByPref)
        {
            final String MOVIES_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";
            final String QUERY_PARAM = "sort_by";
            final String APPID_PARAM = "api_key";
            final String CERTIFICATIONCOUNTRY_PARAM = "certification_country";
            Uri builtUri;

            if (sortByPref.equalsIgnoreCase("popularity"))
            {
                builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, "popularity.desc")
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIEDB_API_KEY)
                        .build();
            }
            else {

                builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(CERTIFICATIONCOUNTRY_PARAM,"US")
                        .appendQueryParameter(QUERY_PARAM, "vote_average.desc")
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIEDB_API_KEY)
                        .build();
            }
            return builtUri;
        }

        @Override
        protected void onPostExecute(List<Movies> result) {
            //super.onPostExecute(result);

            moviesAdapter = new MoviesAdapter(getActivity(),R.id.movies_item,moviesResults );
            mMoviesGridView.setAdapter(moviesAdapter);

        }

    }
}
