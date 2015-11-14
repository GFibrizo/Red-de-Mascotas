package model;

import com.mongodb.BasicDBObjectBuilder;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;

import static utils.Constants.*;
import static utils.Constants.DATE_FORMAT;

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

    public String findingDate;


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

    public static LostPet getById(String id) {
        return LostPet.collection.findOneById(id);
    }

    public static List<LostPet> getPublishedByOwnerId(String ownerId) {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        query.add("ownerId", ownerId);
        query.add("publicationStatus", PUBLISHED);
        return LostPet.collection.find(query.get()).toArray();
    }

    public static List<LostPet> getMatches(String type, String gender, String lastSeenDate, GeoLocation location) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMAT);
        LocalDate date = dateTimeFormatter.parseLocalDate(lastSeenDate);
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        query.add("type", type);
        query.add("gender", gender);
        query.push("lastSeenDate").add("$gte", date.minusDays(1).toString(DATE_FORMAT))
                                  .add("$lte", date.plusDays(1).toString(DATE_FORMAT)).pop();
        List<LostPet> basicMatches = LostPet.collection.find(query.get()).toArray();

        List<LostPet> lostPets = new ArrayList<>();
        Double latitude1 = Double.parseDouble(location.latitude) * (Math.PI / 180);
        Double longitude1 = Double.parseDouble(location.longitude) * (Math.PI / 180);
        for (LostPet basicMatch : basicMatches) {
            Double latitude2 = Double.parseDouble(basicMatch.lastSeenLocation.latitude) * (Math.PI / 180);
            Double longitude2 = Double.parseDouble(basicMatch.lastSeenLocation.longitude) * (Math.PI / 180);
            Double distance = Math.acos(Math.sin(latitude1) * Math.sin(latitude2) + Math.cos(latitude1) * Math.cos(latitude2) * Math.cos(longitude2 - longitude1)) * 6371;
            if (distance <= 1) {
                lostPets.add(basicMatch);
            }
        }
        return lostPets;
    }

    public static int getAverageFindingTimeLapse(String fromDate, String toDate, String petType) {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        DateTimeFormatter localDateFormatter = DateTimeFormat.forPattern(DATE_FORMAT);
        String to = localDateFormatter.parseLocalDate(toDate).plusDays(1).toString(DATE_FORMAT);
        if (petType != null) query.add("type", petType);
        query.push("publicationDate").add("$gte", fromDate).add("$lt", to).pop();
        query.push("findingDate").add("$gte", fromDate).add("$lt", to).pop();
        List<LostPet> pets = LostPet.collection.find(query.get()).toArray();
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_HOUR_FORMAT);
        int timeLapse = 0;
        for (LostPet pet : pets) {
            DateTime findingDate = dateTimeFormatter.parseDateTime(pet.findingDate);
            DateTime publicationDate = dateTimeFormatter.parseDateTime(pet.publicationDate);
            timeLapse += Days.daysBetween(publicationDate, findingDate).getDays();
        }
        return pets.size() == 0 ? 0 : timeLapse / pets.size();
    }

    public static void unpublishPet(String petId) {
        LostPet pet = getById(petId);
        pet.updatePublicationStatusToUnpublished();
        LostPet.collection.updateById(petId, pet);
    }

    public static void updateToFound(String petId) {
        LostPet pet = getById(petId);
        pet.updatePublicationStatusToFound();
        pet.updatePublicationStatusToUnpublished();
        LostPet.collection.updateById(petId, pet);
    }

    public static int countPetsPublished(String fromDate, String toDate, String petType) {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMAT);
        String to = dateTimeFormatter.parseLocalDate(toDate).plusDays(1).toString(DATE_FORMAT);
        if (petType != null) query.add("type", petType);
        query.push("publicationDate").add("$gte", fromDate).add("$lt", to).pop();
        return (int) LostPet.collection.count(query.get());
    }

    public static void delete(String id) {
        LostPet lostPet = LostPet.collection.findOneById(id);
        if (lostPet != null)
            LostPet.collection.remove(lostPet);
    }

    private void updatePublicationStatusToPublished() {
        this.publicationStatus = PUBLISHED;
        this.publicationDate = DateTime.now().toString(DATE_HOUR_FORMAT);
    }

    private void updatePublicationStatusToUnpublished() {
        this.publicationStatus = UNPUBLISHED;
    }

    private void updatePublicationStatusToFound() {
        this.findingDate = DateTime.now().toString(DATE_HOUR_FORMAT);
    }

}
