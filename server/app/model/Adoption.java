package model;

public class Adoption {

    public String adopterId;

    public String requestDate;

    public String lastSeenDate;

    public Adoption() { }

    public Adoption(String adopterId, String requestDate) {
        this.adopterId = adopterId;
        this.requestDate = requestDate;
    }

    public void updateLastSeen(String date) {
        this.lastSeenDate = date;
    }

}
