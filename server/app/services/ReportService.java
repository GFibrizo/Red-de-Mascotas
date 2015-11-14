package services;

import model.FoundPet;
import model.LostPet;
import model.PetAdoption;
import model.Report;
import model.external.ReportRequest;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    public Report getPetsReport(ReportRequest request) {
        return new Report(PetAdoption.countPetsPublished(request.fromDate, request.toDate, request.petType),
                          PetAdoption.countPetsAdopted(request.fromDate, request.toDate, request.petType),
                          LostPet.countPetsPublished(request.fromDate, request.toDate, request.petType),
                          FoundPet.countPetsPublished(request.fromDate, request.toDate, request.petType),
                          PetAdoption.getAverageAdoptionTimeLapse(request.fromDate, request.toDate, request.petType),
                          LostPet.getAverageFindingTimeLapse(request.fromDate, request.toDate, request.petType));
    }

}
