package model;

import java.util.List;

public class MyPet implements Comparable<MyPet> {

    public String id;

    public String ownerId;

    // En el caso de mascota encontrada, el nombre viene vacío
    public String name;

    public String type;

    public String breed;

    public String gender;

    public String size;

    public String age;

    public List<String> colors;

    public String eyeColor;

    public List<String> images;

    public String publicationDate;

    public String publicationType;

    public String publicationStatus;

    public MyPet(String id, String ownerId, String name, String type, String breed, String gender, String size, String age,
                 List<String> colors, String eyeColor, List<String> images, String publicationDate, String publicationType,
                 String publicationStatus) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.gender = gender;
        this.size = size;
        this.age = age;
        this.colors = colors;
        this.eyeColor = eyeColor;
        this.images = images;
        this.publicationDate = publicationDate;
        this.publicationType = publicationType;
        this.publicationStatus = publicationStatus;
    }

    @Override
    public int compareTo(MyPet another) {
        if (this.publicationDate.compareTo(another.publicationDate) < 0) {
            return -1;
        } else {
            return 1;
        }
    }

}
