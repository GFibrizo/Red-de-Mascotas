package model;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import model.external.SearchForAdoptionFilters;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import org.joda.time.DateTime;
import play.modules.mongodb.jackson.MongoDB;

import java.util.List;

public class PetAdoption {

    @Id
    @ObjectId
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

    private static final int MAX_LAST_PUBLICATIONS = 10;


    private static JacksonDBCollection<PetAdoption, String> collection = MongoDB.getCollection("petsAdoption", PetAdoption.class, String.class);


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

    public static List<PetAdoption> getByOwnerId(String ownerId) {
        return PetAdoption.collection.find(new BasicDBObject("ownerId", ownerId)).toArray();
    }

    public static List<PetAdoption> search(SearchForAdoptionFilters filters) {
        return PetAdoption.collection.find(buildSearchFilters(filters)).toArray();
    }

    public static List<PetAdoption> getLastPublications() {
        return PetAdoption.collection.find().sort(new BasicDBObject("publicationDate", -1)).limit(MAX_LAST_PUBLICATIONS).toArray();
    }

    public static void create(PetAdoption petAdoption) {
        petAdoption.updatePublicationStatusToPublished();
        PetAdoption.collection.save(petAdoption);
    }

    public static void delete(String id) {
        PetAdoption petAdoption = PetAdoption.collection.findOneById(id);
        if (petAdoption != null)
            PetAdoption.collection.remove(petAdoption);
    }

    private static DBObject buildSearchFilters(SearchForAdoptionFilters filtros) {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        if (filtros.type != null) query.add("type", filtros.type);
        if (filtros.breed != null) query.add("breed", filtros.breed);
        if (filtros.genders != null) query.push("gender").add("$in", filtros.genders.toArray()).pop();
        if (filtros.ages != null) query.push("age").add("$in", filtros.ages.toArray()).pop();
        if (filtros.sizes != null) query.push("size").add("$in", filtros.sizes.toArray()).pop();
        if (filtros.colors != null) query.push("colors").add("$in", filtros.colors.toArray()).pop();
        if (filtros.eyeColors != null) query.push("eyeColor").add("$in", filtros.eyeColors.toArray()).pop();
        if (filtros.neighbourhood != null) query.add("address.neighbourhood", filtros.neighbourhood);
        if (filtros.city != null) query.add("address.city", filtros.city);
        return query.get();
    }

    private void updatePublicationStatusToPublished() {
        this.publicationStatus = "PUBLISHED";
        this.publicationDate = DateTime.now().toString("yyyy/MM/dd HH:mm:ss.SSS");
    }

}
