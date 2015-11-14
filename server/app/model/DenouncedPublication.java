package model;

public class DenouncedPublication {

    public String publicationId;

    public String type;

    public String petName;

    public String petSize;

    public String petColor;

    public String petBreed;


    public DenouncedPublication(String publicationId, String type, String petName, String petSize, String petColor,
                                String petBreed) {
        this.publicationId = publicationId;
        this.type = type;
        this.petName = petName;
        this.petSize = petSize;
        this.petColor = petColor;
        this.petBreed = petBreed;
    }

}
