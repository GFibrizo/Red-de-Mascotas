package utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.support.android.designlibdemo.model.SearchForAdoptionFilters;

import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SearchRequest {
    RequestHandler requestHandler;

    public SearchRequest(Context context) {
        requestHandler = RequestHandler.getInstance(context);
        requestHandler.setContext(context);
    }

    public JSONArray search(SearchForAdoptionFilters filters) {

        String path =  requestHandler.getServerUrl() + this.buildSearchPetPath(filters);

        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,  path, future, future);
        requestHandler.addToRequestQueue(request);
        JSONArray response = null;
        request.setRetryPolicy(new DefaultRetryPolicy(5000,4,2));

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



//TODO: probar que ande bien.
    private String buildSearchPetPath(SearchForAdoptionFilters filters) {
        String queryString = "type=" + filters.type + "&"  + "userId=" +filters.user + "&";
        if (filters.genders != null) queryString += fromListToString("genders", filters.genders);
        if (filters.breed != null && !filters.breed.isEmpty()) queryString += "breed=" + filters.breed + "&";
        if (filters.ages != null) queryString += fromListToString("ages", filters.ages);
        if (filters.sizes != null) queryString += fromListToString("sizes", filters.sizes);
        if (filters.colors != null) queryString += fromListToString("colors", filters.colors);
        if (filters.eyeColors != null) queryString += fromListToString("eyeColors", filters.eyeColors);
        if (filters.city != null && !filters.city.isEmpty()) queryString += "city=" + filters.city + "&";
        if (filters.neighbourhood != null && !filters.neighbourhood.isEmpty()) queryString += "neighbourhood=" + filters.neighbourhood + "&";
        if (filters.needsTransitHome != null) queryString += "needsTransitHome=" + filters.needsTransitHome;
        Log.i("buildSearchPetPath", "/pets/adoption?" + queryString);
        return "/pets/adoption?" + queryString;
    }




    private String fromListToString(String field, List<String> list) {
        String str = "";
        for (String item : list) {
            str += field + "[]=" + item + "&";
        }
        return str;
    }

}
