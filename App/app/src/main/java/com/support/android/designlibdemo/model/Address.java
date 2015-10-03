package com.support.android.designlibdemo.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Address {

    public String street;
    public String number;
    public String neighbourhood;
    public String city;
    public String province;
    public String country;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public JSONObject toJson() {
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("street",this.street);
            jsonObject.put("number",this.number);
            jsonObject.put("neighbourhood",this.neighbourhood);
            jsonObject.put("city",this.city);
            jsonObject.put("province",this.province);
            jsonObject.put("country",this.country);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
