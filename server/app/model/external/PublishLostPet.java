package model.external;

import model.GeoLocation;

import java.util.List;

public class PublishLostPet {

    public String name;

    public String type;

    public String ownerId;

    public String breed;

    public String gender;

    public String age;

    public String size;

    public List<String> colors;

    public String eyeColor;

    public List<String> images;

    public List<String> videos;

    public Boolean isCastrated;

    public Boolean isOnTemporaryMedicine;

    public Boolean isOnChronicMedicine;

    public String description;

    public GeoLocation lastSeenLocation;

    public String lastSeenDate;

    public String lastSeenHour;

}
