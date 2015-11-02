package com.support.android.designlibdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.support.android.designlibdemo.data.communications.ImageUrlView;
import com.support.android.designlibdemo.model.InquirerNotification;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import utils.AcceptProposalRequest;
import utils.Constants;

/**
 * Clase que servira para mostrar en un listView algo que tenga un texto y una imagen
 */
public class NotificationImageAndTextArrayAdapter extends ArrayAdapter<InquirerNotification> {
    protected final Activity context;
    protected ArrayList<InquirerNotification> elements;
    protected final int layout;
    protected String baseUrlForImage;
    private String IP_EMULADOR = Constants.IP_SERVER; //"http://10.0.2.2:9000"; //ip generica del emulador
    private InquirerNotification currentNotification;
    /**
     * @param layout   El layout que usara para mostrar cada fila
     * @param elements conjunto de elementos en los cuales se mostrara 1 por fila
     */
    public NotificationImageAndTextArrayAdapter(Activity context, int layout, String baseUrlForImage, ArrayList<InquirerNotification> elements) {
        super(context, layout, elements);
        this.layout = layout;
        this.context = context;
        this.elements = elements;
        this.baseUrlForImage = baseUrlForImage;
    }

    public View getView(final int position, final View view, final ViewGroup parent) {
        if (elements.size() == 0) {
            return new View(getContext());
        }
        final InquirerNotification element = elements.get(position);
        currentNotification = elements.get(position);
        final LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(this.layout, null, true);

        TextView fecha = (TextView) rowView.findViewById(R.id.fechaAdoptar);
        TextView quiereAdoptar = (TextView) rowView.findViewById(R.id.quiereAdoptar);
        TextView contacto = (TextView) rowView.findViewById(R.id.contactoAdoptar);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.rowimage);

        String date = element.getRequestDate().split(" ")[0];
        String[] dateSplit = date.split("/");
        String newFormatDate = dateSplit[2] + "/" + dateSplit[1] + "/" + dateSplit[0];

        fecha.setText(fecha.getText() + " " + newFormatDate);
        quiereAdoptar.setText(quiereAdoptar.getText() + " " + element.getPetName());
        contacto.setText(contacto.getText() + " " + element.getInquirerEmail());
        if (!element.getPetImageId().equals("") && !element.getPetImageId().equals("[]")) {
            String id = (element.getPetImageId().replace("[", "").replace("]", "").split(", "))[0];
            baseUrlForImage = IP_EMULADOR + "/pet/image/" + id;
            new ImageUrlView(baseUrlForImage, imageView).connect();
        }

        final Button button = (Button) rowView.findViewById(R.id.button_accept);
        button.setTag(position);
        if (currentNotification.getStatus().equals("ACCEPTED")){
            button.setVisibility(View.INVISIBLE);
            quiereAdoptar.setText(element.getPetName() + " ya fue adoptado");
        }
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentNotification = elements.get((Integer) v.getTag());
                AlertDialog dialogo = crearDialogo("Confirmar adopción",
                        "¿Desea aceptar esta solicitud de adopción?");
                dialogo.show();

            }
        });
        return rowView;
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private AlertDialog crearDialogo(String titulo, String mensaje) {
        // Instanciamos un nuevo AlertDialog Builder y le asociamos titulo y mensaje
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( this.context);
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(mensaje);

        // Creamos un nuevo OnClickListener para el boton OK que realice la conexion
        DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                AcceptProposalTask acceptProposalTask = new AcceptProposalTask();
                acceptProposalTask.execute();
            }
        };

        // Creamos un nuevo OnClickListener para el boton Cancelar
        DialogInterface.OnClickListener listenerCancelar = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        };

        // Asignamos los botones positivo y negativo a sus respectivos listeners
        //OJO: estan al reves para que sea display si - no en vez de no - si
        alertDialogBuilder.setPositiveButton(R.string.dialogNo, listenerCancelar);
        alertDialogBuilder.setNegativeButton(R.string.dialogSi, listenerOk);

        return alertDialogBuilder.create();
    }

    public void setElements(ArrayList<InquirerNotification> elements) {
        this.elements = elements;
    }


    public class AcceptProposalTask extends AsyncTask<Void, Void, Boolean> {

        JSONObject response;
        AcceptProposalTask() {    }
        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("FLOATING", "CLICK | adopterId: " + currentNotification.getInquirerId() +
                    "PETID: " + currentNotification.getPetId() );

            AcceptProposalRequest request = new AcceptProposalRequest(context);
            try {
                response = request.accept(currentNotification);
            }  catch (InterruptedException | ExecutionException  | TimeoutException e) {
                // exception handling
               return false;
            }
//            return (response != null);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                //TODO: Actualizar listado de propuestas
                Toast.makeText(context, "Mascota adoptada", Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(context, NotificationActivity.class);
            context.startActivity(intent);
        }

        @Override
        protected void onCancelled() {
        }

    }

}
