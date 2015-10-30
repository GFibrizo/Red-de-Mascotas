package model;

import com.mongodb.BasicDBObjectBuilder;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;

import static utils.Constants.DATE_FORMAT;
import static utils.Constants.DATE_HOUR_FORMAT;
import static utils.Constants.PUBLISHED;
import static utils.Constants.UNPUBLISHED;

public class FoundPet {

    @Id
    @ObjectId
    public String id;

    public String type;

    public String finderId;

    public String breed;

    public String gender;

    public String size;

    public List<String> colors;

    public String eyeColor;

    public Boolean needsTransitHome;

    public String transitHomeUser;

    public List<String> images;

    public List<String> videos;

    public String description;

    public GeoLocation foundLocation;

    public String foundDate;

    public String foundHour;

    public String publicationStatus;

    public String publicationDate;


    private static JacksonDBCollection<FoundPet, String> collection = MongoDB.getCollection("foundPets", FoundPet.class, String.class);


    public FoundPet() { }

    public FoundPet(String type, String finderId, String breed, String gender, String size, List<String> colors,
                    String eyeColor, Boolean needsTransitHome, List<String> images, List<String> videos,
                    String description, GeoLocation foundLocation, String foundDate, String foundHour) {
        this.type = type;
        this.finderId = finderId;
        this.breed = breed;
        this.gender = gender;
        this.size = size;
        this.colors = colors;
        this.eyeColor = eyeColor;
        this.needsTransitHome = needsTransitHome;
        this.images = images;
        this.videos = videos;
        this.description = description;
        this.foundLocation = foundLocation;
        this.foundDate = foundDate;
        this.foundHour = foundHour;
    }

    public static void create(FoundPet foundPet) {
        foundPet.updatePublicationStatusToPublished();
        FoundPet.collection.save(foundPet);
    }

    public static FoundPet getById(String id) {
        return FoundPet.collection.findOneById(id);
    }

    public static List<FoundPet> getPublishedByFinderId(String finderId) {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        query.add("finderId", finderId);
        query.add("publicationStatus", PUBLISHED);
        return FoundPet.collection.find(query.get()).toArray();
    }

    public static List<FoundPet> getMatches(String type, String gender, String lastSeenDate, GeoLocation location) {
        String lastSeenDateSplit = lastSeenDate;
        if (lastSeenDate.split(" ").length > 1){ //TODO: ver si lo saco o lo dejo
            lastSeenDateSplit = lastSeenDate.split(" ")[1];
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMAT);
        LocalDate date = dateTimeFormatter.parseLocalDate(lastSeenDateSplit);
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        query.add("type", type);
        query.add("gender", gender);
        query.push("foundDate").add("$gte", date.minusDays(1).toString(DATE_FORMAT))
                               .add("$lte", date.plusDays(1).toString(DATE_FORMAT)).pop();
        List<FoundPet> basicMatches = FoundPet.collection.find(query.get()).toArray();

        List<FoundPet> foundPets = new ArrayList<>();
        Double latitude1 = Double.parseDouble(location.latitude) * (Math.PI / 180);
        Double longitude1 = Double.parseDouble(location.longitude) * (Math.PI / 180);
        for (FoundPet basicMatch : basicMatches) {
            Double latitude2 = Double.parseDouble(basicMatch.foundLocation.latitude) * (Math.PI / 180);
            Double longitude2 = Double.parseDouble(basicMatch.foundLocation.longitude) * (Math.PI / 180);
            Double distance = Math.acos(Math.sin(latitude1) * Math.sin(latitude2) + Math.cos(latitude1) * Math.cos(latitude2) * Math.cos(longitude2 - longitude1)) * 6371;
            if (distance <= 1) {
                foundPets.add(basicMatch);
            }
        }
        return foundPets;
    }

    public static void unpublishPet(String petId) {
        FoundPet pet = getById(petId);
        pet.updatePublicationStatusToUnpublished();
        FoundPet.collection.updateById(petId, pet);
    }

    public static void delete(String id) {
        FoundPet foundPet = FoundPet.collection.findOneById(id);
        if (foundPet != null)
            FoundPet.collection.remove(foundPet);
    }

    private void updatePublicationStatusToPublished() {
        this.publicationStatus = PUBLISHED;
        this.publicationDate = DateTime.now().toString(DATE_HOUR_FORMAT);
    }

    private void updatePublicationStatusToUnpublished() {
        this.publicationStatus = UNPUBLISHED;
    }

}
