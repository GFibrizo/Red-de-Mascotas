package services;

import model.FoundPet;
import model.LostPet;
import model.PetAdoption;
import model.external.UnpublishPet;
import org.springframework.stereotype.Service;

import static utils.Constants.FOR_ADOPTION;
import static utils.Constants.LOST;
import static utils.Constants.FOUND;

@Service
public class MyPetService {

    public void unpublishPet(UnpublishPet pet) {
        switch (pet.publicationType) {
            case FOR_ADOPTION:
                PetAdoption.unpublishPet(pet.petId);
                break;
            case LOST:
                LostPet.unpublishPet(pet.petId);
                break;
            case FOUND:
                FoundPet.unpublishPet(pet.petId);
                break;
        }
    }

}
