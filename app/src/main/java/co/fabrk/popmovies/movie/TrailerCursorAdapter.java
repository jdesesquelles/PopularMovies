package co.fabrk.popmovies.movie;

import android.content.Context;
import android.net.Uri;
//import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import co.fabrk.popmovies.R;
import co.fabrk.popmovies.utils.CursorAdapter;
import co.fabrk.popmovies.data.TmdbContract;

/**
 * Cursor Adapter class for movies in the movie list
 * <p/>
 * Created by ebal on 28/07/15.
 */
public class TrailerCursorAdapter extends CursorAdapter<TrailerCursorAdapter.TrailerItemViewHolder> {

    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public TrailerCursorAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public TrailerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_trailer, parent, false);
        final TrailerItemViewHolder holder = new TrailerItemViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClicked(holder.getAdapterPosition());
            }
        });
        return holder;
    }

    /**
     * Method called when any item has been clicked.
     *
     * @param clickedPos clicked item position
     */
    private void itemClicked(int clickedPos) {
        if (onItemClickListener != null) {
            cursor.moveToPosition(clickedPos);

            Uri clickedUri = TmdbContract.TrailerEntry.buildTrailersUri(
                    cursor.getLong(cursor.getColumnIndex(TmdbContract.TrailerEntry._ID)));

            onItemClickListener.onItemClicked(clickedUri);
        }
    }

    @Override
    public void onBindViewHolder(TrailerItemViewHolder holder, int position) {
        cursor.moveToPosition(position);
        // Load thumbnail if video site is YouTube
        int siteColumnIndex = cursor.getColumnIndex(TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_SITE);
        String site = cursor.getString(siteColumnIndex);
        if (site.equals("YouTube")) {
            //TODO: Load thumbnail
            int keyColumnIndex = cursor.getColumnIndex(TmdbContract.TrailerEntry.COLUMN_TMDB_TRAILER_NAME);

            String trailerName = cursor.getString(keyColumnIndex);
            holder.mNameText.setText(trailerName);
        }
    }


    public class TrailerItemViewHolder extends RecyclerView.ViewHolder {
        ImageView mPlayButton;
        TextView mNameText;

        public TrailerItemViewHolder(View itemView) {
            super(itemView);
            mPlayButton = (ImageView) itemView.findViewById(R.id.list_item_trailer_play_icon);
            mNameText = (TextView) itemView.findViewById(R.id.list_item_trailer_name);

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Listener for trailer video item clicked.
     */
    public interface OnItemClickListener {
        /**
         * Triggered when a video item is clicked.
         *
         * @param uri uri of trailer video clicked by user
         */
        void onItemClicked(Uri uri);
    }

}
