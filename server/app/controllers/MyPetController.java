package controllers;

import model.external.UnpublishPet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.mvc.Result;
import services.MyPetService;

@Controller
public class MyPetController {

    @Autowired
    private MyPetService service;

    public Result unpublishPet(String petId) {
        Form<UnpublishPet> form = Form.form(UnpublishPet.class).bindFromRequest();
        UnpublishPet pet = form.get();
        service.unpublishPet(pet);
        return play.mvc.Controller.ok();
    }

}
