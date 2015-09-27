package utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by agu_k_000 on 27/09/2015.
 */
public class LoginRequest {
    private String salt;
    RequestHandler requestHandler;
    public LoginRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
        this.salt= "";
    }


    //Sincronico
    public String getUserSalt(String user) {

        requestHandler.setServerUrl("http://192.168.1.106:9000");
        String url = "/usuario/" + user + "/salt";
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.GET, RequestHandler.getServerUrl() + url,future,future);
        requestHandler.addToRequestQueue(request);
        String salt = null; // this line will block
        try {
            salt = future.get();
        } catch (InterruptedException e) {
            // exception handling
        } catch (ExecutionException e) {
            // exception handling
        }
        // exception handling
        return salt;
    }

    public boolean isValidUserPassword(String user, Password password) {
        String url = "/login/cuenta?nombreDeUsuario=" + user + "&contraseniaEncriptada=" + password.getEncriptacion();
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, RequestHandler.getServerUrl() + url, future, future);
        requestHandler.addToRequestQueue(request);
        JSONObject response = null;
        try {
            response = future.get();
        } catch (InterruptedException e) {
            // exception handling
            return false;
        } catch (ExecutionException e) {
            // exception handling
            return false;
        }
        return true;
    }
}