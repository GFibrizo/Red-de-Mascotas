package controllers;

import model.MascotaAdopcion;
import model.externo.FiltrosBusquedaAdopcion;
import model.externo.MascotaAdopcionPublicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import services.MascotaAdopcionServicio;

import java.util.List;

@Controller
public class MascotaAdopcionControlador {

    @Autowired
    private MascotaAdopcionServicio servicio;

    public Result publicarMascota() {
        Form<MascotaAdopcionPublicacion> form = Form.form(MascotaAdopcionPublicacion.class).bindFromRequest();
        MascotaAdopcionPublicacion mascota = form.get();
        if (servicio.publicarMascota(mascota))
            return play.mvc.Controller.ok();
        return play.mvc.Controller.badRequest();
    }

    public Result buscarMascotas() {
        Form<FiltrosBusquedaAdopcion> form = Form.form(FiltrosBusquedaAdopcion.class).bindFromRequest();
        FiltrosBusquedaAdopcion filtros = form.get();
        List<MascotaAdopcion> mascotas = servicio.buscarMascotas(filtros);
        return play.mvc.Controller.ok(Json.toJson(mascotas));
    }

    public Result traerUltimasPublicaciones() {
        List<MascotaAdopcion> mascotas = servicio.traerUltimasPublicaciones();
        return play.mvc.Controller.ok(Json.toJson(mascotas));
    }

}
