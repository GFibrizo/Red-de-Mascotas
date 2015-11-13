package controllers;

import model.PetAdoption;
import model.external.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import services.PetAdoptionService;

import java.util.List;

@Controller
public class PetAdoptionController {

    @Autowired
    private PetAdoptionService service;

    public Result publishPet() {
        Form<PublishForAdoptionPet> form = Form.form(PublishForAdoptionPet.class).bindFromRequest();
        PublishForAdoptionPet pet = form.get();
        if (service.publishPet(pet)) {
            Logger.info("Pet for adoption successfully published, pet: " + form.data());
            return play.mvc.Controller.ok();
        }
        Logger.error("Pet for adoption could not be published, pet: " + form.data());
        return play.mvc.Controller.badRequest();
    }

    public Result searchPets() {
        Form<SearchForAdoptionFilters> form = Form.form(SearchForAdoptionFilters.class).bindFromRequest();
        Logger.info("Filters for search recieved: " + form.data());
        SearchForAdoptionFilters filters = form.get();
        List<PetAdoption> pets = service.searchPets(filters);
        Logger.info("Number of pets for adoption found in search: " + pets.size());
        return play.mvc.Controller.ok(Json.toJson(pets));
    }

    public Result getLastPublished() {
        List<PetAdoption> pets = service.getLastPublished();
        Logger.info("Number of pets for adoption last published: " + pets.size());
        return play.mvc.Controller.ok(Json.toJson(pets));
    }

    public Result userHasAdoptedPet(String petId) {
        Form<AdoptionRequest> form = Form.form(AdoptionRequest.class).bindFromRequest();
        AdoptionRequest request = form.get();
        Boolean userHasAdoptedPet = service.userHasAdoptedPet(request);
        Logger.info("User " + request.adopterId + " has already adopted pet with id " + petId + ": " + userHasAdoptedPet);
        return play.mvc.Controller.ok(Json.toJson(userHasAdoptedPet));
    }

    public Result adoptPet(String petId) {
        Form<AdoptionRequest> form = Form.form(AdoptionRequest.class).bindFromRequest();
        AdoptionRequest request = form.get();
        service.adoptPet(request);
        Logger.info("Adoption request successfully added for pet with id " + petId);
        return play.mvc.Controller.ok();
    }

    public Result acceptAdoptionRequest(String petId) {
        Form<AdoptionRequest> form = Form.form(AdoptionRequest.class).bindFromRequest();
        AdoptionRequest request = form.get();
        service.acceptAdoptionRequest(request);
        Logger.info("Adoption request successfully accepted for pet with id " + petId);
        return play.mvc.Controller.ok();
    }

    public Result takePetInTransit(String petId) {
        Form<TransitHomeRequest> form = Form.form(TransitHomeRequest.class).bindFromRequest();
        TransitHomeRequest request = form.get();
        service.takePetInTransit(request);
        Logger.info("Take in transit request successfully added for pet with id " + petId);
        return play.mvc.Controller.ok();
    }

    public Result acceptTakeInTransitRequest(String petId) {
        Form<TransitHomeRequest> form = Form.form(TransitHomeRequest.class).bindFromRequest();
        TransitHomeRequest request = form.get();
        service.acceptTakeInTransitRequest(request);
        Logger.info("Take in transit request successfully accepted for pet with id " + petId);
        return play.mvc.Controller.ok();
    }

    public Result reportPublication(String petId) {
        Form<ReportPublicationRequest> form = Form.form(ReportPublicationRequest.class).bindFromRequest();
        ReportPublicationRequest request = form.get();
        service.reportPublication(request);
        Logger.info("Publication for pet with id " + petId + " successfully reported");
        return play.mvc.Controller.ok();
    }

    public Result acceptPublicationReport(String petId) {
        Form<AcceptPublicationReportRequest> form = Form.form(AcceptPublicationReportRequest.class).bindFromRequest();
        AcceptPublicationReportRequest request = form.get();
        service.acceptPublicationReport(request);
        Logger.info("Informer " + request.informer + "'s report of publication for pet with id " + petId + " successfully accepted");
        return play.mvc.Controller.ok();
    }

}
