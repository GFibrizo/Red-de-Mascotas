package controllers;

import model.external.PublishFoundPet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.mvc.Result;
import services.FoundPetService;

@Controller
public class FoundPetController {

    @Autowired
    private FoundPetService service;

    public Result publishPet() {
        Form<PublishFoundPet> form = Form.form(PublishFoundPet.class).bindFromRequest();
        PublishFoundPet pet = form.get();
        if (service.publishPet(pet))
            return play.mvc.Controller.ok();
        return play.mvc.Controller.badRequest();
    }

}
