package com.support.android.designlibdemo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.support.android.designlibdemo.data.communications.ImageUrlView;
import com.support.android.designlibdemo.data.communications.TextUrlView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Clase que servira para mostrar en un listView algo que tenga un texto y una imagen
 *
 */
public class ResultImageAndTextArrayAdapter extends ArrayAdapter<TextAndImage> {
    protected final Activity context;
    protected final ArrayList<TextAndImage> elements;
    protected final int layout;
    protected String baseUrlForImage;
    private String IP_EMULADOR = "http://10.0.2.2:9000"; //ip generica del emulador
    /**
     * @param layout   El layout que usara para mostrar cada fila
     * @param elements conjunto de elementos en los cuales se mostrara 1 por fila
     */
    public ResultImageAndTextArrayAdapter(Activity context, int layout, String baseUrlForImage, ArrayList<TextAndImage> elements) {
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
        final TextAndImage element = elements.get(position);

        final LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(this.layout, null, true);

        TextView nombre = (TextView) rowView.findViewById(R.id.rowtext);
        TextView raza = (TextView) rowView.findViewById(R.id.razaAnimal);
        TextView sexo = (TextView) rowView.findViewById(R.id.sexoAnimal);
        TextView edad = (TextView) rowView.findViewById(R.id.edadAnimal);
        TextView tamanio = (TextView) rowView.findViewById(R.id.tamanioAnimal);
        TextView ubicacion = (TextView) rowView.findViewById(R.id.ubicacionAnimal);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.rowimage);

        nombre.setText(element.getNombre());
        raza.setText(raza.getText()+" "+element.getRaza());
        sexo.setText(sexo.getText()+" "+element.getSexo());
        edad.setText(edad.getText()+" "+element.getEdad());
        tamanio.setText(tamanio.getText()+" "+element.getTamanio());
        ubicacion.setText(ubicacion.getText()+" "+element.getBarrio());

        int id = element.getId(); //String.valueOf(id)
        baseUrlForImage = IP_EMULADOR + "/pet/image/" + String.valueOf(id) + ".jpg";
        new ImageUrlView(baseUrlForImage, imageView).connect();
        return rowView;
    }
}
