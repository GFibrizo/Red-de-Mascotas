package com.support.android.designlibdemo.model;

import java.util.List;

public class PetAdoption {

    public String id;
    public String name;
    public String type;
    public String ownerId;
    public Address address;
    public String breed;
    public String gender;
    public String age;
    public String size;
    public List<String> colors;
    public String eyeColor;
    public List<String> behavior;
    public List<String> images;
    public List<String> videos;
    public Boolean needsTransitHome;
    public String transitHomeUser;
    public Boolean isCastrated;
    public Boolean isOnTemporaryMedicine;
    public Boolean isOnChronicMedicine;
    public String description;
    public String publicationStatus;
    public String publicationDate;

    public PetAdoption() { }

    public PetAdoption(String name, String type, String ownerId, Address address, String breed,
                       String gender, String age, String size, List<String> colors, String eyeColor,
                       List<String> behavior, List<String> images, Boolean needsTransitHome,
                       Boolean isCastrated, Boolean isOnTemporaryMedicine, Boolean isOnChronicMedicine, String description) {
        this.name = name;
        this.type = type;
        this.ownerId = ownerId;
        this.address = address;
        this.breed = breed;
        this.gender = gender;
        this.age = age;
        this.size = size;
        this.colors = colors;
        this.eyeColor = eyeColor;
        this.behavior = behavior;
        this.images = images;
        this.needsTransitHome = needsTransitHome;
        this.isCastrated = isCastrated;
        this.isOnTemporaryMedicine = isOnTemporaryMedicine;
        this.isOnChronicMedicine = isOnChronicMedicine;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<String> getColors() {
        return colors;
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

    public List<String> getBehavior() {
        return behavior;
    }

    public void setBehavior(List<String> behavior) {
        this.behavior = behavior;
    }

    public List<String> getImages() {
        return images;
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

    public Boolean getNeedsTransitHome() {
        return needsTransitHome;
    }

    public void setNeedsTransitHome(Boolean needsTransitHome) {
        this.needsTransitHome = needsTransitHome;
    }

    public String getTransitHomeUser() {
        return transitHomeUser;
    }

    public void setTransitHomeUser(String transitHomeUser) {
        this.transitHomeUser = transitHomeUser;
    }

    public Boolean getIsCastrated() {
        return isCastrated;
    }

    public void setIsCastrated(Boolean isCastrated) {
        this.isCastrated = isCastrated;
    }

    public Boolean getIsOnTemporaryMedicine() {
        return isOnTemporaryMedicine;
    }

    public void setIsOnTemporaryMedicine(Boolean isOnTemporaryMedicine) {
        this.isOnTemporaryMedicine = isOnTemporaryMedicine;
    }

    public Boolean getIsOnChronicMedicine() {
        return isOnChronicMedicine;
    }

    public void setIsOnChronicMedicine(Boolean isOnChronicMedicine) {
        this.isOnChronicMedicine = isOnChronicMedicine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
