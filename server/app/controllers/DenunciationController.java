package controllers;

import model.PublicationDenunciation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import services.DenunciationService;

import java.util.List;

@Controller
public class DenunciationController {

    @Autowired
    private DenunciationService service;

    public Result getPetPublicationDenunciations() {
        List<PublicationDenunciation> denunciations = service.getPetPublicationDenunciations();
        Logger.info("Number of denunciations: " + denunciations.size());
        return play.mvc.Controller.ok(Json.toJson(denunciations));
    }

}
