package utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public final class ConnectionSingleton {
    // Atributos
    private static ConnectionSingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;

    private ConnectionSingleton(Context context) {
        ConnectionSingleton.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ConnectionSingleton getInstance(Context context) {
        if (singleton == null) {
            singleton = new ConnectionSingleton(context);
        }
        return singleton;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }

    public JsonObjectRequest createPostRequest(String url, HashMap<String, String> parametros) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(parametros),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        System.out.print(response.toString());

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores

                    }
                });

        return request;
    }

    public JsonArrayRequest createGetRequest(String url) {

        JsonArrayRequest request = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Manejo de la respuesta
                        System.out.print(response.toString());

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de la respuesta
                        System.out.print(error.toString());
                    }
                });

        return request;
    }

}