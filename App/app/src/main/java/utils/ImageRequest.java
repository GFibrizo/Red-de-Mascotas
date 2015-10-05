package utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.RequestFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ImageRequest {
    RequestHandler requestHandler;

    public ImageRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
        requestHandler.setContext(context);
    }

    public String upload(File file) {
        String path =  requestHandler.getServerUrl() + "/pet/image";
        RequestFuture<String> future = RequestFuture.newFuture();
        MultipartRequest request = new MultipartRequest(path, future, future, file/*, "image"*/);
        requestHandler.addToRequestQueue(request);
        String response = null;
        try {
            response =  future.get(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e("Retrieve cards api call interrupted.", String.valueOf(e));
        } catch (ExecutionException e) {
            Log.e("Retrieve cards api call failed.", String.valueOf(e));
        } catch (TimeoutException e) {
            Log.e("Retrieve cards api call timed out.", String.valueOf(e));
        }
        return response;
    }

}
