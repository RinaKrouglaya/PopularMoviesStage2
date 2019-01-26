package com.example.android.popularmoviesstage2;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.database.FavoriteMovieEntry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieItemAdapter extends RecyclerView.Adapter<MovieItemAdapter.MovieViewHolder> {

    private static final String TAG = MovieItemAdapter.class.getSimpleName();
    private ArrayList<MovieItem> mListOfMovieItemsData;
    private ArrayList<FavoriteMovieEntry> mListOfFavoritesData;

    /*
     * An on-click handler that  defined to make it easy for an Activity to interface with
     * the RecyclerView
     */
    final private ListItemClickListener mOnClickListener;

    /*
     * The number of ViewHolders that have been created.
     *
     */
    private static int viewHolderCount;

    private int mNumberItems;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {

        void onListItemClick(int clickedItemIndex);

    }

    /**
     * Constructor for MovieItemAdapter that accepts a number of items to display and the specification
     * for the ListItemClickListener.
     *
     * @param numberOfItems Number of items to display in list
     * @param listener Listener for list item clicks
     */
    public MovieItemAdapter(int numberOfItems, ListItemClickListener listener) {
        mNumberItems = numberOfItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
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
         *                 {@link MovieItemAdapter#onCreateViewHolder(ViewGroup, int)}
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
            mOnClickListener.onListItemClick(clickedPosition);
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

        viewHolderCount++;
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
      //  Log.d(TAG, "#" + position);

        if (mListOfMovieItemsData!=null) {
            MovieItem currentMovie = mListOfMovieItemsData.get(position);
            holder.mMovieTextView.setText(currentMovie.getOriginalTitle());
            Picasso.get()
                    .load(currentMovie.getPoster())
                    .error(R.drawable.noposter)
                    .into( holder.mMovieImageView);
        }
        else holder.mMovieTextView.setText(R.string.connecting);


    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    /**
     * This method is used to set the Movies Data on a MovieAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new MovieAdapter  to display it.
     *
     * @param movieData The new movie data to be displayed.
     */
    public void setMovieData(ArrayList<MovieItem> movieData) {
        mListOfMovieItemsData = movieData;
        notifyDataSetChanged();
    }



}