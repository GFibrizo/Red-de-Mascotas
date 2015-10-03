package utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.support.android.designlibdemo.model.Password;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

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
        String path =   RequestHandler.getServerUrl() + buildSaltPath(user);

        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.GET, path,future,future);
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
        String path = this.buildValidateUserPath(user,password.getEncryption());
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, RequestHandler.getServerUrl() + path, future, future);
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

    private String buildValidateUserPath(String user, String password){
        return "/login/account?userName=" + user + "&encryptedPassword=" + password;
    }

    private String buildSaltPath(String user){
        return "/user/" + user + "/salt";
    }
}
