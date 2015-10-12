package model;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import org.joda.time.DateTime;
import play.modules.mongodb.jackson.MongoDB;

import java.util.List;

import static utils.Constants.DATE_FORMAT;
import static utils.Constants.PUBLISHED;

public class LostPet {

    @Id
    @ObjectId
    public String id;

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

    public String publicationStatus;

    public String publicationDate;


    private static JacksonDBCollection<LostPet, String> collection = MongoDB.getCollection("lostPets", LostPet.class, String.class);


    public LostPet() { }

    public LostPet(String name, String type, String ownerId, String breed, String gender, String age, String size,
                   List<String> colors, String eyeColor, List<String> images, List<String> videos, Boolean isCastrated,
                   Boolean isOnTemporaryMedicine, Boolean isOnChronicMedicine, String description,
                   GeoLocation lastSeenLocation, String lastSeenDate, String lastSeenHour) {
        this.name = name;
        this.type = type;
        this.ownerId = ownerId;
        this.breed = breed;
        this.gender = gender;
        this.age = age;
        this.size = size;
        this.colors = colors;
        this.eyeColor = eyeColor;
        this.images = images;
        this.videos = videos;
        this.isCastrated = isCastrated;
        this.isOnTemporaryMedicine = isOnTemporaryMedicine;
        this.isOnChronicMedicine = isOnChronicMedicine;
        this.description = description;
        this.lastSeenLocation = lastSeenLocation;
        this.lastSeenDate = lastSeenDate;
        this.lastSeenHour = lastSeenHour;
    }

    public static void create(LostPet lostPet) {
        lostPet.updatePublicationStatusToPublished();
        LostPet.collection.save(lostPet);
    }

    public static void delete(String id) {
        LostPet lostPet = LostPet.collection.findOneById(id);
        if (lostPet != null)
            LostPet.collection.remove(lostPet);
    }

    private void updatePublicationStatusToPublished() {
        this.publicationStatus = PUBLISHED;
        this.publicationDate = DateTime.now().toString(DATE_FORMAT);
    }

}
