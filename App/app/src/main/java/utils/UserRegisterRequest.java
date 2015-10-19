package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.android.volley.Request.*;

/**
 * Created by agu_k_000 on 27/09/2015.
 */
public class UserRegisterRequest {

    private RequestHandler requestHandler;
    private JSONObject facebookUser = null;
    private JSONObject jsonRequest;
    private String facebookId;
    private SharedPreferences preferences;
    private static final String TAG = "UserRegisterRequest";

    public UserRegisterRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
    }


    //Sincronico
    private JSONObject createFacebookUser(JSONObject user) throws InterruptedException, ExecutionException, TimeoutException {
        String path = RequestHandler.getServerUrl() + buildRegisterFacebookUserPath();
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Method.POST, path, user, future, future);
        requestHandler.addToRequestQueue(request);
        JSONObject response = null;
        try {
            response = future.get(10, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            // exception handling
            Log.e(TAG, "create fb user error" +
                    "" + e.toString());
            throw e;
        }

        return response;


    }

    private JSONObject createUser(JSONObject user) throws InterruptedException, ExecutionException, TimeoutException {
        String path = RequestHandler.getServerUrl() + buildRegisterUserPath();
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Method.POST, path, user, future, future);
        requestHandler.addToRequestQueue(request);
        JSONObject response = null;
        try {
            response = future.get(10, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
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

    public void registerFacebookUser(JSONObject json) throws InterruptedException, ExecutionException, TimeoutException {

        if (json == null) {
            return;
        }
        jsonRequest = new JSONObject();
        try {
            //TODO: Ver como recuperar mas datos desde el api de facewbook
            String[] userName = getJsonData(json, "name").split(" ");

            facebookId = getJsonData(json, "id");
            jsonRequest.put("facebookId", facebookId);
            jsonRequest.put("name", userName[0]);
            jsonRequest.put("lastName", userName[1]);
        } catch (JSONException e) {
            return;
        }
        RegisterUserFacebookTask userFacebookTask = new RegisterUserFacebookTask();
        userFacebookTask.execute();
        return;
    }


    public JSONObject registerUser(JSONObject json) {
        JSONObject response = null;
        if (json != null) {

            try {
                response = this.createUser(json);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                // exception handling
                return response;
            }

        }
        return response;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }


    public class RegisterUserFacebookTask extends AsyncTask<Void, Void, Boolean> {


        RegisterUserFacebookTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //TODO: Primero verificar si existe ya ese usuario
            LoginRequest loginRequest = new LoginRequest(requestHandler.getContext());
            try {
                facebookUser = loginRequest.getFacebookUser(facebookId);
                if (facebookUser == null) {
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
                //Lo creo y lo recupero
                CreateUserFacebookTask createUserFacebookTask = new CreateUserFacebookTask();
                createUserFacebookTask.execute();
            }
        }

        @Override
        protected void onCancelled() {
        }

    }

    public class CreateUserFacebookTask extends AsyncTask<Void, Void, Boolean> {

        CreateUserFacebookTask() {

        }


        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                facebookUser = createFacebookUser(jsonRequest);
                if (facebookUser == null) {
                    return false;
                }
            } catch (TimeoutException | ExecutionException | InterruptedException e) {
                return false;

            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Log.e(TAG, "Put user preferences");
                preferences.edit().putString("userData", facebookUser.toString()).commit();
            }
        }

        @Override
        protected void onCancelled() {
        }


    }

}
