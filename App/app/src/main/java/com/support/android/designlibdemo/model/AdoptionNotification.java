package com.support.android.designlibdemo.model;

public class AdoptionNotification {

    public String adopterEmail;
    public String requestDate;
    public String petName;
    public String petImageId;

    public AdoptionNotification(){}

    public AdoptionNotification(String adopterEmail, String requestDate, String petName, String petImageId) {
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
}


