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
    private static String serverUrl;
    private RequestQueue requestQueue;
    private static Context context;

    public static void setServerUrl(String serverUrl) {
        RequestHandler.serverUrl = serverUrl;
    }

    private RequestHandler(Context context) {
        RequestHandler.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestHandler getInstance(Context context) {
        if (singleton == null) {
            singleton = new RequestHandler(context);
        }
        return singleton;
    }

    public static String getServerUrl() {
        return serverUrl;
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



}