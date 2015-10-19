package utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fabrizio on 18/10/15.
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    protected final SimpleStringRecyclerViewAdapter mAdapter;
    Context context;

    public SimpleItemTouchHelperCallback(SimpleStringRecyclerViewAdapter adapter,  Context context) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        try {
            JSONObject object = mAdapter.getAt(viewHolder.getAdapterPosition());
            UnpublishRequest request = new UnpublishRequest(context);
            request.send(object.getString("id"));
        } catch (JSONException e) {
        }
        mAdapter.remove(viewHolder.getAdapterPosition());
    }
}
