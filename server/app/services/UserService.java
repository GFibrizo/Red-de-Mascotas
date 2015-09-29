package services;

import model.PetAdoption;
import model.User;
import model.external.LogInUser;
import model.external.AccountRegistrationUser;
import model.external.FacebookRegistrationUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    public User logInWithAccount(LogInUser logInUser) {
        User user = User.getByUserName(logInUser.userName);
        if (user != null && logInUser.encryptedPassword.equals(user.password.encryption)) {
            return user;
        } else return null;
    }

    public String getUserSalt(String nombreDeUsuario) {
        User user = User.getByUserName(nombreDeUsuario);
        if (user != null) {
            return user.password.salt;
        } else return null;
    }

    public User logInWithFacebook(String facebookId) {
        return User.getByFacebookId(facebookId);
    }

    public User registerAccountUser(AccountRegistrationUser usuarioRegistro) {
        if (User.exists(usuarioRegistro.userName, usuarioRegistro.email))
            return null;
        User user = new User(usuarioRegistro.userName,
                                      usuarioRegistro.name,
                                      usuarioRegistro.lastName,
                                      usuarioRegistro.email,
                                      usuarioRegistro.password,
                                      usuarioRegistro.phone,
                                      usuarioRegistro.address);
        User.create(user);
        return user;
    }

    public void registerFacebookUser(FacebookRegistrationUser usuarioRegistro) {
        User user = new User(usuarioRegistro.name,
                                      usuarioRegistro.lastName,
                                      usuarioRegistro.email,
                                      usuarioRegistro.facebookId,
                                      usuarioRegistro.phone,
                                      usuarioRegistro.address);
        User.create(user);
    }

    public List<PetAdoption> getPetsInAdoption(String usuarioId) {
        return PetAdoption.getByOwnerId(usuarioId);
    }

}
