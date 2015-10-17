package services;

import model.Adoption;
import model.PetAdoption;
import model.User;
import model.external.AdoptionRequest;
import model.external.SearchForAdoptionFilters;
import model.external.PublishForAdoptionPet;
import notifications.NotificationsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static utils.Constants.ADOPTION_MESSAGE;
import static utils.Constants.ADOPTION_REQUEST;

@Service
public class PetAdoptionService {

    @Autowired
    private NotificationsClient notificationsClient;

    public Boolean publishPet(PublishForAdoptionPet pet) {
        User owner = User.getById(pet.ownerId);
        if (owner == null)
            return false;
        PetAdoption petAdoption = new PetAdoption(pet.name, pet.type, pet.ownerId, owner.address, pet.breed,
                                                  pet.gender, pet.age, pet.size, pet.colors, pet.eyeColor,
                                                  pet.behavior, pet.images, pet.needsTransitHome, pet.isCastrated,
                                                  pet.isOnTemporaryMedicine, pet.isOnChronicMedicine, pet.description);
        PetAdoption.create(petAdoption);
        return true;
    }

    public List<PetAdoption> searchPets(SearchForAdoptionFilters filters) {
        filters.decodeFilters();
        return PetAdoption.search(filters);
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

}
