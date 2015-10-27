package services;

import model.FoundPet;
import model.LostPet;
import model.PetAdoption;
import model.external.UnpublishPet;
import org.springframework.stereotype.Service;
import play.Logger;

import static utils.Constants.FOR_ADOPTION;
import static utils.Constants.LOST;
import static utils.Constants.FOUND;

@Service
public class MyPetService {

    public void unpublishPet(UnpublishPet pet) {
        switch (pet.publicationType) {
            case FOR_ADOPTION:
                PetAdoption.unpublishPet(pet.petId);
                Logger.info("Pet for adoption successfully unpublished, petId: " + pet.petId);
                break;
            case LOST:
                LostPet.unpublishPet(pet.petId);
                Logger.info("Lost pet successfully unpublished, petId: " + pet.petId);
                break;
            case FOUND:
                Logger.info("Found pet successfully unpublished, petId: " + pet.petId);
                FoundPet.unpublishPet(pet.petId);
                break;
        }
    }

}
