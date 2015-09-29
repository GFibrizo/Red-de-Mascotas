package controllers;

import model.PetAdoption;
import model.User;
import model.external.LogInUser;
import model.external.AccountRegistrationUser;
import model.external.FacebookRegistrationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import services.UserService;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService servicio;

    // Devuelve el usuario en caso de ser la misma password encriptada, sino devuelve BadRequest.
    // Si no encuentra el usuario devuelve BadRequest.
    public Result logInWithAccount() {
        Form<LogInUser> form = Form.form(LogInUser.class).bindFromRequest();
        LogInUser logInUser = form.get();
        User user = servicio.logInWithAccount(logInUser);
        if (user == null)
            return play.mvc.Controller.badRequest();
        return play.mvc.Controller.ok(Json.toJson(user));
    }

    // Devuelve el salt del usuario, si existe el usuario, sino devuelve BadRequest.
    public Result getUserSalt(String userName) {
        String salt = servicio.getUserSalt(userName);
        if (salt == null)
            return play.mvc.Controller.badRequest();
        return play.mvc.Controller.ok(salt);
    }

    // Devuelve el usuario segun el id de Facebook. Si no lo encuentra devuelve BadRequest.
    public Result logInWithFacebook(String facebookId) {
        User user = servicio.logInWithFacebook(facebookId);
        if (user == null)
            return play.mvc.Controller.badRequest();
        return play.mvc.Controller.ok(Json.toJson(user));
    }

    // Guarda el usuario en la base si no existe el name de usuario ni el mail.
    // Devuelve BadRequest en caso contrario.
    public Result registerAccountUser() {
        Form<AccountRegistrationUser> form = Form.form(AccountRegistrationUser.class).bindFromRequest();
        AccountRegistrationUser user = form.get();
        if (servicio.registerAccountUser(user) == null)
            return play.mvc.Controller.badRequest();
        else return play.mvc.Controller.ok();
    }

    public Result registerFacebookUser() {
        Form<FacebookRegistrationUser> form = Form.form(FacebookRegistrationUser.class).bindFromRequest();
        FacebookRegistrationUser user = form.get();
        servicio.registerFacebookUser(user);
        return play.mvc.Controller.ok();
    }

    public Result getPetsInAdoption(String userId) {
        List<PetAdoption> pets = servicio.getPetsInAdoption(userId);
        return play.mvc.Controller.ok(Json.toJson(pets));
    }

}
