package model;

public class AdoptionNotification implements Comparable<AdoptionNotification> {

    public String adopterEmail;

    public String requestDate;

    public String petName;

    public String petImageId;


    public AdoptionNotification(String adopterEmail, String requestDate, String petName, String petImageId) {
        this.adopterEmail = adopterEmail;
        this.requestDate = requestDate;
        this.petName = petName;
        this.petImageId = petImageId;
    }

    @Override
    public int compareTo(AdoptionNotification another) {
        if (this.requestDate.compareTo(another.requestDate) < 0) {
            return -1;
        } else {
            return 1;
        }
    }

}
