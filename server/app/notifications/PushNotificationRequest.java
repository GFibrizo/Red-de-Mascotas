package notifications;

public class PushNotificationRequest {

    public NotificationQuery where;

    public NotificationData data;

    public PushNotificationRequest(String receiverUserId, String notificationType, String message) {
        this.where = new NotificationQuery(receiverUserId);
        this.data = new NotificationData(notificationType, message);
    }

}
