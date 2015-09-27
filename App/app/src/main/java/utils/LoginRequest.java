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
    private Context actualContext;
    private String salt;

    public LoginRequest(Context context) {
        this.actualContext = context;
        this.salt= "";
    }


    //Sincronico
//    public String getUserSalt(String user) {
//        RequestHandler requestHandler = RequestHandler.getInstance(this.actualContext);
//        String url = "/usuario/" + user + "/salt";
//        RequestFuture<String> future = RequestFuture.newFuture();
//        StringRequest request = new StringRequest(Request.Method.GET, RequestHandler.getServerUrl() + url,future,future);
//        String salt = null; // this line will block
//        try {
//            salt = future.get();
//        } catch (InterruptedException e) {
//            // exception handling
//        } catch (ExecutionException e) {
//            // exception handling
//        }
//        // exception handling
//        return salt;
//    }

    //Asincronico
    public String getUserSalt(String user) {
        RequestHandler requestHandler = RequestHandler.getInstance(this.actualContext);
        String path = "/usuario/" + user + "/salt";
        //final String[] salt = {null};
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestHandler.getServerUrl() + path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //complete salt
                        salt = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // exception handling
        return salt;
    }

    public boolean isValidUserPassword(String user, Password password) {
        RequestHandler requestHandler = RequestHandler.getInstance(this.actualContext);
        String url = "/login/cuenta";
        // Mapeo de los pares clave-valor
        HashMap<String, String> parameters = new HashMap();
        parameters.put("nombreDeUsuario", user);
        parameters.put("contraseniaEncriptada", password.getEncriptacion());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                RequestHandler.getServerUrl() + url,
                new JSONObject(parameters),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonResponse) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestHandler.addToRequestQueue(request);
        return true;
    }
}
