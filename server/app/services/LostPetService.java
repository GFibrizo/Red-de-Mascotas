package services;

import model.FoundPet;
import model.LostPet;
import model.User;
import model.external.PublishLostPet;
import notifications.NotificationsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import play.Logger;

import java.util.List;

import static utils.Constants.PETS_FOUND;
import static utils.Constants.PETS_FOUND_MESSAGE;

@Service
public class LostPetService {

    @Autowired
    private NotificationsClient notificationsClient;

    public Boolean publishPet(PublishLostPet pet) {
        User owner = User.getById(pet.ownerId);
        if (owner == null) {
            Logger.error("Owner with id " + pet.ownerId + " not found");
            return false;
        }
        LostPet lostPet = new LostPet(pet.name, pet.type, pet.ownerId, pet.breed, pet.gender, pet.age,
                                      pet.size, pet.colors, pet.eyeColor, pet.images, pet.videos, pet.isCastrated,
                                      pet.isOnTemporaryMedicine, pet.isOnChronicMedicine, pet.description,
                                      pet.lastSeenLocation, pet.lastSeenDate, pet.lastSeenHour);
        LostPet.create(lostPet);
        sendNotificationsOfFoundPets(lostPet);
        return true;
    }

    private void sendNotificationsOfFoundPets(LostPet lostPet) {
        List<FoundPet> foundPets = FoundPet.getMatches(lostPet.type, lostPet.gender, lostPet.lastSeenDate, lostPet.lastSeenLocation);
        for (FoundPet foundPet : foundPets) {
            User finder = User.getById(foundPet.finderId);
            notificationsClient.pushNotification(finder.notificationId, PETS_FOUND, PETS_FOUND_MESSAGE);
        }
        if (!foundPets.isEmpty()) {
            User owner = User.getById(lostPet.ownerId);
            notificationsClient.pushNotification(owner.notificationId, PETS_FOUND, PETS_FOUND_MESSAGE);
        }
        Logger.info("Number of matches found: " + foundPets.size());
    }

}
