package services;

import model.Contrasenia;
import model.MascotaAdopcion;
import model.Usuario;
import model.externo.UsuarioLogIn;
import model.externo.UsuarioRegistroCuenta;
import model.externo.UsuarioRegistroFacebook;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class UsuarioServicio {

    private SecureRandom secureRandom = new SecureRandom();

    public Usuario logIn(UsuarioLogIn usuarioLogIn) {
        Usuario usuario = Usuario.traerPorNombreDeUsuario(usuarioLogIn.nombreDeUsuario);
        if (usuario != null && contraseniaValida(usuarioLogIn.contrasenia, usuario.contrasenia)) {
            return usuario;
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
                                      crearContrasenia(usuarioRegistro.contrasenia),
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

}
