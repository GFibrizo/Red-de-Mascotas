package com.support.android.designlibdemo.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchForAdoptionFilters {

    public String tipo;

    public String raza;

    public List<String> sexos;

    public List<String> edades;

    public List<String> tamanios;

    public List<String> colores;

    public List<String> coloresDeOjos;

    public String barrio;

    public String ciudad;

    public SearchForAdoptionFilters(JSONObject object) {
        try {
            this.tipo = object.getString("tipo");
            this.raza = object.getString("raza");
            this.sexos = fromJSONArrayToList((JSONArray) object.get("sexos"));
            this.edades = fromJSONArrayToList((JSONArray) object.get("edades"));
            this.tamanios = fromJSONArrayToList((JSONArray) object.get("tamanios"));
            this.colores = fromJSONArrayToList((JSONArray) object.get("colores"));
            this.coloresDeOjos = fromJSONArrayToList((JSONArray) object.get("coloresDeOjos"));
            this.barrio = object.getString("neighbourhood");
            this.ciudad = object.getString("city");
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
