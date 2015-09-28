package com.support.android.designlibdemo.data.communications;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class ServerConnectGet extends AsyncTask<String, String, String> {

    protected AsyncCallerResponse caller = null;

    public ServerConnectGet(AsyncCallerResponse caller) {
        this.caller = caller;
    }

    protected String doInBackground(String... args) {
        String data = null;
        String url = args[0];
        try {
            data = (String) new URL(url).getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    protected void onPostExecute(String data) {
        caller.processFinish(data);
    }
}

