package controllers;

import model.MascotaAdopcion;
import model.external.SearchForAdoptionFilters;
import model.external.PublishForAdoptionPet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import services.PetForAdoptionService;

import java.util.List;

@Controller
public class PetAdoptionController {

    @Autowired
    private PetForAdoptionService servicio;

    public Result publicarMascota() {
        Form<PublishForAdoptionPet> form = Form.form(PublishForAdoptionPet.class).bindFromRequest();
        PublishForAdoptionPet mascota = form.get();
        if (servicio.publishPet(mascota))
            return play.mvc.Controller.ok();
        return play.mvc.Controller.badRequest();
    }

    public Result buscarMascotas() {
        Form<SearchForAdoptionFilters> form = Form.form(SearchForAdoptionFilters.class).bindFromRequest();
        SearchForAdoptionFilters filtros = form.get();
        List<MascotaAdopcion> mascotas = servicio.searchPets(filtros);
        return play.mvc.Controller.ok(Json.toJson(mascotas));
    }

    public Result traerUltimasPublicaciones() {
        List<MascotaAdopcion> mascotas = servicio.getLastPublished();
        return play.mvc.Controller.ok(Json.toJson(mascotas));
    }

}
