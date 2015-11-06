package utils;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by agu on 12/10/15.
 */
public class FoundPetRequest {
    RequestHandler requestHandler;
    public  FoundPetRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
    }

    public JSONObject post(JSONObject object) throws InterruptedException, ExecutionException, TimeoutException {
        String path =   RequestHandler.getServerUrl() + "/pet/found";
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, path,  object, future, future);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,4,2));
        requestHandler.addToRequestQueue(request);
        JSONObject response = null;
        try {
            response = future.get(10, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException  | TimeoutException e) {
            // exception handling
            throw e;
        }

        return response;
    }
}
