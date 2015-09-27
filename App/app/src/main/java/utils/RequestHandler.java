package utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;


public final class RequestHandler {
    // Atributos
    private static RequestHandler singleton;
    private RequestQueue requestQueue;
    private static Context context;
    private static String serverUrl = "http://192.168.1.106:9000";
    private ResponseHandler responseHandler;


    private RequestHandler(Context context) {
        RequestHandler.context = context;
        requestQueue = getRequestQueue();
        responseHandler = new ResponseHandler();
    }

    public static synchronized RequestHandler getInstance(Context context) {
        if (singleton == null) {
            singleton = new RequestHandler(context);
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
                    public void onResponse(JSONObject jsonResponse) {
                        // Manejo de la respuesta
                        responseHandler.setResponse(jsonResponse);
                        responseHandler.setMessage(ResponseHandler.OK);
                        responseHandler.setOkStatus();

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores
                        responseHandler.setMessage(error.getMessage());
                        responseHandler.setErrorStatus();

                    }
                });

        return request;
    }

    public JsonObjectRequest createGetRequestJson(String url) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                this.serverUrl + url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonResponse) {
                        responseHandler.setResponse(jsonResponse);
                        responseHandler.setMessage(ResponseHandler.OK);
                        responseHandler.setOkStatus();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseHandler.setMessage(error.getMessage());
                        responseHandler.setErrorStatus();
                    }
                }
        );

        return request;
    }

    public StringRequest createGetRequestString(String url) {

        StringRequest request = new StringRequest(Request.Method.GET, this.serverUrl + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responseHandler.setResponse(response);
                        responseHandler.setMessage(ResponseHandler.OK);
                        responseHandler.setOkStatus();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseHandler.setMessage(error.getMessage());
                responseHandler.setErrorStatus();
            }
        });

        return request;
    }


    public ResponseHandler getResponseHandler() {
        return responseHandler;
    }
}