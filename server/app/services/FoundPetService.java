package services;

import model.FoundPet;
import model.User;
import model.external.PublishFoundPet;
import org.springframework.stereotype.Service;

@Service
public class FoundPetService {

    public Boolean publishPet(PublishFoundPet pet) {
        User owner = User.getById(pet.finderId);
        if (owner == null)
            return false;
        FoundPet foundPet = new FoundPet(pet.type, pet.finderId, pet.breed, pet.gender, pet.size, pet.colors,
                                         pet.eyeColor, pet.needsTransitHome, pet.images, pet.videos, pet.description,
                                         pet.foundLocation, pet.foundDate, pet.foundHour);
        FoundPet.create(foundPet);
        return true;
    }

}
