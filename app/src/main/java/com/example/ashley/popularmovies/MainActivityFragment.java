package com.example.ashley.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

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
        mMovieAdaptor = new ImageAdapter(getActivity(), new ArrayList<Movie>());
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
            updateMovies();
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
            update();
            return true;
        } else if (id == R.id.highest_rated) {
            Log.v(LOG_TAG, "Highest Rated");
            sortValue = "vote_average.desc";
            update();
        } else if (id == R.id.most_popular) {
            Log.v(LOG_TAG, "Most Popular");
            sortValue = "popularity.desc";
            update();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean updateMovies() {
        if (isNetworkAvailable()) {
            FetchMoviesTask movieTask = new FetchMoviesTask();
            try {
                movies = movieTask.execute(apiKey, sortValue).get();
            } catch (InterruptedException | ExecutionException e) {
                Log.e(LOG_TAG, "Error executing AsyncTask");
                e.printStackTrace();
                return false;
            }

            return true;
        } else {
            Toast.makeText(getActivity(),
                    "A network connection is required.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void updateAdaptor() {
        mMovieAdaptor.clear();
        mMovieAdaptor.setData(movies);
        mMovieAdaptor.notifyDataSetChanged();
    }

    private void update() {
        if (updateMovies()) {
            updateAdaptor();
        }
    }

    //Based on a stackoverflow snippet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onStart() {
        update();
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("MOVIE_KEY", movies);
        savedInstanceState.putString("SORT_VALUE", sortValue);
        super.onSaveInstanceState(savedInstanceState);
    }
}
