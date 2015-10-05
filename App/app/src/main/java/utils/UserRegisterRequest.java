package utils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by agu_k_000 on 27/09/2015.
 */
public class UserRegisterRequest {

    RequestHandler requestHandler;

    public UserRegisterRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
    }


    //Sincronico
    private void createFacebookUser(JSONObject user) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                RequestHandler.getServerUrl() + buildRegisterFacebookUserPath(),
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
                RequestHandler.getServerUrl() + buildRegisterUserPath(),
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

    private String buildRegisterUserPath() {
        return "/user/account";
    }

    private String buildRegisterFacebookUserPath() {
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

    public JSONObject registerFacebookUser(JSONObject json) throws InterruptedException, ExecutionException, TimeoutException {

        if (json == null) {
            return null;
        }
        JSONObject jsonRequest = new JSONObject();
        try {
            //TODO: Ver como recuperar mas datos desde el api de facewbook
            jsonRequest.put("facebookId", getJsonData(json, "id"));
            jsonRequest.put("name", getJsonData(json, "name"));
            jsonRequest.put("lastName", getJsonData(json, "last_name"));

        } catch (JSONException e) {
            return null;

        }
        //TODO: Primero verificar si existe ya ese usuario
        LoginRequest loginRequest = new LoginRequest(requestHandler.getContext());
        JSONObject facebookUser = null;
        try {
            facebookUser = loginRequest.getFacebookUser(getJsonData(json, "id"));
            if (facebookUser == null) {
                //Lo creo y lo recupero
                this.createFacebookUser(jsonRequest);
                facebookUser = loginRequest.getFacebookUser(getJsonData(json, "id"));
            }
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            throw e;
        }
        return facebookUser;
    }


    public JSONObject registerUser(JSONObject json) {
        if (json != null) {
            JSONObject jsonRequest = new JSONObject();
            this.createUser(json);
        }
        return null;
    }

}
