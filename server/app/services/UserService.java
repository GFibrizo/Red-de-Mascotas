package services;

import model.*;
import model.external.LogInUser;
import model.external.AccountRegistrationUser;
import model.external.FacebookRegistrationUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static utils.Constants.FOR_ADOPTION;
import static utils.Constants.FOUND;
import static utils.Constants.LOST;

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
        User user = new User(userRegister.notificationId, userRegister.userName, userRegister.name,
                             userRegister.lastName, userRegister.email, userRegister.password,
                             userRegister.phone, userRegister.address);
        String id = User.create(user);
        user.setId(id);
        return user;
    }

    public User registerFacebookUser(FacebookRegistrationUser userRegister) {
        User user = new User(userRegister.notificationId, userRegister.name, userRegister.lastName,
                             userRegister.email, userRegister.facebookId, userRegister.phone,
                             userRegister.address);
        String id = User.create(user);
        user.setId(id);
        return user;
    }

    public List<MyPet> getPetsByUserId(String userId) {
        List<PetAdoption> petsForAdoption = PetAdoption.getPublishedByOwnerId(userId);
        List<LostPet> lostPets = LostPet.getPublishedByOwnerId(userId);
        List<FoundPet> foundPets = FoundPet.getPublishedByFinderId(userId);

        List<MyPet> myPets = new ArrayList<>();
        addPetsForAdoptionToMyPets(petsForAdoption, myPets);
        addLostPetsToMyPets(lostPets, myPets);
        addFoundPetsToMyPets(foundPets, myPets);

        Collections.reverse(myPets);
        return myPets;
    }

    public List<AdoptionNotification> getAdoptionNotifications(String userId) {
        List<AdoptionNotification> adoptionNotifications = new ArrayList<>();
        List<PetAdoption> pets = PetAdoption.getPublishedByOwnerId(userId);
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
        List<PetAdoption> pets = PetAdoption.getPublishedByOwnerId(userId);
        for (PetAdoption pet : pets) {
            if (pet.hasAdoptionRequestsNotSeen()) {
                return true;
            }
        }
        return false;
    }

    public void updateLastSeenNotifications(String userId) {
        List<PetAdoption> pets = PetAdoption.getPublishedByOwnerId(userId);
        for (PetAdoption pet : pets) {
            PetAdoption.updateLastSeenAdoptionRequests(pet.id);
        }
    }

    public List<MatchingPet> getMatchingPets(String userId) {
        List<MatchingPet> matchingPets = new ArrayList<>();
        List<LostPet> userLostPets = LostPet.getPublishedByOwnerId(userId);
        List<FoundPet> userFoundPets = FoundPet.getPublishedByFinderId(userId);
        addFoundPetsToMatches(userLostPets, matchingPets);
        addLostPetsToMatches(userFoundPets, matchingPets);
        Collections.reverse(matchingPets);
        return matchingPets;
    }

    private void addPetsForAdoptionToMyPets(List<PetAdoption> petsForAdoption, List<MyPet> myPets) {
        for (PetAdoption pet : petsForAdoption) {
            myPets.add(new MyPet(pet.id, pet.name, pet.type, pet.breed, pet.gender, pet.images,
                                 pet.publicationDate, FOR_ADOPTION));
        }
    }

    private void addLostPetsToMyPets(List<LostPet> lostPets, List<MyPet> myPets) {
        for (LostPet pet : lostPets) {
            myPets.add(new MyPet(pet.id, pet.name, pet.type, pet.breed, pet.gender, pet.images,
                                 pet.publicationDate, LOST));
        }
    }

    private void addFoundPetsToMyPets(List<FoundPet> foundPets, List<MyPet> myPets) {
        for (FoundPet pet : foundPets) {
            myPets.add(new MyPet(pet.id, "", pet.type, pet.breed, pet.gender, pet.images,
                                 pet.publicationDate, FOUND));
        }
    }

    private void addFoundPetsToMatches(List<LostPet> userLostPets, List<MatchingPet> matchingPets) {
        for (LostPet userLostPet : userLostPets) {
            List<FoundPet> matches = FoundPet.getMatches(userLostPet.type, userLostPet.gender, userLostPet.lastSeenDate,
                    userLostPet.lastSeenLocation);
            for (FoundPet match : matches) {
                User finder = User.getById(match.finderId);
                matchingPets.add(new MatchingPet(match, userLostPet, finder.email));
            }
        }
    }

    private void addLostPetsToMatches(List<FoundPet> userFoundPets, List<MatchingPet> matchingPets) {
        for (FoundPet userFoundPet : userFoundPets) {
            List<LostPet> matches = LostPet.getMatches(userFoundPet.type, userFoundPet.gender, userFoundPet.foundDate,
                    userFoundPet.foundLocation);
            for (LostPet match : matches) {
                User owner = User.getById(match.ownerId);
                matchingPets.add(new MatchingPet(match, userFoundPet, owner.email));
            }
        }
    }

}
