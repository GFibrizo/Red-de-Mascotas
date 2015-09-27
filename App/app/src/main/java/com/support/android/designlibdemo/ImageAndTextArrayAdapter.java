package com.support.android.designlibdemo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Clase que servira para mostrar en un listView algo que tenga un texto y una imagen
 *
 */
public class ImageAndTextArrayAdapter extends ArrayAdapter<TextAndImage> {
    protected final Activity context;
    protected final ArrayList<TextAndImage> elements;
    protected final int layout;
    protected String baseUrlForImage;
    /**
     * @param layout   El layout que usara para mostrar cada fila
     * @param elements conjunto de elementos en los cuales se mostrara 1 por fila
     */
    public ImageAndTextArrayAdapter(Activity context, int layout, String baseUrlForImage ,ArrayList<TextAndImage> elements) {
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

        TextView text = (TextView) rowView.findViewById(R.id.rowtext);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.rowimage);

        text.setText(element.getText());
        Glide.with(imageView.getContext())
                .load(Cheeses.getRandomCheeseDrawable()) // TODO: cambiar url - new ImageUrlView(IpConfig.LOCAL_IP.url() + "/getstudentpicture/" + friendId, profilePricture).connect();
                .fitCenter()
                .into(imageView);
//                              new ImageUrlView(baseUrlForImage+element.getId(), imageView).connect();

        return rowView;
    }
}
