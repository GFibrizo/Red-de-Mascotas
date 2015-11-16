package com.support.android.designlibdemo;

import com.support.android.designlibdemo.model.Address;

import java.util.ArrayList;
import java.util.List;

public class MatchedPet {

    public String id;
    public String type;
    public String breed;
    public String gender;
    public String size;
    public List<String> colors;
    public String eyeColor;
    public List<String> images;
    public List<String> videos;
    public String publicationStatus;
    public String publicationDate;
    public String contactEmail;
    public String latitude;
    public String longitude;
    public String lastSeenOrFoundDate;
    public String matchingScore;
    public ArrayList<String> informers;

    public MatchedPet() { }

    public MatchedPet(String id, String type, String breed,
                      String gender, String size, List<String> colors, String eyeColor,
                      List<String> images, String contactEmail, String latitude, String longitude,
                      String lastSeenOrFoundDate, String matchingScore, ArrayList<String> informers) {
        this.id = id;
        this.type = type;
        this.breed = breed;
        this.gender = gender;
        this.size = size;
        this.colors = colors;
        this.eyeColor = eyeColor;
        this.images = images;
        this.contactEmail = contactEmail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastSeenOrFoundDate = lastSeenOrFoundDate;
        this.matchingScore = matchingScore;
        this.informers = informers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColors() {
        String colorPelaje = "" ;
        if (this.colors != null) {
            for (int i = 0; i < this.colors.size(); i++) {
                colorPelaje += this.colors.get(i);
                colorPelaje += " ";
            }
        }
        return colorPelaje;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getImages() {
        String imagenes = "";
        imagenes += this.images.get(0);
        for (int i = 1; i < this.images.size(); i++){
            imagenes += ", ";
            imagenes += this.images.get(i);
        }
        return imagenes;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    public String getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(String publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLastSeenOrFoundDate() {
        return lastSeenOrFoundDate;
    }

    public void setLastSeenOrFoundDate(String lastSeenOrFoundDate) {
        this.lastSeenOrFoundDate = lastSeenOrFoundDate;
    }

    public String getMatchingScore() {
        return matchingScore;
    }

    public void setMatchingScore(String matchingScore) {
        this.matchingScore = matchingScore;
    }

    public ArrayList<String> getInformers() {return informers;}

    public void setInformers(ArrayList<String> informers) {
        this.informers = informers;
    }
}
