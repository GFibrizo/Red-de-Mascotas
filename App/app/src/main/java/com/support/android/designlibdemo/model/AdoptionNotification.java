package com.support.android.designlibdemo.model;

import org.json.JSONException;
import org.json.JSONObject;

public class AdoptionNotification {

    public String adopterEmail;
    public String requestDate;
    public String petName;
    public String petImageId;
    public String petId;
    public String adopterId;

    public AdoptionNotification(){}

    public AdoptionNotification(String petId,String adopterId, String adopterEmail, String requestDate, String petName, String petImageId) {
        this.petId = petId;
        this.adopterId = adopterId;
        this.adopterEmail = adopterEmail;
        this.requestDate = requestDate;
        this.petName = petName;
        this.petImageId = petImageId;
    }

    public String getAdopterEmail() {
        return adopterEmail;
    }

    public void setAdopterEmail(String adopterEmail) {
        this.adopterEmail = adopterEmail;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetImageId() {
        return petImageId;
    }

    public void setPetImageId(String petImageId) {
        this.petImageId = petImageId;
    }

    public String getPetId() {
        return petId;
    }

    public String getAdopterId() {
        return adopterId;
    }

    public JSONObject toJson() {
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("petId",this.petId);
            jsonObject.put("adopterId",this.adopterId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}


