package controllers;

import model.MascotaAdopcion;
import model.Usuario;
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
    public Result logInCuenta() {
        Form<LogInUser> form = Form.form(LogInUser.class).bindFromRequest();
        LogInUser logInUser = form.get();
        Usuario usuario = servicio.logInWithAccount(logInUser);
        if (usuario == null)
            return play.mvc.Controller.badRequest();
        return play.mvc.Controller.ok(Json.toJson(usuario));
    }

    // Devuelve el salt del usuario, si existe el usuario, sino devuelve BadRequest.
    public Result traerSaltDeUsuario(String nombreDeUsuario) {
        String salt = servicio.getSalt(nombreDeUsuario);
        if (salt == null)
            return play.mvc.Controller.badRequest();
        return play.mvc.Controller.ok(salt);
    }

    // Devuelve el usuario segun el id de Facebook. Si no lo encuentra devuelve BadRequest.
    public Result logInFacebook(String facebookId) {
        Usuario usuario = servicio.logInWithFacebook(facebookId);
        if (usuario == null)
            return play.mvc.Controller.badRequest();
        return play.mvc.Controller.ok(Json.toJson(usuario));
    }

    // Guarda el usuario en la base si no existe el name de usuario ni el mail.
    // Devuelve BadRequest en caso contrario.
    public Result registrarUsuarioCuenta() {
        Form<AccountRegistrationUser> form = Form.form(AccountRegistrationUser.class).bindFromRequest();
        AccountRegistrationUser usuario = form.get();
        if (servicio.registerAccountUser(usuario) == null)
            return play.mvc.Controller.badRequest();
        else return play.mvc.Controller.ok();
    }

    public Result registrarUsuarioFacebook() {
        Form<FacebookRegistrationUser> form = Form.form(FacebookRegistrationUser.class).bindFromRequest();
        FacebookRegistrationUser usuario = form.get();
        servicio.registerFacebookUser(usuario);
        return play.mvc.Controller.ok();
    }

    public Result traerMascotasEnAdopcion(String usuarioId) {
        List<MascotaAdopcion> mascotas = servicio.getPetsInAdoption(usuarioId);
        return play.mvc.Controller.ok(Json.toJson(mascotas));
    }

}
