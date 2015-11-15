package controllers;

import model.*;
import model.external.LogInUser;
import model.external.AccountRegistrationUser;
import model.external.FacebookRegistrationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Http;
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
        Http.Response response = play.mvc.Controller.response();
        response.setHeader(play.mvc.Controller.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        Form<LogInUser> form = Form.form(LogInUser.class).bindFromRequest();
        LogInUser logInUser = form.get();
        User user = service.logInWithAccount(logInUser);
        if (user == null) {
            Logger.error("User " + logInUser.userName + " not found");
            return play.mvc.Controller.internalServerError();
        }
        if (!logInUser.encryptedPassword.equals(user.password.encryption)) {
            Logger.error("User " + logInUser.userName + "'s password is invalid");
            return play.mvc.Controller.badRequest();
        }
        Logger.info("User " + logInUser.userName + " successfully logged in");
        return play.mvc.Controller.ok(Json.toJson(user));
    }

    // Devuelve el salt del usuario, si existe el usuario, sino devuelve BadRequest.
    public Result getUserSalt(String userName) {
        Http.Response response = play.mvc.Controller.response();
        response.setHeader(play.mvc.Controller.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        String salt = service.getUserSalt(userName);
        if (salt == null) {
            Logger.error("Salt for user " + userName + " could not be retrieved");
            return play.mvc.Controller.badRequest();
        }
        Logger.info("Salt for user " + userName + " successfully retrieved");
        return play.mvc.Controller.ok(salt);
    }

    // Devuelve el usuario segun el id de Facebook. Si no lo encuentra devuelve BadRequest.
    public Result logInWithFacebook(String facebookId) {
        User user = service.logInWithFacebook(facebookId);
        if (user == null) {
            Logger.error("User with facebook id " + facebookId + " not found");
            return play.mvc.Controller.notFound();
        }
        Logger.info("User with facebook id " + facebookId + " successfully logged in");
        return play.mvc.Controller.ok(Json.toJson(user));
    }

    // Guarda el usuario en la base si no existe el name de usuario ni el mail.
    // Devuelve BadRequest en caso contrario.
    public Result registerAccountUser() {
        Form<AccountRegistrationUser> form = Form.form(AccountRegistrationUser.class).bindFromRequest();
        AccountRegistrationUser user = form.get();
        User registeredUser = service.registerAccountUser(user);
        if (registeredUser == null) {
            Logger.error("User with account could not be registered, user: ", form.data());
            return play.mvc.Controller.badRequest();
        }
        Logger.info("User with account successfully registered, user: ", form.data());
        return play.mvc.Controller.ok(Json.toJson(registeredUser));
    }

    public Result registerFacebookUser() {
        Form<FacebookRegistrationUser> form = Form.form(FacebookRegistrationUser.class).bindFromRequest();
        FacebookRegistrationUser user = form.get();
        User registeredUser = service.registerFacebookUser(user);
        if (registeredUser == null) {
            Logger.error("Facebook user could not be registered, user: ", form.data());
            return play.mvc.Controller.badRequest();
        }
        Logger.info("Facebook user successfully registered, user: ", form.data());
        return play.mvc.Controller.ok(Json.toJson(registeredUser));
    }

    public Result getPublishedPets(String userId) {
        List<MyPet> pets = service.getPetsByUserId(userId);
        Logger.info("Number of user's published pets: " + pets.size());
        return play.mvc.Controller.ok(Json.toJson(pets));
    }

    public Result getNotifications(String userId) {
        List<Notification> notifications = service.getNotifications(userId);
        Logger.info("Number of user's notifications: " + notifications.size());
        return play.mvc.Controller.ok(Json.toJson(notifications));
    }

    public Result userHasPendingNotifications(String userId) {
        Boolean userHasPendingNotifications = service.userHasPendingNotifications(userId);
        Logger.info("User has pending notifications: " + userHasPendingNotifications);
        return play.mvc.Controller.ok(Json.toJson(userHasPendingNotifications));
    }

    public Result updateLastSeenNotifications(String userId) {
        service.updateLastSeenNotifications(userId);
        Logger.info("Updated user's last seen notifications");
        return play.mvc.Controller.ok();
    }

    public Result getPetsMatches(String userId) {
        List<MatchingPet> matchingPets = service.getMatchingPets(userId);
        Logger.info("Number of matching pets: " + matchingPets.size());
        return play.mvc.Controller.ok(Json.toJson(matchingPets));
    }

    public Result getPetsMatchingSavedSearches(String userId) {
        List<PetAdoption> pets = service.getPetsMatchingSavedSearches(userId);
        Logger.info("Number of pets matching saved searches: " + pets.size());
        return play.mvc.Controller.ok(Json.toJson(pets));
    }

    public Result blockUser(String userId) {
        service.blockUser(userId);
        Logger.info("User with id " + userId + " successfully blocked");
        Http.Response response = play.mvc.Controller.response();
        response.setHeader(play.mvc.Controller.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        return play.mvc.Controller.ok();
    }

    public Result unblockUser(String userId) {
        service.unblockUser(userId);
        Logger.info("User with id " + userId + " successfully unblocked");
        Http.Response response = play.mvc.Controller.response();
        response.setHeader(play.mvc.Controller.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        return play.mvc.Controller.ok();
    }

}
