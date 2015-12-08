package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Udit on 08-12-2015.
 */
public class MovieDetailFragment extends android.support.v4.app.Fragment {
    private  String selectedId;
    View rootView;
    public MovieDetailFragment()
    {
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        Intent intent = getActivity().getIntent();
        rootView = inflater.inflate(R.layout.movie_detail_fragment,container,false);
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)
                ) {
            selectedId = intent.getStringExtra(Intent.EXTRA_TEXT);

            FetchMovieInfoTask fetchMovieTask = new FetchMovieInfoTask();
            fetchMovieTask.execute(selectedId);

        }

            return rootView;
    }


    //-------------------------------------------------------------------//

    class FetchMovieInfoTask extends AsyncTask<String,Void,Integer>
    {

        String title,imageUrl,synopsis,release_date,rating;

        private void getMovieFromJson(String movieList)
                throws JSONException {
            JSONObject movie = new JSONObject(movieList);
            JSONObject collection = movie.getJSONObject("belongs_to_collection");

            String url = "http://image.tmdb.org/t/p/w185/";
            title = collection.getString("name");
            title = title.replace("Collection","");
            imageUrl=url + collection.getString("poster_path");
            synopsis=movie.getString("overview");
            release_date=movie.getString("release_date");
            rating=movie.getString("vote_average");
            rating = rating + "/10";
            Log.v("PRINT", "title: " + title);
            Log.v("PRINT", "title: " + imageUrl);
            Log.v("PRINT", "title: " + synopsis);
            Log.v("PRINT", "title: " + release_date);
            Log.v("PRINT", "title: " + rating);

        }

        @Override
        protected Integer doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieList = null;
            String ID = "";
            final String APPID_PARAM = "api_key";

            try
            {
                final String MOVIE_BASE_URL=
                        "http://api.themoviedb.org/3/movie/";

                Uri builtUti = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(params[0])
                        .appendQueryParameter(APPID_PARAM, BuildConfig.API_KEY)
                        .build();
                //CHECK--------------------
                URL url = new URL(builtUti.toString());
                Log.v("PRINT","Built URI " + builtUti.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    movieList = null;
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
                    movieList = null;
                }
                movieList = buffer.toString();
                Log.e("APP", "MOVIELIST" + movieList);

            } catch (IOException e) {
                Log.e("APP", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                movieList = null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("ONPOST", "Error closing stream", e);
                    }
                }
            }
            try {
                getMovieFromJson(movieList);
                Log.v("ONPOST","OHOHOHOHOHOHOHO");
            } catch (JSONException e) {
                Log.e("APP", e.getMessage(), e);
                e.printStackTrace();
            }
            Log.v("ONPOST","UUUUUU");
            return 1;
        }

        @Override
        protected void onPostExecute(Integer p)
        {
            ((TextView) rootView.findViewById(R.id.movie_title)).setText(title);
            ((TextView) rootView.findViewById(R.id.rating)).setText(rating);
            ((TextView) rootView.findViewById(R.id.synopsis)).setText(synopsis);
            ((TextView) rootView.findViewById(R.id.release_date)).setText(release_date);
            ImageView iconView = (ImageView)rootView.findViewById(R.id.movie_image);

            Picasso.with(getContext()).load(imageUrl).into(iconView);
        }
    }
}
