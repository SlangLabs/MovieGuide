package com.esoxjem.movieguide.listing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.esoxjem.movieguide.R;
import com.esoxjem.movieguide.Constants;
import com.esoxjem.movieguide.details.MovieDetailsActivity;
import com.esoxjem.movieguide.details.MovieDetailsFragment;
import com.esoxjem.movieguide.Movie;

//import com.slanglabs.slang.SlangClient;
//import com.slanglabs.slang.SlangError;
//import com.slanglabs.slang.SlangIntentMapper;
//import com.slanglabs.slang.SlangIntentMapperBuilder;

import java.util.Map;

public class MoviesListingActivity extends AppCompatActivity implements MoviesListingFragment.Callback
{
    public static final String DETAILS_FRAGMENT = "DetailsFragment";
    private boolean twoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();

        if (findViewById(R.id.movie_details_container) != null) {
            twoPaneMode = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_container, new MovieDetailsFragment())
                    .commit();
            }
        } else {
            twoPaneMode = false;
        }

//        // Slang Labs Intent Mapper
//        SlangIntentMapper intentMapper = new SlangIntentMapperBuilder()
//                .handle(new String[]{"statement"}, new SlangIntentMapper.Callback() {
//                    @Override
//                    public void onIntent(String intent, Map<String, Object> entities) {
//
//                    }
//                })
//                .build();
//
//        // Create a local Client object
//
//        final Context that = this;
//
//        SlangClient client = new SlangClient(this)
//                //.setMode(SlangClient.SlangMode.LOCAL) // comment this for global client
//                .setIntentMapper(intentMapper)
//                .startAsync(new SlangClient.Listener() {
//                    @Override
//                    public void onStart() {
//                        // Nothing to do for now
//                    }
//
//                    @Override
//                    public void onTrigger() {
//                        // The voice interaction has been started
//                    }
//
//                    @Override
//                    public void onError(final SlangError error) {
//                        ((Activity) that).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                AlertDialog.Builder builder =
//                                    new AlertDialog.Builder(that);
//
//                                builder
//                                    .setTitle("Slang Error")
//                                    .setMessage(error.toString())
//                                    .show();
//                            }
//                        });
//                    }
//                });
    }

    private void setToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(R.string.movie_guide);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onMoviesLoaded(Movie movie)
    {
        if(twoPaneMode)
        {
            loadMovieFragment(movie);
        } else
        {
            // Do not load in single pane view
        }
    }

    @Override
    public void onMovieClicked(Movie movie)
    {
        if (twoPaneMode)
        {
            loadMovieFragment(movie);
        } else
        {
            startMovieActivity(movie);
        }
    }

    private void startMovieActivity(Movie movie)
    {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.MOVIE, movie);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void loadMovieFragment(Movie movie)
    {
        MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.getInstance(movie);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_details_container, movieDetailsFragment, DETAILS_FRAGMENT)
                .commit();
    }
}
