package model;

import java.util.List;

public class MyPet implements Comparable<MyPet> {

    public String id;

    // En el caso de mascota encontrada, el nombre viene vacío
    public String name;

    public String type;

    public String breed;

    public String gender;

    public List<String> images;

    public String publicationDate;

    public String publicationType;

    public MyPet(String id, String name, String type, String breed, String gender, List<String> images,
                 String publicationDate, String publicationType) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.gender = gender;
        this.images = images;
        this.publicationDate = publicationDate;
        this.publicationType = publicationType;
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
