package com.example.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {



    public MovieAdapter movieAdapter;
    //public ArrayAdapter movieAdapter;
    String[] paths = new String[20];
    String[] ids = new String[20];

   /* Movie[] movies ={
        new Movie(R.drawable.first),
            new Movie(R.drawable.second),
            new Movie(R.drawable.third),
            new Movie(R.drawable.fourth),
            new Movie(R.drawable.fifth),
            new Movie(R.drawable.sixth),
            new Movie(R.drawable.seventh)
    };*/

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_movie_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.action_refresh)
        {
            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute("popularity.desc");
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view =  inflater.inflate(R.layout.fragment_main, container, false);


        /*movieAdapter = new MovieAdapter(getActivity(), Arrays.asList(movies));*/

        movieAdapter = new MovieAdapter(getActivity(), Arrays.asList(paths));
        Log.v("CREATE", "FINE works---");

        GridView gridView = (GridView) view.findViewById(R.id.grid_view);

        gridView.setAdapter(movieAdapter);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        FetchMovieTask fetchMovieTask = new FetchMovieTask();
        fetchMovieTask.execute("popularity.desc");
    }

    class FetchMovieTask extends AsyncTask<String,Void,Integer>
    {
        final String OWM_RESULTS = "results";
        final String OWM_PATH = "poster_path";



        private void getMovieFromJson(String movieList)
        throws JSONException{
            JSONObject movie = new JSONObject(movieList);
            JSONArray results = movie.getJSONArray(OWM_RESULTS);
            for(int i=0;i<20;i++) {
                JSONObject first = results.getJSONObject(i);
                paths[i] = first.getString(OWM_PATH);
                ids[i] = first.getString("id");
            }
            Log.v("PRINT","HELLOOOOOOOOOS");
            for(int i=0;i<20;i++)
            {
                Log.v("APP","ID: " + ids[i] + "  PATHS: "+ paths[i]);
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieList = null;


            try
            {
                final String MOVIE_BASE_URL=
                        "http://api.themoviedb.org/3/discover/movie?";
                        final String SORT_PARAM = "sort_by";
                        final String APPID_PARAM = "api_key";

                Uri builtUti = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM,params[0])
                        .appendQueryParameter(APPID_PARAM,BuildConfig.API_KEY)
                        .build();
                URL url = new URL(builtUti.toString());
                Log.v("APP","Built URI " + builtUti.toString());
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
            Log.v("ONPOST","YEH FINALLY HERE!!!!!!!!!!!!!!");
            String url = "http://image.tmdb.org/t/p/w342/";
           if(paths != null)
            {
                movieAdapter.clear();
                for(int i=0;i<20;i++)
                {
                    Log.v("ONPOST","IN LOOP");
                    paths[i] = url+paths[i];
                    movieAdapter.add(paths[i]);
                }
            }
        }
    }
}
