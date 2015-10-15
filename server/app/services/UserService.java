package services;

import model.Adoption;
import model.AdoptionNotification;
import model.PetAdoption;
import model.User;
import model.external.LogInUser;
import model.external.AccountRegistrationUser;
import model.external.FacebookRegistrationUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    public User logInWithAccount(LogInUser logInUser) {
        User user = User.getByUserName(logInUser.userName);
        if (user != null && logInUser.encryptedPassword.equals(user.password.encryption)) {
            return user;
        } else return null;
    }

    public String getUserSalt(String userName) {
        User user = User.getByUserName(userName);
        if (user != null) {
            return user.password.salt;
        } else return null;
    }

    public User logInWithFacebook(String facebookId) {
        return User.getByFacebookId(facebookId);
    }

    public User registerAccountUser(AccountRegistrationUser userRegister) {
        if (User.exists(userRegister.userName, userRegister.email))
            return null;
        User user = new User(userRegister.userName,
                             userRegister.name,
                             userRegister.lastName,
                             userRegister.email,
                             userRegister.password,
                             userRegister.phone,
                             userRegister.address);
        String id = User.create(user);
        user.setId(id);
        return user;
    }

    public User registerFacebookUser(FacebookRegistrationUser userRegister) {
        User user = new User(userRegister.name,
                             userRegister.lastName,
                             userRegister.email,
                             userRegister.facebookId,
                             userRegister.phone,
                             userRegister.address);
        String id = User.create(user);
        user.setId(id);
        return user;
    }

    public List<PetAdoption> getPetsInAdoption(String userId) {
        return PetAdoption.getByOwnerId(userId);
    }

    public List<AdoptionNotification> getAdoptionNotifications(String userId) {
        List<AdoptionNotification> adoptionNotifications = new ArrayList<>();
        List<PetAdoption> pets = PetAdoption.getByOwnerId(userId);
        for (PetAdoption pet : pets) {
            if (pet.adoptionRequests != null) {
                for (Adoption adoptionRequest : pet.adoptionRequests) {
                    User adopter = User.getById(adoptionRequest.adopterId);
                    String image;
                    if (pet.images != null) {
                        image = pet.images.get(0);
                    } else {
                        image = "";
                    }
                    AdoptionNotification adoptionNotification = new AdoptionNotification(adopter.email,
                                                                                         adoptionRequest.requestDate,
                                                                                         pet.name,
                                                                                         image);
                    adoptionNotifications.add(adoptionNotification);
                }
            }
        }
        Collections.reverse(adoptionNotifications);
        return adoptionNotifications;
    }

    public Boolean userHasPendingNotifications(String userId) {
        List<PetAdoption> pets = PetAdoption.getByOwnerId(userId);
        for (PetAdoption pet : pets) {
            if (pet.hasAdoptionRequestsNotSeen()) {
                return true;
            }
        }
        return false;
    }

    public void updateLastSeenNotifications(String userId) {
        List<PetAdoption> pets = PetAdoption.getByOwnerId(userId);
        for (PetAdoption pet : pets) {
            PetAdoption.updateLastSeenAdoptionRequests(pet.id);
        }
    }

}
