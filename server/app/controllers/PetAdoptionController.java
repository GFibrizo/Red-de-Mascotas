package controllers;

import model.PetAdoption;
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

}
