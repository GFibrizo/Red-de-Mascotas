package utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by fabrizio on 18/10/15.
 */
public class UnpublishRequest {
    RequestHandler requestHandler;

    public UnpublishRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
        requestHandler.setContext(context);
    }

    public void send(String petId, String type) {

        String path =  requestHandler.getServerUrl() + this.buildUnpublishtPath(petId);
        JSONObject object = new JSONObject();
        try {
            object.put("petId", petId);
            object.put("publicationType", type);
            Log.e("UNPUBLISH", object.toString());
        } catch (JSONException e) {
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                path,
                object.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        Log.e("REMOVE", response.toString());
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR REMOVE", error.toString());
                        // Manejo de errores
                    }
                });
        requestHandler.addToRequestQueue(request);

    }

    private String buildUnpublishtPath(String petId) {
        return "/pet/"+ petId +"/unpublish";
    }

}
