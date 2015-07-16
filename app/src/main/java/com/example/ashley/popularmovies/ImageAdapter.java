package com.example.ashley.popularmovies;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * The <code>ImageAdapter</code> class is provided urls for movie posters
 * and it will populate a grid view with the movie posters.
 * References:
 * http://www.pcsalt.com/android/listview-using-baseadapter-android/
 * http://techiedreams.com/android-custom-gridview-scalable-auto-adjusting-col-width/
 *
 * @author aqparks@gmail.com
 * @version 1.0
 *
 */


public class ImageAdapter extends BaseAdapter {
    private final String LOG_TAG = ImageAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Movie> mMovies;
    private int height, width;

    public ImageAdapter(Context context, ArrayList<Movie> movies) {
        this.mContext = context;
        this.mMovies = movies;
        mInflater = LayoutInflater.from(context);


        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    public int getCount() {
        //Log.v(LOG_TAG, "There are " + mMovies.size() + " posters");
        return mMovies.size();
    }

    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = (ImageView) mInflater.inflate(R.layout.grid_item_movie, null);
            imageView.setLayoutParams(new GridView.LayoutParams(width/2, height/2));
        } else {
            imageView = (ImageView) convertView;
        }

        String url = "http://image.tmdb.org/t/p/w185" + getItem(position).getPoster();
        //Log.v(LOG_TAG, url);

        Picasso.with(mContext).load(url).into(imageView);
        return imageView;
    }

    public void clear() {
        this.mMovies = new ArrayList<>();
        //this.notifyDataSetChanged();
    }

    public void setData(ArrayList<Movie> movies) {
        this.mMovies = movies;
    }
}