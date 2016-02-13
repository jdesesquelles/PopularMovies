package co.fabrk.popmovies;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import co.fabrk.popmovies.tmdb.TmdbConstants;
import co.fabrk.popmovies.movie.MovieActivity;
import co.fabrk.popmovies.movie.MovieFragment;
import co.fabrk.popmovies.discover.DiscoverFragment;
import co.fabrk.popmovies.tmdb.TMDBMovie;

public class MainActivity extends AppCompatActivity implements DiscoverFragment.Callback {
    private static final String TAG = "MainActivity";
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    @Override
    public void onItemSelected(TMDBMovie movie, View view) {
        // In the two pane case, the movie fragment is replace using the fragment manager
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(TmdbConstants.TMDB_MOVIE_PARCELABLE_NAME, movie);
            MovieFragment fragment = new MovieFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            // In the one pane case, the movie activity is kicked off
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this, view, view.getTransitionName()).toBundle();
            Intent intent = new Intent(this, MovieActivity.class)
                            .putExtra(TmdbConstants.TMDB_MOVIE_PARCELABLE_NAME, movie)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent, bundle);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Lazy init, the favorite array list object is created when adding a new movie
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getWindow().setBackgroundDrawable(null);

        if (findViewById(R.id.movie_detail_container) != null) {
            MovieFragment detailFragment = new MovieFragment();
            mTwoPane = true;
            if (savedInstanceState == null) {
                Bundle args = new Bundle();
                args.putParcelable(TmdbConstants.TMDB_MOVIE_PARCELABLE_NAME, getIntent().getParcelableExtra(TmdbConstants.TMDB_MOVIE_PARCELABLE_NAME));
                MovieFragment fragment = new MovieFragment();
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_detail_container, fragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
