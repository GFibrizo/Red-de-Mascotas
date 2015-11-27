package model;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import model.external.*;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;

import static utils.Constants.*;

public class PetAdoption implements Comparable<PetAdoption> {

    @Id
    @ObjectId
    public String id;

    public String name;

    public String type;

    public String ownerId;

    public String adopterId;

    public ShortAddress address;

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

    public String adoptionDate;

    public String lastModifiedDate;

    public List<Adoption> adoptionRequests;

    public List<TransitHome> transitHomeRequests;

    public List<PublicationReport> reports;

    public Boolean hasBeenBlockedOnce;


    private static JacksonDBCollection<PetAdoption, String> collection = MongoDB.getCollection("petsAdoption", PetAdoption.class, String.class);


    public PetAdoption() { }

    public PetAdoption(String name, String type, String ownerId, ShortAddress address, String breed,
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
        this.hasBeenBlockedOnce = false;
    }

    @Override
    public int compareTo(PetAdoption another) {
        if (this.publicationDate.compareTo(another.publicationDate) < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    public Boolean hasAdoptionRequestsNotSeen() {
        if (this.adoptionRequests == null)
            return false;
        for (Adoption request: this.adoptionRequests) {
            if (request.lastSeenDate == null || request.lastSeenDate.compareTo(request.requestDate) < 0) {
                return true;
            }
        }
        return false;
    }

    public static void create(PetAdoption petAdoption) {
        petAdoption.updatePublicationStatusToPublished();
        PetAdoption.collection.save(petAdoption);
    }

    public static PetAdoption getById(String id) {
        return PetAdoption.collection.findOneById(id);
    }

    public static List<PetAdoption> getPublishedAndBlockedByOwnerId(String ownerId) {
        ArrayList<String> status = new ArrayList<>();
        status.add(PUBLISHED);
        status.add(BLOCKED);
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        query.add("ownerId", ownerId);
        query.push("publicationStatus").add("$in", status).pop();
        return PetAdoption.collection.find(query.get()).toArray();
    }

    public static List<PetAdoption> getByOwnerId(String ownerId) {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        query.add("ownerId", ownerId);
        List<PetAdoption> pets = PetAdoption.collection.find(query.get()).toArray();
        List<PetAdoption> petsToRemove = new ArrayList<>();
        for (PetAdoption pet : pets) {
            if ((pet.publicationStatus.equals(UNPUBLISHED) && pet.adopterId == null) || pet.publicationStatus.equals(BLOCKED))
                petsToRemove.add(pet);
        }
        pets.removeAll(petsToRemove);
        return pets;
    }

    public static List<PetAdoption> getPetsWithReports() {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        query.add("publicationStatus", PUBLISHED);
        query.push("reports").add("$ne", null).pop();
        return PetAdoption.collection.find(query.get()).toArray();
    }

    public static List<PetAdoption> search(SearchForAdoptionFilters filters) {
        return PetAdoption.collection.find(buildSearchFilters(filters)).toArray();
    }

    public static List<PetAdoption> getLastPublications() {
        return PetAdoption.collection.find(new BasicDBObject("publicationStatus", PUBLISHED))
                .sort(new BasicDBObject("publicationDate", -1)).limit(MAX_LAST_PUBLICATIONS).toArray();
    }

    public static PetAdoption addAdoptionRequest(AdoptionRequest request) {
        PetAdoption pet = getById(request.petId);
        pet.addNewAdoptionRequest(request);
        PetAdoption.collection.updateById(request.petId, pet);
        return pet;
    }

    public static PetAdoption acceptAdoptionRequest(AdoptionRequest request) {
        PetAdoption pet = getById(request.petId);
        pet.updatePublicationStatusToAdopted(request.adopterId);
        PetAdoption.collection.updateById(request.petId, pet);
        return pet;
    }

    public static PetAdoption addTransitHomeRequest(TransitHomeRequest request) {
        PetAdoption pet = getById(request.petId);
        pet.addNewTransitHomeRequest(request);
        PetAdoption.collection.updateById(request.petId, pet);
        return pet;
    }

    public static PetAdoption acceptTakeInTransitRequest(TransitHomeRequest request) {
        PetAdoption pet = getById(request.petId);
        pet.updateTransitHomeRequestsToAccepted(request.transitHomeUser);
        PetAdoption.collection.updateById(request.petId, pet);
        return pet;
    }

    public static PetAdoption addReport(ReportPublicationRequest request) {
        PetAdoption pet = getById(request.petId);
        pet.addNewReport(request);
        PetAdoption.collection.updateById(request.petId, pet);
        return pet;
    }

    public static PetAdoption acceptReport(AcceptPublicationReportRequest request) {
        PetAdoption pet = getById(request.petId);
        pet.updatePublicationStatusToBlocked(request.informer);
        PetAdoption.collection.updateById(request.petId, pet);
        return pet;
    }

    public static PetAdoption rejectReport(AcceptPublicationReportRequest request) {
        PetAdoption pet = getById(request.petId);
        pet.updateReportToRejected(request.informer);
        PetAdoption.collection.updateById(request.petId, pet);
        return pet;
    }

    public static void updateLastSeenAdoptionRequests(String petId) {
        PetAdoption pet = getById(petId);
        if (!pet.updateLastSeenRequests())
            return;
        PetAdoption.collection.updateById(petId, pet);
    }

    public static void unpublishPet(String petId) {
        PetAdoption pet = getById(petId);
        pet.updatePublicationStatusToUnpublished();
        PetAdoption.collection.updateById(petId, pet);
    }

    public static int countPetsPublished(String fromDate, String toDate, String petType) {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        DateTimeFormatter argDateTimeFormatter = DateTimeFormat.forPattern(ARG_DATE_FORMAT);
        String from = argDateTimeFormatter.parseLocalDate(fromDate).toString(DATE_FORMAT);
        String to = argDateTimeFormatter.parseLocalDate(toDate).plusDays(1).toString(DATE_FORMAT);
        if (petType != null) query.add("type", petType);
        query.push("publicationDate").add("$gte", from).add("$lt", to).pop();
        return (int) PetAdoption.collection.count(query.get());
    }

    public static int countPetsAdopted(String fromDate, String toDate, String petType) {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        DateTimeFormatter argDateTimeFormatter = DateTimeFormat.forPattern(ARG_DATE_FORMAT);
        String from = argDateTimeFormatter.parseLocalDate(fromDate).toString(DATE_FORMAT);
        String to = argDateTimeFormatter.parseLocalDate(toDate).plusDays(1).toString(DATE_FORMAT);
        if (petType != null) query.add("type", petType);
        query.push("adoptionDate").add("$gte", from).add("$lt", to).pop();
        return (int) PetAdoption.collection.count(query.get());
    }

    public static int getAverageAdoptionTimeLapse(String fromDate, String toDate, String petType) {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        DateTimeFormatter argDateTimeFormatter = DateTimeFormat.forPattern(ARG_DATE_FORMAT);
        String from = argDateTimeFormatter.parseLocalDate(fromDate).toString(DATE_FORMAT);
        String to = argDateTimeFormatter.parseLocalDate(toDate).plusDays(1).toString(DATE_FORMAT);
        if (petType != null) query.add("type", petType);
        query.push("publicationDate").add("$gte", from).add("$lt", to).pop();
        query.push("adoptionDate").add("$gte", from).add("$lt", to).pop();
        List<PetAdoption> pets = PetAdoption.collection.find(query.get()).toArray();
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_HOUR_FORMAT);
        int timeLapse = 0;
        for (PetAdoption pet : pets) {
            DateTime adoptionDate = dateTimeFormatter.parseDateTime(pet.adoptionDate);
            DateTime publicationDate = dateTimeFormatter.parseDateTime(pet.publicationDate);
            timeLapse += Days.daysBetween(publicationDate, adoptionDate).getDays();
        }
        return pets.size() == 0 ? 0 : timeLapse / pets.size();
    }

    public static void blockAllPetsFromUser(String userId) {
        List<PetAdoption> pets = PetAdoption.collection.find(new BasicDBObject("ownerId", userId)).toArray();
        for (PetAdoption pet : pets) {
            if (pet.publicationStatus.equals(PUBLISHED)) {
                pet.temporaryBlock();
                PetAdoption.collection.updateById(pet.id, pet);
            }
        }
    }

    public static void unblockPetsFromUser(String userId) {
        List<PetAdoption> pets = PetAdoption.collection.find(new BasicDBObject("ownerId", userId)).toArray();
        for (PetAdoption pet : pets) {
            if (pet.publicationStatus.equals(BLOCKED) && !pet.hasBeenBlockedOnce) {
                pet.unblock();
                PetAdoption.collection.updateById(pet.id, pet);
            }
        }
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
        if (filtros.needsTransitHome != null && filtros.needsTransitHome) {
            query.add("needsTransitHome", true);
            query.add("transitHomeUser", null);
        }
        if (filtros.neighbourhood != null) query.add("address.neighbourhood", filtros.neighbourhood);
        if (filtros.city != null) query.add("address.city", filtros.city);
        query.add("publicationStatus", PUBLISHED);
        return query.get();
    }

    private void updatePublicationStatusToPublished() {
        this.publicationStatus = PUBLISHED;
        this.publicationDate = DateTime.now().toString(DATE_HOUR_FORMAT);
        this.lastModifiedDate = DateTime.now().toString(DATE_HOUR_FORMAT);
    }

    private void updatePublicationStatusToUnpublished() {
        this.publicationStatus = UNPUBLISHED;
        this.lastModifiedDate = DateTime.now().toString(DATE_HOUR_FORMAT);
    }

    private void updatePublicationStatusToAdopted(String adopterId) {
        for (Adoption request : this.adoptionRequests) {
            if (request.adopterId.equals(adopterId))
                request.updateStatus(NOTIFICATION_ACCEPTED);
            else
                request.updateStatus(NOTIFICATION_REJECTED);
        }
        this.adopterId = adopterId;
        this.publicationStatus = UNPUBLISHED;
        this.adoptionDate = DateTime.now().toString(DATE_HOUR_FORMAT);
        this.lastModifiedDate = DateTime.now().toString(DATE_HOUR_FORMAT);
    }

    private void updateTransitHomeRequestsToAccepted(String transitHomeUser) {
        for (TransitHome request : this.transitHomeRequests) {
            if (request.transitHomeUserId.equals(transitHomeUser))
                request.updateStatus(NOTIFICATION_ACCEPTED);
            else
                request.updateStatus(NOTIFICATION_REJECTED);
        }
        this.transitHomeUser = transitHomeUser;
        this.lastModifiedDate = DateTime.now().toString(DATE_HOUR_FORMAT);
    }

    private void updatePublicationStatusToBlocked(String informer) {
        for (PublicationReport report : this.reports) {
            if (report.informer.equals(informer))
                report.updateStatus(REPORT_ACCEPTED);
            else
                report.updateStatus(REPORT_REJECTED);
        }
        this.publicationStatus = BLOCKED;
        this.hasBeenBlockedOnce = true;
        this.lastModifiedDate = DateTime.now().toString(DATE_HOUR_FORMAT);
    }

    private void updateReportToRejected(String informer) {
        for (PublicationReport report : this.reports) {
            if (report.informer.equals(informer)) {
                report.updateStatus(REPORT_REJECTED);
                break;
            }
        }
    }

    private void addNewAdoptionRequest(AdoptionRequest request) {
        Adoption adoptionRequest = new Adoption(request.adopterId,
                                                DateTime.now().toString(DATE_HOUR_FORMAT));
        if (this.adoptionRequests == null)
            this.adoptionRequests = new ArrayList<>();
        this.adoptionRequests.add(adoptionRequest);
    }

    private void addNewTransitHomeRequest(TransitHomeRequest request) {
        TransitHome transitHomeRequest = new TransitHome(request.transitHomeUser,
                                                         DateTime.now().toString(DATE_HOUR_FORMAT));
        if (this.transitHomeRequests == null)
            this.transitHomeRequests = new ArrayList<>();
        this.transitHomeRequests.add(transitHomeRequest);
    }

    private void addNewReport(ReportPublicationRequest request) {
        PublicationReport report = new PublicationReport(request.informer, request.reason, REPORT_PENDING,
                                                         DateTime.now().toString(DATE_HOUR_FORMAT));
        if (this.reports == null)
            this.reports = new ArrayList<>();
        this.reports.add(report);
    }

    private Boolean updateLastSeenRequests() {
        if (this.adoptionRequests == null)
            return false;
        for (Adoption adoptionRequest: this.adoptionRequests) {
            adoptionRequest.updateLastSeen(DateTime.now().toString(DATE_HOUR_FORMAT));
        }
        return true;
    }

    private void temporaryBlock() {
        this.publicationStatus = BLOCKED;
    }

    private void unblock() {
        this.publicationStatus = PUBLISHED;
    }

}
