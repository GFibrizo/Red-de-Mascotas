package controllers;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class ImagenControlador extends play.mvc.Controller {

    public Result subirImagenMascota() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart imagen = body.getFile("imagen");
        if (imagen == null) {
            return play.mvc.Controller.badRequest();
        }
        String fileName = UUID.randomUUID().toString();
        File file = imagen.getFile();
        try {
            FileUtils.moveFile(file, new File("public/images/mascotas", fileName));
            return play.mvc.Controller.ok(fileName);
        } catch (IOException exception) {
            return play.mvc.Controller.internalServerError(exception.getMessage());
        }
    }

    public Result traerImagenMascota(String imagenId) {
        File file = new File("public/images/mascotas", imagenId);
        return play.mvc.Controller.ok(file);
    }

}
