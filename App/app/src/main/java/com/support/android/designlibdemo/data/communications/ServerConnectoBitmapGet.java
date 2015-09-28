package com.support.android.designlibdemo.data.communications;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class ServerConnectoBitmapGet extends AsyncTask<String, String, Bitmap> {

    protected AsyncCallerBitmapResponse caller = null;

    public ServerConnectoBitmapGet(AsyncCallerBitmapResponse caller) {
        this.caller = caller;
    }

    protected Bitmap doInBackground(String... args) {
        Bitmap bitmap = null;
        String imageUrl = args[0];
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap image) {
        caller.processFinish(image);
    }
}

