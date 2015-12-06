package com.example.android.popularmovies;

import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;



import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;



import android.content.Context;
import android.util.AttributeSet;


import com.squareup.picasso.Picasso;

import java.util.List;

/** An image view which always remains square with respect to its width. */



final class SquaredImageView extends ImageView {
    public SquaredImageView(Context context) {
        super(context);
    }

    public SquaredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}

/**
 * Created by Udit on 06-12-2015.
 */
public class MovieAdapter extends ArrayAdapter<String> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private final Context context;
    public MovieAdapter(Activity context,List<String> movies)
    {

        super(context,0);
        this.context = context;
    }

    //getView talks about only one view that is inserting one image from the data film to grid_item
   @Override public View getView(int position, View convertView, ViewGroup parent)
    {

       // Movie movie = getItem(position);
        SquaredImageView view = (SquaredImageView) convertView;
        if (view == null) {
            view = new SquaredImageView(context);
        }
        String movie = getItem(position);
        /*View rootView = LayoutInflater.from(getContext()).inflate(R.layout
                .grid_item, parent, false);
        ImageView iconView = (ImageView)rootView.findViewById(R.id.grid_item_image);*/
        Log.v("ONPOST", movie);
        Picasso.with(context).load(movie).resize(200,200).into(view);
        Log.v("ONPOST", "INSIDE GETVIEW");
        return view;
    }
}
