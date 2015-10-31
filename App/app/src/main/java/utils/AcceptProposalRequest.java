package utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.support.android.designlibdemo.model.AdoptionNotification;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by agu on 30/10/15.
 */
public class AcceptProposalRequest {

    RequestHandler requestHandler;
    public  AcceptProposalRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
    }

    public JSONObject accept(AdoptionNotification currentNotification) throws InterruptedException, ExecutionException, TimeoutException {
        String path =   RequestHandler.getServerUrl() + "/pet/found";
        JSONObject response = null;
//        RequestFuture<JSONObject> future = RequestFuture.newFuture();
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, path,  object, future, future);
//        requestHandler.addToRequestQueue(request);
//        try {
//            response = future.get(10, TimeUnit.SECONDS);
//
//        } catch (InterruptedException | ExecutionException  | TimeoutException e) {
//            // exception handling
//            throw e;
//        }

        return response;
    }
}
