package controllers;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class ImageController extends play.mvc.Controller {

    public Result uploadPetImage() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart image = body.getFile("file");
        if (image == null) {
            Logger.error("File must not be empty");
            return play.mvc.Controller.badRequest();
        }
        String fileName = UUID.randomUUID().toString();
        File file = image.getFile();
        try {
            FileUtils.moveFile(file, new File("public/images/pets", fileName + ".jpg"));
            Logger.info("Image successfully uploaded, key: " + fileName);
            return play.mvc.Controller.ok(fileName);
        } catch (IOException exception) {
            Logger.error("Image could not be uploaded", exception);
            return play.mvc.Controller.internalServerError(exception.getMessage());
        }
    }

    public Result getPetImage(String imageId) {
        File file = new File("public/images/pets", imageId + ".jpg");
        return play.mvc.Controller.ok(file);
    }

}
