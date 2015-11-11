package utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fabrizio on 11/11/15.
 */
public class ReportComplainRequest {

    RequestHandler requestHandler;

    public ReportComplainRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
        requestHandler.setContext(context);
    }

    public void send(String petId, String reporterId, String text) {

        String path =  requestHandler.getServerUrl() + this.buildUnpublishtPath(petId);
        JSONObject object = new JSONObject();
        try {
            object.put("petId", petId);
            object.put("reporterId", reporterId);
            object.put("text", text);
            Log.e("REPORT", object.toString());
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
                        Log.e("REPORT", response.toString());
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR REPORT", error.toString());
                        // Manejo de errores
                    }
                });
        requestHandler.addToRequestQueue(request);

    }

    private String buildUnpublishtPath(String petId) {
        return "/pet/"+ petId +"/report";
    }

}