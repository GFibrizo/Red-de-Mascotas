package model;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import model.external.AcceptPublicationReportRequest;
import model.external.ReportPublicationRequest;
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

import static utils.Constants.*;
import static utils.Constants.REPORT_REJECTED;

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

    public List<PublicationReport> reports;

    public Boolean hasBeenBlockedOnce;


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
        this.hasBeenBlockedOnce = false;
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

    public static List<FoundPet> getPublishedAndBlockedByFinderId(String finderId) {
        ArrayList<String> status = new ArrayList<>();
        status.add(PUBLISHED);
        status.add(BLOCKED);
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        query.add("finderId", finderId);
        query.push("publicationStatus").add("$in", status).pop();
        return FoundPet.collection.find(query.get()).toArray();
    }

    public static List<FoundPet> getPetsWithReports() {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        query.add("publicationStatus", PUBLISHED);
        query.push("reports").add("$ne", null).pop();
        return FoundPet.collection.find(query.get()).toArray();
    }

    public static List<FoundPet> getMatches(String type, String gender, String lastSeenDate, GeoLocation location) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMAT);
        LocalDate date = dateTimeFormatter.parseLocalDate(lastSeenDate);
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        query.add("type", type);
        query.add("gender", gender);
        query.push("foundDate").add("$gte", date.minusDays(3).toString(DATE_FORMAT))
                               .add("$lte", date.plusDays(30).toString(DATE_FORMAT)).pop();
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

    public static int countPetsPublished(String fromDate, String toDate, String petType) {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        DateTimeFormatter argDateTimeFormatter = DateTimeFormat.forPattern(ARG_DATE_FORMAT);
        String from = argDateTimeFormatter.parseLocalDate(fromDate).toString(DATE_FORMAT);
        String to = argDateTimeFormatter.parseLocalDate(toDate).plusDays(1).toString(DATE_FORMAT);
        if (petType != null) query.add("type", petType);
        query.push("publicationDate").add("$gte", from).add("$lt", to).pop();
        return (int) FoundPet.collection.count(query.get());
    }

    public static FoundPet addReport(ReportPublicationRequest request) {
        FoundPet pet = getById(request.petId);
        pet.addNewReport(request);
        FoundPet.collection.updateById(request.petId, pet);
        return pet;
    }

    public static FoundPet acceptReport(AcceptPublicationReportRequest request) {
        FoundPet pet = getById(request.petId);
        pet.updatePublicationStatusToBlocked(request.informer);
        FoundPet.collection.updateById(request.petId, pet);
        return pet;
    }

    public static FoundPet rejectReport(AcceptPublicationReportRequest request) {
        FoundPet pet = getById(request.petId);
        pet.updateReportToRejected(request.informer);
        FoundPet.collection.updateById(request.petId, pet);
        return pet;
    }

    public static void blockAllPetsFromUser(String userId) {
        List<FoundPet> pets = FoundPet.collection.find(new BasicDBObject("finderId", userId)).toArray();
        for (FoundPet pet : pets) {
            if (pet.publicationStatus.equals(PUBLISHED)) {
                pet.temporaryBlock();
                FoundPet.collection.updateById(pet.id, pet);
            }
        }
    }

    public static void unblockPetsFromUser(String userId) {
        List<FoundPet> pets = FoundPet.collection.find(new BasicDBObject("finderId", userId)).toArray();
        for (FoundPet pet : pets) {
            if (pet.publicationStatus.equals(BLOCKED) && !pet.hasBeenBlockedOnce) {
                pet.unblock();
                FoundPet.collection.updateById(pet.id, pet);
            }
        }
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

    private void addNewReport(ReportPublicationRequest request) {
        PublicationReport report = new PublicationReport(request.informer, request.reason, REPORT_PENDING,
                DateTime.now().toString(DATE_HOUR_FORMAT));
        if (this.reports == null)
            this.reports = new ArrayList<>();
        this.reports.add(report);
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
    }

    private void updateReportToRejected(String informer) {
        for (PublicationReport report : this.reports) {
            if (report.informer.equals(informer)) {
                report.updateStatus(REPORT_REJECTED);
                break;
            }
        }
    }

    private void temporaryBlock() {
        this.publicationStatus = BLOCKED;
    }

    private void unblock() {
        this.publicationStatus = PUBLISHED;
    }

}
