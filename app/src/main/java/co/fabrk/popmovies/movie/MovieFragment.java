package co.fabrk.popmovies.movie;

import android.animation.ValueAnimator;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.Swatch;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v4.app.SharedElementCallback;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.fabrk.popmovies.R;
import co.fabrk.popmovies.data.TmdbContract;
import co.fabrk.popmovies.tmdb.TMDBMovie;
import co.fabrk.popmovies.tmdb.TMDBReview;
import co.fabrk.popmovies.tmdb.TMDBTrailer;
import co.fabrk.popmovies.tmdb.TmdbConstants;
import co.fabrk.popmovies.tmdb.TmdbDatabaseOperations;
import co.fabrk.popmovies.tmdb.TmdbProviderUtils;
import co.fabrk.popmovies.ui.transitions.FabDialogMorphSetup;
import co.fabrk.popmovies.ui.transitions.MorphDialogToFab;
import co.fabrk.popmovies.ui.transitions.MorphFabToDialog;
import co.fabrk.popmovies.ui.utils.ColorUtils;
import co.fabrk.popmovies.ui.utils.DateUtils;
import co.fabrk.popmovies.ui.widget.ParallaxScrimageView;

public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private TMDBMovie mMovie;
    private Uri mUri;
    public static final String DETAIL_URI = "URI";
    private static final String TAG = "MovieFragment";
    //    private ViewHolder viewHolder;
    private Palette mPalette;
    private ArrayMap mapSwatch;
    private ArrayMap mapPalette;
    private static final int RATE_COLOR_TRANSLATION = -20;
    private static final int COLOR_BRIGHTNESS_INCREASE_TRANSLATION = -20;
    private static final int COLOR_BRIGHTNESS_DECREASE_TRANSLATION = 30;

    // Cursor loader
    private static final int MOVIE_TRAILERS_LOADER = 3;
    private static final int MOVIE_REVIEWS_LOADER = 4;
    // Cursor adapter
    private ReviewCursorAdapter mReviewAdapter;
    private TrailerCursorAdapter mTrailerAdapter;

    @Bind(R.id.movie_detail_container)
    ViewGroup parentViewGroup;
    //        LinearLayout movieDetaillinearLayout;
    @Bind(R.id.layout_detail_information)
    LinearLayout detailInformationlinearLayout;
    @Bind(R.id.container)
    LinearLayout popupLayout;
    @Bind(R.id.shot)
    ParallaxScrimageView imageView;
    @Bind(R.id.detail_scrollview)
    ScrollView detailScrollView;
    @Bind(R.id.rating_bar)
    FrameLayout ratingBarLayout;
    View rootView;
    @Bind(R.id.textview_title)
    TextView titleView;
    @Bind(R.id.imageview_poster_detail_fragment)
    ImageView posterView;
    @Bind(R.id.backdrop)
    ImageView backdropView;
    @Bind(R.id.textview_release_date)
    TextView dateView;
    @Bind(R.id.textview_rating)
    TextView ratingView;
    @Bind(R.id.textview_overview)
    TextView overviewView;
    @Bind(R.id.detail_trailer_recycler_view)
    RecyclerView trailerList;
    @Bind(R.id.detail_review_recycler_view)
    RecyclerView reviewList;
    @Bind(R.id.fab_favorite)
    FloatingActionButton fabFavorite;
    @Bind(R.id.separator_reviews)
    View separatorReviews;
    @Bind(R.id.separator_trailers)
    View separatorTrailers;
    @Bind(R.id.detail_review_textview_title)
    TextView reviewsTitleTextview;
    @Bind(R.id.detail_trailer_textview_title)
    TextView trailersTitleTextview;
    @Bind(R.id.splash_screen_detail_view)
    ImageView splashScreenDetailView;
    @Bind(R.id.include_detail_section_trailers)
    View includeDetailSectionTrailers;
    @Bind(R.id.include_detail_section_reviews)
    View includeDetailSectionReviews;


    public MovieFragment() {
        setHasOptionsMenu(true);
    }

    //****************************************************************************//
    //                                 Fragment LifeCycle                         //
    //****************************************************************************//

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle arg = getArguments();
        if (arg != null) {
            mMovie = arg.getParcelable(TmdbConstants.TMDB_MOVIE_PARCELABLE_NAME);
            if (mMovie != null) {
                if ((mMovie.getTmdbReviews() != null) && (mMovie.getTmdbTrailers() != null)) {
                    return;
                }
                getLoaderManager().initLoader(MOVIE_REVIEWS_LOADER, arg, this);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        rootView = inflater.inflate(R.layout.detail_fragment, container, false);
        ButterKnife.bind(this, rootView);
        Bundle arguments = getArguments();
        if (mMovie != null) {
//        setExitSharedElementCallback(fabLoginSharedElementCallback);
            showMovieDetail(mMovie);
//            setListeners();
        } else {
            showEmptyScreen(); //splashScreenDetailView
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMovie != null) {
            setListeners();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unsetListeners();
    }

    // Listeners
    private void setListeners() {
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFavorite(fabFavorite);
            }
        });

    }

    private void unsetListeners() {
        fabFavorite.setOnClickListener(null);

    }

    public void toggleFavorite(FloatingActionButton fb) {
        final FloatingActionButton fab = fb;
        if (TmdbProviderUtils.isFavoriteMovie(mMovie, getContext())) {
            TmdbDatabaseOperations.deleteFromFavorite(mMovie.getId(), getContext().getContentResolver());
            fb.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_outline_24dp));
            Snackbar.make(getView(), "Movie deleted from favorite", Snackbar.LENGTH_LONG)
                    .setAction("Deleted from favorite", null).show();
        } else {
            TmdbDatabaseOperations.addToFavorite(mMovie, getContext().getContentResolver());
            fb.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite));
            Snackbar.make(getView(), "Movie added to favorite", Snackbar.LENGTH_LONG)
                    .setAction("Added to favorite", null).show();
        }
        // Launching by calling back the MovieActivity
//            ((Callback) getActivity())
//                    .onItemSelected(mMovie, fab);
//        viewHolder.popupLayout.setVisibility(View.GONE);
//        //
//
//        ArcMotion arcMotion = new ArcMotion();
//        arcMotion.setMinimumHorizontalAngle(50f);
//        arcMotion.setMinimumVerticalAngle(50f);
//        int color = ((Swatch) mapSwatch.get("DarkMuted")).getRgb();
//        Interpolator easeInOut =
//                AnimationUtils.loadInterpolator(getActivity(), android.R.interpolator.fast_out_slow_in);
//        MorphFabToDialog sharedEnter =
//                new MorphFabToDialog(color, dialogCornerRadius, startCornerRadius);
//
//
//        sharedEnter.setPathMotion(arcMotion);
//        sharedEnter.setInterpolator(easeInOut);
//        MorphDialogToFab sharedReturn = new MorphDialogToFab(color, startCornerRadius);
//        sharedReturn.setPathMotion(arcMotion);
//        sharedReturn.setInterpolator(easeInOut);
//        if (target != null) {
//            sharedEnter.addTarget(fab);
//            sharedReturn.addTarget(target);
//        }
//        activity.getWindow().setSharedElementEnterTransition(sharedEnter);
//        activity.getWindow().setSharedElementReturnTransition(sharedReturn);
//
//
//
//        // Launching from the fragment directly
//        Intent popUp = new Intent(getActivity(), Popup.class);
//        popUp.putExtra(FabDialogMorphSetup.EXTRA_SHARED_ELEMENT_START_COLOR,
//                ContextCompat.getColor(getActivity(), R.color.accent));
//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation
//                (getActivity(), fab, getString(R.string.transition_popup));
//        getActivity().startActivityForResult(popUp, 0, options.toBundle());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_fragment, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TmdbConstants.TMDB_MOVIE_PARCELABLE_NAME, mMovie);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(TmdbConstants.TMDB_MOVIE_PARCELABLE_NAME);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //****************************************************************************//
    //                                 Setting the UI                             //
    //****************************************************************************//

    private void showEmptyScreen() {
        fabFavorite.setVisibility(View.GONE);
        popupLayout.setVisibility(View.GONE);
        detailScrollView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        splashScreenDetailView.setVisibility(View.VISIBLE);
    }

    private SharedElementCallback fabLoginSharedElementCallback = new SharedElementCallback() {
        @Override
        public Parcelable onCaptureSharedElementSnapshot(View sharedElement,
                                                         Matrix viewToGlobalMatrix,
                                                         RectF screenBounds) {
            // store a snapshot of the fab to fade out when morphing to the login dialog
            int bitmapWidth = Math.round(screenBounds.width());
            int bitmapHeight = Math.round(screenBounds.height());
            Bitmap bitmap = null;
            if (bitmapWidth > 0 && bitmapHeight > 0) {
                bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
                sharedElement.draw(new Canvas(bitmap));
            }
            return bitmap;
        }
    };

    public void showMovieDetail(TMDBMovie movie) {

        fabFavorite.setVisibility(View.VISIBLE);
        popupLayout.setVisibility(View.VISIBLE);
        detailScrollView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        splashScreenDetailView.setVisibility(View.GONE);

        rootView.setVerticalScrollBarEnabled(true);
        titleView.setText(movie.getTitle());
        titleView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), getActivity().getString(R.string.customTextFont)));
        Picasso.with(rootView.getContext()).load(movie.getThumbnailPath()).into(posterView);
        loadBitmap(movie.getBackdrop());
        dateView.setText(DateUtils.formatDate(movie.getReleaseDate()));
        ratingView.setText(movie.getVoteAverage());
        overviewView.setText(movie.getOverview());
        if (movie.getTmdbTrailers() != null) {
            showTrailers(movie.getTmdbTrailers());
            includeDetailSectionTrailers.setVisibility(View.VISIBLE);
        } else {
            includeDetailSectionTrailers.setVisibility(View.GONE);
        }
        if (movie.getTmdbReviews() != null) {
            showReviews(movie.getTmdbReviews());
            includeDetailSectionReviews.setVisibility(View.VISIBLE);
        } else {
            includeDetailSectionReviews.setVisibility(View.GONE);
        }

        popupLayout.setVisibility(View.GONE);

        // Fab
        if (TmdbProviderUtils.isFavoriteMovie(mMovie, getContext())) {
            fabFavorite.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite));
        } else {
            fabFavorite.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_outline_24dp));
        }
        fabFavorite.setTransitionName(getString(R.string.transition_popup));

    }

    public void showTrailers(ArrayList<TMDBTrailer> trailers) {
        if (mTrailerAdapter == null) {
            mTrailerAdapter = new TrailerCursorAdapter(getActivity());
        }
        trailerList.setAdapter(mTrailerAdapter);
        trailerList.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    public void showReviews(ArrayList<TMDBReview> reviews) {
        if (mReviewAdapter == null) {
            mReviewAdapter = new ReviewCursorAdapter(getActivity());
        }
        reviewList.setAdapter(mReviewAdapter);
        reviewList.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    //****************************************************************************//
    //                         pick palette, Color and Animation                  //
    //****************************************************************************//

    private Target loadtarget;

    private void loadBitmap(String url) {
        if (loadtarget == null) loadtarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // do something with the Bitmap
                handleLoadedBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(getContext()).load(url).into(loadtarget);
    }

    private void handleLoadedBitmap(Bitmap b) {
        backdropView.setImageBitmap(b);
        Palette.from(b).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                mPalette = palette;
                mapSwatch = ColorUtils.processSwatch(palette);
                mapPalette = ColorUtils.processPalette(getContext(), palette);
                animator();
            }
        });
        // do something here
    }

    private void animator() {
        int animationDuration = 500;
        int animationStartDelay = animationDuration / 3;
        detailInformationlinearLayout.setBackground(null);
        overviewView.setBackground(null);
        trailerList.setBackground(null);
        reviewList.setBackground(null);
        animateColorTransition(animationDuration, animationStartDelay);
    }

    private void animateColorTransition(int duration, int titleStartDelay) {
        // Initialization
        int currentAccent = getResources().getColor(R.color.accent);
        int currentPrimaryColor = getResources().getColor(R.color.primary);
        int currentPrimaryLightColor = getResources().getColor(R.color.primary_light);
        int currentPrimaryDarkColor = getResources().getColor(R.color.primary_dark);
        int currentColorPrimaryBright = currentPrimaryDarkColor;
        // Target values
        int colorPrimaryBright = getResources().getColor(R.color.primary_dark);
        int colorPrimary;
        int colorPrimaryLight;
        int colorPrimaryDark = getResources().getColor(R.color.primary_dark);
        ;
        int colorAccent;
        // Iterate in the swatch map
        String currentPalette = "app.theme";
        String targetPalette = "app.theme";
        int mapEndIndex = mapSwatch.keySet().size() - 1;
        Log.e(TAG, mMovie.getTitle() + " - swatchKeySet : " + mapEndIndex);

        String[] keyTab = new String[]{
                ColorUtils.colorKeyLightMuted,
                ColorUtils.colorKeyMuted,
                ColorUtils.colorKeyDarkMuted,
                ColorUtils.colorKeyVibrant,
                ColorUtils.colorKeyDarkVibrant};

        for (String key : keyTab) {
            if (mapSwatch.get(key) == null) {
                Log.e(TAG, mMovie.getTitle() + " - swatchKeySet : " + key + " didn't exist, skipped");
                continue;
            }
            Log.e(TAG, mMovie.getTitle() + " - swatchKeySet : " + key + " has been found");

            targetPalette = key;
            colorPrimary = ((Swatch) mapSwatch.get(key)).getRgb();
            colorPrimaryDark = ColorUtils.getColorWithTranslateBrightness(((Swatch) mapSwatch.get(key)).getRgb(), COLOR_BRIGHTNESS_INCREASE_TRANSLATION);
            colorPrimaryLight = ColorUtils.getColorWithTranslateBrightness(((Swatch) mapSwatch.get(key)).getRgb(), COLOR_BRIGHTNESS_DECREASE_TRANSLATION);
            colorAccent = ((Swatch) mapSwatch.get(key)).getTitleTextColor();

            applyColorAccent(titleView, "textColor", duration, titleStartDelay, currentAccent, colorAccent);
            applyColorAccent(dateView, "textColor", duration, titleStartDelay, currentAccent, colorAccent);
            applyColorAccent(overviewView, "textColor", duration, titleStartDelay, currentAccent, colorAccent);
            applyColorAccent(trailersTitleTextview, "textColor", duration, titleStartDelay, currentAccent, colorAccent);
            applyColorAccent(reviewsTitleTextview, "textColor", duration, titleStartDelay, currentAccent, colorAccent);

            applyColorAccent(titleView, "backgroundColor", duration, titleStartDelay, currentPrimaryDarkColor, colorPrimaryDark);
            applyColorAccent(separatorTrailers, "backgroundColor", duration, titleStartDelay, currentPrimaryDarkColor, colorPrimaryDark);
            applyColorAccent(separatorReviews, "backgroundColor", duration, titleStartDelay, currentPrimaryDarkColor, colorPrimaryDark);
            applyColorAccent(parentViewGroup, "backgroundColor", duration, titleStartDelay, currentPrimaryLightColor, colorPrimaryLight);
            applyColorAccent(trailersTitleTextview, "backgroundColor", duration, titleStartDelay, currentPrimaryColor, colorPrimary);
            applyColorAccent(reviewsTitleTextview, "backgroundColor", duration, titleStartDelay, currentPrimaryColor, colorPrimary);

            currentPalette = targetPalette;
            currentAccent = colorAccent;
            currentPrimaryColor = colorPrimary;
            currentPrimaryDarkColor = colorPrimaryDark;
            currentColorPrimaryBright = colorPrimaryBright;
            currentPrimaryLightColor = colorPrimaryLight;

            titleStartDelay = titleStartDelay + duration;
            switch (duration) {
                case 2000:
                    duration = 500;
                    break;
                case 500:
                    duration = 250;
                    break;
                case 250:
                    duration = 750;
                    break;
                case 750:
                    duration = 1000;
                    break;
                case 1000:
                    duration = 500;
                    break;
            }

        }
        ValueAnimator colorAnimation = ColorUtils.animateColorValue(500, 0, currentPrimaryDarkColor, colorPrimaryDark);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
//                viewHolder..setBackgroundColor((int) animator.getAnimatedValue());
                getActivity().getWindow().setStatusBarColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    private void applyColorAccent(View view, String property, int duration, int titleStartDelay, int currentAccent, int colorAccent) {
        ColorUtils.animateObjectViewColor(duration, titleStartDelay, view, property, currentAccent, colorAccent).start();
    }


    //****************************************************************************//
    //                                 Cursor Loader                              //
    //****************************************************************************//

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        mMovie = bundle.getParcelable(TmdbConstants.TMDB_MOVIE_PARCELABLE_NAME);
        String movieId = mMovie.getId();
        if (movieId != null) {
            switch (loaderID) {
                case MOVIE_TRAILERS_LOADER:
                    Uri movieTrailersUri = TmdbContract.TrailerEntry.buildTrailersWithMovieId(movieId);
                    return new CursorLoader(getActivity(),
                            movieTrailersUri,
                            null,
                            null,
                            null,
                            null);
                case MOVIE_REVIEWS_LOADER:
                    Uri movieReviewsUri = TmdbContract.ReviewEntry.buildReviewsWithMovieId(movieId);
                    return new CursorLoader(getActivity(),
                            movieReviewsUri,
                            null,
                            null,
                            null,
                            null);
                default:
                    throw new UnsupportedOperationException("Unknown loader id: " + loaderID);
            }
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case MOVIE_TRAILERS_LOADER:
                if (0 == cursor.getCount()) {
                    Log.e("jerem", "onLoadFinished: Cursor Count = 0");
                    return;
                }
                if (null == mTrailerAdapter) {
                    Log.e("jerem", "onLoadFinished: TrailerAdapter is null");
                    return;
                }
                mTrailerAdapter.swapCursor(cursor);
                break;

            case MOVIE_REVIEWS_LOADER:
                if (0 == cursor.getCount()) {
                    Log.e("jerem", "onLoadFinished: Cursor Count = 0");
                    return;
                }
                if (null == mReviewAdapter) {
                    Log.e("jerem", "onLoadFinished: ReviewAdapter is null");
                    return;
                }
                mReviewAdapter.swapCursor(cursor);
                getLoaderManager().initLoader(MOVIE_TRAILERS_LOADER, getArguments(), this);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    //****************************************************************************//
    //                                 Event call back                            //
    //****************************************************************************//

    public interface Callback {
        void onItemSelected(TMDBMovie movie, View view);
    }

}
