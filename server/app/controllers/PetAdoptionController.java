package controllers;

import model.PetAdoption;
import model.external.AdoptionRequest;
import model.external.SearchForAdoptionFilters;
import model.external.PublishForAdoptionPet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
        if (service.publishPet(pet))
            return play.mvc.Controller.ok();
        return play.mvc.Controller.badRequest();
    }

    public Result searchPets() {
        Form<SearchForAdoptionFilters> form = Form.form(SearchForAdoptionFilters.class).bindFromRequest();
        SearchForAdoptionFilters filters = form.get();
        List<PetAdoption> pets = service.searchPets(filters);
        return play.mvc.Controller.ok(Json.toJson(pets));
    }

    public Result getLastPublished() {
        List<PetAdoption> pets = service.getLastPublished();
        return play.mvc.Controller.ok(Json.toJson(pets));
    }

    public Result userHasAdoptedPet(String petId) {
        Form<AdoptionRequest> form = Form.form(AdoptionRequest.class).bindFromRequest();
        AdoptionRequest request = form.get();
        Boolean userHasAdoptedPet = service.userHasAdoptedPet(request);
        return play.mvc.Controller.ok(Json.toJson(userHasAdoptedPet));
    }

    public Result adoptPet(String petId) {
        Form<AdoptionRequest> form = Form.form(AdoptionRequest.class).bindFromRequest();
        AdoptionRequest request = form.get();
        service.adoptPet(request);
        return play.mvc.Controller.ok();
    }

}
