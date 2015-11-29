package com.support.android.designlibdemo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.support.android.designlibdemo.data.communications.ImageUrlView;

import java.util.ArrayList;

import utils.Constants;

/**
 * Clase que servira para mostrar en un listView algo que tenga un texto y una imagen
 *
 */
public class MatchResultImageAndTextArrayAdapter extends ArrayAdapter<MatchedPet> {
    protected final Activity context;
    protected final ArrayList<MatchedPet> elements;
    protected final int layout;
    protected String baseUrlForImage;
    private String IP_EMULADOR = Constants.IP_SERVER;//"http://10.0.2.2:9000"; //ip generica del emulador
    /**
     * @param layout   El layout que usara para mostrar cada fila
     * @param elements conjunto de elementos en los cuales se mostrara 1 por fila
     */
    public MatchResultImageAndTextArrayAdapter(Activity context, int layout, String baseUrlForImage, ArrayList<MatchedPet> elements) {
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
        final MatchedPet element = elements.get(position);

        final LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(this.layout, null, true);

        TextView raza = (TextView) rowView.findViewById(R.id.razaAnimal);
        TextView sexo = (TextView) rowView.findViewById(R.id.sexoAnimal);
        TextView tipo = (TextView) rowView.findViewById(R.id.tipoAnimal);
        TextView tamanio = (TextView) rowView.findViewById(R.id.tamanioAnimal);
        TextView fecha = (TextView) rowView.findViewById(R.id.fechaAnimal);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.rowimage);

        raza.setText(raza.getText()+" "+element.getBreed());
        sexo.setText(sexo.getText()+" "+element.getGender());
        tipo.setText(tipo.getText()+" "+element.getType());
        tamanio.setText(tamanio.getText()+" "+element.getSize());
        String[] dateSplit = element.getLastSeenOrFoundDate().split("/");
        String newFormatDate = dateSplit[2] + "/" + dateSplit[1] + "/" + dateSplit[0];
        fecha.setText(fecha.getText()+" "+newFormatDate);
        String id;
//        String id = element.getId(); //TODO: ojo que no debe ser el mismo id de imagen
        String images[] = element.getImages().split(", ");
        if (images.length != 0) {
            id = images[0];
        }else{
            id = "2";
        }
        baseUrlForImage = IP_EMULADOR + "/pet/image/" + id;
        new ImageUrlView(baseUrlForImage, imageView).connect();

        return rowView;
    }
}
