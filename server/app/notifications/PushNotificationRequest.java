package notifications;

public class PushNotificationRequest {

    public NotificationQuery where;

    public NotificationData data;

    public PushNotificationRequest(String receiverUserId, String message) {
        this.where = new NotificationQuery(receiverUserId);
        this.data = new NotificationData(message);
    }

}
