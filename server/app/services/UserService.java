package services;

import model.MascotaAdopcion;
import model.Usuario;
import model.external.LogInUser;
import model.external.AccountRegistrationUser;
import model.external.FacebookRegistrationUser;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO: Pasar a Android
/*
import model.Contrasenia;
import org.apache.commons.codec.digest.DigestUtils;
import java.security.SecureRandom;
*/

@Service
public class UserService {

    // TODO: Pasar a Android
    /*
    private SecureRandom secureRandom = new SecureRandom();
    */

    public Usuario logIn(LogInUser logInUser) {
        Usuario usuario = Usuario.traerPorNombreDeUsuario(logInUser.userName);
        if (usuario != null && logInUser.encryptedPassword.equals(usuario.password.encryption)) {
            return usuario;
        } else return null;
    }

    public String traerSalt(String nombreDeUsuario) {
        Usuario usuario = Usuario.traerPorNombreDeUsuario(nombreDeUsuario);
        if (usuario != null) {
            return usuario.password.salt;
        } else return null;
    }

    public Usuario logInFacebook(String facebookId) {
        return Usuario.traerPorFacebookId(facebookId);
    }

    public Usuario registrarUsuarioCuenta(AccountRegistrationUser usuarioRegistro) {
        if (Usuario.existente(usuarioRegistro.userName, usuarioRegistro.email))
            return null;
        Usuario usuario = new Usuario(usuarioRegistro.userName,
                                      usuarioRegistro.name,
                                      usuarioRegistro.lastName,
                                      usuarioRegistro.email,
                                      usuarioRegistro.password,
                                      usuarioRegistro.phone,
                                      usuarioRegistro.address);
        Usuario.crear(usuario);
        return usuario;
    }

    public void registrarUsuarioFacebook(FacebookRegistrationUser usuarioRegistro) {
        Usuario usuario = new Usuario(usuarioRegistro.name,
                                      usuarioRegistro.lastName,
                                      usuarioRegistro.email,
                                      usuarioRegistro.facebookId,
                                      usuarioRegistro.phone,
                                      usuarioRegistro.address);
        Usuario.crear(usuario);
    }

    public List<MascotaAdopcion> traerMascotasEnAdopcion(String usuarioId) {
        return MascotaAdopcion.traerPorDuenioId(usuarioId);
    }

    // TODO: Pasar a Android
    /*
    private Contrasenia crearContrasenia(String password) {
        byte[] salt = new byte[20];
        secureRandom.nextBytes(salt);
        String saltStr = salt.toString();
        return new Contrasenia(encriptarContraseña(password, saltStr), saltStr);
    }
    private String encriptarContraseña(String password, String salt) {
        return DigestUtils.sha1Hex(salt + password);
    }

    private Boolean contraseniaValida(String password, Contrasenia contraseniaGuardada) {
        return contraseniaGuardada.encryption.equals(encriptarContraseña(password, contraseniaGuardada.salt));
    }
    */


}
