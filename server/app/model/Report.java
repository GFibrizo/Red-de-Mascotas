package model;

public class Report {

    public int petsPublishedForAdoption;

    public int petsAdopted;

    public int lostPetsPublished;

    public int foundPetsPublished;

    public int averageAdoptionTimeLapse;

    public int averageFindingTimeLapse;


    public Report(int petsPublishedForAdoption, int petsAdopted, int lostPetsPublished, int foundPetsPublished,
                  int averageAdoptionTimeLapse, int averageFindingTimeLapse) {
        this.petsPublishedForAdoption = petsPublishedForAdoption;
        this.petsAdopted = petsAdopted;
        this.lostPetsPublished = lostPetsPublished;
        this.foundPetsPublished = foundPetsPublished;
        this.averageAdoptionTimeLapse = averageAdoptionTimeLapse;
        this.averageFindingTimeLapse = averageFindingTimeLapse;
    }

}
