package utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/** Trae los match de un usuario a partir de su Id
 *  Pueden ser match de hallazgo/extravio o de busqueda sin resultado
 ***/

public class MatchRequest {
    private final String PETS_FOUND = "PETS_FOUND";
    private final String NEW_SEARCH_MATCHES = "NEW_SEARCH_MATCHES";
    RequestHandler requestHandler;

    public MatchRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
        requestHandler.setContext(context);
    }

    /** Llama al servicio de match de hallazgo/extravio o al servicio
     *  de match de busquedas sin resultado segun el notificationType
     * @param userId id de usuario
     * @param notificationType puede ser PETS_FOUND o NEW_SEARCH_MATCHES
     * @return JSONArray de resultados del match
     */
    public JSONArray getMatch(String userId, String notificationType) {
        String path;
        if (notificationType.equals(PETS_FOUND)){
            path =  requestHandler.getServerUrl() + this.buildMatchNotificationPath(userId);
        }else { //NEW_SEARCH_MATCHES
            path = requestHandler.getServerUrl() + this.buildSearchNotificationPath(userId);
        }

        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, path, future, future);
        request.setRetryPolicy(new DefaultRetryPolicy(5000,4,2));
        requestHandler.addToRequestQueue(request);
        JSONArray response = null;
        try {
            response =  future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e("Retrieve cards api call interrupted.", String.valueOf(e));
        } catch (ExecutionException e) {
            Log.e("Retrieve cards api call failed.", String.valueOf(e));
        } catch (TimeoutException e) {
            Log.e("Retrieve cards api call timed out.", String.valueOf(e));
        }
        return response;
    }

    private String buildMatchNotificationPath(String userId) {
        return "/user/"+userId+"/pets/matches";
    }

    private String buildSearchNotificationPath(String userId){
        return "/user/"+userId+"/search-matches";
    }

}
