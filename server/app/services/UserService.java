package services;

import model.*;
import model.external.LogInUser;
import model.external.AccountRegistrationUser;
import model.external.FacebookRegistrationUser;
import model.external.SearchForAdoptionFilters;
import org.springframework.stereotype.Service;
import play.Logger;

import java.util.*;

import static utils.Constants.*;

@Service
public class UserService {

    public User logInWithAccount(LogInUser logInUser) {
        return User.getByUserName(logInUser.userName);
    }

    public String getUserSalt(String userName) {
        User user = User.getByUserName(userName);
        if (user != null) {
            return user.password.salt;
        }
        Logger.error("Salt for user " + userName + " not found");
        return null;
    }

    public User logInWithFacebook(String facebookId) {
        return User.getByFacebookId(facebookId);
    }

    public User registerAccountUser(AccountRegistrationUser userRegister) {
        if (User.exists(userRegister.userName, userRegister.email)) {
            Logger.error("Username or email already exists");
            return null;
        }
        User user = new User(userRegister.notificationId, userRegister.userName, userRegister.name,
                             userRegister.lastName, userRegister.email, userRegister.password,
                             userRegister.phone, userRegister.address);
        String id = User.create(user);
        user.setId(id);
        return user;
    }

    public User registerFacebookUser(FacebookRegistrationUser userRegister) {
        if (User.existsWithFacebook(userRegister.facebookId)) {
            Logger.error("Facebook account already exists");
            return null;
        }
        User user = new User(userRegister.notificationId, userRegister.name, userRegister.lastName,
                             userRegister.email, userRegister.facebookId, userRegister.phone,
                             userRegister.address);
        String id = User.create(user);
        user.setId(id);
        return user;
    }

    public List<MyPet> getPetsByUserId(String userId) {
        List<PetAdoption> petsForAdoption = PetAdoption.getPublishedAndBlockedByOwnerId(userId);
        List<LostPet> lostPets = LostPet.getPublishedAndBlockedByOwnerId(userId);
        List<FoundPet> foundPets = FoundPet.getPublishedAndBlockedByFinderId(userId);

        List<MyPet> myPets = new ArrayList<>();
        addPetsForAdoptionToMyPets(petsForAdoption, myPets);
        addLostPetsToMyPets(lostPets, myPets);
        addFoundPetsToMyPets(foundPets, myPets);

        Collections.sort(myPets, Collections.reverseOrder());
        return myPets;
    }

    public List<Notification> getNotifications(String userId) {
        User user = User.getById(userId);
        List<Notification> notifications = new ArrayList<>();
        List<PetAdoption> pets = PetAdoption.getByOwnerId(userId);
        for (PetAdoption pet : pets) {
            addAdoptionNotifications(notifications, pet);
            addTransitHomeNotifications(notifications, pet);
        }
        if (user.notifications != null)
            notifications.addAll(user.notifications);
        Collections.sort(notifications, Collections.reverseOrder());
        return notifications;
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

    public List<MatchingPet> getMatchingPets(String userId) {
        Map<String, MatchingPet> matchingPets = new HashMap<>();
        List<LostPet> userLostPets = LostPet.getPublishedByOwnerId(userId);
        List<FoundPet> userFoundPets = FoundPet.getPublishedByFinderId(userId);
        addFoundPetsToMatches(userLostPets, matchingPets);
        addLostPetsToMatches(userFoundPets, matchingPets);
        List<MatchingPet> pets = new ArrayList<>(matchingPets.values());
        Collections.sort(pets, Collections.reverseOrder());
        return pets;
    }

    public List<PetAdoption> getPetsMatchingSavedSearches(String userId) {
        Map<String, PetAdoption> petsFromSearch = new HashMap<>();
        User user = User.getById(userId);
        List<Integer> indexesToRemove = new ArrayList<>();
        if (user.savedSearchFilters != null) {
            for (int i = 0; i < user.savedSearchFilters.size(); i++) {
                SearchForAdoptionFilters filters = user.savedSearchFilters.get(i);
                List<PetAdoption> results = PetAdoption.search(filters);
                for (PetAdoption result : results)
                    petsFromSearch.put(result.id, result);
                if (results.size() > 0)
                    indexesToRemove.add(i);
            }
        }
        List<PetAdoption> pets = new ArrayList<>(petsFromSearch.values());
        User.removeFiltersFromSavedSearchFilters(userId, indexesToRemove);
        Collections.sort(pets, Collections.reverseOrder());
        return pets;
    }

    public void blockUser(String userId) {
        PetAdoption.blockAllPetsFromUser(userId);
        LostPet.blockAllPetsFromUser(userId);
        FoundPet.blockAllPetsFromUser(userId);
        User.blockUser(userId);
    }

    public void unblockUser(String userId) {
        PetAdoption.unblockPetsFromUser(userId);
        LostPet.unblockPetsFromUser(userId);
        FoundPet.unblockPetsFromUser(userId);
        User.unblockUser(userId);
    }

    private void addPetsForAdoptionToMyPets(List<PetAdoption> petsForAdoption, List<MyPet> myPets) {
        for (PetAdoption pet : petsForAdoption) {
            myPets.add(new MyPet(pet.id, pet.ownerId, pet.name, pet.type, pet.breed, pet.gender, pet.size, pet.age,
                    pet.colors, pet.eyeColor, pet.images, pet.publicationDate, FOR_ADOPTION, pet.publicationStatus));
        }
    }

    private void addLostPetsToMyPets(List<LostPet> lostPets, List<MyPet> myPets) {
        for (LostPet pet : lostPets) {
            myPets.add(new MyPet(pet.id, pet.ownerId, pet.name, pet.type, pet.breed, pet.gender, pet.size, pet.age,
                    pet.colors, pet.eyeColor, pet.images, pet.publicationDate, LOST, pet.publicationStatus));
        }
    }

    private void addFoundPetsToMyPets(List<FoundPet> foundPets, List<MyPet> myPets) {
        for (FoundPet pet : foundPets) {
            myPets.add(new MyPet(pet.id, pet.finderId, "", pet.type, pet.breed, pet.gender, pet.size, "",
                    pet.colors, pet.eyeColor, pet.images, pet.publicationDate, FOUND, pet.publicationStatus));
        }
    }

    private void addFoundPetsToMatches(List<LostPet> userLostPets, Map<String, MatchingPet> matchingPets) {
        for (LostPet userLostPet : userLostPets) {
            List<FoundPet> matches = FoundPet.getMatches(userLostPet.type, userLostPet.gender, userLostPet.lastSeenDate,
                    userLostPet.lastSeenLocation);
            for (FoundPet match : matches) {
                User finder = User.getById(match.finderId);
                matchingPets.put(match.id, new MatchingPet(match, userLostPet, finder.email));
            }
        }
    }

    private void addLostPetsToMatches(List<FoundPet> userFoundPets, Map<String, MatchingPet> matchingPets) {
        for (FoundPet userFoundPet : userFoundPets) {
            List<LostPet> matches = LostPet.getMatches(userFoundPet.type, userFoundPet.gender, userFoundPet.foundDate,
                    userFoundPet.foundLocation);
            for (LostPet match : matches) {
                User owner = User.getById(match.ownerId);
                matchingPets.put(match.id, new MatchingPet(match, userFoundPet, owner.email));
            }
        }
    }

    private void addAdoptionNotifications(List<Notification> notifications, PetAdoption pet) {
        if (pet.adoptionRequests != null) {
            for (Adoption adoptionRequest : pet.adoptionRequests) {
                if (!adoptionRequest.status.equals(NOTIFICATION_REJECTED)) {
                    User adopter = User.getById(adoptionRequest.adopterId);
                    String image;
                    if (pet.images != null) {
                        image = pet.images.get(0);
                    } else {
                        Logger.error("Pet with id " + pet.id + " does not have images");
                        image = "";
                    }
                    Notification notification = new Notification(ADOPTION_REQUEST,
                                                                 pet.id,
                                                                 adopter.id,
                                                                 adopter.email,
                                                                 adoptionRequest.requestDate,
                                                                 pet.name,
                                                                 image,
                                                                 adoptionRequest.status);
                    notifications.add(notification);
                }
            }
        }
    }

    private void addTransitHomeNotifications(List<Notification> notifications, PetAdoption pet) {
        if (pet.transitHomeRequests != null) {
            for (TransitHome transitHomeRequest : pet.transitHomeRequests) {
                if (!transitHomeRequest.status.equals(NOTIFICATION_REJECTED)) {
                    User transitHomeUser = User.getById(transitHomeRequest.transitHomeUserId);
                    String image;
                    if (pet.images != null) {
                        image = pet.images.get(0);
                    } else {
                        Logger.error("Pet with id " + pet.id + " does not have images");
                        image = "";
                    }
                    Notification notification = new Notification(TAKE_IN_TRANSIT_REQUEST,
                                                                 pet.id,
                                                                 transitHomeUser.id,
                                                                 transitHomeUser.email,
                                                                 transitHomeRequest.requestDate,
                                                                 pet.name,
                                                                 image,
                                                                 transitHomeRequest.status);
                    notifications.add(notification);
                }
            }
        }
    }

}
