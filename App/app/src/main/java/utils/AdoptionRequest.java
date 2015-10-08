package utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/** Pedido de adopción, llama al servicio "/pet/petId/adoption" que crea notificación hacia el usuario que
 *  publicó la mascota en adopción. **/

public class AdoptionRequest {
    RequestHandler requestHandler;

    public AdoptionRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
        requestHandler.setContext(context);
    }

    public JSONArray send(String petId) {

        String path =  requestHandler.getServerUrl() + this.buildAdoptionRequestPath(petId);

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

    private String buildAdoptionRequestPath(String petId) {
        return "/pet/"+petId+"/adoption";
    }


}
