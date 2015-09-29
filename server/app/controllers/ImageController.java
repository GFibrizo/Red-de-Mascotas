package controllers;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class ImageController extends play.mvc.Controller {

    public Result uploadPetImage() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart image = body.getFile("image");
        if (image == null) {
            return play.mvc.Controller.badRequest();
        }
        String fileName = UUID.randomUUID().toString();
        File file = image.getFile();
        try {
            FileUtils.moveFile(file, new File("public/images/pets", fileName));
            return play.mvc.Controller.ok(fileName);
        } catch (IOException exception) {
            return play.mvc.Controller.internalServerError(exception.getMessage());
        }
    }

    public Result getPetImage(String imageId) {
        File file = new File("public/images/pets", imageId);
        return play.mvc.Controller.ok(file);
    }

}
