package com.esoxjem.movieguide.listing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.esoxjem.movieguide.R;
import com.esoxjem.movieguide.Constants;
import com.esoxjem.movieguide.details.MovieDetailsActivity;
import com.esoxjem.movieguide.details.MovieDetailsFragment;
import com.esoxjem.movieguide.Movie;

import com.slanglabs.slang.SlangClient;
import com.slanglabs.slang.SlangError;
import com.slanglabs.slang.SlangIntentMapper;

import java.util.List;
import java.util.Map;

public class MoviesListingActivity extends AppCompatActivity implements MoviesListingFragment.Callback, SlangClient.Listener
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

        // Slang Labs Intent Mapper
        SlangClient.Callback cb = new SlangClient.Callback() {
                    @Override
                    public void onIntentDetected(
                        Context context,
                        String intent,
                        Map<String, String> entities
                    ) {
                        List<Movie> movies = MoviesListingParser.getMovies();

                        for (Movie movie: movies) {
                            if (movie.getTitle().contains("Spider")) {
                                startMovieActivity(context, movie);
                                break;
                            }
                        }
                    }
                };

        SlangClient.getInstance()
            .init(this)
            .setAPIKey("3dd1f449-d4d0-4389-b64f-821a9982d61b")
            .registerIntents(new String[]{"intent_capture"}, cb);

    }

    @Override
    public void onResume() {
        super.onResume();
        SlangClient.getInstance()
            .show(this);
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
            startMovieActivity(this, movie);
        }
    }

    private void startMovieActivity(Context context, Movie movie)
    {
        Intent intent = new Intent(context, MovieDetailsActivity.class);
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

    @Override
    public void onSlangStart() {
        Log.d("SlangClient", "Started client callback..");
    }

    @Override
    public void onSlangTrigger() {
        Log.d("SlangClient", "Triggered client callback..");
    }

    @Override
    public void onSlangError(SlangError error) {

    }
}
