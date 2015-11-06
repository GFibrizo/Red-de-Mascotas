package utils;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fabrizio on 30/10/15.
 */
public abstract class BasicOwnerPetRequest {
    protected RequestHandler requestHandler;
    protected String PET_ID;
    protected String OWNER_ID;
    protected String PATH;


    public BasicOwnerPetRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
        requestHandler.setContext(context);
    }

    public void send(String petId, String adopterId) {

        String path =  requestHandler.getServerUrl() + this.buildAdoptionRequestPath(petId);
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(PET_ID, petId);
            jsonRequest.put(OWNER_ID, adopterId);
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
        request.setRetryPolicy(new DefaultRetryPolicy(10000,4,2));

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
        return PATH;
    }

}
