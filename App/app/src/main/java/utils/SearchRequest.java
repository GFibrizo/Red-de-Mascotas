package utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.support.android.designlibdemo.model.FiltrosBusquedaAdopcion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchRequest {
    RequestHandler requestHandler;

    public SearchRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
        requestHandler.setServerUrl("http://10.0.2.2:9000");
    }

    public JSONArray search(FiltrosBusquedaAdopcion filters) {
        String path = this.buildSearchPetPath(filters);
        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, RequestHandler.getServerUrl() + path, future, future);
        requestHandler.addToRequestQueue(request);
        JSONArray response = null;
        try {
            response = future.get();
        } catch (InterruptedException e) {
            Log.e("Error al hacer GET", e.getMessage());
        } catch (ExecutionException e) {
            Log.e("Error al hacer GET", e.getMessage());
        }
        return response;
    }

    private String buildSearchPetPath(FiltrosBusquedaAdopcion filters) {
        String queryString = "tipo=" + filters.tipo + "&";
        if (filters.sexos != null) queryString += fromListToString("sexos", filters.sexos);
        if (filters.raza != null && !filters.raza.isEmpty()) queryString += "raza=" + filters.raza + "&";
        if (filters.edades != null) queryString += fromListToString("edades", filters.edades);
        if (filters.tamanios != null) queryString += fromListToString("tamanios", filters.tamanios);
        if (filters.colores != null) queryString += fromListToString("colores", filters.colores);
        if (filters.coloresDeOjos != null) queryString += fromListToString("coloresDeOjos", filters.coloresDeOjos);
        if (filters.ciudad != null && !filters.ciudad.isEmpty()) queryString += "ciudad=" + filters.ciudad + "&";
        if (filters.barrio != null && !filters.barrio.isEmpty()) queryString += "barrio=" + filters.barrio;
        return "/mascotas/adopcion?" + queryString;
    }

    private String fromListToString(String field, List<String> list) {
        String str = "";
        for (String item : list) {
            str += field + "[]=" + item + "&";
        }
        return str;
    }

}
