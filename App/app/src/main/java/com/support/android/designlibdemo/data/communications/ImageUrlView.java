package com.support.android.designlibdemo.data.communications;

import android.graphics.Bitmap;
import android.widget.ImageView;


public class ImageUrlView implements AsyncCallerBitmapResponse {
    String url;
    ImageView imageViewToFill;

    public ImageUrlView(String url, ImageView imageViewToFill) {
        this.url = url;
        this.imageViewToFill = imageViewToFill;
    }

    public void connect() {
        new ServerConnectoBitmapGet(this).execute(url);
    }

    @Override
    public void processFinish(Bitmap image) {
        imageViewToFill.setImageBitmap(image);
    }
}
