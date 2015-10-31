package model;

import static utils.Constants.NOTIFICATION_PENDING;

public class TransitHome {

    public String transitHomeUserId;

    public String requestDate;

    public String status;

    public TransitHome() { }

    public TransitHome(String transitHomeUserId, String requestDate) {
        this.transitHomeUserId = transitHomeUserId;
        this.requestDate = requestDate;
        this.status = NOTIFICATION_PENDING;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

}
