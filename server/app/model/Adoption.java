package model;

import static utils.Constants.NOTIFICATION_PENDING;

public class Adoption {

    public String adopterId;

    public String requestDate;

    public String lastSeenDate;

    public String status;

    public Adoption() { }

    public Adoption(String adopterId, String requestDate) {
        this.adopterId = adopterId;
        this.requestDate = requestDate;
        this.status = NOTIFICATION_PENDING;
    }

    public void updateLastSeen(String date) {
        this.lastSeenDate = date;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

}
