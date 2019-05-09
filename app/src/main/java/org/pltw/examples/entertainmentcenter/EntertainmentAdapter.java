package org.pltw.examples.entertainmentcenter;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;

public class EntertainmentAdapter extends RecyclerView.Adapter<EntertainmentAdapter.EntertainmentViewHolder> {
    private List<Entertainment> entertainmentList;
    private static final String TAG = EntertainmentAdapter.class.getSimpleName();
    private Entertainment RecentlyRemovedItem;
    private int RecentlyRemovedItemPosition;


    // Provide a suitable constructor (depends on the kind of dataset)
    public EntertainmentAdapter(List<Entertainment> entertainment) {
        entertainmentList = entertainment;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public EntertainmentViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());

        EntertainmentViewHolder vh = new EntertainmentViewHolder(layoutInflater, parent);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EntertainmentViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Entertainment et = entertainmentList.get(position);
        holder.bind(et);
    }
    public void deleteItem(int position) {

        final Entertainment entertainment = entertainmentList.get(position);
        final int delPosition = position;

        Backendless.Persistence.of(Entertainment.class).remove( entertainment, new AsyncCallback<Long>() {
            @Override
            public void handleResponse( Long response )
            {
                entertainmentList.remove(delPosition);
                notifyItemRemoved(delPosition);
                RecentlyRemovedItem = entertainment;
                RecentlyRemovedItemPosition = delPosition;
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        } );
    }
    private void showUndoSnackbar(RecyclerView.ViewHolder holder) {
        View eVH = ((EntertainmentViewHolder) holder).viewContainer;

        Snackbar snackbar = Snackbar
                .make(eVH, R.string.snack_bar_text, Snackbar.LENGTH_LONG)
                .setAction(R.string.snack_bar_undo, v -> undoRemove());
        snackbar.show();
    }

    private void undoRemove() {
        entertainmentList.add(RecentlyRemovedItemPosition, RecentlyRemovedItem);
        notifyItemInserted(RecentlyRemovedItemPosition);

        RecentlyRemovedItem = null;
    }

    public void removeItemWithUndo(RecyclerView.ViewHolder holder, int position) {
        final Entertainment entertainment = entertainmentList.get(position);

        // If there has been an item already deleted
        if(RecentlyRemovedItem != null) {
            // Actually delete the previous item from the DB
            deleteEntertainmentItem();
        }

        Log.i(TAG, "Item removed from RecyclerView: " + position);
        entertainmentList.remove(position);
        notifyItemRemoved(position);
        RecentlyRemovedItem = entertainment;
        RecentlyRemovedItemPosition = position;
        showUndoSnackbar(holder);
    }

    public void deleteEntertainmentItem(){
        // New contact object has been saved, now it can be deleted
        Backendless.Persistence.of(Entertainment.class).remove(
                RecentlyRemovedItem, new AsyncCallback<Long>() {
                    @Override
                    public void handleResponse( Long response )
                    {
                        Log.i(TAG, "Item Deleted");
                    }
                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Log.i(TAG, fault.toString());
                    }
                } );
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return entertainmentList.size();
    }




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class EntertainmentViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        private TextView tvTitle;
        private TextView tvDescription;
        private RatingBar rbRating;
        private Entertainment entertainment;
        private Entertainment RecentlyDeletedItem;
        private int RecentlyDeletedItemPosition;
        private List<Entertainment> ListItems;
        private View view;
        public View viewContainer;
        // each data item is just a string in this case




        public EntertainmentViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_simple_entertainment, parent, false));
            this.viewContainer = itemView;

            itemView.setOnClickListener(this);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            rbRating = itemView.findViewById(R.id.rb_rating);
        }



        public void bind(Entertainment entertainment){
            this.entertainment = entertainment;
            tvTitle.setText(entertainment.getTitle());
            tvDescription.setText(entertainment.getDescription());
            rbRating.setRating(entertainment.getRating());
        }





        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), EntertainmentActivity.class);

            i.putExtra(MainActivity.ET_ITEM, entertainment);

            v.getContext().startActivity(i);
            //Toast.makeText(v.getContext(), "Clicked!!!!!!", Toast.LENGTH_SHORT).show();
        }

    }


}