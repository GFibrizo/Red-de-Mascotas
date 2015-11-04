package utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.support.android.designlibdemo.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fabrizio on 18/10/15.
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    protected final SimpleStringRecyclerViewAdapter mAdapter;
    Context context;

    public SimpleItemTouchHelperCallback(SimpleStringRecyclerViewAdapter adapter,  Context context) {
        this.mAdapter = adapter;
        this.context = context;
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
        AlertDialog dialog = createDialog(viewHolder, "Confirmación",
                "¿Está seguro de que desea borrar la publicación?");
        dialog.show();
    }

    public AlertDialog createDialog(final RecyclerView.ViewHolder viewHolder, String titulo, String mensaje) {
        // Instanciamos un nuevo AlertDialog Builder y le asociamos titulo y mensaje
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.context);
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(mensaje);

        // Creamos un nuevo OnClickListener para el boton OK que realice la conexion
        DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    JSONObject object = mAdapter.getAt(viewHolder.getAdapterPosition());
                    UnpublishRequest request = new UnpublishRequest(context);
                    Log.e("REMOVE", object.getString("id") + " : " + object.getString("publicationType"));
                    request.send(object.getString("id"), object.getString("publicationType"));
                } catch (JSONException e) {
                    Log.e("ERROR UNPUBLISH SWIPE", e.getMessage());
                }
                mAdapter.remove(viewHolder.getAdapterPosition());
            }
        };

        // Creamos un nuevo OnClickListener para el boton Cancelar
        DialogInterface.OnClickListener listenerCancelar = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        };

        // Asignamos los botones positivo y negativo a sus respectivos listeners
        //OJO: estan al reves para que sea display si - no en vez de no - si
        alertDialogBuilder.setPositiveButton("No", listenerCancelar);
        alertDialogBuilder.setNegativeButton("Si", listenerOk);

        return alertDialogBuilder.create();
    }
}
