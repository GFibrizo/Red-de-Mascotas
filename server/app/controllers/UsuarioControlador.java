package controllers;

import model.MascotaAdopcion;
import model.Usuario;
import model.externo.UsuarioLogIn;
import model.externo.UsuarioRegistroCuenta;
import model.externo.UsuarioRegistroFacebook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import services.UsuarioServicio;

import java.util.List;

@Controller
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio servicio;

    public Result logInCuenta() {
        Form<UsuarioLogIn> form = Form.form(UsuarioLogIn.class).bindFromRequest();
        UsuarioLogIn usuarioLogIn = form.get();
        Usuario usuario = servicio.logIn(usuarioLogIn);
        if (usuario == null)
            return play.mvc.Controller.badRequest();
        return play.mvc.Controller.ok(Json.toJson(usuario));
    }

    public Result logInFacebook(String facebookId) {
        Usuario usuario = servicio.logInFacebook(facebookId);
        if (usuario == null)
            return play.mvc.Controller.badRequest();
        return play.mvc.Controller.ok(Json.toJson(usuario));
    }

    public Result registrarUsuarioCuenta() {
        Form<UsuarioRegistroCuenta> form = Form.form(UsuarioRegistroCuenta.class).bindFromRequest();
        UsuarioRegistroCuenta usuario = form.get();
        if (servicio.registrarUsuarioCuenta(usuario) == null)
            return play.mvc.Controller.badRequest();
        else return play.mvc.Controller.ok();
    }

    public Result registrarUsuarioFacebook() {
        Form<UsuarioRegistroFacebook> form = Form.form(UsuarioRegistroFacebook.class).bindFromRequest();
        UsuarioRegistroFacebook usuario = form.get();
        servicio.registrarUsuarioFacebook(usuario);
        return play.mvc.Controller.ok();
    }

    public Result traerMascotasEnAdopcion(String usuarioId) {
        List<MascotaAdopcion> mascotas = servicio.traerMascotasEnAdopcion(usuarioId);
        return play.mvc.Controller.ok(Json.toJson(mascotas));
    }

}
