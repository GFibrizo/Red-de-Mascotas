package com.support.android.designlibdemo.model;

import org.json.JSONException;
import org.json.JSONObject;

public class InquirerNotification {

    public String inquirerEmail;
    public String requestDate;
    public String petName;
    public String petImageId;
    public String petId;
    public String inquirerId;

    public InquirerNotification(){}

    public InquirerNotification(String petId, String inquirerId, String inquirerEmail, String requestDate, String petName, String petImageId) {
        this.petId = petId;
        this.inquirerId = inquirerId;
        this.inquirerEmail = inquirerEmail;
        this.requestDate = requestDate;
        this.petName = petName;
        this.petImageId = petImageId;
    }

    public String getInquirerEmail() {
        return inquirerEmail;
    }

    public void setInquirerEmail(String inquirerEmail) {
        this.inquirerEmail = inquirerEmail;
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

    public String getInquirerId() {
        return inquirerId;
    }

    public JSONObject toJson() {
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("petId",this.petId);
            jsonObject.put("inquirerId",this.inquirerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}


