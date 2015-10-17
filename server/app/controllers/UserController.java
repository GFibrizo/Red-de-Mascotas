package controllers;

import model.AdoptionNotification;
import model.MyPet;
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
    private UserService service;

    // Devuelve el usuario en caso de ser la misma password encriptada, sino devuelve BadRequest.
    // Si no encuentra el usuario devuelve BadRequest.
    public Result logInWithAccount() {
        Form<LogInUser> form = Form.form(LogInUser.class).bindFromRequest();
        LogInUser logInUser = form.get();
        User user = service.logInWithAccount(logInUser);
        if (user == null)
            return play.mvc.Controller.badRequest();
        return play.mvc.Controller.ok(Json.toJson(user));
    }

    // Devuelve el salt del usuario, si existe el usuario, sino devuelve BadRequest.
    public Result getUserSalt(String userName) {
        String salt = service.getUserSalt(userName);
        if (salt == null)
            return play.mvc.Controller.badRequest();
        return play.mvc.Controller.ok(salt);
    }

    // Devuelve el usuario segun el id de Facebook. Si no lo encuentra devuelve BadRequest.
    public Result logInWithFacebook(String facebookId) {
        User user = service.logInWithFacebook(facebookId);
        if (user == null)
//            return play.mvc.Controller.badRequest(Json.newObject());
            return play.mvc.Controller.notFound();
        return play.mvc.Controller.ok(Json.toJson(user));
    }

    // Guarda el usuario en la base si no existe el name de usuario ni el mail.
    // Devuelve BadRequest en caso contrario.
    public Result registerAccountUser() {
        Form<AccountRegistrationUser> form = Form.form(AccountRegistrationUser.class).bindFromRequest();
        AccountRegistrationUser user = form.get();
        User registeredUser = service.registerAccountUser(user);
        if (registeredUser == null)
            return play.mvc.Controller.badRequest();
        else return play.mvc.Controller.ok(Json.toJson(registeredUser));
    }

    public Result registerFacebookUser() {
        Form<FacebookRegistrationUser> form = Form.form(FacebookRegistrationUser.class).bindFromRequest();
        FacebookRegistrationUser user = form.get();
        User registeredUser = service.registerFacebookUser(user);
        return play.mvc.Controller.ok(Json.toJson(registeredUser));
    }

    public Result getPublishedPets(String userId) {
        List<MyPet> pets = service.getPetsByUserId(userId);
        return play.mvc.Controller.ok(Json.toJson(pets));
    }

    public Result getAdoptionNotifications(String userId) {
        List<AdoptionNotification> adoptionNotifications = service.getAdoptionNotifications(userId);
        return play.mvc.Controller.ok(Json.toJson(adoptionNotifications));
    }

    public Result userHasPendingNotifications(String userId) {
        Boolean userHasPendingNotifications = service.userHasPendingNotifications(userId);
        return play.mvc.Controller.ok(Json.toJson(userHasPendingNotifications));
    }

    public Result updateLastSeenNotifications(String userId) {
        service.updateLastSeenNotifications(userId);
        return play.mvc.Controller.ok();
    }

}
