package co.fabrk.popmovies.movie;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import co.fabrk.popmovies.Popup;
import co.fabrk.popmovies.R;
import co.fabrk.popmovies.tmdb.TMDBMovie;
import co.fabrk.popmovies.tmdb.TmdbConstants;
import co.fabrk.popmovies.ui.transitions.FabDialogMorphSetup;

public class MovieActivity extends AppCompatActivity implements MovieFragment.Callback {

    private View mUpButtonContainer;
    private View mUpButton;

    @Override
    public void onItemSelected(TMDBMovie movie, View view) {
        Intent popUp = new Intent(this, Popup.class);
        popUp.putExtra(FabDialogMorphSetup.EXTRA_SHARED_ELEMENT_START_COLOR,
                ContextCompat.getColor(this, R.color.accent));
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation
                (this, view, view.getTransitionName());
        startActivityForResult(popUp, 0, options.toBundle());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putParcelable(TmdbConstants.TMDB_MOVIE_PARCELABLE_NAME, getIntent().getParcelableExtra(TmdbConstants.TMDB_MOVIE_PARCELABLE_NAME));
            MovieFragment fragment = new MovieFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }

        mUpButtonContainer = findViewById(R.id.up_container);

        mUpButton = findViewById(R.id.action_up);
        mUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSupportNavigateUp();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
