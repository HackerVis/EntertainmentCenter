package org.pltw.examples.entertainmentcenter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchUIUtil;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private EntertainmentAdapter EntertainmentAdapter;

    public SwipeToDeleteCallback(EntertainmentAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        EntertainmentAdapter = adapter;
    }
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        EntertainmentAdapter.removeItemWithUndo(viewHolder, position);
    }
}
