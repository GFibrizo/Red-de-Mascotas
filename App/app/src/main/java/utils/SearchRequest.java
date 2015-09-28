package utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.support.android.designlibdemo.model.Password;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class SearchRequest {
    RequestHandler requestHandler;

    public SearchRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
        requestHandler.setServerUrl("http://192.168.1.106:9000");
    }


    public boolean search(String user, Password password) {
        String path = this.buildSearchPetPath("filtros");
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


    private String buildSearchPetPath(String s){
        return "/mascotas/adopcion?";  //TODO: cambiar por path verdadero
    }

}
