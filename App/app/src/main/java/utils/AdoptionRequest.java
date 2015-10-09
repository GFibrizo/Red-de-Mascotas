package utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public void send(String petId, String adopterId) {

        String path =  requestHandler.getServerUrl() + this.buildAdoptionRequestPath(petId);
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("petId", petId);
            jsonRequest.put("adopterId", adopterId);
        } catch (JSONException e) {
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, path,
                jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        System.out.println(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores

                    }
                });

//        RequestFuture<JSONArray> future = RequestFuture.newFuture();
//        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,  path, future, future);
        requestHandler.addToRequestQueue(request);
//        JSONArray response = null;
//        try {
//            response =  future.get(10, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            Log.e("Retrieve cards api call interrupted.", String.valueOf(e));
//        } catch (ExecutionException e) {
//            Log.e("Retrieve cards api call failed.", String.valueOf(e));
//        } catch (TimeoutException e) {
//            Log.e("Retrieve cards api call timed out.", String.valueOf(e));
//        }
//        return response;
    }

    private String buildAdoptionRequestPath(String petId) {
        return "/pet/petId/adoption";
    }


}
