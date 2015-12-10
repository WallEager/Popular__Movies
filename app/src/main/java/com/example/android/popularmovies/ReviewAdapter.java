package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Udit on 10-12-2015.
 */

public class ReviewAdapter extends ArrayAdapter<ReviewData> {
    private ArrayList<ReviewData> reviews;

    public ReviewAdapter(Activity context,ArrayList<ReviewData> reviews)
    {
        super(context,0,reviews);
        this.reviews = reviews;
    }

    //getView talks about only one view that is inserting one image from the data film to grid_item
    @Override public View getView(int position, View convertView, ViewGroup parent)
    {
        Log.v("OKAY SO",Integer.toString(position));
        ReviewData review = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);
        }

        TextView name = (TextView)convertView.findViewById(R.id.person_name);
        TextView content = (TextView)convertView.findViewById(R.id.review);

        name.setText(review.review_name);
        content.setText(review.review_content);

        Log.v("FINAL",review.review_name);
        Log.v("FINAL",review.review_content);

        return convertView;
    }
}


