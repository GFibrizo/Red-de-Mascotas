package services;

import model.FoundPet;
import model.LostPet;
import model.User;
import model.external.PublishFoundPet;
import notifications.NotificationsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static utils.Constants.PETS_FOUND;
import static utils.Constants.PETS_FOUND_MESSAGE;

@Service
public class FoundPetService {

    @Autowired
    private NotificationsClient notificationsClient;

    public Boolean publishPet(PublishFoundPet pet) {
        User owner = User.getById(pet.finderId);
        if (owner == null)
            return false;
        FoundPet foundPet = new FoundPet(pet.type, pet.finderId, pet.breed, pet.gender, pet.size, pet.colors,
                                         pet.eyeColor, pet.needsTransitHome, pet.images, pet.videos, pet.description,
                                         pet.foundLocation, pet.foundDate, pet.foundHour);
        FoundPet.create(foundPet);
        sendNotificationsOfLostPets(foundPet);
        return true;
    }

    private void sendNotificationsOfLostPets(FoundPet foundPet) {
        List<LostPet> lostPets = LostPet.getMatches(foundPet.type, foundPet.gender, foundPet.foundDate, foundPet.foundLocation);
        for (LostPet lostPet : lostPets) {
            notificationsClient.pushNotification(lostPet.ownerId, PETS_FOUND, PETS_FOUND_MESSAGE);
        }
        if (!lostPets.isEmpty())
            notificationsClient.pushNotification(foundPet.finderId, PETS_FOUND, PETS_FOUND_MESSAGE);
    }

}
