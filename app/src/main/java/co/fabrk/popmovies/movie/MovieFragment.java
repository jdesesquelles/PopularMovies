package co.fabrk.popmovies.movie;

import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.Swatch;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ArcMotion;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v4.app.SharedElementCallback;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import co.fabrk.popmovies.Popup;
import co.fabrk.popmovies.R;
import co.fabrk.popmovies.data.TmdbContract;
import co.fabrk.popmovies.tmdb.TMDBMovie;
import co.fabrk.popmovies.tmdb.TMDBReview;
import co.fabrk.popmovies.tmdb.TMDBTrailer;
import co.fabrk.popmovies.tmdb.TmdbConstants;
import co.fabrk.popmovies.tmdb.TmdbDatabaseOperations;
import co.fabrk.popmovies.tmdb.TmdbProviderUtils;
import co.fabrk.popmovies.transitions.FabDialogMorphSetup;
import co.fabrk.popmovies.transitions.MorphDialogToFab;
import co.fabrk.popmovies.transitions.MorphFabToDialog;
import co.fabrk.popmovies.utils.ColorUtils;
import co.fabrk.popmovies.utils.DateUtils;
import  co.fabrk.popmovies.utils.AnimUtils;
import co.fabrk.popmovies.widget.ParallaxScrimageView;

public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, MovieContract.View {

    private TMDBMovie mMovie;
    private Uri mUri;
    public static final String DETAIL_URI = "URI";
    private static final String TAG = "MovieFragment";
    private ViewHolder viewHolder;
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
        view = inflater.inflate(R.layout.detail_fragment, container, false);
        viewHolder = new ViewHolder(view);
        Bundle arguments = getArguments();
        if (mMovie != null) {
//        setExitSharedElementCallback(fabLoginSharedElementCallback);
            showMovieDetail(mMovie);
            setListeners();
        }
        else {
            showEmptyScreen(); //splashScreenDetailView
        }
        return view;
    }

    private void showEmptyScreen() {
        viewHolder.fabFavorite.setVisibility(View.GONE);
        viewHolder.popupLayout.setVisibility(View.GONE);
        viewHolder.detailScrollView.setVisibility(View.GONE);
        viewHolder.imageView.setVisibility(View.GONE);
        viewHolder.splashScreenDetailView.setVisibility(View.VISIBLE);
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


    // Listeners
    // Todo converto to Rx programming using Rx Android
    private void setListeners() {
        final FloatingActionButton fab = viewHolder.fabFavorite;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFavorite(fab);
            }
        });

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
    public void toggleFavorite() {

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

    // Contract
    @Override
    public void showMovieDetail(TMDBMovie movie) {

        viewHolder.fabFavorite.setVisibility(View.VISIBLE);
        viewHolder.popupLayout.setVisibility(View.VISIBLE);
        viewHolder.detailScrollView.setVisibility(View.VISIBLE);
        viewHolder.imageView.setVisibility(View.VISIBLE);
        viewHolder.splashScreenDetailView.setVisibility(View.GONE);

        viewHolder.rootView.setVerticalScrollBarEnabled(true);

        viewHolder.titleView.setText(movie.getTitle());
        viewHolder.titleView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), getActivity().getString(R.string.customTextFont)));
        Picasso.with(viewHolder.rootView.getContext()).load(movie.getThumbnailPath()).into(viewHolder.posterView);
        loadBitmap(movie.getBackdrop());
//        Picasso.with(rootView.getContext()).load(movie.getBackdrop()).into(backdropView);
        viewHolder.dateView.setText(DateUtils.formatDate(movie.getReleaseDate()));
        viewHolder.ratingView.setText(movie.getVoteAverage());
        viewHolder.overviewView.setText(movie.getOverview());
        if (movie.getTmdbTrailers() != null) {
            showTrailers(movie.getTmdbTrailers());
            viewHolder.includeDetailSectionTrailers.setVisibility(View.VISIBLE);
        }
        else {viewHolder.includeDetailSectionTrailers.setVisibility(View.GONE);}
        if (movie.getTmdbReviews() != null) {
            showReviews(movie.getTmdbReviews());
            viewHolder.includeDetailSectionReviews.setVisibility(View.VISIBLE);
        }
        else {viewHolder.includeDetailSectionReviews.setVisibility(View.GONE);}

        viewHolder.popupLayout.setVisibility(View.GONE);

        // Fab
        if (TmdbProviderUtils.isFavoriteMovie(mMovie, getContext())) {
            viewHolder.fabFavorite.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite));
        } else {
            viewHolder.fabFavorite.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_outline_24dp));
        }
        viewHolder.fabFavorite.setTransitionName(getString(R.string.transition_popup));

    }

    @Override
    public void showTrailers(ArrayList<TMDBTrailer> trailers) {
        if (mTrailerAdapter == null) {
//            mTrailerAdapter = new TrailerCursorAdapter(getActivity(), trailerCursor, 0);
            mTrailerAdapter = new TrailerCursorAdapter(getActivity());

        }
        viewHolder.trailerList.setAdapter(mTrailerAdapter);
        viewHolder.trailerList.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void showReviews(ArrayList<TMDBReview> reviews) {
        if (mReviewAdapter == null) {
            mReviewAdapter = new ReviewCursorAdapter(getActivity());
        }
        viewHolder.reviewList.setAdapter(mReviewAdapter);
        viewHolder.reviewList.setLayoutManager(
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
        viewHolder.backdropView.setImageBitmap(b);
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
        viewHolder.detailInformationlinearLayout.setBackground(null);
        viewHolder.overviewView.setBackground(null);
        viewHolder.trailerList.setBackground(null);
        viewHolder.reviewList.setBackground(null);
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

            applyColorAccent(viewHolder.titleView, "textColor", duration, titleStartDelay, currentAccent, colorAccent);
            applyColorAccent(viewHolder.dateView, "textColor", duration, titleStartDelay, currentAccent, colorAccent);
            applyColorAccent(viewHolder.overviewView, "textColor", duration, titleStartDelay, currentAccent, colorAccent);
            applyColorAccent(viewHolder.trailersTitleTextview, "textColor", duration, titleStartDelay, currentAccent, colorAccent);
            applyColorAccent(viewHolder.reviewsTitleTextview, "textColor", duration, titleStartDelay, currentAccent, colorAccent);

            applyColorAccent(viewHolder.titleView, "backgroundColor", duration, titleStartDelay, currentPrimaryDarkColor, colorPrimaryDark);
            applyColorAccent(viewHolder.separatorTrailers, "backgroundColor", duration, titleStartDelay, currentPrimaryDarkColor, colorPrimaryDark);
            applyColorAccent(viewHolder.separatorReviews, "backgroundColor", duration, titleStartDelay, currentPrimaryDarkColor, colorPrimaryDark);
            applyColorAccent(viewHolder.movieDetaillinearLayout, "backgroundColor", duration, titleStartDelay, currentPrimaryLightColor, colorPrimaryLight);
            applyColorAccent(viewHolder.trailersTitleTextview, "backgroundColor", duration, titleStartDelay, currentPrimaryColor, colorPrimary);
            applyColorAccent(viewHolder.reviewsTitleTextview, "backgroundColor", duration, titleStartDelay, currentPrimaryColor, colorPrimary);

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

    private void applyColorAccent(ArrayList<View> viewArrayList, String property, int duration, int titleStartDelay, int currentAccent, int colorAccent) {
        for (View view : viewArrayList) {
            ColorUtils.animateObjectViewColor(duration, titleStartDelay, view, property, currentAccent, colorAccent).start();
        }
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
        if (cursor.getCount() == 0) {
//            FetchTmdbDetail fetchTmdbDetail = new FetchTmdbDetail(getContext(), getString(R.string.api_key));
//            fetchTmdbDetail.execute(mMovie);
        }
        switch (loader.getId()) {
            case MOVIE_TRAILERS_LOADER:
                mTrailerAdapter.swapCursor(cursor);
                break;

            case MOVIE_REVIEWS_LOADER:
                mReviewAdapter.swapCursor(cursor);
                getLoaderManager().initLoader(MOVIE_TRAILERS_LOADER, getArguments(), this);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    //****************************************************************************//
    //                                 View Holder                                //
    //****************************************************************************//

    public class ViewHolder {

        //TODO: StreamLine
        private ViewGroup parentViewGroup;
        private LinearLayout movieDetaillinearLayout;
        private LinearLayout detailInformationlinearLayout;
        private LinearLayout popupLayout;
        private ParallaxScrimageView imageView;
        private ScrollView detailScrollView;
        private FrameLayout ratingBarLayout;
        private View rootView;
        private TextView titleView;
        private ImageView posterView;
        private ImageView backdropView;
        private TextView dateView;
        private TextView ratingView;
        private TextView overviewView;
        private RecyclerView trailerList;
        private RecyclerView reviewList;
        private FloatingActionButton fabFavorite;
        private View separatorReviews;
        private View separatorTrailers;
        private TextView reviewsTitleTextview;
        private TextView trailersTitleTextview;
        private ImageView splashScreenDetailView;
        private View includeDetailSectionTrailers;
        private View includeDetailSectionReviews;


//        private View scrimView;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            splashScreenDetailView = (ImageView) rootView.findViewById(R.id.splash_screen_detail_view);

            parentViewGroup = (ViewGroup) rootView.findViewById(R.id.movie_detail_container);
            movieDetaillinearLayout = (LinearLayout) rootView.findViewById(R.id.movie_detail_container);
            includeDetailSectionTrailers = rootView.findViewById(R.id.include_detail_section_trailers);
            includeDetailSectionReviews  = rootView.findViewById(R.id.include_detail_section_reviews);

            ratingBarLayout = (FrameLayout) rootView.findViewById(R.id.rating_bar);
            titleView = (TextView) rootView.findViewById(R.id.textview_title);
            ratingView = (TextView) rootView.findViewById(R.id.textview_rating);
            backdropView = (ImageView) rootView.findViewById(R.id.backdrop);
            posterView = (ImageView) rootView.findViewById(R.id.imageview_poster_detail_fragment);
            dateView = (TextView) rootView.findViewById(R.id.textview_release_date);
            overviewView = (TextView) rootView.findViewById(R.id.textview_overview);
            fabFavorite = (FloatingActionButton) rootView.findViewById(R.id.fab_favorite);
//            scrimView = rootView.findViewById(R.id.scrim);
            trailerList = (RecyclerView) rootView.findViewById(R.id.detail_trailer_recycler_view);
            reviewList = (RecyclerView) rootView.findViewById(R.id.detail_review_recycler_view);
            detailInformationlinearLayout = (LinearLayout) rootView.findViewById(R.id.layout_detail_information);
            separatorReviews = rootView.findViewById(R.id.separator_reviews);
            separatorTrailers = rootView.findViewById(R.id.separator_trailers);

            reviewsTitleTextview = (TextView) rootView.findViewById(R.id.detail_review_textview_title);
            trailersTitleTextview = (TextView) rootView.findViewById(R.id.detail_trailer_textview_title);
            imageView = (ParallaxScrimageView) rootView.findViewById(R.id.shot);
            popupLayout = (LinearLayout)rootView.findViewById(R.id.container);
            detailScrollView = (ScrollView) rootView.findViewById(R.id.detail_scrollview);
        }
    }



    //****************************************************************************//
    //                                 Event call back                            //
    //****************************************************************************//

    public interface Callback {
        void onItemSelected(TMDBMovie movie, View view);
    }

}