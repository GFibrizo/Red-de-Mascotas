package com.support.android.designlibdemo.model;

import org.json.JSONException;
import org.json.JSONObject;

public class InquirerNotification {

    public String notificationType;
    public String inquirerEmail;
    public String requestDate;
    public String petName;
    public String petImageId;
    public String petId;
    public String inquirerId;
    public String status;

    public InquirerNotification(){}

    public InquirerNotification(String notificationType,
                                String petId,
                                String inquirerId,
                                String inquirerEmail,
                                String requestDate,
                                String petName,
                                String petImageId,
                                String status) {
        this.notificationType = notificationType;
        this.petId = petId;
        this.inquirerId = inquirerId;
        this.inquirerEmail = inquirerEmail;
        this.requestDate = requestDate;
        this.petName = petName;
        this.petImageId = petImageId;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JSONObject toJson() {
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("petId",this.petId);
            jsonObject.put("adopterId",this.inquirerId);
            jsonObject.put("transitHomeUser", this.inquirerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}


