package com.support.android.designlibdemo.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchForAdoptionFilters {

    public String user;

    public String type;

    public String breed;

    public List<String> genders;

    public List<String> ages;

    public List<String> sizes;

    public List<String> colors;

    public List<String> eyeColors;

    public String neighbourhood;

    public String city;

    public Boolean needsTransitHome;

    public SearchForAdoptionFilters(JSONObject object) {
        try {
            this.type = object.getString("type");
            this.breed = object.getString("breed");
            this.genders = fromJSONArrayToList((JSONArray) object.get("genders"));
            this.ages = fromJSONArrayToList((JSONArray) object.get("ages"));
            this.sizes = fromJSONArrayToList((JSONArray) object.get("sizes"));
            this.colors = fromJSONArrayToList((JSONArray) object.get("colors"));
            this.eyeColors = fromJSONArrayToList((JSONArray) object.get("eyeColors"));
            this.neighbourhood = object.getString("neighbourhood");
            this.city = object.getString("city");
            this.user = object.getString("userId");
            this.needsTransitHome = (Boolean) object.get("needsTransitHome");
        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
    }

    private List<String> fromJSONArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        try {
            for (int i=0; i<jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
        return list;
    }

}
