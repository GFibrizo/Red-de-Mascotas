package com.support.android.designlibdemo.data.communications;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;


public class TextUrlView implements AsyncCallerResponse {
    String url;
    TextView textViewToFill;

    public TextUrlView(String url, TextView textViewToFill) {
        this.url = url;
        this.textViewToFill = textViewToFill;
    }

    public void connect() {
        new ServerConnectGet(this).execute(url);
    }

    @Override
    public void processFinish(String data) {
        textViewToFill.setText(data);
    }
}
