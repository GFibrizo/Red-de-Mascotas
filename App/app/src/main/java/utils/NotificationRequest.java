package utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.support.android.designlibdemo.model.SearchForAdoptionFilters;

import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/** Trae las notificaciones de un usuario a partir de su Id **/

public class NotificationRequest {
    RequestHandler requestHandler;

    public NotificationRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
        requestHandler.setContext(context);
    }

    public JSONArray search(String userId) {

        String path =  requestHandler.getServerUrl() + this.buildSearchNotificationPath(userId);

        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,  path, future, future);
        requestHandler.addToRequestQueue(request);
        JSONArray response = null;
        try {
            response =  future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e("Retrieve cards api call interrupted.", String.valueOf(e));
        } catch (ExecutionException e) {
            Log.e("Retrieve cards api call failed.", String.valueOf(e));
        } catch (TimeoutException e) {
            Log.e("Retrieve cards api call timed out.", String.valueOf(e));
        }
        return response;
    }

    private String buildSearchNotificationPath(String userId) {
        return "/user/"+userId+"/notifications";
    }

}
