package services;

import model.PetAdoption;
import model.User;
import model.external.SearchForAdoptionFilters;
import model.external.PublishForAdoptionPet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetAdoptionService {

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

}
