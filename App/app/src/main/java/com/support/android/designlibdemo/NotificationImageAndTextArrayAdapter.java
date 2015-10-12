package com.support.android.designlibdemo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.support.android.designlibdemo.data.communications.ImageUrlView;
import com.support.android.designlibdemo.model.AdoptionNotification;

import java.util.ArrayList;

/**
 * Clase que servira para mostrar en un listView algo que tenga un texto y una imagen
 *
 */
public class NotificationImageAndTextArrayAdapter extends ArrayAdapter<AdoptionNotification> {
    protected final Activity context;
    protected ArrayList<AdoptionNotification> elements;
    protected final int layout;
    protected String baseUrlForImage;
    private String IP_EMULADOR = "http://10.0.2.2:9000"; //ip generica del emulador
    /**
     * @param layout   El layout que usara para mostrar cada fila
     * @param elements conjunto de elementos en los cuales se mostrara 1 por fila
     */
    public NotificationImageAndTextArrayAdapter(Activity context, int layout, String baseUrlForImage, ArrayList<AdoptionNotification> elements) {
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
        final AdoptionNotification element = elements.get(position);

        final LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(this.layout, null, true);

        TextView fecha = (TextView) rowView.findViewById(R.id.fechaAdoptar);
        TextView quiereAdoptar = (TextView) rowView.findViewById(R.id.quiereAdoptar);
        TextView contacto = (TextView) rowView.findViewById(R.id.contactoAdoptar);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.rowimage);

        fecha.setText(fecha.getText()+" "+(element.getRequestDate().split(" "))[0]);
        quiereAdoptar.setText(quiereAdoptar.getText()+" "+element.getPetName());
        contacto.setText(contacto.getText()+" "+element.getAdopterEmail());
        if (!element.getPetImageId().equals("") && !element.getPetImageId().equals("[]")) {
            String id = (element.getPetImageId().replace("[", "").replace("]", "").split(", "))[0];
            baseUrlForImage = IP_EMULADOR + "/pet/image/" + id;
            new ImageUrlView(baseUrlForImage, imageView).connect();
        }
        return rowView;
    }

    public void setElements(ArrayList<AdoptionNotification> elements){
        this.elements = elements;
    }


}
