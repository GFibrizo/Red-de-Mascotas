package controllers;

import model.external.PublishLostPet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.mvc.Result;
import services.LostPetService;

@Controller
public class LostPetController {

    @Autowired
    private LostPetService service;

    public Result publishPet() {
        Form<PublishLostPet> form = Form.form(PublishLostPet.class).bindFromRequest();
        PublishLostPet pet = form.get();
        if (service.publishPet(pet))
            return play.mvc.Controller.ok();
        return play.mvc.Controller.badRequest();
    }

}
