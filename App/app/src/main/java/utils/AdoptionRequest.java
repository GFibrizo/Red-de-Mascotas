package utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/** Pedido de adopción, llama al servicio "/pet/petId/adoption" que crea notificación hacia el usuario que
 *  publicó la mascota en adopción. **/

public class AdoptionRequest extends BasicOwnerPetRequest {

    public AdoptionRequest(Context context) {
        super(context);
        PATH = "/pet/petId/adoption";
        PET_ID = "petId";
        OWNER_ID = "adopterId";
    }
}
