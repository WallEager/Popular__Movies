package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Udit on 08-12-2015.
 */
public class MovieDetailFragment extends android.support.v4.app.Fragment {
    private  String selectedId;
    public MovieDetailFragment()
    {
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.movie_detail_fragment,container,false);
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)
                ) {
            selectedId = intent.getStringExtra(Intent.EXTRA_TEXT);

            ((TextView) rootView.findViewById(R.id.movie_title)).setText("HELLO");
        }

            return rootView;
    }
}
