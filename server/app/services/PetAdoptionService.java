package services;

import model.Adoption;
import model.PetAdoption;
import model.User;
import model.external.AdoptionRequest;
import model.external.SearchForAdoptionFilters;
import model.external.PublishForAdoptionPet;
import model.external.TransitHomeRequest;
import notifications.NotificationsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import play.Logger;

import java.util.List;

import static utils.Constants.*;

@Service
public class PetAdoptionService {

    @Autowired
    private NotificationsClient notificationsClient;

    public Boolean publishPet(PublishForAdoptionPet pet) {
        User owner = User.getById(pet.ownerId);
        if (owner == null) {
            Logger.error("Owner with id " + pet.ownerId + " not found");
            return false;
        }
        PetAdoption petAdoption = new PetAdoption(pet.name, pet.type, pet.ownerId, pet.address, pet.breed,
                                                  pet.gender, pet.age, pet.size, pet.colors, pet.eyeColor,
                                                  pet.behavior, pet.images, pet.needsTransitHome, pet.isCastrated,
                                                  pet.isOnTemporaryMedicine, pet.isOnChronicMedicine, pet.description);
        PetAdoption.create(petAdoption);
        alertUsersAboutMatchingSavedSearches();
        return true;
    }

    public List<PetAdoption> searchPets(SearchForAdoptionFilters filters) {
        filters.decodeFilters();
        List<PetAdoption> pets = PetAdoption.search(filters);
        if (pets.size() == 0)
            User.saveSearchRequest(filters);
        return pets;
    }

    public List<PetAdoption> getLastPublished() {
        return PetAdoption.getLastPublications();
    }

    public boolean userHasAdoptedPet(AdoptionRequest request) {
        PetAdoption pet = PetAdoption.getById(request.petId);
        if (pet.adoptionRequests != null) {
            for (Adoption adoptionRequest : pet.adoptionRequests) {
                if (adoptionRequest.adopterId.equals(request.adopterId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void adoptPet(AdoptionRequest request) {
        PetAdoption pet = PetAdoption.addAdoptionRequest(request);
        User user = User.getById(pet.ownerId);
        notificationsClient.pushNotification(user.notificationId, ADOPTION_REQUEST, ADOPTION_MESSAGE + pet.name);
    }

    public void acceptAdoptionRequest(AdoptionRequest request) {
        PetAdoption pet = PetAdoption.acceptAdoptionRequest(request);
        User adopter = User.getById(request.adopterId);
        notificationsClient.pushNotification(adopter.notificationId, ADOPTION_ACCEPTED, ADOPTION_ACCEPTED_MESSAGE_1 + pet.name + ADOPTION_ACCEPTED_MESSAGE_2);
    }

    public void takePetInTransit(TransitHomeRequest request) {
        PetAdoption pet = PetAdoption.addTransitHomeRequest(request);
        User user = User.getById(pet.ownerId);
        notificationsClient.pushNotification(user.notificationId, TAKE_IN_TRANSIT_REQUEST, TAKE_IN_TRANSIT_MESSAGE_1 + pet.name + TAKE_IN_TRANSIT_MESSAGE_2);
    }

    public void acceptTakeInTransitRequest(TransitHomeRequest request) {
        PetAdoption pet = PetAdoption.acceptTakeInTransitRequest(request);
        User transitHomeUser = User.getById(request.transitHomeUser);
        notificationsClient.pushNotification(transitHomeUser.notificationId, TAKE_IN_TRANSIT_ACCEPTED, TAKE_IN_TRANSIT_ACCEPTED_MESSAGE_1 + pet.name + TAKE_IN_TRANSIT_ACCEPTED_MESSAGE_2);
    }

    private void alertUsersAboutMatchingSavedSearches() {
        List<User> users = User.getUsersWithSavedSearchFilters();
        for (User user : users) {
            Boolean needsToBeAlerted = false;
            for (SearchForAdoptionFilters filters : user.savedSearchFilters) {
                List<PetAdoption> pets = PetAdoption.search(filters);
                if (pets.size() > 0) {
                    needsToBeAlerted = true;
                    break;
                }
            }
            if (needsToBeAlerted)
                notificationsClient.pushNotification(user.notificationId, NEW_SEARCH_MATCHES, NEW_SEARCH_MATCHES_MESSAGE);
        }
    }

}
