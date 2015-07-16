package com.example.ashley.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    //protected ArrayAdapter<> mMovieAdaptor;

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private ArrayList<Movie> movies;
    private ImageAdapter mMovieAdaptor;
    private String sortValue;
    String apiKey;

    public MainActivityFragment() {
        sortValue = "popularity.desc";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mMovieAdaptor = new ImageAdapter(getActivity(), movies);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(mMovieAdaptor);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovieAdaptor.getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra("MovieObject", movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        apiKey = getActivity().getString(R.string.TMDBApiKey);
        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList("MOVIE_KEY");
            sortValue = savedInstanceState.getString("SORT_VALUE");
        } else {
            try {
                movies = new FetchMoviesTask().execute(apiKey, sortValue).get();
            } catch (InterruptedException|ExecutionException e) {
                Log.e(LOG_TAG, "Error executing AsyncTask");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_activity_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        } else if (id == R.id.highest_rated) {
            Log.v(LOG_TAG, "Highest Rated");
            sortValue = "vote_average.desc";
            updateMovies();
        } else if (id == R.id.most_popular) {
            Log.v(LOG_TAG, "Most Popular");
            sortValue = "popularity.desc";
            updateMovies();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMovies() {
        FetchMoviesTask movieTask = new FetchMoviesTask();
        try {
            movies = movieTask.execute(apiKey, sortValue).get();
            mMovieAdaptor.clear();
            mMovieAdaptor.setData(movies);
            mMovieAdaptor.notifyDataSetChanged();
        } catch (InterruptedException|ExecutionException e) {
            Log.e(LOG_TAG, "Error executing AsyncTask");
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("MOVIE_KEY", movies);
        savedInstanceState.putString("SORT_VALUE", sortValue);
        super.onSaveInstanceState(savedInstanceState);
    }
}
