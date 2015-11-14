package services;

import model.*;
import model.external.AcceptPublicationReportRequest;
import model.external.ReportPublicationRequest;
import org.springframework.stereotype.Service;
import play.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static utils.Constants.*;

@Service
public class DenunciationService {

    public List<PublicationDenunciation> getPetPublicationDenunciations() {
        List<PublicationDenunciation> denunciations = new ArrayList<>();
        List<PetAdoption> petAdoptions = PetAdoption.getPetsWithReports();
        List<LostPet> lostPets = LostPet.getPetsWithReports();
        List<FoundPet> foundPets = FoundPet.getPetsWithReports();
        addPetAdoptionDenunciations(petAdoptions, denunciations);
        addLostPetDenunciations(lostPets, denunciations);
        addFoundPetDenunciations(foundPets, denunciations);
        Collections.sort(denunciations, Collections.reverseOrder());
        return denunciations;
    }

    public void reportPublication(ReportPublicationRequest request) {
        switch (request.publicationType) {
            case FOR_ADOPTION:
                PetAdoption.addReport(request);
                Logger.info("Publication for pet for adoption with id " + request.petId + " successfully reported");
                break;
            case LOST:
                LostPet.addReport(request);
                Logger.info("Publication for lost pet with id " + request.petId + " successfully reported");
                break;
            case FOUND:
                FoundPet.addReport(request);
                Logger.info("Publication for found pet with id " + request.petId + " successfully reported");
                break;
        }
    }

    public void acceptPublicationReport(AcceptPublicationReportRequest request) {
        switch (request.publicationType) {
            case FOR_ADOPTION:
                PetAdoption petAdoption = PetAdoption.acceptReport(request);
                User.incrementAcceptedPublicationReports(petAdoption.ownerId);
                Logger.info("Informer " + request.informer + "'s report of publication for pet for adoption with id " + request.petId + " successfully accepted");
                break;
            case LOST:
                LostPet lostPet = LostPet.acceptReport(request);
                User.incrementAcceptedPublicationReports(lostPet.ownerId);
                Logger.info("Informer " + request.informer + "'s report of publication for lost pet with id " + request.petId + " successfully accepted");
                break;
            case FOUND:
                FoundPet foundPet = FoundPet.acceptReport(request);
                User.incrementAcceptedPublicationReports(foundPet.finderId);
                Logger.info("Informer " + request.informer + "'s report of publication for found pet with id " + request.petId + " successfully accepted");
                break;
        }
    }

    public void rejectPublicationReport(AcceptPublicationReportRequest request) {
        switch (request.publicationType) {
            case FOR_ADOPTION:
                PetAdoption.rejectReport(request);
                Logger.info("Informer " + request.informer + "'s report of publication for pet for adoption with id " + request.petId + " successfully rejected");
                break;
            case LOST:
                LostPet.rejectReport(request);
                Logger.info("Informer " + request.informer + "'s report of publication for lost pet with id " + request.petId + " successfully rejected");
                break;
            case FOUND:
                FoundPet.rejectReport(request);
                Logger.info("Informer " + request.informer + "'s report of publication for found pet with id " + request.petId + " successfully rejected");
                break;
        }
    }

    public List<UserDenunciation> getUserDenunciations() {
        List<UserDenunciation> denunciations = new ArrayList<>();
        List<User> users = User.getAll();
        for (User user : users) {
            denunciations.add(new UserDenunciation(user.id, user.userName, user.name, user.lastName, user.email,
                                                   user.phone, user.address, user.acceptedPublicationReports, user.active));
        }
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

    private void addLostPetDenunciations(List<LostPet> publications, List<PublicationDenunciation> denunciations) {
        for (LostPet publication : publications) {
            User owner = User.getById(publication.ownerId);
            for (PublicationReport report : publication.reports) {
                if (report.status.equals(REPORT_PENDING)) {
                    User informer = User.getById(report.informer);
                    String color = "";
                    if (publication.colors.size() > 0) {
                        color = publication.colors.get(0);
                    }
                    DenouncedPublication publicationApi = new DenouncedPublication(publication.id, LOST,
                            publication.name, publication.size, color, publication.breed);
                    denunciations.add(new PublicationDenunciation(owner.userName, report.informer, informer.userName,
                            report.reason, report.reportDate, publicationApi));
                }
            }
        }
    }

    private void addFoundPetDenunciations(List<FoundPet> publications, List<PublicationDenunciation> denunciations) {
        for (FoundPet publication : publications) {
            User owner = User.getById(publication.finderId);
            for (PublicationReport report : publication.reports) {
                if (report.status.equals(REPORT_PENDING)) {
                    User informer = User.getById(report.informer);
                    String color = "";
                    if (publication.colors.size() > 0) {
                        color = publication.colors.get(0);
                    }
                    DenouncedPublication publicationApi = new DenouncedPublication(publication.id, FOUND,
                            "", publication.size, color, publication.breed);
                    denunciations.add(new PublicationDenunciation(owner.userName, report.informer, informer.userName,
                            report.reason, report.reportDate, publicationApi));
                }
            }
        }
    }

}
