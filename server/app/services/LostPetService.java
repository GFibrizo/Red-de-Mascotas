package services;

import model.LostPet;
import model.User;
import model.external.PublishLostPet;
import org.springframework.stereotype.Service;

@Service
public class LostPetService {

    public Boolean publishPet(PublishLostPet pet) {
        User owner = User.getById(pet.ownerId);
        if (owner == null)
            return false;
        LostPet lostPet = new LostPet(pet.name, pet.type, pet.ownerId, pet.breed, pet.gender, pet.age,
                                      pet.size, pet.colors, pet.eyeColor, pet.images, pet.videos, pet.isCastrated,
                                      pet.isOnTemporaryMedicine, pet.isOnChronicMedicine, pet.description,
                                      pet.lastSeenLocation, pet.lastSeenDate, pet.lastSeenHour);
        LostPet.create(lostPet);
        return true;
    }

}
