package utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PetWasFoundRequest {
    RequestHandler requestHandler;
    public PetWasFoundRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
    }

    public JSONObject markAsFound(String petId){
        String path =   RequestHandler.getServerUrl() + "/pet/" + petId + "/found";
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, path, future, future);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,4,2));
        requestHandler.addToRequestQueue(request);
        JSONObject response = null;
        try {
            response = future.get(10, TimeUnit.SECONDS);

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
