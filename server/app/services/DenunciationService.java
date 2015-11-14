package services;

import model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static utils.Constants.FOR_ADOPTION;
import static utils.Constants.REPORT_PENDING;

@Service
public class DenunciationService {

    public List<PublicationDenunciation> getPetPublicationDenunciations() {
        List<PublicationDenunciation> denunciations = new ArrayList<>();
        List<PetAdoption> petAdoptions = PetAdoption.getPetsWithReports();
        addPetAdoptionDenunciations(petAdoptions, denunciations);
        Collections.sort(denunciations, Collections.reverseOrder());
        return denunciations;
    }

    private void addPetAdoptionDenunciations(List<PetAdoption> publications, List<PublicationDenunciation> denunciations) {
        for (PetAdoption publication : publications) {
            User owner = User.getById(publication.ownerId);
            for (PublicationReport report : publication.reports) {
                if (report.status.equals(REPORT_PENDING)) {
                    User informer = User.getById(report.informer);
                    String color = "";
                    if (publication.colors.size() > 0) {
                        color = publication.colors.get(0);
                    }
                    DenouncedPublication publicationApi = new DenouncedPublication(publication.id, FOR_ADOPTION,
                            publication.name, publication.size, color, publication.breed);
                    denunciations.add(new PublicationDenunciation(owner.userName, report.informer, informer.userName,
                            report.reason, report.reportDate, publicationApi));
                }
            }
        }
    }

}
