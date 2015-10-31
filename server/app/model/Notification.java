package model;

public class Notification implements Comparable<Notification> {

    public String notificationType;

    public String petId;

    public String inquirerId;

    public String inquirerEmail;

    public String petName;

    public String petImageId;

    public String requestDate;


    public Notification(String notificationType, String petId, String inquirerId, String inquirerEmail,
                        String requestDate, String petName, String petImageId) {
        this.notificationType = notificationType;
        this.petId = petId;
        this.inquirerId = inquirerId;
        this.inquirerEmail = inquirerEmail;
        this.petName = petName;
        this.petImageId = petImageId;
        this.requestDate = requestDate;
    }

    @Override
    public int compareTo(Notification another) {
        if (this.requestDate.compareTo(another.requestDate) < 0) {
            return -1;
        } else {
            return 1;
        }
    }

}
