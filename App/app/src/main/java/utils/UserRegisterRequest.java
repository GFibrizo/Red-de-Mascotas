package utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by agu_k_000 on 27/09/2015.
 */
public class UserRegisterRequest {

    RequestHandler requestHandler;

    public UserRegisterRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
//        requestHandler.setServerUrl("http://192.168.1.106:9000");
    }


    //Sincronico
    private void createFacebookUser(JSONObject user) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                RequestHandler.getServerUrl() + buildRegisterFacebooUserPath(),
                user,
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
        requestHandler.addToRequestQueue(request);

    }

    private void createUser(JSONObject user) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                RequestHandler.getServerUrl() + buildRegisterFacebooUserPath(),
                user,
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
        requestHandler.addToRequestQueue(request);

    }

    private String buildRegisterFacebooUserPath() {
        return "/user/facebook";
    }

    private String getJsonData(JSONObject json, String key) {
        String data;
        try {
            data = json.getString(key);
        } catch (JSONException e) {
            data = "";
        }
        return data;
    }

    public void registerFacebookUser(JSONObject json) {
        if (json != null) {

            JSONObject jsonRequest = new JSONObject();
            try {
                //Ver como recuperar mas datos desde el api de facewbook
                jsonRequest.put("facebookId", getJsonData(json, "id"));
                jsonRequest.put("name", getJsonData(json, "name"));
                jsonRequest.put("lastName", getJsonData(json, "last_name"));
                this.createFacebookUser(jsonRequest);
            } catch (JSONException e) {
                return;

            }
        }
    }

    public JSONObject registerUser(JSONObject json) {
        if (json != null) {

            JSONObject jsonRequest = new JSONObject();
            this.createUser(json);

        }
        return null;
    }

}
