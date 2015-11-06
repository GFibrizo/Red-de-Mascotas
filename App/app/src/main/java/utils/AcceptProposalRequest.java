package utils;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.support.android.designlibdemo.model.InquirerNotification;

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

    public JSONObject accept(InquirerNotification currentNotification) throws InterruptedException, ExecutionException, TimeoutException {

        String path =   RequestHandler.getServerUrl() + "/pet/" + currentNotification.getPetId() + "/adoption/accepted";
        JSONObject response = null;
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, path,  currentNotification.toJson(), future, future);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,4,2));
        requestHandler.addToRequestQueue(request);
        try {
            response = future.get(10, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException  | TimeoutException e) {
            // exception handling
            throw e;
        }

        return response;
    }

}
