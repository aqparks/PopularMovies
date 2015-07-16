package com.example.ashley.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    TextView mMovieTitle;
    ImageView mMoviePoster;
    TextView mMovieReleaseDate;
    TextView mMovieDuration;
    TextView mMovieVoteAverage;
    TextView mMovieOverview;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        Movie movie = intent.getParcelableExtra("MovieObject");

        mMovieTitle = (TextView) rootView.findViewById(R.id.movie_title_text);
        mMoviePoster = (ImageView) rootView.findViewById(R.id.movie_poster_image);
        mMovieReleaseDate = (TextView) rootView.findViewById(R.id.movie_release_date_text);
        mMovieDuration = (TextView) rootView.findViewById(R.id.movie_duration_text);
        mMovieVoteAverage = (TextView) rootView.findViewById(R.id.movie_vote_average_text);
        mMovieOverview = (TextView) rootView.findViewById(R.id.movie_overview_text);

        if (movie != null) {
            mMovieTitle.setText(movie.getTitle());
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185" + movie.getPoster())
                    .into(mMoviePoster);
            mMovieReleaseDate.setText(movie.getReleaseDate());
            mMovieDuration.setText(Integer.toString(movie.getVoteCount()));
            mMovieVoteAverage.setText(Double.toString(movie.getVoteAverage()) + " / 10.0");
            mMovieOverview.setText(movie.getOverview());
        }

        return rootView;
    }
}
