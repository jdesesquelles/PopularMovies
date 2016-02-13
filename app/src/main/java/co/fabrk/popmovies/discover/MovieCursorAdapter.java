package co.fabrk.popmovies.discover;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import co.fabrk.popmovies.R;
import co.fabrk.popmovies.data.TmdbContract;

/**
 * Cursor Adapter class for movies in the movie list
 * Created by ebal on 28/07/15.
 */
class MovieCursorAdapter extends CursorAdapter {

    public MovieCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_movie, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int idx_imageUrl = cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_POSTER_PATH);
        String imageUrl = cursor.getString(idx_imageUrl);
        ImageView imageView = (ImageView) view;

        // Images Url Logging
        int idx_backdropUrl = cursor.getColumnIndex(TmdbContract.MovieEntry.COLUMN_BACKDROP);
        String backdropUrl = cursor.getString(idx_backdropUrl);
        Log.e("Thumbnail", "MovieCursorAdapter: " + imageUrl);
        Log.e("BackDrop", "MovieCursorAdapter: " + backdropUrl);

        if (imageView != null) {
                Picasso.with(context).load(imageUrl).into(imageView);
        }
    }

}
