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
    private static String serverUrl = "http://192.168.1.106:9000";
    //private static String serverUrl = "http://10.0.2.2:9000";
    private RequestQueue requestQueue;
    private static Context context;



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
        return RequestHandler.serverUrl;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public void setContext(Context context) {
        RequestHandler.context= context;
    }
    public void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }



}