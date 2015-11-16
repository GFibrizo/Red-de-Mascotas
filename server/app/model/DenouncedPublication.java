package model;

import java.util.List;

public class DenouncedPublication {

    public String publicationId;

    public String type;

    public String petName;

    public String petSize;

    public String petColor;

    public String petBreed;

    public String gender;

    public String eyeColor;

    public List<String> images;

    public String description;


    public DenouncedPublication(String publicationId, String type, String petName, String petSize, String petColor,
                                String petBreed, String gender, String eyeColor, List<String> images, String description) {
        this.publicationId = publicationId;
        this.type = type;
        this.petName = petName;
        this.petSize = petSize;
        this.petColor = petColor;
        this.petBreed = petBreed;
        this.gender = gender;
        this.eyeColor = eyeColor;
        this.images = images;
        this.description = description;
    }

}
