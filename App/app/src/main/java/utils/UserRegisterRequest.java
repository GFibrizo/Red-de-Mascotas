package utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.facebook.Profile;
import com.support.android.designlibdemo.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.android.volley.Request.*;

/**
 * Created by agu_k_000 on 27/09/2015.
 */
public class UserRegisterRequest extends AsyncTask<Void, Void, Boolean>{

    private RequestHandler requestHandler;
    private JSONObject facebookUser = null;
    private JSONObject jsonRequest;
    private String facebookId;
    public UserRegisterRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
    }


    //Sincronico
    private JSONObject createFacebookUser(JSONObject user) throws InterruptedException, ExecutionException, TimeoutException{
        String path =   RequestHandler.getServerUrl() + buildRegisterFacebookUserPath();
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Method.POST, path,  user, future, future);
        requestHandler.addToRequestQueue(request);
        JSONObject response = null;
        try {
            response = future.get(10, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException  | TimeoutException e) {
            // exception handling
            throw e;
        }

        return response;


    }

    private JSONObject createUser(JSONObject user) throws InterruptedException, ExecutionException, TimeoutException{
        String path =   RequestHandler.getServerUrl() + buildRegisterUserPath();
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Method.POST, path,  user, future, future);
        requestHandler.addToRequestQueue(request);
        JSONObject response = null;
        try {
            response = future.get(10, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException  | TimeoutException e) {
            // exception handling
            throw e;
        }

        return response;
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
        jsonRequest = new JSONObject();
        try {
            //TODO: Ver como recuperar mas datos desde el api de facewbook

            facebookId = getJsonData(json, "id");
            jsonRequest.put("facebookId", facebookId);
            jsonRequest.put("name", getJsonData(json, "name"));
            jsonRequest.put("lastName", getJsonData(json, "last_name"));
        } catch (JSONException e) {
            return null;
        }

        this.execute();

        return facebookUser;
    }


    public JSONObject registerUser(JSONObject json) {
        JSONObject response = null;
        if (json != null) {

            try {
                response = this.createUser(json);
            } catch (InterruptedException | ExecutionException  | TimeoutException e) {
                // exception handling
                return response;
            }

        }
        return response;
    }



        @Override
        protected Boolean doInBackground(Void... params) {
            //TODO: Primero verificar si existe ya ese usuario
            LoginRequest loginRequest = new LoginRequest(requestHandler.getContext());
            try {
                facebookUser = loginRequest.getFacebookUser(facebookId);
                if (facebookUser == null) {
                    //Lo creo y lo recupero
                    return false;

                }
            } catch (TimeoutException | ExecutionException | InterruptedException e) {
                return false;
            }
            return true;


        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (!success) {
                try {
                    facebookUser  = this.createFacebookUser(jsonRequest);
                } catch (TimeoutException | ExecutionException | InterruptedException e) {

                }
            }
        }

        @Override
        protected void onCancelled() { }



}
