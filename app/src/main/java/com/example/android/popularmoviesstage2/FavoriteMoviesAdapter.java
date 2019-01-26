package com.example.android.popularmoviesstage2;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.database.FavoriteMovieEntry;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.MovieViewHolder> {

    private static final String TAG = FavoriteMoviesAdapter.class.getSimpleName();

    // Class variables for the List that holds task data and the Context
    private List<FavoriteMovieEntry> mListOfFavoritesData;



    /*
     * An on-click handler that  defined to make it easy for an Activity to interface with
     * the RecyclerView
     */
    final private ItemClickListener mItemClickListener;

    private Context mContext;

    /**
     * The interface that receives onClick messages.
     */
    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    /**
     * Constructor for MovieItemAdapter that accepts a number of items to display and the specification
     * for the ListItemClickListener.
     *
     * @param context  the current Context
     * @param listener the ItemClickListener
     */
    public FavoriteMoviesAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    /**
     * Cache of the children views for a list item.
     */
    class MovieViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        TextView mMovieTextView;
        ImageView mMovieImageView;

        /**
         * Constructor for the ViewHolder. Within this constructor, we get a reference to our
         * TextView and ImageView and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param itemView The View that you inflated in
         *                 {@link FavoriteMoviesAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public MovieViewHolder(View itemView) {
            super(itemView);

            mMovieTextView = itemView.findViewById(R.id.movie_title);
            mMovieImageView = itemView.findViewById(R.id.movie_poster);

            itemView.setOnClickListener(this);
        }

        /**
         * Called whenever a user clicks on an item in the list.
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mItemClickListener.onItemClickListener(clickedPosition);
        }
    }

    /**
     *
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item  you
     *                  can use this viewType integer to provide a different layout.
     * @return A new NumberViewHolder that holds the View for each list item
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movies_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        CardView cardView = (CardView) inflater.inflate(layoutIdForListItem,
                viewGroup, shouldAttachToParentImmediately);


        MovieViewHolder viewHolder = new MovieViewHolder(cardView);


        return viewHolder;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {


        if (mListOfFavoritesData!=null) {
            FavoriteMovieEntry currentMovie = mListOfFavoritesData.get(position);
            holder.mMovieTextView.setText(currentMovie.getTitle());
            Picasso.get()
                    .load(currentMovie.getPoster())
                    .error(R.drawable.noposter)
                    .into( holder.mMovieImageView);
        }
        else holder.mMovieTextView.setText(R.string.connecting);


    }

    @Override
    public int getItemCount() {
        if (mListOfFavoritesData == null) {
            return 0;
        }
        return mListOfFavoritesData.size();
    }



    public void setFavoritesMovieData(List<FavoriteMovieEntry> movieData) {
        mListOfFavoritesData = movieData;
        notifyDataSetChanged();
    }


}