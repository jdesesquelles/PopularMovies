package co.fabrk.popmovies.viewItem;

import android.content.Context;
//import android.support.v4.widget.CursorAdapter;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.fabrk.popmovies.R;
import co.fabrk.popmovies.data.TmdbContract;
import co.fabrk.popmovies.ui.utils.CursorAdapter;

/**
 * Created by ebal on 14/09/15.
 */
public class ReviewCursorAdapter extends CursorAdapter<ReviewCursorAdapter.ReviewItemViewHolder> {

    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public ReviewCursorAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ReviewItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_review, parent, false);
        final ReviewItemViewHolder holder = new ReviewItemViewHolder(view);
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
    }

    @Override
    public void onBindViewHolder(ReviewItemViewHolder holder, int position) {
        cursor.moveToPosition(position);

        // Loading the content of the review
        int ReviewContentColumnIndex = cursor.getColumnIndex(TmdbContract.ReviewEntry.COLUMN_TMDB_REVIEW_CONTENT);
        String reviewContent = cursor.getString(ReviewContentColumnIndex);
        holder.mReviewContent.setText(reviewContent);

        // Loading the author of the review
        int ReviewAuthorColumnIndex = cursor.getColumnIndex(TmdbContract.ReviewEntry.COLUMN_TMDB_REVIEW_AUTHOR);
        String reviewAuthor = cursor.getString(ReviewAuthorColumnIndex);
        holder.mReviewAuthor.setText(reviewAuthor);
    }


    public class ReviewItemViewHolder extends RecyclerView.ViewHolder {
        TextView mReviewContent;
        TextView mReviewAuthor;

        public ReviewItemViewHolder(View itemView) {
            super(itemView);
            mReviewContent = (TextView) itemView.findViewById(R.id.list_item_review_content);
            mReviewAuthor = (TextView) itemView.findViewById(R.id.list_item_review_author);
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
