package co.fabrk.popmovies.discover;

import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;

import co.fabrk.popmovies.tmdb.TmdbConstants;
import co.fabrk.popmovies.jobs.FetchTmdbMovies;
import co.fabrk.popmovies.R;
import co.fabrk.popmovies.data.TmdbContract;
import co.fabrk.popmovies.tmdb.TMDBMovie;
import co.fabrk.popmovies.BuildConfig;
//import co.fabrk.popmovies.Injection;
import butterknife.Bind;
import butterknife.ButterKnife;


public class DiscoverFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    View mRootView;
    private static final String TAG = "DiscoverFragment";
    //    private DiscoverContract.UserActionsListener mActionsListener;
    private String selectedTabParcelableName = "selectedTab";
    private String currentPositionParcelableName = "currentPosition";
    private String sortOption = TmdbConstants.SORT_VALUE_POPULAR;

    //Todo: implement view holder
    @Bind(R.id.splash_screen_view)
    ImageView splashScreenImageView;

    @Bind(R.id.main_tab_layout)
    TabLayout tabLayout;

    @Bind(R.id.gridview_movies)
    GridView discoverGridView;

    @Bind(R.id.empty_image_view)
    ImageView emptyView;

    @Bind(R.id.main_layout)
    LinearLayout mainLayout;

    @Bind(R.id.fab_anim)
    FloatingActionButton fabAnim;

    @Bind(R.id.fab_sync)
    FloatingActionButton fabSync;

    // Adapter
    private MovieCursorAdapter mMovieAdapter;

    // Loader
    private static final int POPULAR_MOVIE_LOADER = 0;
    private static final int HIGHEST_RATED_MOVIE_LOADER = 1;
    private static final int FAVORITE_MOVIE_LOADER = 2;

    // State variable - @save session
    private int mPosition = GridView.INVALID_POSITION;

    private void updateMovie() {
        //Most popular
        FetchTmdbMovies movieTaskPopular = new FetchTmdbMovies(getActivity().getContentResolver());
        movieTaskPopular.execute(TmdbConstants.POPULAR);
        //Highest Rated
        FetchTmdbMovies movieTaskHighestRated = new FetchTmdbMovies(getActivity().getContentResolver());
        movieTaskHighestRated.execute(TmdbConstants.HIGHEST_RATED);
    }

    //****************************************************************************//
    //                                 Fragment Initialisation                    //
    //****************************************************************************//

    private void setViews(View rootView) {

        // Initliazing tabs
        tabLayout.addTab(tabLayout.newTab().setText(TmdbConstants.SORT_VALUE_POPULAR).setIcon(R.drawable.ic_whatshot_black));
        tabLayout.addTab(tabLayout.newTab().setText(TmdbConstants.SORT_VALUE_HIGHEST_RATED).setIcon(R.drawable.ic_star_black));
        tabLayout.addTab(tabLayout.newTab().setText(TmdbConstants.SORT_VALUE_FAVORITE).setIcon(R.drawable.ic_favorite));
        discoverGridView.setEmptyView(emptyView);
        discoverGridView.setAdapter(mMovieAdapter);
    }

    // Setting the listeners
    private void setListeners() {

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                startLoader(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        discoverGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                view.setTransitionName(getString(R.string.transition_thumbnail));
                TMDBMovie movie = new TMDBMovie(cursor);
                if (cursor != null) {
                    ((Callback) getActivity())
                            .onItemSelected(movie, view);
                }
                mPosition = position;
            }
        });


        //FAB
        fabSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMovie();
                Snackbar.make(view, "Resynch launched", Snackbar.LENGTH_LONG)
                        .show();
            }
        });

        fabAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animSplash();

            }
        });


    }

// Unregistering the listeners
    private void unsetListeners() {
        tabLayout.setOnTabSelectedListener(null);
        discoverGridView.setOnItemClickListener(null);
        fabSync.setOnClickListener(null);
        fabAnim.setOnClickListener(null);
    }

    //****************************************************************************//
    //                                 Fragment LifeCycle                         //
    //****************************************************************************//

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(selectedTabParcelableName, sortOption);
        outState.putInt(currentPositionParcelableName, mPosition);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            sortOption = savedInstanceState.getString(selectedTabParcelableName);
            mPosition = savedInstanceState.getInt(currentPositionParcelableName);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(POPULAR_MOVIE_LOADER, null, this);
//        mActionsListener = new DiscoverPresenter(Injection.provideTmdbService(), this);

    }

    @Override
    public void onResume() {
        super.onResume();
        setListeners();
//        Log.e(TAG, "onResume: ");
//        if (sortOption.equals("favorite")) {
//            updateMovie();
//        } else getLoaderManager().initLoader(FAVORITE_MOVIE_LOADER, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        unsetListeners();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMovieAdapter = new MovieCursorAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, rootView);
        setViews(rootView);
        // Start Animation
        showSplash();
        animSplash();
//        showMovieGrid();
//        setListeners();
        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_pop) {
            sortOption = TmdbConstants.SORT_VALUE_POPULAR;
            FetchTmdbMovies movieTask = new FetchTmdbMovies(getActivity().getContentResolver());
            movieTask.execute(sortOption);
            getLoaderManager().initLoader(POPULAR_MOVIE_LOADER, null, this);
            return true;
        }
        if (id == R.id.action_sort_rate) {
            sortOption = TmdbConstants.SORT_VALUE_HIGHEST_RATED;
            FetchTmdbMovies movieTask = new FetchTmdbMovies(getActivity().getContentResolver());
            movieTask.execute(sortOption);
            getLoaderManager().initLoader(HIGHEST_RATED_MOVIE_LOADER, null, this);
            return true;
        }
        if (id == R.id.action_favorite_movie) {
            sortOption = TmdbConstants.SORT_VALUE_FAVORITE;
            getLoaderManager().initLoader(FAVORITE_MOVIE_LOADER, null, this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //****************************************************************************//
    //                                 Cursor Loader                              //
    //****************************************************************************//

    // Mapping selected option and loader
    private void startLoader(int position) {
        switch (position) {
            case POPULAR_MOVIE_LOADER:
                getLoaderManager().initLoader(POPULAR_MOVIE_LOADER, null, this);
                break;
            case HIGHEST_RATED_MOVIE_LOADER:
                getLoaderManager().initLoader(HIGHEST_RATED_MOVIE_LOADER, null, this);
                break;
            case FAVORITE_MOVIE_LOADER:
                getLoaderManager().initLoader(FAVORITE_MOVIE_LOADER, null, this);
                break;
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case POPULAR_MOVIE_LOADER:
                Uri popularMovieUri = TmdbContract.PopularEntry.CONTENT_URI;
                Log.e("URI", "onCreateLoader: " + popularMovieUri.toString());
                return new CursorLoader(getActivity(),
                        popularMovieUri,
                        null,
                        null,
                        null,
                        null);
            case HIGHEST_RATED_MOVIE_LOADER:
                Uri highestRatedMovieUri = TmdbContract.HighestRatedEntry.CONTENT_URI;
                Log.e("URI", "onCreateLoader: " + highestRatedMovieUri.toString());
                return new CursorLoader(getActivity(),
                        highestRatedMovieUri,
                        null,
                        null,
                        null,
                        null);
            case FAVORITE_MOVIE_LOADER:
                Uri favoriteMovieUri = TmdbContract.FavoriteEntry.CONTENT_URI;
                Log.e("URI", "onCreateLoader: " + favoriteMovieUri.toString());
                return new CursorLoader(getActivity(),
                        favoriteMovieUri,
                        null,
                        null,
                        null,
                        null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        // If there is no record in the database
        Log.e("Cursor count", "onLoadFinished: " + cursor.getCount());

        if (cursor.getCount() == 0) {
            Snackbar.make(getView(), "No item founds", Snackbar.LENGTH_LONG)
                    .setAction("Check server", null).show();
            updateMovie();

        }
        mMovieAdapter.swapCursor(cursor);

//        // Loading the first ovie in the detail Screen for the two pane UI
//        if ((cursor != null) && cursor.getCount()>0) {
//            Cursor cursorMovie = (Cursor) mMovieAdapter.getItem(0);
//            TMDBMovie movie = new TMDBMovie(cursorMovie);
//            mPosition = 0;
//            ((Callback) getActivity())
//                    .onItemSelected(movie, discoverGridView.getChildAt(0));
//        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mMovieAdapter.swapCursor(null);
    }

    //****************************************************************************//
    //                                 Event call back                            //
    //****************************************************************************//

    public interface Callback {
        void onItemSelected(TMDBMovie movie, View view);
    }

    //****************************************************************************//
    //                                 UI Commands                                //
    //****************************************************************************//

    public void showMovieGrid() {
        // Show main_layout
        splashScreenImageView.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
    }

    public void showSplash() {
        // Show branded splash screen
        splashScreenImageView.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
    }

    private void animSplash() {
        Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.hyperspace_jump);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showMovieGrid();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splashScreenImageView.startAnimation(animation2);
    }
}