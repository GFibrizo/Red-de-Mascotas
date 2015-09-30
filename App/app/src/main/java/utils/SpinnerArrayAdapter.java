package utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.support.android.designlibdemo.R;

public class SpinnerArrayAdapter {

    public static ArrayAdapter<CharSequence> createSpinnerArrayAdapter(Context context, String[] items, String hint) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount()));
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1;
            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (String color : items) {
            adapter.add(color);
        }
        adapter.add(hint);
        return adapter;
    }

}
