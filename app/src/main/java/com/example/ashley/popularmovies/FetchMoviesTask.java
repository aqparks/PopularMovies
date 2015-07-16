package com.example.ashley.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The <code>FetchMoviesTask</code> implements <code>AsynchTask</code> and when
 * executed will send a HTTP get request to the restful api hosted by
 * themoviedb.org.
 *
 * @author aqparks@gmail.com
 * @version 1.0
 */
public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();


    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        if (params[0] == null || params[1] == null) {
            Log.e(LOG_TAG, "Error: API key or sort preference not provided");
            return new ArrayList<Movie>();
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        final String DISCOVER_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
        final String API_KEY = params[0];
        final String SORT_PARAM = "sort_by";
        final String API_PARAM = "api_key";

        final String sortValue = params[1];

        try {
            Uri buildUri = Uri.parse(DISCOVER_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, sortValue)
                    .appendQueryParameter(API_PARAM, API_KEY)
                    .build();

            URL url = new URL(buildUri.toString());

            //Log.v(LOG_TAG, "Built URI " + buildUri.toString());

            // Create connection and send request to TMDB
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read response
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                Log.d(LOG_TAG, "No response was received.");
            } else {

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line);
                    buffer.append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    Log.v(LOG_TAG, "Stream was empty.");
                    return null;
                }

                moviesJsonStr = buffer.toString();
                //Log.v(LOG_TAG, "Movie JSON String: " + moviesJsonStr);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException");
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "IOException: error closing reader");
                    e.printStackTrace();
                }
            }
        }

        try {
            return getMovieDataFromJson(moviesJsonStr, 12);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSONException");
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr, int numMovies)
        throws JSONException {

        // Created using http://json.parser.online.fr/
        // These are the names of the JSON objects that need to be extracted.
        final String TMDB_RESULTS = "results";
        final String TMDB_BACKDROP = "backdrop_path";
        final String TMDB_GENRES = "genre_ids";
        final String TMDB_ID = "id";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_POSTER = "poster_path";
        final String TMDB_POPULARITY = "popularity";
        final String TMDB_TITLE = "title";
        final String TMDB_VOTE_AVG = "vote_average";
        final String TMDB_VOTE_COUNT = "vote_count";

        JSONArray movieArray = new JSONObject(movieJsonStr).getJSONArray(TMDB_RESULTS);

        if (movieArray == null) {
            Log.e(LOG_TAG, "Null movieJson object.");
        }

        ArrayList<Movie> movies = new ArrayList<>();
        for(int i = 0; i < movieArray.length(); i++) {

            Movie movie = new Movie();

            // Get the JSON object representing the movieJson
            JSONObject movieJson = movieArray.getJSONObject(i);

            // Start extracting data
            movie.setBackdrop(movieJson.getString(TMDB_BACKDROP));

            // Genres are stored as ints in a JSONArray,
            // so we have to parse them into a regular array
            JSONArray jsonGenres = movieJson.getJSONArray(TMDB_GENRES);
            ArrayList<Integer> genres = new ArrayList<>();
            for (int x = 0; x < jsonGenres.length(); x++) {
                genres.add(jsonGenres.getInt(x));
            }
            movie.setGenres(genres);
            movie.setId(movieJson.getInt(TMDB_ID));
            movie.setOverview(movieJson.getString(TMDB_OVERVIEW));
            movie.setReleaseDate(movieJson.getString(TMDB_RELEASE_DATE));
            movie.setPoster(movieJson.getString(TMDB_POSTER));
            movie.setPopularity(movieJson.getDouble(TMDB_POPULARITY));
            movie.setTitle(movieJson.getString(TMDB_TITLE));
            movie.setVoteAverage(movieJson.getDouble(TMDB_VOTE_AVG));
            movie.setVoteCount(movieJson.getInt(TMDB_VOTE_COUNT));

            movies.add(movie);
        }
        return movies;
    }
}
