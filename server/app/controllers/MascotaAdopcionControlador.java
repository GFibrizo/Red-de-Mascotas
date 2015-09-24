package controllers;

import model.MascotaAdopcion;
import model.externo.FiltrosBusquedaAdopcion;
import model.externo.MascotaAdopcionPublicacion;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.libs.Json;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import services.MascotaAdopcionServicio;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class MascotaAdopcionControlador extends play.mvc.Controller {

    @Autowired
    private MascotaAdopcionServicio servicio;

    public Result publicarMascota() {
        Form<MascotaAdopcionPublicacion> form = Form.form(MascotaAdopcionPublicacion.class).bindFromRequest();
        MascotaAdopcionPublicacion mascota = form.get();
        servicio.publicarMascota(mascota);
        return play.mvc.Controller.ok();
    }

    public Result subirImagen() {
        MultipartFormData body = request().body().asMultipartFormData();
        FilePart imagen = body.getFile("imagen");
        if (imagen == null) {
            return play.mvc.Controller.badRequest();
        }
        String fileName = UUID.randomUUID().toString();
        File file = imagen.getFile();
        try {
            FileUtils.moveFile(file, new File("public/images/mascotas/adopcion", fileName));
            return play.mvc.Controller.ok(fileName);
        } catch (IOException exception) {
            return play.mvc.Controller.internalServerError(exception.getMessage());
        }
    }

    public Result buscarMascotas() {
        Form<FiltrosBusquedaAdopcion> form = Form.form(FiltrosBusquedaAdopcion.class).bindFromRequest();
        FiltrosBusquedaAdopcion filtros = form.get();
        List<MascotaAdopcion> mascotas = servicio.buscarMascotas(filtros);
        return play.mvc.Controller.ok(Json.toJson(mascotas));
    }

}
