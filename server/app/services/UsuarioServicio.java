package services;

import model.MascotaAdopcion;
import model.Usuario;
import model.externo.UsuarioLogIn;
import model.externo.UsuarioRegistroCuenta;
import model.externo.UsuarioRegistroFacebook;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO: Pasar a Android
/*
import model.Contrasenia;
import org.apache.commons.codec.digest.DigestUtils;
import java.security.SecureRandom;
*/

@Service
public class UsuarioServicio {

    // TODO: Pasar a Android
    /*
    private SecureRandom secureRandom = new SecureRandom();
    */

    public Usuario logIn(UsuarioLogIn usuarioLogIn) {
        Usuario usuario = Usuario.traerPorNombreDeUsuario(usuarioLogIn.nombreDeUsuario);
        if (usuario != null && usuarioLogIn.contraseniaEncriptada.equals(usuario.contrasenia.encriptacion)) {
            return usuario;
        } else return null;
    }

    public String traerSalt(String nombreDeUsuario) {
        Usuario usuario = Usuario.traerPorNombreDeUsuario(nombreDeUsuario);
        if (usuario != null) {
            return usuario.contrasenia.salt;
        } else return null;
    }

    public Usuario logInFacebook(String facebookId) {
        return Usuario.traerPorFacebookId(facebookId);
    }

    public Usuario registrarUsuarioCuenta(UsuarioRegistroCuenta usuarioRegistro) {
        if (Usuario.existente(usuarioRegistro.nombreDeUsuario, usuarioRegistro.email))
            return null;
        Usuario usuario = new Usuario(usuarioRegistro.nombreDeUsuario,
                                      usuarioRegistro.nombre,
                                      usuarioRegistro.apellido,
                                      usuarioRegistro.email,
                                      usuarioRegistro.contrasenia,
                                      usuarioRegistro.telefono,
                                      usuarioRegistro.domicilio);
        Usuario.crear(usuario);
        return usuario;
    }

    public void registrarUsuarioFacebook(UsuarioRegistroFacebook usuarioRegistro) {
        Usuario usuario = new Usuario(usuarioRegistro.nombre,
                                      usuarioRegistro.apellido,
                                      usuarioRegistro.email,
                                      usuarioRegistro.facebookId,
                                      usuarioRegistro.telefono,
                                      usuarioRegistro.domicilio);
        Usuario.crear(usuario);
    }

    public List<MascotaAdopcion> traerMascotasEnAdopcion(String usuarioId) {
        return MascotaAdopcion.traerPorDuenioId(usuarioId);
    }

    // TODO: Pasar a Android
    /*
    private Contrasenia crearContrasenia(String contrasenia) {
        byte[] salt = new byte[20];
        secureRandom.nextBytes(salt);
        String saltStr = salt.toString();
        return new Contrasenia(encriptarContraseña(contrasenia, saltStr), saltStr);
    }
    private String encriptarContraseña(String contrasenia, String salt) {
        return DigestUtils.sha1Hex(salt + contrasenia);
    }

    private Boolean contraseniaValida(String contrasenia, Contrasenia contraseniaGuardada) {
        return contraseniaGuardada.encriptacion.equals(encriptarContraseña(contrasenia, contraseniaGuardada.salt));
    }
    */


}
