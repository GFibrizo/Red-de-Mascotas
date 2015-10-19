package utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fabrizio on 18/10/15.
 */
public class UnpublishRequest {
    RequestHandler requestHandler;

    public UnpublishRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
        requestHandler.setContext(context);
    }

    public void send(String petId) {

        String path =  requestHandler.getServerUrl() + this.buildUnpublishtPath(petId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, path,
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

    }

    private String buildUnpublishtPath(String petId) {
        return " /pet/"+ petId +"/unpublish";
    }

}
